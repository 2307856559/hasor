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
import java.io.IOException;
import org.more.hypha.ApplicationContext;
import org.more.hypha.ExpandPoint;
import org.more.hypha.beans.AbstractBeanDefine;
/**
 * �ֽ�����Ϣ��ȡ��չ�㣺����չ��λ��<b>���ʹ������ȡ�׶�</b>������չ��������ⲿ�����п���Bean�ֽ����ȫ�����ض��������ֽ��뼶������ɶ�����޸ġ�
 * <br/>ע�⣺1.��������˶��{@link ClassByteExpandPoint}��չ�㣬����չ�㽫������ִ�С�����ÿ��ִ��֮������ֽ������ݻᱻ����ڶ�����չ�㡣
 * <br/>ע�⣺2.������չ�㷵����һ��null����ô����һ����չ���н�����õ���������һ����չ����ֽ������ݡ�
 * <br/>��չ��˳��<i><b>{@link ClassByteExpandPoint}</b></i>-&gt{@link ClassTypeExpandPoint}-&gt{@link BeforeCreateExpandPoint}-&gt{@link AfterCreateExpandPoint}
 * @version 2011-3-7
 * @author ������ (zyc@byshell.org)
 */
public interface ClassByteExpandPoint extends ExpandPoint {
    /**
     * ִ����չ������
     * @param beanBytes ϵͳͨ��{@link AbstractBeanBuilder}������������һ��{@link ClassByteExpandPoint}��չ����ֽ������ݡ�
     * @param define ��ǰ������bean�������
     * @param context ��չ�������������ġ�
     * @return ������չ��ִ�н����
     * @throws IOException ��ִ���ڼ���ܳ��ֵ��쳣��
     */
    public byte[] decorateBytes(byte[] beanBytes, AbstractBeanDefine define, ApplicationContext context) throws IOException;
};