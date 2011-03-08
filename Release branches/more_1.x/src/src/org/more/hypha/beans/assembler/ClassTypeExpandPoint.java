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
package org.more.hypha.beans.assembler;
import org.more.hypha.ApplicationContext;
import org.more.hypha.ExpandPoint;
import org.more.hypha.beans.AbstractBeanDefine;
/**
 * �ֽ���װ����չ�㣺����չ��λ��<b>���ʹ������ȡ�׶�</b>������չ��Ĺ����ǽ��ֽ������װ�س�ΪClass���Ͷ���
 * <br/>ע�⣺1.����չ������������˶��{@link ClassByteExpandPoint}��չ�㣬����չ�㽫������ִ�С�����ÿ��ִ��֮��������ͺ��ֽ������ݻᱻ����ڶ�����չ�㡣 
 * <br/>ע�⣺2.����{@link ClassByteExpandPoint}������չ��ִ�н��������һ��null����hyphaϵͳ��ʹ�����õ�classLoaderװ���ֽ��롣
 * <br/>��չ��ִ��˳��{@link ClassByteExpandPoint}-&gt<i><b>{@link ClassTypeExpandPoint}</b></i>-&gt{@link BeforeCreateExpandPoint}-&gt{@link AfterCreateExpandPoint}
 * @version 2011-3-1
 * @author ������ (zyc@byshell.org)
 */
public interface ClassTypeExpandPoint extends ExpandPoint {
    /**
     * ִ����չ������
     * @param beanType װ�ص�bean���ͣ�ע���������չ�㲻���κ��¡����뷵�ظò������ݡ�
     * @param tryType ����Ҫװ�ص����ͣ����ڸ���չ���״�ִ��ʱ�ò���Ϊ�գ���ִ�еڶ���{@link ClassTypeExpandPoint}������չ��ʱ�ò�����ʾΪ��һ����չ�㷵�ص����͡�
     * @param define bean���塣
     * @param context �����Ķ���
     * @return ����װ��֮����������ݡ�
     */
    public Class<?> decorateType(byte[] beanType, Class<?> tryType, AbstractBeanDefine define, ApplicationContext context);
};