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
package org.more.workflow.state;
import org.more.util.attribute.IAttribute;
/**
 * ״̬����ӿڡ�״̬���棬��ģ�Ͷ���Ҫ���ָ�һ���߳�ʹ��ʱģ�ͱ��������һЩ������Ϣ��Ҫ�ݴ�������
 * �ѱ�����ԭ�߳��ٴ�ʹ�ã�����һ��ģ�Ͷ���Ϳ��Է����ںܶ���̡߳�ÿ���л��߳�ʱģ�͵�������Ϣ������Ҫ�ظ���
 * ������ٴλָ������ýӿھ����ṩһ��ʹģ�Ϳ���֧���������ܵķ�������Ȼ����֮�������������ӿڶ�����ϵͳ��һ�����ա�
 * Date : 2010-6-16
 * @author ������
 */
public interface StateCache {
    /**����ģ�͵�״̬��IAttribute�ӿ��У���ģ��ִ�б���״̬֮������ٴ�ִ��recoverState�������Խ���������ݻָ���ע��ÿ��ģ��ֻ��ӵ��һ��ո��״̬�㡣*/
    public void saveState(IAttribute states);
    /**�ָ��Ѿ�����ı���ģ�͵�״̬��*/
    public void recoverState(IAttribute states);
};