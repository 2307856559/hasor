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
package org.more.submit;
import java.lang.reflect.Method;
/**
 * submit�İ���action�Ǳ�������������档
 * @version : 2011-7-18
 * @author ������ (zyc@byshell.org)
 */
public interface ActionPackage {
    /**��ȡ������*/
    public String packageName();
    /**��action�������һ��·��ӳ�䣬·��ӳ���Ŀ���ǽ�action������·��ӳ�䵽һ���ⲿ��ַ�ϡ�actionPathΪaction���ڲ���ַ��*/
    public void addRouteMapping(String mappingPath, Method actionPath);
    /**ͨ���ⲿ��ַ��ȡaction���ڲ���ַ��*/
    public Method getActionPath(String mappingPath);
};