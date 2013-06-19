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
package org.more.core.database;
import java.util.List;
import java.util.Map;
/**
 * ����ѯ������з�ҳ��ȡ��
 * @version : 2011-11-10
 * @author ������ (zyc@byshell.org)
 */
public interface PagesList {
    /**��ȡһ��ֵ����ֵ��ʾ��ҳ��ҳ��С��*/
    public int getPageSize();
    /**��ȡ��ǰ��ҳ���ݵ�������¼����������ѯ�����λ�á�*/
    public int getIndex();
    /**��ȡ��ҳ��*/
    public int getPageCount();
    /**��ȡ��ǰҳ��*/
    public int getCurrentPageNumber();
    //
    /**ִ�е�ǰ��ҳ��ѯ�����ؽ��ʹ��Map��װ��*/
    public List<Map<String, Object>> query();
    /**ִ�е�ǰ��ҳ��ѯ�����ؽ��ʹ�ø��������ͷ�װ��*/
    public <T> List<T> query(Class<T> dataType);
    //
    /**��ѯ��һҳ���ҷ��ط�ҳ����*/
    public PagesList firstPage();
    /**��ѯ��һҳ���ҷ��ط�ҳ����*/
    public PagesList previousPage();
    /**��ѯ��һҳ���ҷ��ط�ҳ����*/
    public PagesList nextPage();
    /**��ѯ���һҳ���ҷ��ط�ҳ����*/
    public PagesList lastPage();
}