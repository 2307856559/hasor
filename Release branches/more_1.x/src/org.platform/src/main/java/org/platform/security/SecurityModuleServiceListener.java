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
import org.platform.binder.ApiBinder;
import org.platform.context.AbstractModuleListener;
import org.platform.context.InitListener;
import com.google.inject.matcher.AbstractMatcher;
/**
 * ֧��Service��ע�⹦�ܡ�
 * @version : 2013-4-8
 * @author ������ (zyc@byshell.org)
 */
@InitListener(displayName = "SecurityModuleServiceListener", description = "org.platform.security���������֧�֡�", startIndex = 0)
public class SecurityModuleServiceListener extends AbstractModuleListener {
    /**��ʼ��.*/
    @Override
    public void initialize(ApiBinder event) {
        //1.ע��Aop 
        event.getGuiceBinder().bindInterceptor(new ClassPowerMatcher(), new MethodPowerMatcher(), new SecurityInterceptor());
        //2.Session�䶯֪ͨ���� 
        event.sessionListener().bind(SecuritySessionListener.class);
    }
    /*-------------------------------------------------------------------------------------*/
    /*���������Ƿ�ƥ�䡣����ֻҪ���ͻ򷽷��ϱ����@Power��*/
    private static class ClassPowerMatcher extends AbstractMatcher<Class<?>> {
        @Override
        public boolean matches(Class<?> matcherType) {
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
    private static class MethodPowerMatcher extends AbstractMatcher<Method> {
        @Override
        public boolean matches(Method matcherType) {
            if (matcherType.isAnnotationPresent(Power.class) == true)
                return true;
            if (matcherType.getDeclaringClass().isAnnotationPresent(Power.class) == true)
                return true;
            return false;
        }
    }
}