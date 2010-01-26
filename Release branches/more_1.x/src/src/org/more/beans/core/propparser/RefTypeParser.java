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
package org.more.beans.core.propparser;
import org.more.DoesSupportException;
import org.more.beans.core.ResourceBeanFactory;
import org.more.beans.info.BeanDefinition;
import org.more.beans.info.BeanProp;
import org.more.beans.info.BeanProperty;
import org.more.beans.info.Prop;
import org.more.beans.info.PropRefValue;
import org.more.util.StringConvert;
/**
 * ��PropRefValue�������ö����ṩ�˽���֧�֡�
 * @version 2009-11-18
 * @author ������ (zyc@byshell.org)
 */
public class RefTypeParser implements PropertyParser {
    @Override
    public Object parser(Object context, Object[] contextParams, BeanProp prop, BeanProperty propContext, BeanDefinition definition, ResourceBeanFactory factory, PropertyParser contextParser) throws Exception {
        PropRefValue p = (PropRefValue) prop;
        String refType = p.getRefType();
        if (refType == PropRefValue.PRV_Bean)
            //��������bean���壨PRV_Bean��
            return factory.getBean(p.getRefValue(), contextParams);
        else if (refType == PropRefValue.PRV_Param) {
            //���ô���������PRV_Param��
            int paramIndex = StringConvert.parseInt(p.getRefValue(), 0);
            return (paramIndex < contextParams.length) ? contextParams[paramIndex] : null;
        } else if (refType == PropRefValue.PRV_Mime) {
            //Ԫ��Ϣ���ԣ�PRV_Mime��
            String attName = p.getRefValue();
            if (prop.containsKey(attName) == true)
                return prop.getAttribute(attName);
            else if (propContext.containsKey(attName) == true)
                return propContext.getAttribute(attName);
            else if (definition.containsKey(attName) == true)
                return definition.getAttribute(attName);
            else if (factory.contains(attName) == true)
                return factory.getAttribute(attName);
            else
                return null;
        } else if (refType == PropRefValue.PRV_ContextAtt) {
            //���������ԣ�PRV_ContextAtt��
            String attName = p.getRefValue();
            if (factory.contains(attName) == true)
                return factory.getAttribute(attName);
            else
                return null;
        } else
            throw new DoesSupportException("��֧�ֵ����÷�ʽ��Ŀǰ�汾ֻ֧�֡�PRV_Bean��PRV_Param��PRV_Mime��PRV_ContextAtt�����ָ�ʽ��");
    }
    @Override
    public Class<?> parserType(Object context, Object[] contextParams, BeanProp prop, BeanProperty propContext, BeanDefinition definition, ResourceBeanFactory factory, PropertyParser contextParser) throws Exception {
        PropRefValue p = (PropRefValue) prop;
        String refType = p.getRefType();
        String propType = p.getPropType();
        if (propType == null)
            propType = propContext.getPropType();
        if (propType != null)
            return Prop.getType(propType, factory.getBeanClassLoader());
        /*---------------*/
        if (refType == PropRefValue.PRV_Bean)
            //��������bean���壨PRV_Bean��
            return factory.getBeanType(p.getRefValue());
        else if (refType == PropRefValue.PRV_Param) {
            //���ô���������PRV_Param��
            int paramIndex = StringConvert.parseInt(p.getRefValue(), 0);
            return (paramIndex < contextParams.length) ? contextParams[paramIndex].getClass() : null;
        } else if (refType == PropRefValue.PRV_Mime) {
            //Ԫ��Ϣ���ԣ�PRV_Mime��
            String attName = p.getRefValue();
            if (prop.containsKey(attName) == true)
                return prop.getAttribute(attName).getClass();
            else if (propContext.containsKey(attName) == true)
                return propContext.getAttribute(attName).getClass();
            else if (definition.containsKey(attName) == true)
                return definition.getAttribute(attName).getClass();
            else if (factory.contains(attName) == true)
                return factory.getAttribute(attName).getClass();
            else
                return null;
        } else if (refType == PropRefValue.PRV_ContextAtt) {
            //���������ԣ�PRV_ContextAtt��
            String attName = p.getRefValue();
            if (factory.contains(attName) == true)
                return factory.getAttribute(attName).getClass();
            else
                return null;
        } else
            throw new DoesSupportException("��֧�ֵ����÷�ʽ��Ŀǰ�汾ֻ֧�֡�PRV_Bean��PRV_Param��PRV_Mime��PRV_ContextAtt�����ָ�ʽ��");
    }
}
