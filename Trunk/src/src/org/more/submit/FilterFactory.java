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
 * ��������������ͨ���ýӿڿ��Ի�ȡ��Ӧ��Action��������
 * Date : 2009-6-29
 * @author ������
 */
public interface FilterFactory {
    /**
     * ����ָ�����ƵĹ�����������Ҳ���ָ�����ƵĹ������򷵻�null��
     * @param name ����������
     * @return ���ز��ҵ��Ĺ�������������Ҳ���ָ�����ƵĹ������򷵻�null��
     */
    public ActionFilter findFilter(String name);
    /**
     * ��ȡȫ�ֹ��������ơ�
     * @return ����ȫ�ֹ��������ơ�
     */
    public String[] findPublicFilterNames();
    /**
     * ��ȡ���й��������ơ�
     * @return �������й��������ơ�
     */
    public String[] findFilterNames();
    /**
     * ���Ҳ�����ָ��filter��ĳһ�����ԣ����Ҳ���ʱ�򷵻�null��
     * @param name Ҫ���ҵ�filter��
     * @param propName Ҫ���ҵ�������
     * @return ���ز��Ҳ�����ָ��filter��ĳһ�����ԣ����Ҳ���ʱ�򷵻�null��
     */
    public Object findFilterProp(String name, String propName);
}