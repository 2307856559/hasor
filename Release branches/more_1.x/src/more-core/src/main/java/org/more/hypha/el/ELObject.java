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
import org.more.hypha.ApplicationContext;
/**
 * EL���󣬸ýӿڿ�����ȷ�ֽ硣ִ��el���ʽʱ�Զ���Ķ�����д������
 * Date : 2011-4-11
 * @author ������ (zyc@byshell.org)
 */
public interface ELObject {
    /**��ʼ��{@link ELObject}����*/
    public void init(ApplicationContext context);
    /**����ö����ǲ���д���������ʵ�ָýӿ�ʱ����true��*/
    public boolean isReadOnly();
    /**�ı����el����ֵ�������ã���*/
    public void setValue(Object value) throws ELException;
    /**��ȡ���el����*/
    public Object getValue() throws ELException;
};