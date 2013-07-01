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
package org.moreframework.security.support;
import static org.moreframework.MoreFramework.getIndexStr;
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
import org.more.util.StringUtils;
import org.moreframework.MoreFramework;
import org.moreframework.binder.ApiBinder;
import org.moreframework.context.AppContext;
import org.moreframework.context.PlatformListener;
import org.moreframework.context.startup.PlatformExt;
import org.moreframework.security.AuthRequestProcess;
import org.moreframework.security.AuthSession;
import org.moreframework.security.PermissionException;
import org.moreframework.security.Power;
import org.moreframework.security.SecAccess;
import org.moreframework.security.SecAuth;
import org.moreframework.security.SecurityAccess;
import org.moreframework.security.SecurityAuth;
import org.moreframework.security.SecurityContext;
import org.moreframework.security.SecurityQuery;
import org.moreframework.security.TestPermissionProcess;
import org.moreframework.security.Power.Level;
import org.moreframework.security.support.impl.InternalSecurityContext;
import org.moreframework.security.support.process.DefaultAuthRequestProcess;
import org.moreframework.security.support.process.DefaultTestPermissionProcess;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.internal.UniqueAnnotations;
import com.google.inject.matcher.AbstractMatcher;
/**
 * ֧��Service��ע�⹦�ܡ���������Lv0Max
 * @version : 2013-4-8
 * @author ������ (zyc@byshell.org)
 */
