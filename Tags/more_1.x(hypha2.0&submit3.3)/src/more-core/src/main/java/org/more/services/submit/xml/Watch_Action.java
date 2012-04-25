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
package org.more.services.submit.xml;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.more.hypha.anno.KeepWatchParser;
import org.more.hypha.context.xml.XmlDefineResource;
import org.more.services.submit.Action;
import org.more.services.submit.ActionPack;
/**
 * ������Actionע��ʹ�á�
 * @version : 2011-7-15
 * @author ������ (zyc@byshell.org)
 */
class Watch_Action implements KeepWatchParser {
    private B_Config config = null;
    public Watch_Action(B_Config config) {
        this.config = config;
    }
    public void process(Object target, Annotation annoData, XmlDefineResource resource) {
        Method method = (Method) target;
        Class<?> type = method.getDeclaringClass();
        Action ac = (Action) annoData;
        ActionPack pack = type.getAnnotation(ActionPack.class);
        B_AnnoActionInfo mapping = new B_AnnoActionInfo();
        //1.������ʵAction·��
        mapping.actionPath = method;
        //2.����������
        if (pack != null)
            mapping.packageString = pack.value();
        //3.����ӳ���ַ
        mapping.mappingPath = ac.value();
        if (mapping.mappingPath.equals("") == true)
            if (mapping.packageString != null)
                mapping.mappingPath = type.getSimpleName() + "." + method.getName();
            else
                mapping.mappingPath = type.getName() + "." + method.getName();
        //4.��ӵ�ӳ��
        this.config.acMappingList.add(mapping);
    };
};