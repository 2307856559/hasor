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
package org.more.hypha.beans;
import org.more.hypha.ApplicationContext;
/**
 * {@link ValueMetaData}ֵԪ��Ϣ����������ʵ����Ծ�����ν������Ԫ��Ϣ�����ҽ������Ľ�����ء�
 * ����ڽ����ڼ���Ҫ�ݹ����ֵԪ��Ϣ���������ߵ�����������ֵԪ��Ϣ��������ô����ͨ��rootParser����������е��á�
 * @version 2011-1-18
 * @author ������ (zyc@byshell.org)
 */
public interface ValueMetaDataParser<T extends ValueMetaData> {
    /**
     * ����Ԫ��Ϣ�����ҽ������Ķ��������ء�
     * @param data Ҫ������Ԫ��Ϣ����
     * @param rootParser ����������
     * @param context {@link ApplicationContext}�ӿڶ���
     * @return ���ؽ����Ľ����
     */
    public Object parser(T data, ValueMetaDataParser<T> rootParser, ApplicationContext context) throws Throwable;
    /**
     * ����Ԫ��Ϣ�����ҽ������Ķ������ͷ��ء�
     * @param data Ҫ������ֵԪ��Ϣ����
     * @param rootParser ����������
     * @param context {@link ApplicationContext}�ӿڶ���
     * @return ���ؽ����Ľ����
     */
    public Class<?> parserType(T data, ValueMetaDataParser<T> rootParser, ApplicationContext context) throws Throwable;
};