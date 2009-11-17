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
package org.more.beans.info;
import org.more.util.attribute.AttBase;
/**
 * 
 * Date : 2009-11-4
 * @author Administrator
 */
public class BeanDefinition extends AttBase {
    /**  */
    private static final long serialVersionUID      = 75468455223536954L;
    //��������
    private String            id                    = null;              //Ψһ��Bean ID��
    private String            name                  = null;              //��һ��beanFactory�е�Ψһ����
    private String            type                  = null;              //�������bean��ԭʼ���ͣ�������Ӧ�ÿ���ͨ��Class.forName��ȡ��
    private boolean           lazyInit              = false;             //�Ƿ��ӳٳ�ʼ�����bean��ֻ�е�bean�ǵ�̬ģʽ�²���Ч��
    private String            scope                 = null;              //bean�����÷�Χ��
    private String            description           = null;              //bean������Ϣ��
    //ioc��������
    private IocTypeEnum       iocType               = null;              //����ע�뷽ʽ��InjectionFactory�����������
    private String            exportIocRefBean      = null;              //�������ע�뷽ʽΪexport����Ҫ�����Ե�֧�֣�������Ա�ʾһ���ⲿ����ע����
    private BeanProperty[]    propertys             = null;              //bean��ע���������Щ������Ҫ����ע��
    //create��������
    private CreateTypeEnum    createType            = null;              //������ʽ
    private BeanConstructor   constructor           = null;              //��������ʽΪ������ʽʱ�����췽����ʧЧ��һ�д�������ί�и�����������
    private String            factoryRefBean        = null;              //ʹ�ù�����ʽ����ʱ�Ĺ���bean���ơ�
    private boolean           factoryIsStaticMethod = false;             //���ù�����ķ����Ƿ�Ϊһ����̬������
    private String            factoryMethodName     = null;              //���ù�����ķ�����
    private BeanProperty[]    factoryMethodParams   = null;              //���ù�����ķ���ʱ��Ҫ���ݵĲ�������������������Ԫ��˳��
    //aop��������
    private String[]          aopFilterRefBean      = null;              //AOP������bean��CreateTypeEnum���ΪFactory��ʽ��AOPʹ�ô���ʽ�����������New��ʽ�����򴴽�ģʽʹ��Super
    private BeanInterface[]   implImplInterface     = null;              //Ҫ����ʵ�ֵĽӿ�
    //��������
    private boolean           isFactory             = false;             //
    private boolean           isSingleton           = false;             //
    private boolean           isPrototype           = false;             //
    private boolean           isInterface           = false;             //
    private boolean           isAbstract            = false;             //
    //=================================================================
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
    public boolean isLazyInit() {
        return lazyInit;
    }
    public String getScope() {
        return scope;
    }
    public String getDescription() {
        return description;
    }
    public IocTypeEnum getIocType() {
        return iocType;
    }
    public String getExportIocRefBean() {
        return exportIocRefBean;
    }
    public BeanProperty[] getPropertys() {
        return propertys;
    }
    public CreateTypeEnum getCreateType() {
        return createType;
    }
    public BeanConstructor getConstructor() {
        return constructor;
    }
    public String getFactoryRefBean() {
        return factoryRefBean;
    }
    public boolean isFactoryIsStaticMethod() {
        return factoryIsStaticMethod;
    }
    public String getFactoryMethodName() {
        return factoryMethodName;
    }
    public BeanInterface[] getImplImplInterface() {
        return implImplInterface;
    }
    public boolean isFactory() {
        return isFactory;
    }
    public boolean isSingleton() {
        return isSingleton;
    }
    public boolean isPrototype() {
        return isPrototype;
    }
    public boolean isInterface() {
        return isInterface;
    }
    public boolean isAbstract() {
        return isAbstract;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }
    public void setScope(String scope) {
        this.scope = scope;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setIocType(IocTypeEnum iocType) {
        this.iocType = iocType;
    }
    public void setExportIocRefBean(String exportIocRefBean) {
        this.exportIocRefBean = exportIocRefBean;
    }
    public void setPropertys(BeanProperty[] propertys) {
        this.propertys = propertys;
    }
    public void setCreateType(CreateTypeEnum createType) {
        this.createType = createType;
    }
    public void setConstructor(BeanConstructor constructor) {
        this.constructor = constructor;
    }
    public void setFactoryRefBean(String factoryRefBean) {
        this.factoryRefBean = factoryRefBean;
    }
    public void setFactoryIsStaticMethod(boolean factoryIsStaticMethod) {
        this.factoryIsStaticMethod = factoryIsStaticMethod;
    }
    public void setFactoryMethodName(String factoryMethodName) {
        this.factoryMethodName = factoryMethodName;
    }
    public void setImplImplInterface(BeanInterface[] implImplInterface) {
        this.implImplInterface = implImplInterface;
    }
    public void setFactory(boolean isFactory) {
        this.isFactory = isFactory;
    }
    public void setSingleton(boolean isSingleton) {
        this.isSingleton = isSingleton;
    }
    public void setPrototype(boolean isPrototype) {
        this.isPrototype = isPrototype;
    }
    public void setInterface(boolean isInterface) {
        this.isInterface = isInterface;
    }
    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }
    public String[] getAopFilterRefBean() {
        return aopFilterRefBean;
    }
    public void setAopFilterRefBean(String[] aopFilterRefBean) {
        this.aopFilterRefBean = aopFilterRefBean;
    }
    public BeanProperty[] getFactoryMethodParams() {
        return factoryMethodParams;
    }
    public void setFactoryMethodParams(BeanProperty[] factoryMethodParams) {
        this.factoryMethodParams = factoryMethodParams;
    }
}