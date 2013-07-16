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
package org.more.classcode;
/**
 * �ýӿ��ǻ������Ժ�ί�����ԵĲ��Խӿڣ���Щ���ӵ������������class֮ǰ����ͨ���ýӿ���ȷ���Ƿ�������Լ����ԵĶ�д���ԡ�
 * @version 2010-9-3
 * @author ������ (zyc@byshell.org)
 */
public interface PropertyStrategy extends BaseStrategy {
    /**
     * �÷��������ھ����Ƿ���Ը��ӵ����ԡ��������true���ʾ����������ԣ������Ե����Բ����ٴε�����isReadOnly��isWriteOnly������
     * @param name ���ӵ���������
     * @param type ���ӵ��������͡�
     * @param isDelegate �������Ƿ���һ���������ԡ�
     * @return ���������������򷵻�true�����򷵻�false��
     */
    public boolean isIgnore(String name, Class<?> type, boolean isDelegate);
    /**
     * �÷�������ȷ����������������Ƿ�Ϊһ��ֻ�����ԡ�
     * @param name ���ӵ���������
     * @param type ���ӵ��������͡�
     * @param isDelegate �������Ƿ���һ���������ԡ�
     * @return �����������һ��ֻ�������򷵻�true�����򷵻�false��
     */
    public boolean isReadOnly(String name, Class<?> type, boolean isDelegate);
    /**
     * �÷�������ȷ����������������Ƿ�Ϊһ��ֻд���ԡ�
     * @param name ���ӵ���������
     * @param type ���ӵ��������͡�
     * @param isDelegate �������Ƿ���һ���������ԡ�
     * @return �����������һ��ֻд�����򷵻�true�����򷵻�false��
    */
    public boolean isWriteOnly(String name, Class<?> type, boolean isDelegate);
}