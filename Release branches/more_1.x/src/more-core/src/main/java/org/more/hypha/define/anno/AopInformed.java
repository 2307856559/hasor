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
package org.more.hypha.define.anno;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.more.hypha.define.AopPointcutType;
/**
 * �����һ��aop֪ͨ�ߣ�ͨ����ע������aop��һ����������㡣
 * @version 2010-10-13
 * @author ������ (zyc@byshell.org)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface AopInformed {
    /**�����֪ͨ�ϵ�����㣬���������Ҫʹ���������ʽ��Ĭ��ֵ��*��*/
    public String pointcut() default "*";
    /**����㴦��bean�����ƻ�id���á�*/
    public String refBean();
    /**��������ͣ�Ĭ��ʹ��{@link AopPointcutType#Auto}��*/
    public AopPointcutType type() default AopPointcutType.Auto;
}