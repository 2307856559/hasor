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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.internal.UniqueAnnotations;
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
        /*װ��SecurityAccess*/
        this.loadSecurityAuth(event);
        this.loadSecurityAccess(event);
        /*�󶨺��Ĺ���ʵ���ࡣ*/
        event.getGuiceBinder().bind(SecuritySettings.class).toInstance(this.settings);//ͨ��Guice
        event.getGuiceBinder().bind(SecurityContext.class).to(InternalSecurityService.class).asEagerSingleton();
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
    //
    /*װ��SecurityAccess*/
    protected void loadSecurityAuth(ApiBinder event) {
        Platform.info("begin loadSecurityAuth...");
        //1.��ȡ
        Set<Class<?>> authSet = event.getClassSet(SecurityAuth.class);
        List<Class<? extends ISecurityAuth>> authList = new ArrayList<Class<? extends ISecurityAuth>>();
        for (Class<?> cls : authSet) {
            if (ISecurityAuth.class.isAssignableFrom(cls) == false) {
                Platform.warning("loadSecurityAuth : not implemented ISecurityAuth of type " + Platform.logString(cls));
            } else {
                authList.add((Class<? extends ISecurityAuth>) cls);
            }
        }
        //3.ע�����
        Binder binder = event.getGuiceBinder();
        Map<String, Integer> authIndex = new HashMap<String, Integer>();
        for (Class<? extends ISecurityAuth> authType : authList) {
            SecurityAuth authAnno = authType.getAnnotation(SecurityAuth.class);
            Key<? extends ISecurityAuth> authKey = Key.get(authType);
            String authSystem = authAnno.authSystem();
            //
            SecurityAuthDefinition authDefine = new SecurityAuthDefinition(authSystem, authKey);
            int maxIndex = (authIndex.containsKey(authSystem) == false) ? Integer.MAX_VALUE : authIndex.get(authSystem);
            if (authAnno.sort() <= maxIndex/*ֵԽСԽ����*/) {
                authIndex.put(authSystem, authAnno.sort());
                binder.bind(SecurityAuthDefinition.class).annotatedWith(UniqueAnnotations.create()).toInstance(authDefine);
                binder.bind(ISecurityAuth.class).annotatedWith(UniqueAnnotations.create()).toProvider(authDefine);
                Platform.info(authSystem + "[" + authAnno.sort() + "] at SecurityAuth of type " + Platform.logString(authType));
            }
        }
    }
    //
    /*װ��SecurityAccess*/
    protected void loadSecurityAccess(ApiBinder event) {
        Platform.info("begin loadSecurityAccess...");
        //1.��ȡ
        Set<Class<?>> accessSet = event.getClassSet(SecurityAccess.class);
        List<Class<? extends ISecurityAccess>> accessList = new ArrayList<Class<? extends ISecurityAccess>>();
        for (Class<?> cls : accessSet) {
            if (ISecurityAccess.class.isAssignableFrom(cls) == false) {
                Platform.warning("loadSecurityAccess : not implemented ISecurityAccess of type " + Platform.logString(cls));
            } else {
                accessList.add((Class<? extends ISecurityAccess>) cls);
            }
        }
        //3.ע�����
        Binder binder = event.getGuiceBinder();
        Map<String, Integer> accessIndex = new HashMap<String, Integer>();
        for (Class<? extends ISecurityAccess> accessType : accessList) {
            SecurityAccess accessAnno = accessType.getAnnotation(SecurityAccess.class);
            Key<? extends ISecurityAccess> accessKey = Key.get(accessType);
            String authSystem = accessAnno.authSystem();
            //
            SecurityAccessDefinition accessDefine = new SecurityAccessDefinition(authSystem, accessKey);
            int maxIndex = (accessIndex.containsKey(authSystem) == false) ? Integer.MAX_VALUE : accessIndex.get(authSystem);
            if (accessAnno.sort() <= maxIndex/*ֵԽСԽ����*/) {
                accessIndex.put(authSystem, accessAnno.sort());
                binder.bind(SecurityAccessDefinition.class).annotatedWith(UniqueAnnotations.create()).toInstance(accessDefine);
                binder.bind(ISecurityAccess.class).annotatedWith(UniqueAnnotations.create()).toProvider(accessDefine);
                Platform.info(authSystem + "[" + accessAnno.sort() + "] at SecurityAccess of type " + Platform.logString(accessType));
            }
        }
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
                try {
                    AuthSession newAuthSession = secService.createAuthSession();
                    StringBuilder authSessionIDs = new StringBuilder("");
                    for (AuthSession authSession : authSessions)
                        authSessionIDs.append(authSession.getSessionID() + ",");
                    authSessionIDs.append(newAuthSession.getSessionID());
                    se.getSession().setAttribute(InternalSecurityProcess.HttpSessionAuthSessionSetName, authSessionIDs.toString());
                } catch (SecurityException e) {
                    Platform.error(Platform.logString(e));
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