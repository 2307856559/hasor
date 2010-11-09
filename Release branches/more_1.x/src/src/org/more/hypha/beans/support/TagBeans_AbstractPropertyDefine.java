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
package org.more.hypha.beans.support;
import java.util.HashMap;
import java.util.Map;
import org.more.LostException;
import org.more.core.xml.XmlStackDecorator;
import org.more.core.xml.stream.StartElementEvent;
import org.more.hypha.beans.TypeManager;
import org.more.hypha.beans.ValueMetaData;
import org.more.hypha.beans.define.AbstractPropertyDefine;
import org.more.hypha.configuration.DefineResourceImpl;
import org.more.util.StringConvert;
import org.more.util.attribute.AttBase;
/**
 * beans�����ռ�����Ա�ǩ�������ࡣ���಻�ᴦ������ֵԪ��Ϣ�Ľ����ⲿ����Ϣ�Ľ���������ר�б�ǩ������������{@link QuickPropertyParser}�ӿڸ�����
 * @version 2010-9-19
 * @author ������ (zyc@byshell.org)
 */
public abstract class TagBeans_AbstractPropertyDefine<T extends AbstractPropertyDefine> extends TagBeans_AbstractDefine<T> {
    /**����Ԫ��Ϣ.*/
    public static final String PropertyDefine = "$more_Beans_PropertyDefine";
    /**����{@link TagBeans_AbstractPropertyDefine}����*/
    public TagBeans_AbstractPropertyDefine(DefineResourceImpl configuration) {
        super(configuration);
    }
    /**���ԵĶ�������*/
    protected String getAttributeName() {
        return PropertyDefine;
    }
    /**�����������Ͷ���*/
    protected abstract T createDefine();
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
    /**��ʼ�������Ա�ǩ��*/
    public void beginElement(XmlStackDecorator context, String xpath, StartElementEvent event) {
        //1.��������
        super.beginElement(context, xpath, event);
        //2.������������classType��
        AbstractPropertyDefine pdefine = this.getDefine(context);
        {
            //1).��ͼ��typeת��ΪVariableTypeö��.
            String classType = event.getAttributeValue("type");
            if (classType == null)
                classType = "null";
            VariableType typeEnum = (VariableType) StringConvert.changeType(classType, VariableType.class);
            //2).���ת��ʧ����ֱ��ʹ��ClassLoaderװ��.
            Class<?> propType = null;
            if (typeEnum != null)
                propType = getBaseType(typeEnum);
            else
                try {
                    ClassLoader loader = Thread.currentThread().getContextClassLoader();
                    propType = loader.loadClass(classType);
                } catch (Exception e) {
                    throw new LostException("ClassNotFoundException,��������[" + classType + "]�޷���װ��.", e);
                }
            pdefine.setClassType(propType);
        }
        //3.��Ԫ�ض�����������Զ���ӵ�att�С�
        AttBase att = new AttBase();
        for (int i = 0; i < event.getAttributeCount(); i++)
            att.put(event.getAttributeName(i).getLocalPart(), event.getAttributeValue(i));
        //4.�����������ֵԪ��Ϣ
        TypeManager typeManager = this.getConfiguration().getTypeManager();
        ValueMetaData valueMETADATA = typeManager.parserType(event.getAttributeValue("value"), att, pdefine);
        if (valueMETADATA == null)
            throw new NullPointerException("ͨ��TypeManager��������Ԫ��Ϣ����ʧ�ܣ�����ֵΪ�ա�");
        pdefine.setValueMetaData(valueMETADATA);
    }
}