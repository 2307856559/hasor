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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.more.NoDefinitionException;
import org.more.hypha.AbstractBeanDefine;
import org.more.hypha.ApplicationContext;
import org.more.hypha.ELContext;
import org.more.hypha.Event;
import org.more.hypha.EventManager;
import org.more.hypha.ExpandPointManager;
import org.more.hypha.ScopeContext;
import org.more.hypha.ScriptContext;
import org.more.hypha.commons.AbstractELContext;
import org.more.hypha.commons.AbstractScopeContext;
import org.more.hypha.commons.AbstractScriptContext;
import org.more.hypha.commons.engine.BeanEngine;
import org.more.hypha.context.app.DestroyEvent;
import org.more.hypha.context.app.InitEvent;
import org.more.util.attribute.IAttribute;
/**
 * �򵥵�{@link ApplicationContext}�ӿ�ʵ���࣬����ֻ���ṩ��һ��ƽ̨��
 * Date : 2011-4-8
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractApplicationContext implements ApplicationContext {
    private PropxyClassLoader       classLoader     = null;
    //init�ڼ���빹���������������
    private Object                  contextObject   = null;
    private ELContext               elContext       = null;
    private ScopeContext            scopeContext    = null;
    private ScriptContext           scriptContext   = null;
    //
    private Map<String, Object>     singleBeanCache = null;
    private Map<String, Class<?>>   singleTypeCache = null;
    private Map<String, BeanEngine> engineMap       = null;
    /*------------------------------------------------------------*/
    public AbstractApplicationContext(ClassLoader classLoader) {
        this.classLoader = new PropxyClassLoader();
        this.classLoader.setLoader(classLoader);
    };
    public Object getContextObject() {
        return this.contextObject;
    };
    public void setContextObject(Object contextObject) {
        this.contextObject = contextObject;
    };
    public EventManager getEventManager() {
        return this.getBeanResource().getEventManager();
    };
    public ExpandPointManager getExpandPointManager() {
        return this.getBeanResource().getExpandPointManager();
    };
    public ELContext getELContext() {
        return this.elContext;
    };
    public ScopeContext getScopeContext() {
        return this.scopeContext;
    };
    public ScriptContext getScriptContext() {
        return this.scriptContext;
    };
    public ClassLoader getBeanClassLoader() {
        return this.classLoader;
    };
    /**�滻��ǰ��ClassLoader��*/
    public void setBeanClassLoader(ClassLoader loader) {
        this.classLoader.setLoader(loader);
    };
    /**��ȡ{@link AbstractApplicationContext}��������Bean����������*/
    protected BeanEngine getEngine(String key) throws Throwable {
        return this.engineMap.get(key);
    };
    /**���һ��bean�������棬ÿ��bean����һ��getBuildFactory()�����÷��������beanʹ�õ����������档
     * ���Ǹ��������������ע���ϵġ�*/
    public void addBeanEngine(String key, BeanEngine engine) throws Throwable {
        engine.init(this, this.getFlash());
        this.engineMap.put(key, engine);
    };
    /*------------------------------------------------------------*/
    /**�����{@link AbstractApplicationContext}������������ĵ���Bean����*/
    public void clearSingleBean() {
        this.singleBeanCache.clear();
    };
    /**��ȡһ��int��int��ʾ��{@link AbstractApplicationContext}�������Ѿ������˵ĵ���������Ŀ��*/
    public int getCacheBeanCount() {
        return this.singleBeanCache.size();
    };
    public abstract AbstractDefineResource getBeanResource();
    /**��init�ڼ䱻���ã����������д�������滻EL�����ġ�*/
    protected AbstractELContext createELContext() {
        return new AbstractELContext(this) {};
    };
    /**��init�ڼ䱻���ã����������д�������滻�������������*/
    protected AbstractScopeContext createScopeContext() {
        return new AbstractScopeContext(this) {};
    };
    /**��init�ڼ䱻���ã����������д�������滻Ĭ�ϵĽű������������*/
    protected AbstractScriptContext createScriptContext() {
        return new AbstractScriptContext(this) {};
    };
    /**�÷������Ի�ȡ{@link AbstractApplicationContext}�ӿڶ�����ʹ�õ����Թ��������������ͨ����д�÷��������������Թ���������*/
    protected IAttribute getAttribute() {
        return this.getBeanResource();
    };
    /**��ȡFlash�����flash��һ���ڲ���ϢЯ���塣�����Թᴩ����hypha�����н׶Ρ����Ҳ��ܿ��߳����ơ�*/
    public IAttribute getFlash() {
        return this.getBeanResource().getFlash();
    };
    /**��ȡFlash�����flash��һ���ڲ���ϢЯ���塣�����Թᴩ����hypha�����н׶Ρ��������FLASH�ܿ��߳����ơ�*/
    public IAttribute getThreadFlash() {
        return this.getBeanResource().getThreadFlash();
    };
    /*------------------------------------------------------------*/
    public void init() throws Throwable {
        this.elContext = this.createELContext();
        this.scopeContext = this.createScopeContext();
        this.scriptContext = this.createScriptContext();
        this.engineMap = new HashMap<String, BeanEngine>();
        this.singleBeanCache = new HashMap<String, Object>();
        //
        this.getEventManager().doEvent(Event.getEvent(InitEvent.class), this);
    };
    /**��JVM���ոö���ʱ�Զ��������ٷ�����*/
    protected void finalize() throws Throwable {
        try {
            this.destroy();
        } catch (Exception e) {}
        super.finalize();
    };
    public void destroy() throws Throwable {
        /**�����¼�*/
        this.getEventManager().doEvent(Event.getEvent(DestroyEvent.class), this);
        this.getEventManager().popEvent();//���������¼�
        //
        this.elContext = null;
        this.scopeContext = null;
        this.scriptContext = null;
        this.engineMap = null;
        this.singleBeanCache = null;
    };
    public <T> T getBean(String defineID, Object... objects) throws Throwable {
        //-------------------------------------------------------------------��鵥̬
        if (this.singleBeanCache.containsKey(defineID) == true)
            return (T) this.singleBeanCache.get(defineID);
        //-------------------------------------------------------------------��ȡ
        final String KEY = "GETBEAN_PARAM";
        try {
            this.getThreadFlash().setAttribute(KEY, objects);
            AbstractBeanDefine define = this.getBeanDefinition(defineID);
            if (define == null)
                throw new NoDefinitionException("������idΪ[" + defineID + "]��Bean���塣");
            String beName = define.getBuildFactory();
            BeanEngine be = this.getEngine(beName);
            if (be == null)
                throw new NoDefinitionException("idΪ[" + defineID + "]��Bean���壬�޷�ʹ��δ�����[" + beName + "]���湹����");
            //
            Object bean = be.builderBean(define, objects);
            if (define.isSingleton() == true)
                this.singleBeanCache.put(defineID, bean);
            return (T) bean;
        } catch (Throwable e) {
            throw e;
        } finally {
            this.getThreadFlash().removeAttribute(KEY);
        }
    };
    public Class<?> getBeanType(String defineID, Object... objects) throws Throwable {
        //-------------------------------------------------------------------��鵥̬
        if (this.singleTypeCache.containsKey(defineID) == true)
            return this.singleTypeCache.get(defineID);
        //-------------------------------------------------------------------��ȡ
        final String KEY = "GETBEAN_PARAM";
        try {
            this.getThreadFlash().setAttribute(KEY, objects);
            AbstractBeanDefine define = this.getBeanDefinition(defineID);
            if (define == null)
                throw new NoDefinitionException("������idΪ[" + defineID + "]��Bean���塣");
            String beName = define.getBuildFactory();
            BeanEngine be = this.getEngine(beName);
            if (be == null)
                throw new NoDefinitionException("idΪ[" + defineID + "]��Bean���壬�޷�ʹ��δ�����[" + beName + "]���湹����");
            //
            Class<?> beanType = be.builderType(define, objects);
            if (define.isSingleton() == true)
                this.singleTypeCache.put(defineID, beanType);
            return beanType;
        } catch (Throwable e) {
            throw e;
        } finally {
            this.getThreadFlash().removeAttribute(KEY);
        }
    };
    public List<String> getBeanDefinitionIDs() {
        return this.getBeanResource().getBeanDefinitionIDs();
    };
    public AbstractBeanDefine getBeanDefinition(String id) throws NoDefinitionException {
        return this.getBeanResource().getBeanDefine(id);
    };
    public boolean containsBean(String id) {
        return this.getBeanResource().containsBeanDefine(id);
    };
    public boolean isPrototype(String id) throws NoDefinitionException {
        return this.getBeanResource().isPrototype(id);
    };
    public boolean isSingleton(String id) throws NoDefinitionException {
        return this.getBeanResource().isSingleton(id);
    };
    public boolean isFactory(String id) throws NoDefinitionException {
        return this.getBeanResource().isFactory(id);
    };
    public boolean isTypeMatch(String id, Class<?> targetType) throws Throwable {
        //Object.class.isAssignableFrom(XmlTest.class); return true;
        if (targetType == null)
            throw new NullPointerException("����targetType����Ϊ��.");
        Class<?> beanType = this.getBeanType(id);
        return targetType.isAssignableFrom(beanType);
    };
    /*------------------------------------------------------------*/
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
    public Map<String, Object> toMap() {
        return this.getAttribute().toMap();
    };
    /*------------------------------------------------------------*/
    public Object getServices(Class<?> servicesType) {
        // TODO Auto-generated method stub
        return null;
    };
};