@PlatformExt(displayName = "SecurityModuleServiceListener", description = "org.platform.security���������֧�֡�", startIndex = PlatformExt.Lv_0Max)
public class SecurityPlatformListener implements PlatformListener {
    private SecurityContext         secService  = null;
    private SecuritySessionListener secListener = null;
    private SecuritySettings        settings    = null;
    /**��ʼ��.*/
    @Override
    public void initialize(ApiBinder event) {
        Binder binder = event.getGuiceBinder();
        /*����*/
        this.settings = new SecuritySettings();
        this.settings.loadConfig(event.getSettings());
        /*HttpSession����������֪ͨ����*/
        this.secListener = new SecuritySessionListener();
        event.sessionListener().bind(this.secListener);
        /*aop������ִ��Ȩ��֧��*/
        event.getGuiceBinder().bindInterceptor(new ClassPowerMatcher(), new MethodPowerMatcher(), new SecurityInterceptor());/*ע��Aop*/
        /*װ��SecurityAccess*/
        this.loadSecurityAuth(event);
        this.loadSecurityAccess(event);
        /*�󶨺��Ĺ���ʵ���ࡣ*/
        binder.bind(SecuritySettings.class).toInstance(this.settings);//ͨ��Guice
        binder.bind(SecurityContext.class).to(InternalSecurityContext.class).asEagerSingleton();
        binder.bind(SecurityQuery.class).to(DefaultSecurityQuery.class);
        /**/
        binder.bind(AuthRequestProcess.class).to(DefaultAuthRequestProcess.class);/*�������*/
        binder.bind(TestPermissionProcess.class).to(DefaultTestPermissionProcess.class);/*URLȨ�޼�����*/
        //
        event.filter("*").through(SecurityFilter.class);
    }
    @Override
    public void initialized(AppContext appContext) {
        appContext.getSettings().addSettingsListener(this.settings);
        //
        this.secService = appContext.getInstance(InternalSecurityContext.class);
        this.secService.initSecurity(appContext);
        //
        MoreFramework.info("online ->> security is %s", (this.settings.isEnable() ? "enable." : "disable."));
    }
    @Override
    public void destroy(AppContext appContext) {
        appContext.getSettings().removeSettingsListener(this.settings);
        //
        this.secService.destroySecurity(appContext);
    }
    //
    /*װ��SecurityAccess*/
    protected void loadSecurityAuth(ApiBinder event) {
        //1.��ȡ
        Set<Class<?>> authSet = event.getClassSet(SecAuth.class);
        if (authSet == null)
            return;
        List<Class<? extends SecurityAuth>> authList = new ArrayList<Class<? extends SecurityAuth>>();
        for (Class<?> cls : authSet) {
            if (SecurityAuth.class.isAssignableFrom(cls) == false) {
                MoreFramework.warning("loadSecurityAuth : not implemented ISecurityAuth , class=%s", cls);
            } else {
                authList.add((Class<? extends SecurityAuth>) cls);
            }
        }
        //3.ע�����
        Binder binder = event.getGuiceBinder();
        Map<String, Integer> authIndex = new HashMap<String, Integer>();
        for (Class<? extends SecurityAuth> authType : authList) {
            SecAuth authAnno = authType.getAnnotation(SecAuth.class);
            Key<? extends SecurityAuth> authKey = Key.get(authType);
            String authSystem = authAnno.authSystem();
            //
            SecurityAuthDefinition authDefine = new SecurityAuthDefinition(authSystem, authKey);
            int maxIndex = (authIndex.containsKey(authSystem) == false) ? Integer.MAX_VALUE : authIndex.get(authSystem);
            if (authAnno.sort() <= maxIndex/*ֵԽСԽ����*/) {
                authIndex.put(authSystem, authAnno.sort());
                binder.bind(SecurityAuthDefinition.class).annotatedWith(UniqueAnnotations.create()).toInstance(authDefine);
                binder.bind(SecurityAuth.class).annotatedWith(UniqueAnnotations.create()).toProvider(authDefine);
                MoreFramework.info(authSystem + "[" + getIndexStr(authAnno.sort()) + "] is SecurityAuth , class=" + MoreFramework.logString(authType));
            }
        }
    }
    //
    /*װ��SecurityAccess*/
    protected void loadSecurityAccess(ApiBinder event) {
        //1.��ȡ
        Set<Class<?>> accessSet = event.getClassSet(SecAccess.class);
        if (accessSet == null)
            return;
        List<Class<? extends SecurityAccess>> accessList = new ArrayList<Class<? extends SecurityAccess>>();
        for (Class<?> cls : accessSet) {
            if (SecurityAccess.class.isAssignableFrom(cls) == false) {
                MoreFramework.warning("loadSecurityAccess : not implemented ISecurityAccess. class=%s", cls);
            } else {
                accessList.add((Class<? extends SecurityAccess>) cls);
            }
        }
        //3.ע�����
        Binder binder = event.getGuiceBinder();
        Map<String, Integer> accessIndex = new HashMap<String, Integer>();
        for (Class<? extends SecurityAccess> accessType : accessList) {
            SecAccess accessAnno = accessType.getAnnotation(SecAccess.class);
            Key<? extends SecurityAccess> accessKey = Key.get(accessType);
            String authSystem = accessAnno.authSystem();
            //
            SecurityAccessDefinition accessDefine = new SecurityAccessDefinition(authSystem, accessKey);
            int maxIndex = (accessIndex.containsKey(authSystem) == false) ? Integer.MAX_VALUE : accessIndex.get(authSystem);
            if (accessAnno.sort() <= maxIndex/*ֵԽСԽ����*/) {
                accessIndex.put(authSystem, accessAnno.sort());
                binder.bind(SecurityAccessDefinition.class).annotatedWith(UniqueAnnotations.create()).toInstance(accessDefine);
                binder.bind(SecurityAccess.class).annotatedWith(UniqueAnnotations.create()).toProvider(accessDefine);
                MoreFramework.info(authSystem + "[" + getIndexStr(accessAnno.sort()) + "] is SecurityAccess. class=" + MoreFramework.logString(accessType));
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
            if (Level.NeedLogin == powerAnno.level()) {
                passPower = this.doNeedLogin(powerAnno, invocation.getMethod());
            } else if (Level.NeedAccess == powerAnno.level()) {
                passPower = this.doNeedAccess(powerAnno, invocation.getMethod());
            } else if (Level.Free == powerAnno.level()) {
                passPower = true;
            }
            //3.ִ�д���
            if (passPower)
                return invocation.proceed();
            String msg = powerAnno.errorMsg();
            if (StringUtils.isBlank(msg) == true)
                msg = "has no permission Level=" + powerAnno.level().name() + " Code : " + MoreFramework.logString(powerAnno.value());
            throw new PermissionException(msg);
        }
        private boolean doNeedLogin(Power powerAnno, Method method) {
            AuthSession[] authSessions = secService.getCurrentAuthSession();
            for (AuthSession authSession : authSessions)
                if (authSession.isLogin())
                    return true;
            return false;
        }
        private boolean doNeedAccess(Power powerAnno, Method method) {
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
            //
        }
        @Override
        public void sessionDestroyed(HttpSessionEvent se) {
            //
        }
    }
}