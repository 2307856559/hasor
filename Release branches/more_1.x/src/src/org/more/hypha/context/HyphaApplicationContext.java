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
package org.more.hypha.context;
import java.io.IOException;
import java.util.List;
import org.more.ClassFormatException;
import org.more.DoesSupportException;
import org.more.NoDefinitionException;
import org.more.hypha.AbstractEventManager;
import org.more.hypha.AbstractExpandPointManager;
import org.more.hypha.ApplicationContext;
import org.more.hypha.DefineResource;
import org.more.hypha.ELContext;
import org.more.hypha.beans.AbstractBeanDefine;
import org.more.hypha.beans.assembler.AbstractBeanEngine;
import org.more.hypha.beans.assembler.AbstractELContext;
import org.more.hypha.beans.assembler.RootValueMetaDataParser;
import org.more.hypha.event.DestroyEvent;
import org.more.hypha.event.InitEvent;
import org.more.util.attribute.AttBase;
import org.more.util.attribute.IAttribute;
/**
 * �򵥵�{@link ApplicationContext}�ӿ�ʵ���ࡣ
 * Date : 2011-4-8
 * @author ������ (zyc@byshell.org)
 */
public class HyphaApplicationContext implements ApplicationContext {
    private DefineResource          defineResource     = null;
    private ClassLoader             classLoader        = null; //Context����װ����
    private Object                  context            = null; //�󶨵�Context�ϵ������ġ�
    //���ӳٿ��滻
    private IAttribute              attributeContext   = null; //���Լ�
    private IAttribute              flashContext       = null; //ȫ��FLASH
    //init�ڼ���빹��
    private RootValueMetaDataParser rootMetaDataParser = null; //Ԫ��Ϣ������
    private AbstractBeanEngine      engine             = null; //Bean��������
    private AbstractELContext       elContext          = null; //EL������
    /**
     * ����{@link HyphaApplicationContext}����������췽���ᵼ��{@link HyphaApplicationContext}�ڲ�����һ��{@link ArrayDefineResource}����
     * @param context ���췽������һ�������ò�����ʾһ�������� ��ͨ��el���Է��ʵ��������
     */
    public HyphaApplicationContext(Object context) {
        ArrayDefineResource adr = new ArrayDefineResource();
        this.flashContext = adr.getFlash();
        this.attributeContext = adr.getAttribute();
        this.defineResource = adr;
        this.context = context;
    };
    /**
     * ����{@link HyphaApplicationContext}����������췽���ᵼ��{@link HyphaApplicationContext}�ڲ�����һ��{@link ArrayDefineResource}����
     * @param defineResource ָ��{@link HyphaApplicationContext}����һ��Bean���������Դ��
     * @param context ���췽������һ�������ò�����ʾһ�������� ��ͨ��el���Է��ʵ��������
     */
    public HyphaApplicationContext(DefineResource defineResource, Object context) {
        if (defineResource == null)
            throw new NullPointerException("defineResource��������Ϊ��");
        this.defineResource = defineResource;
        this.context = context;
    };
    /**����ClassLoader��ͨ���ڳ�ʼ��֮ǰ�������á�*/
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    };
    public List<String> getBeanDefinitionIDs() {
        return this.defineResource.getBeanDefinitionIDs();
    };
    public AbstractBeanDefine getBeanDefinition(String id) throws NoDefinitionException {
        return this.defineResource.getBeanDefine(id);
    };
    public DefineResource getBeanResource() {
        return this.defineResource;
    };
    public ClassLoader getBeanClassLoader() {
        if (this.classLoader == null)
            return ClassLoader.getSystemClassLoader();
        return this.classLoader;
    };
    public boolean containsBean(String id) {
        return this.defineResource.containsBeanDefine(id);
    };
    public boolean isPrototype(String id) throws NoDefinitionException {
        return this.defineResource.isPrototype(id);
    };
    public boolean isSingleton(String id) throws NoDefinitionException {
        return this.defineResource.isSingleton(id);
    };
    public boolean isFactory(String id) throws NoDefinitionException {
        return this.defineResource.isFactory(id);
    };
    public boolean isTypeMatch(String id, Class<?> targetType) throws Throwable {
        //Object.class.isAssignableFrom(XmlTest.class); return true;
        if (targetType == null)
            throw new NullPointerException("����targetType����Ϊ��.");
        Class<?> beanType = this.getBeanType(id);
        return targetType.isAssignableFrom(beanType);
    };
    public synchronized void init() throws Throwable {
        //1.������ʼ�������defineResourceû��׼���þ�һֱ�ȴ�
        while (!this.defineResource.isReady())
            Thread.sleep(500);
        //----------------------------------------
        //2.����RootValueMetaDataParser
        this.rootMetaDataParser = new RootValueMetaDataParser(this, this.getFlash());
        this.rootMetaDataParser.loadConfig();//����regedit-metadata.prop�������ļ���
        //3.����AbstractBeanEngine
        this.engine = new AbstractBeanEngine(this, this.getFlash()) {
            protected AbstractExpandPointManager getExpandPointManager() {
                return HyphaApplicationContext.this.defineResource.getExpandPointManager();
            }
        };
        this.engine.loadConfig();//����regedit-beantype.prop�������ļ���
        //4.����AbstractELContext
        this.elContext = new AbstractELContext() {};
        this.elContext.loadConfig();//����regedit-el.prop�������ļ���
        //----------------------------------------
        //5.��ʼ��bean
        for (String id : this.getBeanDefinitionIDs()) {
            AbstractBeanDefine define = this.getBeanDefinition(id);
            if (define.isLazyInit() == false)
                this.getBean(id);
        }
        //6.��ʼ���¼� 
        this.getEventManager().doEvent(new InitEvent(this, this));
    };
    public void destroy() throws Throwable {
        this.engine = null;
        this.elContext = null;
        this.getAttribute().clearAttribute();
        this.defineResource.clearDefine();
        this.getEventManager().doEvent(new DestroyEvent(this, this));
    };
    public Object getBean(String id, Object... objects) throws Throwable {
        AbstractBeanDefine define = this.getBeanDefinition(id);
        return this.engine.builderBean(define, objects);
    };
    public Class<?> getBeanType(String id) throws DoesSupportException, IOException, ClassFormatException, ClassNotFoundException {
        AbstractBeanDefine define = this.getBeanDefinition(id);
        return this.engine.builderType(define);
    };
    public Object getContext() {
        return this.context;
    };
    /*------------------------------------------------------------*/
    /**�÷������Ի�ȡ{@link HyphaApplicationContext}�ӿڶ�����ʹ�õ����Թ��������������ͨ����д�÷��������������Թ���������*/
    protected IAttribute getAttribute() {
        if (this.attributeContext == null)
            this.attributeContext = this.defineResource.getAttribute();
        if (this.attributeContext == null)
            this.attributeContext = new AttBase();
        return this.attributeContext;
    };
    public boolean contains(String name) {
        return this.getAttribute().contains(name);
    };
    public void setAttribute(String name, Object value) {
        this.getAttribute().setAttribute(name, value);
    };
    public Object getAttribute(String name) {
        return this.getAttribute().getAttribute(name);
    };
    public void removeAttribute(String name) {
        this.getAttribute().removeAttribute(name);
    };
    public String[] getAttributeNames() {
        return this.getAttribute().getAttributeNames();
    };
    public void clearAttribute() {
        this.getAttribute().clearAttribute();
    };
    /*------------------------------------------------------------*/
    /**��ȡ���ڱ������ԵĶ���*/
    /**��ȡȫ���������棬�������ͨ����д�÷������滻FLASH��*/
    protected IAttribute getFlash() {
        if (this.flashContext == null)
            this.flashContext = new AttBase();
        return this.flashContext;
    };
    public AbstractEventManager getEventManager() {
        return this.defineResource.getEventManager();
    };
    public AbstractExpandPointManager getExpandPointManager() {
        return this.defineResource.getExpandPointManager();
    }
    public ELContext getELContext() {
        return this.elContext;
    };
};