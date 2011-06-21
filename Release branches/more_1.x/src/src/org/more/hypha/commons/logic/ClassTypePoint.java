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
package org.more.hypha.commons.logic;
import org.more.hypha.AbstractBeanDefine;
import org.more.hypha.ApplicationContext;
import org.more.hypha.ExpandPoint;
/**
 * �ֽ���װ����չ�㣺����չ��λ��<b>���ʹ������ȡ�׶�</b>������չ��Ĺ����ǽ��ֽ������װ�س�ΪClass���Ͷ���
 * <br/>ע�⣺1.����չ������������˶��{@link ClassBytePoint}��չ�㣬����չ�㽫������ִ�С�����ÿ��ִ��֮��������ͺ��ֽ������ݻᱻ����ڶ�����չ�㡣 
 * <br/>ע�⣺2.����{@link ClassBytePoint}������չ��ִ�н��������һ��null����hyphaϵͳ��ʹ�����õ�classLoaderװ���ֽ��롣
 * <br/>��չ��ִ��˳��{@link ClassBytePoint}-&gt<i><b>{@link ClassTypePoint}</b></i>-&gt{@link BeforeCreatePoint}-&gt{@link AfterCreatePoint}
 * @version 2011-3-1
 * @author ������ (zyc@byshell.org)
 */
public interface ClassTypePoint extends ExpandPoint<Class<?>> {
    /**
     * ִ����չ��������һ����������һ����չ��ִ�еķ��ؽ�����ڶ����������±�ʾ��<br/>
     * target  װ�ص�bean����{@link Class}���ͣ�ע���������չ�㲻���κ��£����뷵�ظò������ݡ�<br/>
     * param[0] {@link AbstractBeanDefine}��ǰ������bean�������<br/>
     * param[1] {@link ApplicationContext}��չ�������������ġ�
     */
    public Class<?> doIt(Class<?> target, Object lastReturnObj, Object[] params);
};