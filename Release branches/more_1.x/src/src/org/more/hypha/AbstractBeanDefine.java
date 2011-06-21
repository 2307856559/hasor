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
package org.more.hypha;
import java.util.Collection;
import org.more.util.attribute.IAttribute;
/**
 * �ýӿ����ڶ���һ��bean��������ʲô���͵�Bean����Ҫʵ�ָýӿڡ�
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public interface AbstractBeanDefine extends IAttribute {
    /**����bean��Ψһ��ţ����û��ָ��id������idֵ����fullName����ֵ��*/
    public String getID();
    /**����bean�����ƣ����ָ����package������ôname��ֵ���Գ����ظ���*/
    public String getName();
    /**��ȡBean���߼������壬��������������ʵ����������ͬ��������Ϊһ�����ڵ��߼�������ʽ��*/
    public String getPackage();
    /**��ȡ����package��bean���������������޶�����*/
    public String getFullName();
    /**��ȡbean�������bean�����͸�������class�����������ֵģ�class���Ϳ��Ա���һ���������͵����޷���������ࡣ*/
    public String getBeanType();
    /**����ע����ʹ�õ�ע�뷽ʽ��Fact��Ioc��User*/
    public String getIocEngine();
    /**����һ��booleanֵ����ʾ���Ƿ�Ϊһ������ġ�*/
    public boolean isAbstract();
    /**����һ��booleanֵ����ʾ���bean�Ƿ�Ϊ��̬�ġ�*/
    public boolean isSingleton();
    /**����һ��booleanֵ����ʾ���bean�Ƿ�Ϊ�ӳ�װ�صġ�*/
    public boolean isLazyInit();
    /**����bean��������Ϣ��*/
    public String getDescription();
    /**��ȡ������bean�Ĺ���������*/
    public AbstractMethodDefine factoryMethod();
    /**��ȡ��ʼ�����������÷�����һ���޲εķǾ�̬������*/
    public String getInitMethod();
    /**��ȡ���ٷ�������*/
    public String getDestroyMethod();
    /**������������������bean�ϵ�һЩ���������صļ�����һ��ֻ�����ϡ�*/
    public Collection<? extends AbstractMethodDefine> getMethods();
    /**
     * �����Զ����˵��������beanʱ����Ҫ������������
     * ��������ͨ����ָ���췽�����������ڹ�����ʽ�����������������˹��������Ĳ����б�
     * ���صļ�����һ��ֻ�����ϡ�
     */
    public Collection<? extends InitPropertyDefine> getInitParams();
    /**����bean�Ķ������Լ��ϣ����صļ�����һ��ֻ�����ϡ�*/
    public Collection<? extends BeanPropertyDefine> getPropertys();
    /**���ؾ����������ַ�����*/
    public String toString();
}