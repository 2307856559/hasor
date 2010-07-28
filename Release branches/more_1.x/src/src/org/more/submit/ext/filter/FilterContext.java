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
import org.more.CastException;
import org.more.NoDefinitionException;
import org.more.submit.ActionContext;
/**
 * FilterContext�ӿ���{@link ActionFilter}�ӿڵ������������ڻ���submit֧�����ǲ�����filter���Ե� ��
 * ���Filter���ԵĹ�����ͨ����{@link ActionContext}��������Filterװ�������ṩ��
 * �����ActionContext�ӿڵ�Filterװ������Ҫһ��FilterContext������������filter���Ծ�����FilterContext����ӿڡ�
 * @version : 2010-7-26
 * @author ������(zyc@byshell.org)
 */
public interface FilterContext {
    /**����ĳ��Action�����Ƿ���ڣ�������Ҫ���Ե�action��������������ڷ���true���򷵻�false���÷���������������ĵ�һ�͵ڶ����ڡ�*/
    public boolean containsFilter(String filterName);
    /**���Ҳ��ҷ���һ��Action���������� */
    public ActionFilter findFilter(String filterName) throws NoDefinitionException, CastException;
    /** ��ȡһ��ָ����Action���ͣ�����Ϊaction��������*/
    public Class<?> getFilterType(String filterName);
    /** ͨ��������������Ե�������Action��������*/
    public Iterator<String> getFilterNameIterator();
    /**������������filterName����ȡָ����Action��*/
    public Object getFilterProperty(String filterName, String property);
}