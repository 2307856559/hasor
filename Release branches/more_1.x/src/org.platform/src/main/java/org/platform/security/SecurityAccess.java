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
package org.platform.security;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
* �����Ǹ���װ���û�Ȩ�޵��࣬������Ҫʵ��{@link ISecurityAccess}�ӿڡ�
* ͨ��������Խ�����Ȩ��ģ�ͼ��ɵ�һ��
* @version : 2013-3-25
* @author ������ (zyc@byshell.org)
*/
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface SecurityAccess {
    /**����ͬ��ʱ������˳�򡣣�ԽСԽ���ȣ���*/
    public int sort() default Integer.MAX_VALUE;
    /**��֤ϵͳ����*/
    public String authSystem();
}