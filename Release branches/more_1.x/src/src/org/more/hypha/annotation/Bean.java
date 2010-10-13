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
package org.more.hypha.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * ��Ǹ����ǡ���˿���е�һ����ЧBean��
 * @version 2010-10-13
 * @author ������ (zyc@byshell.org)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Bean {
    /**����bean��id�����������û����������id������id���Ե�ͬname��*/
    public String id() default "";
    /**����bean�����ƣ����û������bean������bean����������Class�����������塣*/
    public String name() default "";
    /**�߼������������������ڲ�Ӱ��bean������ĳ�����������ȥ��������������һ������Ȩ�������û��ָ��������ֵ��ô��ֵ������õ�ǰ����������ʵ������Ϊֵ��*/
    public String logicPackage() default "";
    /**��Bean��֧�ֵ�������*/
    public String scope() default "";
    /**�Ƿ�Ϊ��̬ģʽ��Ĭ��Ϊtrue*/
    public boolean isSingleton() default true;
    /**�Ƿ��ӳٳ�ʼ�����bean��ֻ�е�bean�ǵ�̬ģʽ�²���Ч��Ĭ�ϸ�������true��*/
    public boolean lazyInit() default true;
    /**�����Ҫʹ�ù�����ʽ��ʼ����Bean����զ�ô���Ҫָ��������Bean���ƻ�ID��*/
    public String factoryName() default "";
    /**��ȡ����bean�ڴ���beanʱʹ�õķ��������������Ҫ��һ���޲εķ�����*/
    public String factoryMethod() default "";
    /**��������ļ���������ĳ��ģ��bean��ô�Ϳ���ʹ�ø�����������ģ���Խ�һ������ע�����÷�ʽ��*/
    public String useTemplate() default "";
    /**ע�͡�*/
    public String description() default "";
}