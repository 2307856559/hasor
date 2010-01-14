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
package org.more.beans.resource.annotation.core;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.more.beans.info.CreateTypeEnum;
import org.more.beans.info.IocTypeEnum;
/**
 * 
 * @version 2010-1-7
 * @author ������ (zyc@byshell.org)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE })
public @interface Bean {
    public String name() default ""; //��һ��beanFactory�е�Ψһ���ơ�
    public boolean lazyInit() default true; //�Ƿ��ӳٳ�ʼ�����bean��ֻ�е�bean�ǵ�̬ģʽ�²���Ч��
    public String description() default ""; //bean������Ϣ��
    //ioc��������
    public IocTypeEnum iocType() default IocTypeEnum.Ioc; //����ע�뷽ʽ��InjectionFactory����������ԣ�Ĭ��ֵΪIoc��
    public String exportRefBean() default ""; //����ע�뷽ʽΪexportʱ����������ע����bean����
    //create��������
    public CreateTypeEnum createType() default CreateTypeEnum.New; //������ʽ��Ĭ��ΪNew��
    public String factoryRefBean() default "";//ʹ�ù�����ʽ����ʱ�Ĺ���bean���ơ�
    public boolean factoryIsStaticMethod() default true; //���ù�����ķ����Ƿ�Ϊһ����̬������
    public String factoryMethodName() default ""; //���ù�����ķ�����
    //aop��������
    public String[] aopFiltersRefBean() default {}; //AOP������bean��CreateTypeEnum���ΪFactory��ʽ��AOPʹ�ô���ʽ�����������New��ʽ�����򴴽�ģʽʹ��Super
    //  public BeanInterface[] implImplInterface() default {}; //Ҫ����ʵ�ֵĽӿ�
    //��������
    public boolean isSingleton() default true; //�Ƿ�Ϊ��̬ģʽ
}