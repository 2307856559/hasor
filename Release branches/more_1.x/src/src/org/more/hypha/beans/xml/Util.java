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
package org.more.hypha.beans.xml;
import org.more.core.error.DefineException;
import org.more.hypha.AbstractBeanDefine;
import org.more.hypha.beans.define.AbstractBaseBeanDefine;
import org.more.hypha.context.xml.XmlDefineResource;
/**
 * xml���������еĹ��ߡ�
 * @version 2011-6-21
 * @author ������ (zyc@byshell.org)
 */
public class Util {
    public static AbstractBeanDefine passerUseTemplate(String useTemplate, AbstractBaseBeanDefine define, XmlDefineResource beanDefineManager) {
        /*1.useTemplate����*/
        if (useTemplate != null) {
            AbstractBeanDefine template = null;
            if (beanDefineManager.containsBeanDefine(useTemplate) == true)
                template = beanDefineManager.getBeanDefine(useTemplate);
            else {
                /**��bean�������ڰ����ҡ�*/
                String packageStr = define.getPackage();
                packageStr = (packageStr == null) ? useTemplate : packageStr + "." + useTemplate;
                template = beanDefineManager.getBeanDefine(packageStr);
            }
            //
            if (template == null)
                throw new DefineException("[" + define.getName() + "]�Ҳ���[" + useTemplate + "]��Beanģ�嶨��.");
            return template;
        } else
            return null;
    }
};