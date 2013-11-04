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
package org.hasor.test.simple.aop.interceptor;
import net.hasor.core.ApiBinder;
import net.hasor.core.plugin.Plugin;
import net.hasor.core.plugin.PluginFace;
import org.aopalliance.intercept.MethodInterceptor;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
@Plugin
public class GlobalAopInterceptor implements PluginFace {
    public void loadPlugin(ApiBinder apiBinder) {
        boolean global = apiBinder.getEnvironment().getSettings().getBoolean("testApp.globalAop", false);
        if (global == false)
            return;
        //
        Matcher matcher = Matchers.not( //�ų�����������
                Matchers.subclassesOf(MethodInterceptor.class));
        apiBinder.getGuiceBinder().bindInterceptor(//
                matcher, Matchers.any(), new AopInterceptor_A());
    }
}