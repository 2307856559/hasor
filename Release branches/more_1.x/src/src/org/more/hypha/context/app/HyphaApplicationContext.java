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
package org.more.hypha.context.app;
import org.more.hypha.AbstractBeanDefine;
import org.more.hypha.ApplicationContext;
import org.more.hypha.DefineResource;
import org.more.hypha.Event;
import org.more.hypha.beans.assembler.BeanEngine;
import org.more.hypha.context.AbstractApplicationContext;
import org.more.hypha.context.AbstractDefineResource;
import org.more.hypha.context.array.ArrayDefineResource;
import org.more.hypha.event.DestroyEvent;
import org.more.hypha.event.InitEvent;
import org.more.util.attribute.IAttribute;
/**
 * �򵥵�{@link ApplicationContext}�ӿ�ʵ���ࡣ
 * Date : 2011-4-8
 * @author ������ (zyc@byshell.org)
 */
public class HyphaApplicationContext extends AbstractApplicationContext {
    private DefineResource defineResource = null;
    private BeanEngine     engine         = null;
    //
    //
    public HyphaApplicationContext() {
        ArrayDefineResource adr = new ArrayDefineResource();
        this.setAttributeContext(adr);
        this.setFlashContext(adr.getFlash());
        this.defineResource = adr;
    }
    public HyphaApplicationContext(AbstractDefineResource defineResource) {
        this.setAttributeContext(defineResource);
        this.setFlashContext(defineResource.getFlash());
        this.defineResource = defineResource;
    }
    protected HyphaApplicationContext(DefineResource defineResource, IAttribute flash) {
        if (defineResource == null)
            throw new NullPointerException("����defineResourceû��ָ��һ����Ч��ֵ���ò���������Ϊ�ա�");
        this.setAttributeContext(defineResource);
        this.setFlashContext(flash);
        this.defineResource = defineResource;
    }
    public DefineResource getBeanResource() {
        return this.defineResource;
    }
    public void init() throws Throwable {
        this.engine = new BeanEngine();
        //
        // TODO Auto-generated method stub
        //5.��ʼ��bean
        for (String id : this.getBeanDefinitionIDs()) {
            AbstractBeanDefine define = this.getBeanDefinition(id);
            if (define.isLazyInit() == false)
                this.getBean(id);
        }
        //6.��ʼ���¼� 
        this.getEventManager().doEvent(Event.getEvent(InitEvent.class), this);
    }
    public void destroy() throws Throwable {
        this.getEventManager().doEvent(Event.getEvent(DestroyEvent.class), this);
        this.engine.clearBeanCache();
        this.engine.clearSingleBean();
        this.engine = null;
        this.clearAttribute();
        this.getELContext().clearAttribute();
        this.getEventManager().clearEvent();
        //this.getExpandPointManager().
        this.getFlash().clearAttribute();
        //this.getScopeContext().
        //this.getScriptContext().
    }
    public Object getServices(Class<?> servicesType) {
        // TODO Auto-generated method stub
        return null;
    }
    protected Object builderBean(AbstractBeanDefine define, Object[] params) throws Throwable {
        return this.engine.builderBean(define, params);
    }
    protected Class<?> builderType(AbstractBeanDefine define, Object[] params) throws Throwable {
        return this.engine.builderType(define, params);
    }
    //    public static final String              ELConfig           = "/META-INF/resource/hypha/regedit-el.prop";      //HyphaApplicationContext��������Ϣ
    //    public static final String              MetaDataConfig     = "/META-INF/resource/hypha/regedit-metadata.prop"; //HyphaApplicationContext��������Ϣ
    //    public static final String              BeanTypeConfig     = "/META-INF/resource/hypha/regedit-beantype.prop"; //HyphaApplicationContext��������Ϣ
    //    private static final int                InitTimeOut        = 120;                                             //120��500����=1���ӡ�init��ʱ
    //    /**
    //     * ����{@link HyphaApplicationContext}����������췽���ᵼ��{@link HyphaApplicationContext}�ڲ�����һ��{@link ArrayDefineResource}����
    //     * @param defineResource ָ��{@link HyphaApplicationContext}����һ��Bean���������Դ��
    //     * @param context ���췽������һ�������ò�����ʾһ�������� ��ͨ��el���Է��ʵ��������
    //     */
    //    public HyphaApplicationContext(DefineResource defineResource, Object context) {
    //        if (defineResource == null)
    //            throw new NullPointerException("defineResource��������Ϊ��");
    //        this.defineResource = defineResource;
    //        this.attributeContext = defineResource;
    //        this.contextObject = context;
    //    };
    //    public synchronized void init() throws Throwable {
    //        //1.������ʼ�������defineResourceû��׼���þ�һֱ�ȴ�
    //        int i = 0;
    //        while (!this.defineResource.isReady()) {
    //            Thread.sleep(500);
    //            i++;
    //            if (i >= InitTimeOut)
    //                throw new TimeOutException("����defineResource��isReady()ʼ�շ���false���޷����Context�ĳ�ʼ��������");
    //        }
    //        //---------------------------------------------------------------------------------------------------
    //        this.rootMetaDataParser = new AbstractRootValueMetaDataParser() {
    //            public void init(ApplicationContext context, IAttribute flash) throws Throwable {
    //                super.init(context, flash);
    //                List<InputStream> ins = ClassPathUtil.getResource(HyphaApplicationContext.MetaDataConfig);
    //                Properties prop = new Properties();
    //                for (InputStream is : ins)
    //                    prop.load(is);
    //                for (Object key : prop.keySet()) {
    //                    String beanBuilderClass = prop.getProperty((String) key);
    //                    Object builder = Class.forName(beanBuilderClass).newInstance();
    //                    this.addParser((String) key, (ValueMetaDataParser) builder);
    //                }
    //            };
    //        };
    //        //---------------------------------------------------------------------------------------------------
    //        this.engine = new AbstractBeanEngine() {
    //            public void init(ApplicationContext context, IAttribute flash) throws Throwable {
    //                super.init(context, flash);
    //                List<InputStream> ins = ClassPathUtil.getResource(HyphaApplicationContext.BeanTypeConfig);
    //                Properties prop = new Properties();
    //                for (InputStream is : ins)
    //                    prop.load(is);
    //                for (Object key : prop.keySet()) {
    //                    String beanBuilderClass = prop.getProperty((String) key);
    //                    Object builder = Class.forName(beanBuilderClass).newInstance();
    //                    this.regeditBeanBuilder((String) key, (AbstractBeanBuilder) builder);
    //                }
    //            };
    //        };
    //        //---------------------------------------------------------------------------------------------------
    //        this.elContext = new AbstractELContext() {
    //            public void init(ApplicationContext context, IAttribute flash) throws Throwable {
    //                super.init(context, flash);
    //                //װ��regedit-metadata.prop�����ļ���------------------------------------
    //                List<InputStream> ins = ClassPathUtil.getResource(HyphaApplicationContext.ELConfig);
    //                Properties prop = new Properties();
    //                for (InputStream is : ins)
    //                    prop.load(is);
    //                for (Object key : prop.keySet()) {
    //                    String k = (String) key;
    //                    String beanBuilderClass = prop.getProperty(k);
    //                    Object builder = Class.forName(beanBuilderClass).getConstructor().newInstance();
    //                    this.addELObject(k, (ELObject) builder);
    //                }
    //            }
    //        };
    //        //---------------------------------------------------------------------------------------------------
    //        this.rootMetaDataParser.init(this, this.getFlash());//����regedit-metadata.prop�������ļ���
    //        this.engine.init(this, this.getFlash());//����regedit-beantype.prop�������ļ���
    //        this.elContext.init(this, this.getFlash());//����regedit-el.prop�������ļ���
    //        this.scopeContext.init(this, this.getFlash());
    //        this.scriptContext.init(this, this.getFlash());
    //        //---------------------------------------------------------------------------------------------------
    //    };
};