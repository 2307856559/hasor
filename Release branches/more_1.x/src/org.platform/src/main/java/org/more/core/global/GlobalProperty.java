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
package org.more.core.global;
/**
 * �ýӿڱ�ʾһ���Զ����д��ʽ�����Զ������ö����������ࡣglobalҲ��ִ�н������ࡣ
 * @version : 2011-9-30
 * @author ������ (zyc@byshell.org)
 */
public interface GlobalProperty {
    /** ��ȡ����*/
    public <T> T getValue(AbstractGlobal global, Class<T> toType);
    /** д������*/
    public void setValue(Object newValue, AbstractGlobal global);
}