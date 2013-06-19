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
package org.more.workflow.util;
import org.more.workflow.context.ApplicationContext;
/**
 * �ڳ�ʼ��ʱ��ʹ�õ��ýӿڣ�������Ŀ��ӿڴ��ݳ�ʼ������������ϵͳ�����ġ�
 * Date : 2010-6-14
 * @author ������
 */
public interface Config {
    /**��ȡ����ʹ�õĲ������Ƽ��ϣ������Ե�������ʽ���ء�*/
    public Iterable<String> getParamNamesIterable();
    /**����keyֵ��ȡһ����ʼ��������keyֵ������getParamNamesIterable�������صĵ������д��ڵ����ԡ�����÷���������null��*/
    public Object getParam(String key);
};