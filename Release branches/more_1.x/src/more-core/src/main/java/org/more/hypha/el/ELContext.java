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
package org.more.hypha.el;
/**
 * ELִ�л�����EL��ִ���ڼ�����л����ͻ��������ͨ���ýӿ�ʵ�֡�
 * @version : 2011-4-19
 * @author ������ (zyc@byshell.org)
 */
public interface ELContext {
    /**��һ��el�ַ�������Ϊ{@link EvalExpression}���Ͷ����������Ϊ���򷵻ؿա�*/
    public EvalExpression getExpression(String elString) throws ELException;
    /** ����һ���ַ�����ELֵ�����ҷ��ؼ��������������Ϊ���򷵻ؿա� */
    public Object evalExpression(String elString) throws ELException;
    /**
     * �������Է��ʷ���ȡһ�����Զ�д����ʹ��������Զ�д�����Է��������һ�����Ի��߶�д�����ԡ�
     * @param propertyEL ���Է��ʷ������Ǽ򵥵�java�ֶ������ߡ�abc.def����ʽ�������������������Ϊ���򷵻ؿա�
     * @param object �������ڵ���������
     */
    public PropertyBinding getPropertyBinding(String propertyEL, Object object) throws ELException;
    /**
     * ���һ��EL���󣬵���ͼ�Ըö��������д��ʱ�������ض��Ľӿڷ�����
     * @param name EL���ʽͨ���ò�������ʾ���������������EL������������ظ����µĻ��滻ԭ�����ƵĶ���
     * @param elObject Ҫ��ӵ�{@link ELObject}��������������һ����ֵ���ʾɾ��������ƵĶ���(�滻)��
     */
    public void addELObject(String name, ELObject elObject);
};