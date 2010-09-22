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
import java.util.HashMap;
import java.util.Map;
import org.more.DoesSupportException;
import org.more.beans.ValueMetaData;
import org.more.beans.define.AbstractPropertyDefine;
import org.more.beans.define.VariableBeanDefine;
import org.more.beans.define.VariableBeanDefine.VariableType;
import org.more.core.xml.stream.StartElementEvent;
import org.more.util.StringConvert;
import org.more.util.attribute.StackDecorator;
/**
 * ���Ա�ǩ�������ࡣ
 * @version 2010-9-19
 * @author ������ (zyc@byshell.org)
 */
public abstract class TagBeans_AbstractPropertyDefine extends TagBeans_AbstractDefine {
    /**����Ԫ��Ϣ.*/
    public static final String PropertyDefine = "$more_PropertyDefine";
    /**���ԵĶ�������*/
    protected String getDefineName() {
        return PropertyDefine;
    };
    /**�����������Ͷ���*/
    protected abstract Object createDefine(StackDecorator context);
    /**����ͨ�õ����ԡ�*/
    public enum PropertyKey {
        value, boolLazyInit, classType, description
    };
    /**����������xml�����Զ�Ӧ��ϵ��*/
    protected Map<Enum<?>, String> getPropertyMappings() {
        HashMap<Enum<?>, String> propertys = new HashMap<Enum<?>, String>();
        //propertys.put(PropertyKey.classType, "type");
        propertys.put(PropertyKey.description, "description");
        return propertys;
    }
    /**��ʼ�������Ա�ǩ���÷����д�����һ��{@link StackDecorator}ջ��*/
    public void beginElement(StackDecorator context, String xpath, StartElementEvent event) {
        super.beginElement(context, xpath, event);
        AbstractPropertyDefine pdefine = (AbstractPropertyDefine) this.getDefine(context);
        //1.����classType����
        String classType = event.getAttributeValue("type");
        if (classType != null) {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try {
                VariableType varType = (VariableType) StringConvert.parseEnum(classType, VariableType.class, VariableType.Null);
                Class<?> value = VariableBeanDefine.getBaseType(varType);
                if (value == null && varType != VariableType.Null)
                    value = loader.loadClass(classType);
                pdefine.setClassType(value);
            } catch (Exception e) {
                throw new DoesSupportException("��������[" + classType + "]��ʧ.", e);
            }
        }
        //2.������ʱ����Ԫ��Ϣ����
        Temp_ValueMetaData temp = new Temp_ValueMetaData();
        temp.value = event.getAttributeValue("value");
        temp.enumeration = event.getAttributeValue("enum");
        temp.refBean = event.getAttributeValue("refBean");
        temp.file = event.getAttributeValue("file");
        temp.directory = event.getAttributeValue("directory");
        temp.uriLocation = event.getAttributeValue("uriLocation");
        temp.date = event.getAttributeValue("date");
        temp.format = event.getAttributeValue("format");
        temp.refScope = event.getAttributeValue("refScope");
        pdefine.setValueMetaData(temp);
    }
    /**
     * ValueMetaData��һ����ʱʵ��
     * @version 2010-9-21
     * @author ������ (zyc@byshell.org)
     */
    public class Temp_ValueMetaData extends ValueMetaData {
        public String value       = null; // event.getAttributeValue("value");
        public String enumeration = null; //= event.getAttributeValue("enum");
        public String refBean     = null; //= event.getAttributeValue("refBean");
        public String file        = null; //= event.getAttributeValue("file");
        public String directory   = null; //= event.getAttributeValue("directory");
        public String uriLocation = null; //= event.getAttributeValue("uriLocation");
        public String date        = null; //= event.getAttributeValue("date");
        public String format      = null; // = event.getAttributeValue("format");
        public String refScope    = null; // = event.getAttributeValue("refScope");
        public PropertyMetaTypeEnum getPropertyType() {
            return null;
        }
    }
}