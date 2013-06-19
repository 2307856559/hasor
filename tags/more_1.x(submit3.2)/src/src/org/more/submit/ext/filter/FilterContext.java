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
package org.more.submit.ext.filter;
import java.util.Iterator;
import org.more.submit.ActionContext;
/**
 * FilterContext�ӿ���һ�������ӿڡ������ڻ���submit֧�����ǲ�����filter���Ե� �����filter���ԵĹ�����ͨ����
 * {@link ActionContext}��������filterװ�ζ��ṩ��filterװ������Ҫͨ������ӿ������Һ͹���filter��
 * @version : 2010-8-2
 * @author ������(zyc@byshell.org)
 */
public interface FilterContext {
    /**����ĳ��filter�����Ƿ���ڣ�������Ҫ���Ե�filter��������������ڷ���true���򷵻�false��*/
    public boolean containsFilter(String filterName);
    /**���Ҳ��ҷ���һ��filter���������� */
    public ActionFilter findFilter(String filterName);
    /** ��ȡһ��ָ����filter���ͣ�����Ϊfilter��������*/
    public Class<?> getFilterType(String filterName);
    /** ͨ��������������Ե�������filter��������*/
    public Iterator<String> getFilterNameIterator();
    /**������������filterName����ȡָ����filter���ԡ�*/
    public Object getFilterProperty(String filterName, String property);
}