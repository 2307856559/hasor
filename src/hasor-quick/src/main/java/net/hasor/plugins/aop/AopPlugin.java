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
package net.hasor.plugins.aop;
import java.lang.reflect.Method;
import net.hasor.core.ApiBinder;
import net.hasor.core.ApiBinder.Matcher;
import net.hasor.core.binder.matcher.AopMatchers;
import net.hasor.core.plugin.AbstractHasorPlugin;
import net.hasor.quick.plugin.Plugin;
/**
 * �ṩ <code>@Aop</code>ע�� ����֧�֡�
 * @version : 2013-9-13
 * @author ������ (zyc@byshell.org)
 */
@Plugin
public class AopPlugin extends AbstractHasorPlugin {
    public void loadPlugin(ApiBinder apiBinder) {
        //2.@Aop������
        Matcher<Class<?>> matcherClass = AopMatchers.annotatedWithClass(Aop.class);//
        Matcher<Method> matcherMethod = AopMatchers.annotatedWithMethod(Aop.class);//
        apiBinder.bindInterceptor(matcherClass, matcherMethod, new AopInterceptor(apiBinder));
    }
}