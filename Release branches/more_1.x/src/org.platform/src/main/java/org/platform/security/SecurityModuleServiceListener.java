/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.platform.security;
import java.lang.reflect.Method;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.platform.Platform;
import org.platform.binder.ApiBinder;
import org.platform.context.AbstractModuleListener;
import org.platform.context.AppContext;
import org.platform.context.InitListener;
import org.platform.context.setting.Config;
import org.platform.security.Power.Level;
import org.platform.security.internal.DefaultSecurityService;
import com.google.inject.matcher.AbstractMatcher;
/**
 * ֧��Service��ע�⹦�ܡ�
 * @version : 2013-4-8
 * @author ������ (zyc@byshell.org)
 */
@InitListener(displayName = "SecurityModuleServiceListener", description = "org.platform.security���������֧�֡�", startIndex = 1)
public class SecurityModuleServiceListener extends AbstractModuleListener {
    private SecurityContext         secService  = null;
    private SecuritySessionListener secListener = null;
    private SecuritySettings        settings    = null;
    /**��ʼ��.*/
    @Override
    public void initialize(ApiBinder event) {
        /*����*/
        this.settings = new SecuritySettings();
        this.settings.loadConfig(event.getSettings());
        /*HttpSession����������֪ͨ����*/
        this.secListener = new SecuritySessionListener();
        event.sessionListener().bind(this.secListener);
        /*request������������*/
        event.filter("*").through(SecurityFilter.class);
        /*aop������ִ��Ȩ��֧��*/
        event.getGuiceBinder().bindInterceptor(new ClassPowerMatcher(), new MethodPowerMatcher(), new SecurityInterceptor());/*ע��Aop*/
        /*�󶨺��Ĺ���ʵ���ࡣ*/
        event.getGuiceBinder().bind(SecuritySettings.class).toInstance(this.settings);//ͨ��Guice
        event.getGuiceBinder().bind(SecurityContext.class).to(DefaultSecurityService.class);
        event.getGuiceBinder().bind(SecurityQuery.class).to(DefaultSecurityQuery.class);
    }
    @Override
    public void initialized(AppContext appContext) {
        Config systemConfig = appContext.getInitContext().getConfig();
        systemConfig.addSettingsListener(this.settings);
        //
        this.secService = appContext.getBean(SecurityContext.class);
        this.secService.initSecurity(appContext);
        Platform.info("online ->> security is " + (this.settings.isEnable() ? "enable." : "disable."));
    }
    @Override
    public void destroy(AppContext appContext) {
        Config systemConfig = appContext.getInitContext().getConfig();
        systemConfig.removeSettingsListener(this.settings);
        //
        this.secService.destroySecurity(appContext);
    }
    /*-------------------------------------------------------------------------------------*/
    /*���������Ƿ�ƥ�䡣����ֻҪ���ͻ򷽷��ϱ����@Power��*/
    private class ClassPowerMatcher extends AbstractMatcher<Class<?>> {
        @Override
        public boolean matches(Class<?> matcherType) {
            /*������ڽ���״̬�����Ȩ�޼��*/
            if (settings.isEnableMethod() == false)
                return false;
            /*----------------------------*/
            if (matcherType.isAnnotationPresent(Power.class) == true)
                return true;
            Method[] m1s = matcherType.getMethods();
            Method[] m2s = matcherType.getDeclaredMethods();
            for (Method m1 : m1s) {
                if (m1.isAnnotationPresent(Power.class) == true)
                    return true;
            }
            for (Method m2 : m2s) {
                if (m2.isAnnotationPresent(Power.class) == true)
                    return true;
            }
            return false;
        }
    }
    /*�����ⷽ���Ƿ�ƥ�䡣���򣺷����򷽷��������ϱ����@Power��*/
    private class MethodPowerMatcher extends AbstractMatcher<Method> {
        @Override
        public boolean matches(Method matcherType) {
            /*������ڽ���״̬�����Ȩ�޼��*/
            if (settings.isEnableMethod() == false)
                return false;
            /*----------------------------*/
            if (matcherType.isAnnotationPresent(Power.class) == true)
                return true;
            if (matcherType.getDeclaringClass().isAnnotationPresent(Power.class) == true)
                return true;
            return false;
        }
    }
    /*������*/
    private class SecurityInterceptor implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            /*������ڽ���״̬�����Ȩ�޼��*/
            if (settings.isEnableMethod() == false)
                return invocation.proceed();
            /*----------------------------*/
            //1.��ȡȨ������
            Power powerAnno = invocation.getMethod().getAnnotation(Power.class);
            if (powerAnno == null)
                powerAnno = invocation.getMethod().getDeclaringClass().getAnnotation(Power.class);
            //2.����Ȩ��
            boolean passPower = true;
            if (Level.PassLogin == powerAnno.level()) {
                passPower = this.doPassLogin(powerAnno, invocation.getMethod());
            } else if (Level.PassPolicy == powerAnno.level()) {
                passPower = this.doPassPolicy(powerAnno, invocation.getMethod());
            } else if (Level.Free == powerAnno.level()) {
                passPower = true;
            }
            //3.ִ�д���
            if (passPower)
                return invocation.proceed();
            String msg = "has no permission Level=" + powerAnno.level().name() + " Code : " + Platform.logString(powerAnno.value());
            throw new PermissionException(msg);
        }
        private boolean doPassLogin(Power powerAnno, Method method) {
            AuthSession[] authSessions = secService.getCurrentAuthSession();
            for (AuthSession authSession : authSessions)
                if (authSession.isLogin())
                    return true;
            return false;
        }
        private boolean doPassPolicy(Power powerAnno, Method method) {
            AuthSession[] authSessions = secService.getCurrentAuthSession();
            String[] powers = powerAnno.value();
            SecurityQuery query = secService.newSecurityQuery();
            for (String anno : powers)
                query.and(anno);
            return query.testPermission(authSessions);
        }
    }
    /*HttpSession��̬����*/
    private class SecuritySessionListener implements HttpSessionListener {
        @Override
        public void sessionCreated(HttpSessionEvent se) {
            if (settings.isEnable() == false)
                return;
            AuthSession[] authSessions = secService.getCurrentAuthSession();
            boolean needCreateSession = authSessions.length == 0;
            if (needCreateSession) {
                AuthSession authSession = secService.createAuthSession();
                if (settings.isGuestEnable() == true) {
                    authSession.doLoginGuest();/*��½�����ʺ�*/
                    authSession.setSupportCookieRecover(false);/*���ָ�*/
                }
            }
        }
        @Override
        public void sessionDestroyed(HttpSessionEvent se) {
            if (settings.isEnable() == false)
                return;
            AuthSession[] authSessions = secService.getCurrentAuthSession();
            for (AuthSession authSession : authSessions)
                secService.inactivationAuthSession(authSession.getSessionID()); /*�ۻ�AuthSession*/
        }
    }
}