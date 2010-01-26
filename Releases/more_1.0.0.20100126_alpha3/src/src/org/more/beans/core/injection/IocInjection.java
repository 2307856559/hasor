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
package org.more.beans.core.injection;
import java.lang.reflect.Method;
import org.more.beans.core.ResourceBeanFactory;
import org.more.beans.core.propparser.MainPropertyParser;
import org.more.beans.info.BeanDefinition;
import org.more.beans.info.BeanProperty;
import org.more.beans.info.IocTypeEnum;
/**
 * ������ʵ����{@link IocTypeEnum#Ioc Ioc}����ע�뷽ʽ�����ַ�ʽʹ��java.lang.reflect���е�����з��������ʵ������ע�롣
 * ��IocInjection��������д�뷽������set + ������(����ĸ��д) �������������������ҵ�֮���
 * ��������{@link BeanProperty}�С�
 * @version 2009-11-7
 * @author ������ (zyc@byshell.org)
 */
public class IocInjection implements Injection {
    //========================================================================================Field
    /** ���Ի�����󣬻����������� */
    private String             propCatchName = "$more_Injection_Ioc";
    /**���Խ�����*/
    private MainPropertyParser propParser    = null;
    //==================================================================================Constructor
    /**����һ��IocInjection���󣬴���ʱ����ָ�����Խ�������*/
    public IocInjection(MainPropertyParser propParser) {
        if (propParser == null)
            throw new NullPointerException("����ָ��propParser��������IocInjectionʹ��������Խ������������ԡ�");
        this.propParser = propParser;
    }
    //==========================================================================================Job
    /** ʹ��set + ������(����ĸ��д)����������Ŀ�귴��ע�뷽���� */
    @Override
    public Object ioc(Object object, Object[] params, BeanDefinition definition, ResourceBeanFactory context) throws Exception {
        BeanProperty[] bps = definition.getPropertys();
        if (bps == null)
            return object;
        for (int i = 0; i < bps.length; i++) {
            BeanProperty prop = bps[i];
            Method writeMethod = null;
            //���if��������7���������ٶȣ�BeanDefinition����Դ�������ӵ�л��湦�ܵ�ǰ���¡�
            if (prop.containsKey(this.propCatchName) == false) {
                //ת������ĸ��д
                StringBuffer sb = new StringBuffer(prop.getName());
                char firstChar = sb.charAt(0);
                sb.delete(0, 1);
                sb.insert(0, (char) ((firstChar >= 97) ? firstChar - 32 : firstChar));
                sb.insert(0, "set");
                Class<?> mt = this.propParser.parserType(context, params, prop.getRefValue(), prop, definition);
                writeMethod = object.getClass().getMethod(sb.toString(), mt);
                prop.setAttribute(this.propCatchName, writeMethod);
            } else
                writeMethod = (Method) prop.get(this.propCatchName);
            //
            Object obj = propParser.parser(object, params, prop.getRefValue(), prop, definition);
            writeMethod.invoke(object, obj);
        }
        return object;
    }
}