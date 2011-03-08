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
 * Ԥ������չ�㣺����չ��λ��<b>���󴴽��׶�</b>������չ��Ŀ����ṩһ��ʹ�ⲿ�������Bean���͹�����пڣ����Ԥ�����ɹ����ȡ��<b>Ĭ�ϴ�������</b>��
 * <br/>ע�⣺1.����չ������������˶��{@link BeforeCreateExpandPoint}��չ�㣬����չ�㽫������ִ�С� ֱ������һ����Ϊ�յ�ִ�н����
 * <br/>ע�⣺2.����{@link BeforeCreateExpandPoint}������չ��ִ�н��������һ��null����hyphaϵͳ��ʹ�����õĴ�������ִ��Bean�Ĵ�����
 * <br/>��չ��ִ��˳��{@link ClassByteExpandPoint}-&gt{@link ClassTypeExpandPoint}-&gt<i><b>{@link BeforeCreateExpandPoint}</b></i>-&gt{@link AfterCreateExpandPoint}
 * @version 2011-3-7
 * @author ������ (zyc@byshell.org)
 */
public interface BeforeCreateExpandPoint extends ExpandPoint {
    /**
     * ִ����չ������
     * @param beanType bean����ִ��֮����װ�ص�Bean���͡�
     * @param params getBean����������Ķ�̬������
     * @param define bean���塣
     * @param context ��չ�������������ġ�
     * @return ������չ��ִ�н����
     */
    public Object beforeCreate(Class<?> beanType, Object[] params, AbstractBeanDefine define, ApplicationContext context);
};