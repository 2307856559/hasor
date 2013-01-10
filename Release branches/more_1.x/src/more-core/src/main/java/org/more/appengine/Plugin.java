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
package org.more.appengine;
/**
 * ��Ǹ���Ϊһ�������
 * @version : 2013-1-5
 * @author ������ (zyc@byshell.org)
 */
public @interface Plugin {
    /**���ע������ơ�*/
    public String name();
    /**�������˳��ϵͳ����ֵ0~1000������˳���ǰ��մ�С�����˳������������˳����ͬ�ĳ�����������Ⱥ�˳��Ϊ׼��*/
    public int index() default 0;
}