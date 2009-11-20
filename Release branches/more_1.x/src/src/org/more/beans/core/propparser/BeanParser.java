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
import org.more.beans.core.ResourceBeanFactory;
import org.more.beans.info.BeanDefinition;
import org.more.beans.info.BeanProp;
import org.more.beans.info.BeanProperty;
import org.more.beans.info.PropBean;
/**
 * ������Ϊ{@link PropBean}�����Զ����ṩ����֧�֣����{@link PropBean}û������beanDefinition������������������쳣������BeanParser�����ǲ���Ҫ����propType���Եġ�
 * <br/>Date : 2009-11-18
 * @author ������
 */
public class BeanParser implements PropertyParser {
    @Override
    public Object parser(Object context, Object[] contextParams, BeanProp prop, BeanProperty propContext, BeanDefinition definition, ResourceBeanFactory factory, PropertyParser contextParser) throws Exception {
        PropBean p = (PropBean) prop;
        BeanDefinition bean = p.getBeanDefinition();
        if (bean != null)
            return factory.getBean(bean.getName(), contextParams);
        else
            throw new NullPointerException("����[" + propContext.getName() + "]�����쳣���޷���������Ϊ�յ�Bean����");
    }
    @Override
    public Class<?> parserType(Object context, Object[] contextParams, BeanProp prop, BeanProperty propContext, BeanDefinition definition, ResourceBeanFactory factory, PropertyParser contextParser) throws Exception {
        PropBean p = (PropBean) prop;
        BeanDefinition bean = p.getBeanDefinition();
        return factory.getBeanType(bean.getName());
    }
}
