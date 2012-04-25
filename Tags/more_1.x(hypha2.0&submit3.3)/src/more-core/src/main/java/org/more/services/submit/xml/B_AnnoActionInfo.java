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
package org.more.services.submit.xml;
import java.lang.reflect.Method;
/**
 * �����Ŀ���ǽ�һ�������Ķ�λ�ַ�����һ����ַ����ӳ�䣬��������ͨ���򵥵ĵ�ַ���ʸ��ӵķ���·����
 * @version : 2011-7-15
 * @author ������ (zyc@byshell.org)
 */
public class B_AnnoActionInfo {
    /**Action���ܴ��ڵİ���ַ��*/
    public String packageString = null;
    /**Action����ʵ��ַ��*/
    public Method actionPath    = null;
    /**Actionӳ���ȥ�ĵ�ַ��*/
    public String mappingPath   = null;
}