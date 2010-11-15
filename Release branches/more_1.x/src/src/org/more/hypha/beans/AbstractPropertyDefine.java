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
package org.more.hypha.beans;
import org.more.util.attribute.IAttribute;
/**
 * �ýӿ����ڶ���һ��bean�����е�һ�����Ի������Ϣ��
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public interface AbstractPropertyDefine extends BeanDefinePluginSet, IAttribute {
    /**���ص�ִ������ע��ʱ��Ҫִ�е�����ת�����͡�*/
    public Class<?> getClassType();
    /**�������Ե�������Ϣ��*/
    public String getDescription();
    /**��ȡ�Ը����Ե�ֵ��Ϣ������*/
    public ValueMetaData getMetaData();
    /**���ؾ����������ַ�����*/
    public String toString();
}