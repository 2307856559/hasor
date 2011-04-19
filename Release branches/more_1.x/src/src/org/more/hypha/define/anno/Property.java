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
/**
 * ��ǵ�ǰ�ֶ���һ����Ҫע������ԣ��������û��ָ���κ�����ֵ�����򡰾�˿���������������Ե�ע��Ҫ��
 * @version 2010-10-13
 * @author ������ (zyc@byshell.org)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Property {
    /**�����������Ƿ��ӳ�ע�룬����ӳ�ע�뵱ֻ����ͼ���ʸ�����ʱ�Ż�����Խ��г�ʼ����*/
    public boolean lazyInit() default true;
    /**���Ե�ע��*/
    public String desc() default "";
    /**�ı���ʽ������ֵ������ͨ���������͵��������Ա�ʾ���õĻ������͡����ӵ�����ע��ע��һ��bean����Ҫʹ��elע�롣*/
    public String value() default "";
    /**��el���н���Ȼ�󽫽������ע�뵽���ֶ��ϡ�*/
    public String el() default "";
    /**Я���ĸ�����Ϣ����*/
    public MetaData[] metaData() default {};
}