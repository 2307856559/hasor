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
import org.more.beans.define.BigText_ValueMetaData;
import org.more.core.xml.XmlTextHook;
import org.more.core.xml.stream.EndElementEvent;
import org.more.core.xml.stream.TextEvent;
import org.more.util.attribute.StackDecorator;
/**
 * ���ڽ���bigText��ǩ
 * @version 2010-9-22
 * @author ������ (zyc@byshell.org)
 */
public class TagBeans_BigText extends TagBeans_AbstractValueMetaDataDefine implements XmlTextHook {
    /**�������������еĽű�ֵ����*/
    private static final String BigText = "$more_BigText";
    /**����{@link BigText_ValueMetaData}����*/
    protected Object createDefine(StackDecorator context) {
        return new BigText_ValueMetaData();
    }
    /**����null��*/
    protected Map<Enum<?>, String> getPropertyMappings() {
        return null;
    }
    /**����������ǩ���÷�������д�����õ�CDATA��Ϣ��*/
    public void endElement(StackDecorator context, String xpath, EndElementEvent event) {
        BigText_ValueMetaData define = (BigText_ValueMetaData) this.getDefine(context);
        if (define.getTextValue() == null) {
            StringBuffer scriptText = (StringBuffer) context.getAttribute(BigText);
            if (scriptText != null)
                define.setTextValue(scriptText.toString());
        }
        super.endElement(context, xpath, event);
    }
    /**����CDATA������*/
    public void text(StackDecorator context, String xpath, TextEvent event) {
        if (event.isCommentEvent() == true)
            return;
        StringBuffer scriptText = (StringBuffer) context.getAttribute(BigText);
        if (scriptText == null) {
            scriptText = new StringBuffer();
            context.setAttribute(BigText, scriptText);
        }
        String value = event.getTrimText();
        if (value != null)
            scriptText.append(value);
    };
}