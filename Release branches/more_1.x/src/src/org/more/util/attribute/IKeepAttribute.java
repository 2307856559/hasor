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
/**  */
package org.more.util.attribute;
import org.more.NoDefinitionException;
import org.more.ReadOnlyException;
/**
 *    �������Խӿڡ��ýӿ���չ��Attribute�ӿڣ����ҽ�������Ա��������⡣ʹ�������ڱ�ɾ������
 * ���滻ʱ����ȷ������Ϊ��ͨ���ýӿڿ��Խ���������Ϊ�������Ի�������ԡ�
 *    �������ԣ�����������ָ�����óɱ��ֵ������Ƿ���Ը���������ֵ����һ����ExtAttribute�ӿڵ�
 * ReplaceMode_Throw���Ժ��񡣲�ͬ����ExtAttribute�ӿ�������������ԣ���KeepAttribute�ӿ�
 * �����һ�����ԡ������ó�Ϊ���ֵ����Կ���ͨ��removeAttribute����ɾ��������ɾ��һ��ӵ�б���
 * ���Ե�����ʱ����ͬʱɾ���䱣�����ԡ�
 *    �������ԣ�����������ָ�����Կ��Ա��������õ��ǲ����Ա�ɾ�������ñ������Կ��Ա�֤ĳЩ�����
 * ϵͳ�����ܹ����ʵ�ĳЩ���ԣ�������Щ���Ա��벻�ܲ����ڡ�
 *    ��ʾ���������Ժͱ������Կ���ͬʱ���õ�һ�������ϡ�
 * Date : 2009-4-28
 * @author ������
 */
public interface IKeepAttribute extends IAttribute {
    /**
     * �������ԣ���������Ѿ�������Ϊ���������������ReadOnlyException�쳣��
     * @param name Ҫ�������������
     * @param value Ҫ���������ֵ��
     * @throws ReadOnlyException ��������õ�����ӵ�б����������������쳣��
     */
    public void setAttribute(String name, Object value) throws ReadOnlyException;
    /**
     * ���������Լ�����ɾ��ָ�����ԡ������ɾ��������ӵ�б��������������MustException�쳣��
     * @param name Ҫɾ�����������ơ�
     * @throws UnsupportedOperationException �����ɾ��������ӵ�б����������������쳣��
     */
    public void removeAttribute(String name) throws UnsupportedOperationException;
    /**
     * ���������Ƿ��Ǳ��ֵġ������ͼ��һ�������ڵ��������ñ�������ʱ����NoDefinitionException�쳣��
     * @param attName Ҫ���ñ������Ե���������
     * @param isKeep �������ֵΪtrue���ʾ�����䱣�����ԣ������ȡ���䱣�����ԡ�
     * @throws NoDefinitionException δ�����쳣�������ͼ��һ�������ڵ��������ñ�������ʱ������
     */
    public void setKeep(String attName, boolean isKeep) throws NoDefinitionException;
    /**
     * ����ĳ�������Ƿ�ӵ�б������ԡ��������һ�������ڵ����Ը÷�����ʼ�շ���false��
     * @param attName �����Ե���������
     * @return ����ĳ�������Ƿ�ӵ�б������ԡ�
     */
    public boolean isKeep(String attName);
    /**
     * ���������Ƿ��Ǳ���ġ������ͼ��һ�������ڵ��������ñ�������ʱ����NoDefinitionException�쳣��
     * @param attName Ҫ���ñ������Ե���������
     * @param isMaster �������ֵΪtrue���ʾ������������ԣ������ȡ����������ԡ�
     * @throws NoDefinitionException δ�����쳣�������ͼ��һ�������ڵ��������ñ�������ʱ������
     */
    public void setMaster(String attName, boolean isMaster) throws NoDefinitionException;
    /**
     * ����ĳ�������Ƿ�ӵ�б������ԡ��������һ�������ڵ����Ը÷�����ʼ�շ���false��
     * @param attName �����Ե���������
     * @return ����ĳ�������Ƿ�ӵ�б������ԡ�
     */
    public boolean isMaster(String attName);
}
