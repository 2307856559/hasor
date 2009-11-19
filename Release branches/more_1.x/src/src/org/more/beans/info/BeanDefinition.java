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
import org.more.beans.core.ResourceBeanFactory;
import org.more.beans.core.injection.ExportInjectionProperty;
import org.more.util.attribute.AttBase;
/**
 * һ��Bean������������Ϣ���������һ����׼��pojo��more.beans����ͨ����ͬ��ʽ�ṩ���bean���塣����bean��������
 * more.beans�ͻṤ��������<br/>��BeanDefinition��һ����Ϊ�������ݣ�
 * ���Ƿֱ��ǡ��������ݡ�ioc�������ݡ�create�������ݡ�aop�������ݡ��������ݡ��������ݡ�����ֱ�������������ݡ�
 * <br/><br/>һ����������:<br/>
 * ���������ж�����bean�����֣������Ƿ��ӳٳ�ʼ�����Լ��������������Ϣ������name��type�������á�lazyInit����Ĭ��Ϊtrue��
 * lazyInit��֧������{@link ResourceBeanFactory}��ʵ�ֵġ�
 * <br/><br/>����ioc��������:<br/>
 * ioc�����������ṩ�˶���������ע���������Ϣ��iocType��ָ����ע���������Ǹ���{@link IocTypeEnum}ö�ٶ��塣
 * ����exportIocRefBean�ǵ�iocType����Ϊ{@link IocTypeEnum#Export Export}��ʽʱ�����������ݱ�ʾ����������ע����������ע����������
 * {@link ExportInjectionProperty}�ӿڶ���propertys������Ҫע����������飬���ж����Ե����ö�����������С�
 * ʹ��Export��ʽʱһ��ע�������{@link ExportInjectionProperty}�ӿ� ������iocType��Ĭ��ֵ��{@link IocTypeEnum#Ioc Ioc}��
 * <br/><br/>����create��������:<br/>
 * �����йش�����������ݶ����������ǰ�����createType��������(Ĭ����New)��constructor��������ʱ���õĹ��췽�����塢factoryRefBean��������
 * ��createTypeΪ{@link CreateTypeEnum#Factory Factory}ʱ����������ʾ��ʹ�õĹ���bean���ơ�factoryIsStaticMethod��ʾ��ʹ�õĹ��������Ƿ�Ϊ��̬������
 * ��Ծ�̬������������Factory��ʽ�в���ȥ����Factory��������Ҫ����factoryIsStaticMethod���ԡ�factoryMethodName������������
 * factoryMethodParams���ù�������ʱ��Ҫ���ݵĲ�����Ϣ��
 * <br/><br/>�ġ�aop��������:<br/>
 * ������aop�������Ϣ��ͬʱ���������л������˸��ӽӿ�ʵ��������ݡ�aopFilterRefBean����AOP����������bean���ơ�
 * ��implImplInterface���Ǹ��ӽӿ�ʵ��������ݡ�
 * <br/><br/>�塢��������:<br/>
 * ������������һ������Ҫ�����Ծ���isSingleton��������{@link ResourceBeanFactory ResourceBeanFactory}�Ƿ������л��档
 * <br/><br/>������������:<br/>
 * ������������BeanDefinition�̳е�{@link AttBase AttBase}���ṩ֧�֣�ͨ����Щ���Զ������õĸ���������Ϣ��ע�����������벻Ҫʹ�á�$more_����Ϊ���Կ�ͷ��
 * ��$more_����more.beans�������Ƶı���������Щ�����������ܵĻ������ݶ���ʹ�����������������ƴ�ŵġ�
 * <br/>Date : 2009-11-17
 * @author ������
 */
