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
package org.platform.action;
import java.lang.reflect.Method;
/***
 * 
 * @version : 2013-5-11
 * @author ������ (zyc@byshell.org)
 */
public interface ActionBinder {
    /**��һ�������ռ�*/
    public NameSpaceBindingBuilder bindNameSpace(String path);
    /**������һ�������ռ�*/
    public static interface NameSpaceBindingBuilder {
        public String getNameSpace();
        /**ע��һ��Action*/
        public ActionBindingBuilder bindAction(String actionName);
    }
    /**������action��ִ��Ŀ��*/
    public static interface ActionBindingBuilder {
        public String getActionName();
        /**��action�󶨵�Http�����ϡ�*/
        public ActionBindingBuilder onMethod(String httpMethod);
        /**��action�󶨵������ϡ�*/
        public void bindClass(Class<?> targetClass);
        /**��action�󶨵������ϡ�*/
        public void bindObject(Object targetObject);
        /**��action�󶨵������ϡ�*/
        public void bindMethod(Method targetMethod);
        /**��action�󶨵�{@link ActionInvoke}�ӿڡ�*/
        public void bindActionInvoke(ActionInvoke targetInvoke);
    }
}