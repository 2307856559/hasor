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
 * �ֽ�����Ϣ��ȡ��չ�㣺����չ��λ��<b>���ʹ������ȡ�׶�</b>������չ��������ⲿ�����п���Bean�ֽ����ȫ�����ض��������ֽ��뼶������ɶ�����޸ġ�
 * <br/>ע�⣺1.��������˶��{@link ClassBytePoint}��չ�㣬����չ�㽫������ִ�С�����ÿ��ִ��֮������ֽ������ݻᱻ����ڶ�����չ�㡣
 * <br/>ע�⣺2.������չ�㷵����һ��null����ô����һ����չ���н�����õ���������һ����չ����ֽ������ݡ�
 * <br/>��չ��˳��<i><b>{@link ClassBytePoint}</b></i>-&gt{@link ClassTypePoint}-&gt{@link BeforeCreatePoint}-&gt{@link AfterCreatePoint}
 * @version 2011-3-7
 * @author ������ (zyc@byshell.org)
 */
public interface ClassBytePoint extends ExpandPoint<ClassData> {
    /**
     * ִ����չ��������һ����������һ����չ��ִ�еķ��ؽ�����ڶ����������±�ʾ��<br/>
     * target ϵͳͨ��BeanBuilder ������������һ��{@link ClassBytePoint}��չ����ֽ������ݡ��ֽ��������͡�<br/>
     * param[0] {@link AbstractBeanDefine}��ǰ������bean�������<br/>
     * param[1] {@link ApplicationContext}��չ�������������ġ�
     */
    public ClassData doIt(ClassData target, Object lastReturnObj, Object[] params);
};