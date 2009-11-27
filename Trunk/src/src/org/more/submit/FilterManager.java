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
package org.more.submit;
/**
 * ���������������ýӿڸ���������й������������ṩ�����ض�actionװ������������
 * ���ҷ���װ��֮���Action�������ͨ��ִ�д��������Դﵽִ����������Ŀ�ġ�
 * Date : 2009-6-29
 * @author ������
 */
public class FilterManager {
    private FilterFactory factory = null; //���ع���������
    //===============================================================
    /**
    * װ��ָ��Action���󣬸����ض�actionװ������������
    * ���ҷ���װ��֮���Action�������
    * @param action Ҫװ���Ŀ��Action����
     * @param filters 
    * @return ����װ��֮���Action�������
    */
    public PropxyAction installFilter(PropxyAction action, String[] filters) {
        if (action == null)
            return null;
        // ��װ��˽�й�������
        ActionFilterChain chain = new ActionFilterChain(action, null, null);//���մ������
        for (String fname : filters) {
            chain = new ActionFilterChain(action, this.findFilter(fname), chain);//�����������
            chain.setName(fname);
            chain.setTarget(action.getFinalTarget());
        }
        //װ��ȫ�ֹ�����
        String[] publicNS = this.factory.findPublicFilterNames();
        for (String fname : publicNS) {
            chain = new ActionFilterChain(action, this.findFilter(fname), chain);//�����������
            chain.setName(fname);
            chain.setTarget(action.getFinalTarget());
        }
        return chain;
    }
    /**
     * ����ĳһ���ƵĹ�����������ҵ�Ŀ��������򷵻ع��������󣬷��򷵻�null��
     * @return ���ز��ҵĹ�������
     */
    public ActionFilter findFilter(String name) {
        if (factory != null)
            return factory.findFilter(name);
        else
            return null;
    }
    /**
     * ��ù�������������
     * @return ���ع�������������
     */
    public FilterFactory getFactory() {
        return factory;
    }
    /**
     * ���ù�������������
     * @param factory Ҫ���õ�Ŀ�������������
     */
    public void setFactory(FilterFactory factory) {
        this.factory = factory;
    }
}
