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
package org.more.beans.resource.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.more.beans.info.IocTypeEnum;
/**
 * ���ø�����һ��Bean����ע���������һЩbean�Ļ��������������������˹��췽����ôbeans���������췽��
 * ������ز�����ע���������ڶ�����췽��beansֻ��ѡȡ��һ��������aop�Ĺ�����Ҳ����ͨ����ע�����á�
 * @version 2010-1-7
 * @author ������ (zyc@byshell.org)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE })
public @interface Bean {
    /**��������bean�����ƣ����û������bean������bean����������Class�ļ������(����ĸСд)�����塣*/
    public String name() default "";
    /**�Ƿ��ӳٳ�ʼ�����bean��ֻ�е�bean�ǵ�̬ģʽ�²���Ч��Ĭ�ϸ�������true��*/
    public boolean lazyInit() default true;
    /**����ע�뷽ʽ��InjectionFactory����������ԣ�Ĭ��ֵΪIoc��*/
    public IocTypeEnum iocType() default IocTypeEnum.Ioc;
    /**����ע�뷽ʽΪexportʱ����������ע����bean����*/
    public String exportRefBean() default "";
    /**AOP������bean��CreateTypeEnum���ΪFactory��ʽ��AOPʹ�ô���ʽ�����������New��ʽ�����򴴽�ģʽʹ��Super*/
    public String[] aopFiltersRefBean() default {};
    /**�Ƿ�Ϊ��̬ģʽ��Ĭ��Ϊtrue*/
    public boolean isSingleton() default true;
}