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
import org.more.util.attribute.IAttribute;
/**
 * ELִ�л�����EL��ִ���ڼ�����л����ͻ��������ͨ���ýӿ�ʵ�֡�
 * @version : 2011-4-19
 * @author ������ (zyc@byshell.org)
 */
public interface ELContext extends IAttribute {
    /** ����һ���ַ�����ELֵ�����ҷ��ؼ������� */
    public Object evalExpression(String elString) throws Throwable;
    /**
     * �������Է��ʷ���ȡһ�����Զ�д����ʹ��������Զ�д�����Է��������һ�����Ի��߶�д�����ԡ�
     * @param propertyEL ���Է��ʷ������Ǽ򵥵�java�ֶ������ߡ�abc.def����ʽ������������
     * @param object �������ڵ���������
     */
    public PropertyBinding getPropertyBinding(String propertyEL, Object object) throws Throwable;
    /**
     * ���һ��EL���󣬵���ͼ�Ըö��������д��ʱ�������ض��Ľӿڷ�����
     * @param name EL���ʽͨ���ò�������ʾ���������������EL����ע�����Ʋ����ظ���
     * @param elObject Ҫ��ӵ�{@link ELObject}����
     */
    public void addELObject(String name, ELObject elObject);
};