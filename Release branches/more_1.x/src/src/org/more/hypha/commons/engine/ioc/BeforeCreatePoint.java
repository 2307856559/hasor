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
package org.more.hypha.commons.engine.ioc;
import org.more.hypha.AbstractBeanDefine;
import org.more.hypha.ApplicationContext;
import org.more.hypha.ExpandPoint;
/**
 * Ԥ������չ�㣺����չ��λ��<b>���󴴽��׶�</b>������չ��Ŀ����ṩһ��ʹ�ⲿ�������Bean���͹�����пڣ����Ԥ�����ɹ����ȡ��<b>Ĭ�ϴ�������</b>��
 * <br/>ע�⣺1.����չ������������˶��{@link BeforeCreatePoint}��չ�㣬����չ�㽫������ִ�С� ֱ������һ����Ϊ�յ�ִ�н����
 * <br/>ע�⣺2.����{@link BeforeCreatePoint}������չ��ִ�н��������һ��null����hyphaϵͳ��ʹ�����õĴ�������ִ��Bean�Ĵ�����
 * <br/>��չ��ִ��˳��{@link ClassBytePoint}-&gt{@link ClassTypePoint}-&gt<i><b>{@link BeforeCreatePoint}</b></i>-&gt{@link AfterCreatePoint}
 * @version 2011-3-7
 * @author ������ (zyc@byshell.org)
 */
public interface BeforeCreatePoint extends ExpandPoint {
    /**
     * ִ����չ��������һ����������һ����չ��ִ�еķ��ؽ�����ڶ����������±�ʾ��<br/>
     * param[0] bean����ִ��֮����װ�ص�Bean����,{@link Class}���͡�<br/>
     * param[1] getBean����������Ķ�̬������<br/>
     * param[2] {@link AbstractBeanDefine}��ǰ������bean�������<br/>
     * param[3] {@link ApplicationContext}��չ�������������ġ�
     */
    public Object doIt(Object returnObj, Object[] params);
};