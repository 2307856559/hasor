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
import java.util.HashSet;
import java.util.Set;
import org.more.beans.core.ResourceBeanFactory;
import org.more.beans.info.BeanDefinition;
import org.more.beans.info.BeanProp;
import org.more.beans.info.BeanProperty;
import org.more.beans.info.PropList;
import org.more.beans.info.PropSet;
/**
 * ������Ϊ{@link PropSet}�����Զ����ṩ����֧�֣����{@link PropSet}û�����þ���set������ʹ��{@link HashSet}��Ϊ{@link Set}���� 
 * <br/>Date : 2009-11-18
 * @author ������
 */
public class SetParser implements PropertyParser {
    @Override
    @SuppressWarnings("unchecked")
    public Object parser(Object context, Object[] contextParams, BeanProp prop, BeanProperty propContext, BeanDefinition definition, ResourceBeanFactory factory, PropertyParser contextParser) throws Exception {
        PropSet p = (PropSet) prop;
        Class<?> setType = this.parserType(context, contextParams, prop, propContext, definition, factory, contextParser);
        if (setType == Set.class)
            setType = HashSet.class;
        Set returnObject = (Set) setType.newInstance();
        //ѭ����������Ԫ�ض����Դ������Ԫ�ء�
        BeanProp[] element = p.getSetElements();
        for (int i = 0; i < element.length; i++) {
            BeanProp e = element[i];
            Object itemObject = null;
            if (e instanceof PropList)
                itemObject = this.parser(context, contextParams, e, propContext, definition, factory, contextParser);
            else
                itemObject = contextParser.parser(context, contextParams, e, propContext, definition, factory, contextParser);
            returnObject.add(itemObject);
        }
        return returnObject;
    }
    @Override
    public Class<?> parserType(Object context, Object[] contextParams, BeanProp prop, BeanProperty propContext, BeanDefinition definition, ResourceBeanFactory factory, PropertyParser contextParser) throws Exception {
        /*
         * propType���Թ���
         * 1.���PropSet������propType������ֱ�ӷ��أ����򷵻�propContext��propType���ԡ�
         * 2.���propContextҲû������propType�����򷵻�java.util.Set��
         */
        String propType = prop.getPropType();
        if (propType == null)
            propType = propContext.getPropType();
        if (propType == null)
            propType = "java.util.Set";
        return factory.getBeanClassLoader().loadClass(propType);
    }
}