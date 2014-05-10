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
package net.hasor.plugins.result;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.hasor.core.ApiBinder.Matcher;
import net.hasor.core.Hasor;
import net.hasor.plugins.aop.matchers.AopMatchers;
import net.hasor.plugins.controller.AbstractController;
import net.hasor.plugins.controller.Controller;
import net.hasor.plugins.restful.RestfulService;
import net.hasor.quick.plugin.Plugin;
import net.hasor.web.WebApiBinder;
import net.hasor.web.plugin.AbstractWebHasorPlugin;
/**
 * 
 * @version : 2013-9-26
 * @author ������ (zyc@byshell.org)
 */
@Plugin
public class ResultPlugin extends AbstractWebHasorPlugin {
    public void loadPlugin(WebApiBinder apiBinder) {
        Map<Class<?>, Class<ResultProcess>> defineMap = new HashMap<Class<?>, Class<ResultProcess>>();
        //1.��ȡ
        Set<Class<?>> resultDefineSet = apiBinder.findClass(ResultDefine.class);
        if (resultDefineSet == null)
            return;
        //2.ע�����
        for (Class<?> resultDefineType : resultDefineSet) {
            if (ResultProcess.class.isAssignableFrom(resultDefineType) == false) {
                Hasor.logWarn("loadResultDefine : not implemented ResultProcess. class=%s", resultDefineType);
                continue;
            }
            ResultDefine resultDefineAnno = resultDefineType.getAnnotation(ResultDefine.class);
            Class<ResultProcess> defineType = (Class<ResultProcess>) resultDefineType;
            Class<?> resultType = resultDefineAnno.value();
            Hasor.logInfo("loadResultDefine annoType is %s toInstance %s", resultType, resultDefineType);
            defineMap.put(resultType, defineType);
        }
        {
            /*���м̳� AbstractController ���ұ���� @Controller ע����඼�ǿ�����*/
            Matcher<Class<?>> matcherController = AopMatchers.subClassesOf(AbstractController.class);
            matcherController = AopMatchers.createDevice(matcherController).and(AopMatchers.annotatedWithClass(Controller.class));
            ResultCaller_Controller caller_1 = new ResultCaller_Controller(apiBinder, defineMap);
            apiBinder.bindInterceptor(matcherController, AopMatchers.anyMethod(), caller_1);
        }
        {
            /*���б���� @RestfulService ע����඼��Restful����*/
            ResultCaller_Restful caller_2 = new ResultCaller_Restful(apiBinder, defineMap);
            apiBinder.bindInterceptor(AopMatchers.annotatedWithClass(RestfulService.class), AopMatchers.anyMethod(), caller_2);
        }
    }
}