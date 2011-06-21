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
import org.more.core.error.DefineException;
import org.more.hypha.AbstractBeanDefine;
import org.more.hypha.ApplicationContext;
import org.more.hypha.ELContext;
import org.more.hypha.Event;
import org.more.hypha.EventManager;
import org.more.hypha.ExpandPointManager;
import org.more.hypha.commons.AbstractELContext;
import org.more.hypha.commons.logic.EngineLogic;
import org.more.log.ILog;
import org.more.log.LogFactory;
import org.more.util.attribute.IAttribute;
/**
 * �򵥵�{@link ApplicationContext}�ӿ�ʵ���࣬����ֻ���ṩ��һ��ƽ̨��
 * Date : 2011-4-8
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractApplicationContext implements ApplicationContext {
    private static ILog           log             = LogFactory.getLog(AbstractApplicationContext.class);
    private PropxyClassLoader     classLoader     = null;
    //init�ڼ���빹���������������
    private Object                contextObject   = null;
    private AbstractELContext     elContext       = null;
    //
    private Map<String, Object>   singleBeanCache = null;
    private Map<String, Class<?>> typeCache       = null;
    //
    private EngineLogic           engineLogic     = null;
    /*------------------------------------------------------------*/
    public AbstractApplicationContext(ClassLoader classLoader) {
        this.classLoader = new PropxyClassLoader();
        this.classLoader.setLoader(classLoader);
    };
    public Object getContextObject() {
        return this.contextObject;
    };
    public void setContextObject(Object contextObject) {
        log.info("change contextObject form '{%0}' to '{%1}'", this.contextObject, contextObject);
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
    public ClassLoader getBeanClassLoader() {
        return this.classLoader;
    };
    /**�滻��ǰ��ClassLoader��*/
    public void setBeanClassLoader(ClassLoader loader) {
        log.info("change ParentClassLoader form '{%0}' to '{%1}'", this.classLoader.getLoader(), loader);
        this.classLoader.setLoader(loader);
    };
    public EngineLogic getEngineLogic() {
        return this.engineLogic;
    };
    /*------------------------------------------------------------*/
    /**�����{@link AbstractApplicationContext}������������ĵ���Bean����*/
    public void clearSingleBean() {
        log.info("clear all Single Bean!");
        this.singleBeanCache.clear();
    };
    /**��ȡһ��int��int��ʾ��{@link AbstractApplicationContext}�������Ѿ������˵ĵ���������Ŀ��*/
    public int getCacheBeanCount() {
        return this.singleBeanCache.size();
    };
    public abstract AbstractDefineResource getBeanResource();
    /**��init�ڼ䱻���ã����������д�������滻EL�����ġ�*/
    protected AbstractELContext createELContext() {
        return new AbstractELContext() {};
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
    public void init() {
        log.info("starting init ApplicationContext...");
        this.singleBeanCache = new HashMap<String, Object>();
        this.typeCache = new HashMap<String, Class<?>>();
        log.info("created cache, singleBeanCache = {%0}, typeCache = {%1}.", singleBeanCache, typeCache);
        //
        this.elContext = this.createELContext();
        this.engineLogic = new EngineLogic();
        log.info("created elContext = {%0}, engineLogic = {%1}", elContext, engineLogic);
        //
        this.elContext.init(this);
        this.engineLogic.init(this);
        log.info("inited elContext and engineLogic OK!");
        //
        log.info("sending event...");
        this.getEventManager().doEvent(Event.getEvent(InitEvent.class), this);
        this.getEventManager().doEvent(Event.getEvent(InitedEvent.class), this);
        log.info("started!");
    };
    /**��JVM���ոö���ʱ�Զ��������ٷ�����*/
    protected void finalize() throws Throwable {
        if (this.engineLogic != null)
            try {
                log.info("use finalize destroy ApplicationContext...");
                this.destroy();
            } catch (Exception e) {
                log.warning("use finalize destroy ApplicationContext an error , error = {%0}", e);
            }
        super.finalize();
    };
    public void destroy() {
        /**�����¼�*/
        log.info("sending Context Destroy event...");
        this.getEventManager().doEvent(Event.getEvent(DestroyEvent.class), this);
        log.info("popEvent all event...");
        this.getEventManager().popEvent();//���������¼�
        //
        log.info("set null...");
        this.elContext = null;
        this.singleBeanCache = null;
        this.typeCache = null;
        this.engineLogic = null;
        log.info("destroy is OK!");
    };
    /*------------------------------------------------------------*/
    private final String KEY = "GETBEAN_PARAM";
    @SuppressWarnings("unchecked")
    public <T> T getBean(String defineID, Object... objects) throws Throwable {
        if (defineID == null || defineID.equals("") == true) {
            log.error("error , defineID is null or empty.");
            throw new NullPointerException("error , defineID is null or empty.");
        }
        if (this.singleBeanCache.containsKey(defineID) == true) {
            Object obj = this.singleBeanCache.get(defineID);
            log.info("{%0} bean form cache return.", defineID);
            return (T) obj;
        }
        AbstractBeanDefine define = this.getBeanDefinition(defineID);
        if (define == null) {
            log.error("{%0} define is not exist.", defineID);
            throw new DefineException(defineID + " define is not exist.");
        }
        //-------------------------------------------------------------------��ȡ
        log.info("start building {%0} bean , params is {%1}", defineID, objects);
        try {
            this.getThreadFlash().setAttribute(KEY, objects);
            log.debug("put params to ThreadFlash key is {%0}", KEY);
            //1.��ȡbean�Լ�bean���͡�
            Class<?> beanType = this.getBeanType(defineID, objects);//��ȡ����
            Object bean = this.engineLogic.builderBean(define, objects);//����Bean
            log.info("finish build!, object = {%0}", bean);
            //3.��̬����&����ƥ��
            if (beanType != null)
                /**�����������Ƿ�ƥ�䶨�����ͣ����û��ָ��beanType������ֱ�ӷ��ء�*/
                bean = beanType.cast(bean);
            log.debug("bean cast type {%0} OK!", beanType);
            if (define.isSingleton() == true) {
                log.info("{%0} bean is Singleton!", defineID);
                this.singleBeanCache.put(defineID, bean);
            }
            return (T) bean;
        } catch (Throwable e) {
            log.error("get bean is error, error = {%0}", e);
            throw e;
        } finally {
            log.debug("remove params from ThreadFlash key is {%0}", KEY);
            this.getThreadFlash().removeAttribute(KEY);
        }
    };
    public Class<?> getBeanType(String defineID, Object... objects) throws Throwable {
        if (defineID == null || defineID.equals("") == true) {
            log.error("error , defineID is null or empty.");
            throw new NullPointerException("error , defineID is null or empty.");
        }
        if (this.typeCache.containsKey(defineID) == true) {
            Class<?> type = this.typeCache.get(defineID);
            log.info("{%0} bean type form cache return.", defineID);
            return type;
        }
        AbstractBeanDefine define = this.getBeanDefinition(defineID);
        if (define == null) {
            log.error("{%0} define is not exist.", defineID);
            throw new DefineException(defineID + " define is not exist.");
        }
        //-------------------------------------------------------------------��ȡ
        log.info("start building {%0} bean type , params is {%1}", defineID, objects);
        try {
            this.getThreadFlash().setAttribute(KEY, objects);
            log.debug("put params to ThreadFlash key is {%0}", KEY);
            Class<?> beanType = this.engineLogic.builderType(define, objects);
            log.info("finish build! type = {%0}", beanType);
            this.typeCache.put(defineID, beanType);
            return beanType;
        } catch (Throwable e) {
            log.error("get BeanType is error, error = {%0}", e);
            throw e;
        } finally {
            log.debug("remove params from ThreadFlash key is {%0}", KEY);
            this.getThreadFlash().removeAttribute(KEY);
        }
    };
    /**���ص�ǰ�߳�getBeanʱ��������в����������getBean����֮��ʹ�ø÷��������᷵��һ��null��*/
    public Object[] getGetBeanParams() {
        Object[] params = null;
        IAttribute tflash = this.getThreadFlash();
        if (tflash.contains(KEY) == false)
            return null;
        params = (Object[]) tflash.getAttribute(KEY);
        log.debug("GetBean params KEY = {%0}, params = {%1}", KEY, params);
        return params.clone();//ʹ�ÿ�¡�Է�ֹ�ⲿ�����������
    }
    /**���ص�ǰ�߳�getBeanʱ�����ָ��λ�ò����������getBean����֮��ʹ�ø÷��������᷵��һ��null��*/
    public Object getGetBeanParam(int index) {
        IAttribute tflash = this.getThreadFlash();
        Object[] params = (Object[]) tflash.getAttribute(KEY);
        if (params == null) {
            log.debug("GetBean params {%0} is null.");
            return null;
        }
        if (index < 0 || index > params.length) {
            log.debug("GetBean params [{%0}] index out of bounds.", index);
            return null;
        }
        Object obj = params[index];
        log.debug("GetBean params index = {%0}, value = {%1}", index, obj);
        return obj;
    }
    /*------------------------------------------------------------*/
    public List<String> getBeanDefinitionIDs() {
        return this.getBeanResource().getBeanDefinitionIDs();
    };
    public AbstractBeanDefine getBeanDefinition(String id) {
        return this.getBeanResource().getBeanDefine(id);
    };
    public boolean containsBean(String id) {
        return this.getBeanResource().containsBeanDefine(id);
    };
    public boolean isPrototype(String id) throws DefineException {
        return this.getBeanResource().isPrototype(id);
    };
    public boolean isSingleton(String id) throws DefineException {
        return this.getBeanResource().isSingleton(id);
    };
    public boolean isFactory(String id) throws DefineException {
        return this.getBeanResource().isFactory(id);
    };
    public boolean isTypeMatch(String id, Class<?> targetType) throws Throwable {
        //Object.class.isAssignableFrom(XmlTest.class); return true;
        if (id == null || targetType == null)
            throw new NullPointerException("����id��targetType����Ϊ��.");
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
};