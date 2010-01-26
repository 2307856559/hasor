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
package org.more.beans.resource.annotation.util;
import org.more.util.attribute.AttBase;
/**
 * ����ע��Ľڵ��ջ���Ӵ������ע�⿪ʼ��������ջ����ÿһ����Ԫ�ض�����һ���µĶ�ջ��<br/>
 * ͨ����ջ�����򸸽ڵ㴫����Ҫ�����ݺ�һЩ�������ݡ�
 * @version 2010-1-10
 * @author ������ (zyc@byshell.org)
 */
public class AnnoContextStack extends AttBase {
    /**  */
    private static final long serialVersionUID = 4300339262589765696L;
    private AnnoContextStack  parent           = null;                //��ǰ��ջ�ĸ�����ջ��
    /**��ǰ��ջ�б������Ҫ���ݶ���*/
    public Object             context          = null;
    /**���ڱ�ɨ�����*/
    private Class<?>          atClass          = null;
    /**��ǰ��ջ����������*/
    private AnnoScopeEnum     scope            = null;
    AnnoContextStack(AnnoContextStack parent, Class<?> atClass, AnnoScopeEnum scope) {
        this.parent = parent;
        this.atClass = atClass;
        this.scope = scope;
    }
    /**��ȡ��ǰ��ջ�ĸ�����ջ��*/
    public AnnoContextStack getParent() {
        return this.parent;
    }
    /**���ڱ�ɨ�����*/
    public Class<?> getAtClass() {
        return atClass;
    }
    /**��ǰ��ջ����������*/
    public AnnoScopeEnum getScope() {
        return scope;
    }
}