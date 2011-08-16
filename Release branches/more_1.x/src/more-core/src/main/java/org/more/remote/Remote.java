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
package org.more.remote;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * ��Ǹ��౻��¶��RMI�����У���RMI��������ʱ������Ա����������ϵ�RMI������ʵ���
 * @version : 2011-8-15
 * @author ������ (zyc@byshell.org)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Remote {
    /**���������ƣ����տͻ�����ʹ�ø�RMI����ʱ��ͨ����������ַ�ڼ��������������λ�������ָ����forPublisher������������һ�������*/
    public String name();
    /**��ǵĵ�ǰԶ�̶��������Bean ID��*/
    public String refBeanID() default "";
    /**Ϊ��RMI�����ṩһ��ָ���ķ����ߣ�{@link RemoteService}����ͨ�����������޸ķ����Ļ���ַ���˿��Լ��󶨵�IP����Ϣ��*/
    public String forPublisher() default "";
    /**��ʾ������¶��ȥ֮��ʹ�õĽӿڡ�*/
    public Class<? extends java.rmi.Remote>[] faces() default {};
}