public class BeanDefinition extends Prop {
    //========================================================================================Field
    /**  */
    private static final long serialVersionUID      = 75468455223536954L;
    //��������
    private String            name                  = null;              //��һ��beanFactory�е�Ψһ���ơ�
    private boolean           lazyInit              = true;              //�Ƿ��ӳٳ�ʼ�����bean��ֻ�е�bean�ǵ�̬ģʽ�²���Ч��
    private String            description           = null;              //bean������Ϣ��
    //ioc��������
    private IocTypeEnum       iocType               = IocTypeEnum.Ioc;   //����ע�뷽ʽ��InjectionFactory����������ԣ�Ĭ��ֵΪIoc��
    private String            exportIocRefBean      = null;              //����ע�뷽ʽΪexportʱ����������ע����bean����
    private BeanProperty[]    propertys             = null;              //bean��ע���������Щ������Ҫ����ע�롣
    //create��������
    private CreateTypeEnum    createType            = CreateTypeEnum.New; //������ʽ��Ĭ��ΪNew��
    private BeanConstructor   constructor           = null;              //���õĹ��췽������������ʽΪ������ʽʱ�����췽����ʧЧ��һ�д�������ί�и�����������
    private String            factoryRefBean        = null;              //ʹ�ù�����ʽ����ʱ�Ĺ���bean���ơ�
    private boolean           factoryIsStaticMethod = false;             //���ù�����ķ����Ƿ�Ϊһ����̬������
    private String            factoryMethodName     = null;              //���ù�����ķ�����
    private BeanProperty[]    factoryMethodParams   = null;              //���ù�����ķ���ʱ��Ҫ���ݵĲ�������������������Ԫ��˳��
    //aop��������
    private String[]          aopFilterRefBean      = null;              //AOP������bean��CreateTypeEnum���ΪFactory��ʽ��AOPʹ�ô���ʽ�����������New��ʽ�����򴴽�ģʽʹ��Super
    private BeanInterface[]   implImplInterface     = null;              //Ҫ����ʵ�ֵĽӿ�
    //��������
    private boolean           isSingleton           = false;             //�Ƿ�Ϊ��̬ģʽ
    //==========================================================================================Job
    /**��ȡ��һ��beanFactory�е�Ψһ���ơ�*/
    public String getName() {
        return name;
    }
    /**������һ��beanFactory�е�Ψһ���ơ�*/
    public void setName(String name) {
        this.name = name;
    }
    /**��ȡ�Ƿ��ӳٳ�ʼ�����bean��ֻ�е�bean�ǵ�̬ģʽ�²���Ч��*/
    public boolean isLazyInit() {
        return lazyInit;
    }
    /**�����Ƿ��ӳٳ�ʼ�����bean��ֻ�е�bean�ǵ�̬ģʽ�²���Ч��*/
    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }
    /**��ȡbean������Ϣ��*/
    public String getDescription() {
        return description;
    }
    /**����bean������Ϣ��*/
    public void setDescription(String description) {
        this.description = description;
    }
    /**��ȡ����ע�뷽ʽ��InjectionFactory����������ԣ�Ĭ��ֵΪIoc��*/
    public IocTypeEnum getIocType() {
        return iocType;
    }
    /**��������ע�뷽ʽ��InjectionFactory����������ԣ�Ĭ��ֵΪIoc��*/
    public void setIocType(IocTypeEnum iocType) {
        this.iocType = iocType;
    }
    /**��ȡ����ע�뷽ʽΪexportʱ����������ע����bean����*/
    public String getExportIocRefBean() {
        return exportIocRefBean;
    }
    /**��������ע�뷽ʽΪexportʱ����������ע����bean����*/
    public void setExportIocRefBean(String exportIocRefBean) {
        this.exportIocRefBean = exportIocRefBean;
    }
    /**��ȡbean��ע���������Щ������Ҫ����ע�롣*/
    public BeanProperty[] getPropertys() {
        return propertys;
    }
    /**����bean��ע���������Щ������Ҫ����ע�롣*/
    public void setPropertys(BeanProperty[] propertys) {
        this.propertys = propertys;
    }
    /**��ȡ������ʽ��Ĭ��ΪNew��*/
    public CreateTypeEnum getCreateType() {
        return createType;
    }
    /**���ô�����ʽ��Ĭ��ΪNew��*/
    public void setCreateType(CreateTypeEnum createType) {
        this.createType = createType;
    }
    /**��ȡBean��Newģʽ�´����Ĺ��췽�����á�*/
    public BeanConstructor getConstructor() {
        return constructor;
    }
    /**����Bean��Newģʽ�´����Ĺ��췽�����á�*/
    public void setConstructor(BeanConstructor constructor) {
        this.constructor = constructor;
    }
    /**��ȡʹ�ù�����ʽ����ʱ�Ĺ���bean���ơ�*/
    public String getFactoryRefBean() {
        return factoryRefBean;
    }
    /**����ʹ�ù�����ʽ����ʱ�Ĺ���bean���ơ�*/
    public void setFactoryRefBean(String factoryRefBean) {
        this.factoryRefBean = factoryRefBean;
    }
    /**��ȡ���ù�����ķ����Ƿ�Ϊһ����̬������*/
    public boolean isFactoryIsStaticMethod() {
        return factoryIsStaticMethod;
    }
    /**���õ��ù�����ķ����Ƿ�Ϊһ����̬������*/
    public void setFactoryIsStaticMethod(boolean factoryIsStaticMethod) {
        this.factoryIsStaticMethod = factoryIsStaticMethod;
    }
    /**��ȡ���ù�����ķ�������*/
    public String getFactoryMethodName() {
        return factoryMethodName;
    }
    /**���õ��ù�����ķ�������*/
    public void setFactoryMethodName(String factoryMethodName) {
        this.factoryMethodName = factoryMethodName;
    }
    /**��ȡ���ù�����ķ���ʱ��Ҫ���ݵĲ������á�*/
    public BeanProperty[] getFactoryMethodParams() {
        return factoryMethodParams;
    }
    /**���õ��ù�����ķ���ʱ��Ҫ���ݵĲ������á�*/
    public void setFactoryMethodParams(BeanProperty[] factoryMethodParams) {
        this.factoryMethodParams = factoryMethodParams;
    }
    /**��ȡAOP������bean�������顣*/
    public String[] getAopFilterRefBean() {
        return aopFilterRefBean;
    }
    /**����AOP������bean�������顣*/
    public void setAopFilterRefBean(String[] aopFilterRefBean) {
        this.aopFilterRefBean = aopFilterRefBean;
    }
    /**��ȡҪ����ʵ�ֵĽӿڡ�*/
    public BeanInterface[] getImplImplInterface() {
        return implImplInterface;
    }
    /**����Ҫ����ʵ�ֵĽӿڡ�*/
    public void setImplImplInterface(BeanInterface[] implImplInterface) {
        this.implImplInterface = implImplInterface;
    }
    /**��ȡ�Ƿ�Ϊ��̬ģʽ��*/
    public boolean isSingleton() {
        return isSingleton;
    }
    /**�����Ƿ�Ϊ��̬ģʽ��*/
    public void setSingleton(boolean isSingleton) {
        this.isSingleton = isSingleton;
    }
}