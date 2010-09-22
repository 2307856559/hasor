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
import java.lang.reflect.Method;
import java.util.Map;
import org.more.DoesSupportException;
import org.more.PropertyException;
import org.more.core.xml.XmlElementHook;
import org.more.core.xml.stream.EndElementEvent;
import org.more.core.xml.stream.StartElementEvent;
import org.more.util.StringConvert;
import org.more.util.StringUtil;
import org.more.util.attribute.StackDecorator;
/**
 * ����bean�Ļ������������ڸ����ж�����һЩ�����Եķ�����
 * @version 2010-9-16
 * @author ������ (zyc@byshell.org)
 */
public abstract class TagBeans_AbstractDefine implements XmlElementHook {
    /**��������*/
    protected abstract String getDefineName();
    /**�����������Ͷ���*/
    protected abstract Object createDefine(StackDecorator context);
    /**��ȡһ�����壬���û�о͵���createDefine������������*/
    protected final Object getDefine(StackDecorator context) {
        String defineName = getDefineName();
        Object define = context.getAttribute(defineName);
        if (define != null)
            return define;
        define = this.createDefine(context);
        context.setAttribute(defineName, define);
        return define;
    }
    /**�÷�������һ��Map��Map��key������Ƕ��������������ԣ���Value�б�����Ƕ�Ӧ��XMLԪ����������*/
    protected abstract Map<Enum<?>, String> getPropertyMappings();
    /**����ĳ�����Ƶķ������÷���������һ��������*/
    private Method findMethod(String methodName, Class<?> type) {
        for (Method m : type.getMethods())
            if (m.getName().equals(methodName) == true)
                if (m.getParameterTypes().length == 1)
                    return m;
        return null;
    }
    /**ִ������ע�룬����ע��int,short,long,�Ȼ�������֮��÷�����֧��ע��ö�����͡�*/
    protected final void putAttribute(Object define, String attName, Object value) {
        if (define == null || attName == null)
            throw new NullPointerException("����������Ҫע���������Ϊ�ա�");
        //1.���ҷ���
        String methodName = "set" + StringUtil.toUpperCase(attName);
        Method writeMethod = this.findMethod(methodName, define.getClass());
        if (writeMethod == null)
            throw new DoesSupportException(define.getClass().getSimpleName() + "�������в�����[" + methodName + "]������");
        try {
            //2.ִ������ת��
            Class<?> toType = writeMethod.getParameterTypes()[0];
            Object attValueObject = StringConvert.changeType(value, toType);
            //3.ִ������ע��
            writeMethod.invoke(define, attValueObject);
        } catch (Exception e) {
            throw new PropertyException("�޷���Xml�ж����" + attName + ",����д��[" + define + "]�Ķ���.", e);
        }
    };
    /**��ʼ������ǩ�����а���������ӦBean�ͽ����������ԡ�*/
    public void beginElement(StackDecorator context, String xpath, StartElementEvent event) {
        context.createStack();
        //1.����BeanDefine
        Object define = this.getDefine(context);
        //2.����BeanDefine��ֵ
        Map<Enum<?>, String> propertys = this.getPropertyMappings();
        if (propertys == null)
            return;
        for (Enum<?> att : propertys.keySet()) {
            String definePropertyName = att.name();
            String xmlPropertyName = propertys.get(att);
            //
            String xmlPropertyValue = event.getAttributeValue(xmlPropertyName);
            if (xmlPropertyValue == null)
                continue;
            this.putAttribute(define, definePropertyName, xmlPropertyValue);
        }
    };
    /**����������ǩ��*/
    public void endElement(StackDecorator context, String xpath, EndElementEvent event) {
        context.dropStack();
    }
}