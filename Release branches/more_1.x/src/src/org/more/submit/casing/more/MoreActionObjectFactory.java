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
package org.more.submit.casing.more;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.more.CastException;
import org.more.NoDefinitionException;
import org.more.beans.BeanFactory;
import org.more.beans.info.BeanDefinition;
import org.more.submit.ActionFilter;
import org.more.submit.ActionObjectFactory;
import org.more.submit.annotation.Filter;
/**
 * More����ṩ�����ActionObjectFactory�ӿ�ʵ�֡�
 * @version 2010-1-9
 * @author ������ (zyc@byshell.org)
 */
class MoreActionObjectFactory implements ActionObjectFactory {
    private BeanFactory factory;
    public MoreActionObjectFactory(BeanFactory factory) {
        this.factory = factory;
    };
    @Override
    public boolean contains(String name) {
        return this.factory.contains(name);
    };
    @Override
    public Object findObject(String name) throws NoDefinitionException {
        try {
            return factory.getBean(name);
        } catch (Exception e) {
            throw new NoDefinitionException("�ڲ��Ҷ���[" + name + "]ʱ�����쳣", e);
        }
    };
    @Override
    public Iterator<String> getObjectNameIterator() {
        return this.factory.getBeanResource().getBeanDefinitionNames().iterator();
    }
    @Override
    public Class<?> getObjectType(String name) {
        return factory.getBeanType(name);
    };
    @Override
    public ActionFilter getActionFilter(String filterName) {
        try {
            Object filter = factory.getBean(filterName);
            if (filter instanceof ActionFilter == false)
                throw new CastException("�ɹ���ȡ������[" + filterName + "]���Ǹö�������Ч��ActionFilter���͡�");
            return (ActionFilter) filter;
        } catch (Exception e) {
            throw new NoDefinitionException("�ڲ��ҹ���������[" + filterName + "]ʱ�����쳣", e);
        }
    };
    @Override
    public Iterator<String> getPublicFilterNames(String actionName) {
        List<String> beans = this.factory.getBeanResource().getBeanDefinitionNames();//��ȡ����bean���Ƽ���
        ArrayList<String> ns = new ArrayList<String>(beans);//�½����Ƽ��ϣ�����ͬ��������beans��
        for (String n : beans) {
            //ѭ������ɾ������û������Ϊ���й�����������
            Filter filter = this.factory.getBeanType(n).getAnnotation(Filter.class);
            if (filter != null)
                if (filter.isPublic() == true)
                    continue;
            Object strIsPublicFilter = this.factory.getBeanResource().getBeanDefinition(n).getAttribute("isPublicFilter");
            if (strIsPublicFilter == null || strIsPublicFilter.toString().equals("true") == false)
                ns.remove(n);
        }
        return ns.iterator();
    };
    @Override
    public Iterator<String> getPrivateFilterNames(String actionName) {
        BeanDefinition beanDefinition = this.factory.getBeanResource().getBeanDefinition(actionName);
        Object privateFilters = beanDefinition.getAttribute("actionFilters");
        if (privateFilters == null)
            return new ArrayList<String>(0).iterator();
        return Arrays.asList(privateFilters.toString().split(",")).iterator();
    };
}