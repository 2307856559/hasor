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
package org.more.beans.resource.namespace;
import java.util.Map;
import org.more.beans.define.ScriptBeanDefine;
import org.more.core.xml.XmlTextHook;
import org.more.core.xml.stream.EndElementEvent;
import org.more.core.xml.stream.StartElementEvent;
import org.more.core.xml.stream.TextEvent;
import org.more.util.attribute.StackDecorator;
/**
 * ���ڽ���/beans/scriptBean��ǩ
 * @version 2010-9-16
 * @author ������ (zyc@byshell.org)
 */
public class TagBeans_ScriptBean extends TagBeans_TemplateBean implements XmlTextHook {
    /**�������������еĽű�ֵ����*/
    private static final String ScriptText = "$more_ScriptText";
    /**����ScriptBeanDefine���Ͷ���*/
    protected Object createDefine(StackDecorator context) {
        return new ScriptBeanDefine();
    }
    /**����ű�Bean����*/
    public enum PropertyKey {
        language, scriptText, sourcePath
    };
    /**����������xml�����Զ�Ӧ��ϵ��*/
    protected Map<Enum<?>, String> getPropertyMappings() {
        Map<Enum<?>, String> propertys = super.getPropertyMappings();
        propertys.put(PropertyKey.language, "language");
        propertys.put(PropertyKey.scriptText, "scriptText");
        propertys.put(PropertyKey.sourcePath, "sourcePath");
        return propertys;
    }
    /**��ʼ������ǩ���÷������ڶ�ȡimplements���ԡ�*/
    public void beginElement(StackDecorator context, String xpath, StartElementEvent event) {
        super.beginElement(context, xpath, event);
        ScriptBeanDefine define = (ScriptBeanDefine) this.getDefine(context);
        String impl = event.getAttributeValue("implements");
        if (impl != null)
            define.addImplement(impl);
    }
    /**����������ǩ���÷�������д�����õ�CDATA��Ϣ��*/
    public void endElement(StackDecorator context, String xpath, EndElementEvent event) {
        ScriptBeanDefine define = (ScriptBeanDefine) this.getDefine(context);
        if (define.getScriptText() == null) {
            StringBuffer scriptText = (StringBuffer) context.getAttribute(ScriptText);
            if (scriptText != null)
                define.setScriptText(scriptText.toString());
        }
        super.endElement(context, xpath, event);
    }
    /**�ű�����CDATA������*/
    public void text(StackDecorator context, String xpath, TextEvent event) {
        if (event.isCommentEvent() == true)
            return;
        StringBuffer scriptText = (StringBuffer) context.getAttribute(ScriptText);
        if (scriptText == null) {
            scriptText = new StringBuffer();
            //����Ҫ��ȷɾ��������ΪendElement�����������ֱ����յ�ǰ��ջ
            context.setAttribute(ScriptText, scriptText);
        }
        String value = event.getTrimText();
        if (value != null)
            scriptText.append(value);
    };
}