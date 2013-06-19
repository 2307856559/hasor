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
package org.more.hypha.aop.assembler;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.more.core.classcode.AopBeforeListener;
import org.more.core.classcode.AopInvokeFilter;
import org.more.core.classcode.AopReturningListener;
import org.more.core.classcode.AopThrowingListener;
import org.more.core.classcode.BuilderMode;
import org.more.core.classcode.ClassEngine;
import org.more.core.classcode.RootClassLoader;
import org.more.hypha.AbstractBeanDefine;
import org.more.hypha.ApplicationContext;
import org.more.hypha.aop.define.AbstractInformed;
import org.more.hypha.aop.define.AopConfigDefine;
import org.more.hypha.aop.define.PointcutType;
import org.more.util.BeanUtil;
/**
 * �������������ɺ�����aop���ࡣ
 * @version : 2011-7-8
 * @author ������ (zyc@byshell.org)
 */
public class AopBuilder {
    private ApplicationContext         context   = null;
    private RootClassLoader            loader    = null; //��Loader�Ǹ���װ��aop�����
    private Map<Class<?>, ClassEngine> engineMap = null; //����ClassEngine
    //
    public AopBuilder(ApplicationContext context) {
        this.context = context;
    };
    public ApplicationContext getContext() {
        return this.context;
    };
    /*��ʼ������*/
    public void init() {
        this.loader = new RootClassLoader(this.context.getClassLoader());
        this.engineMap = new HashMap<Class<?>, ClassEngine>();
    };
    /*���ٷ���*/
    public void destroy() {
        this.loader = null;
        this.engineMap.clear();
        this.engineMap = null;
    };
    /**/
    private AopPropxyInformed passerInformed(AbstractInformed informed) {
        return new AopPropxyInformed(this.context, informed);
    };
    private void configAop(ClassEngine engine, AopConfigDefine aopDefine) {
        engine.resetAop();
        for (AbstractInformed informed : aopDefine.getAopInformedList()) {
            PointcutType type = informed.getPointcutType();
            Object informedObject = this.passerInformed(informed);
            if (type == PointcutType.Before)//ע��before֪ͨ
                engine.addListener((AopBeforeListener) informedObject);
            else if (type == PointcutType.Returning)//ע��return֪ͨ
                engine.addListener((AopReturningListener) informedObject);
            else if (type == PointcutType.Throwing)//ע��throw֪ͨ
                engine.addListener((AopThrowingListener) informedObject);
            else if (type == PointcutType.Filter)//ע��filter
                engine.addAopFilter((AopInvokeFilter) informedObject);
            else {//ע��auto��ȫ��ע���ϡ�����AopPropxyInformed���Զ��ж��Ƿ�ִ�С�
                engine.addListener((AopBeforeListener) informedObject);
                engine.addListener((AopReturningListener) informedObject);
                engine.addListener((AopThrowingListener) informedObject);
                engine.addAopFilter((AopInvokeFilter) informedObject);
            }
        }
    };
    /**��ȡһ��aop bean���͡�*/
    public Class<?> builderType(Class<?> beanType, AopConfigDefine aopDefine, AbstractBeanDefine define) throws ClassNotFoundException, IOException {
        if (aopDefine.getAopMode() == BuilderMode.Propxy)
            return beanType;
        //
        if (this.engineMap.containsKey(beanType) == true)
            return this.engineMap.get(beanType).toClass();
        //
        ClassEngine engine = new ClassEngine();
        engine.setRootClassLoader(this.loader);
        engine.setBuilderMode(BuilderMode.Super);
        engine.setSuperClass(beanType);
        engine.generateName();
        engineMap.put(beanType, engine);//����Ҫ����aop�����͡�
        //
        BeanUtil.writePropertyOrField(define, "boolCheckType", false);//�ر����ͼ��
        //
        return engine.builderClass().toClass();
    }
    /**����һ��aop���õ�bean�����bean�ǹ�����ʽ�������������ｫʹ�ô���ʽʵ����aop���ܡ�*/
    public Object builderBean(Object beanObject, AopConfigDefine aopDefine, AbstractBeanDefine define) throws ClassNotFoundException, IOException {
        ClassEngine engine = null;
        Object aopBean = null;
        //��ȡengine����beanObject
        ClassLoader loader = beanObject.getClass().getClassLoader();
        if (loader instanceof RootClassLoader == true) {
            //1.Super��ʽaop
            RootClassLoader rootLoader = (RootClassLoader) loader;
            engine = rootLoader.getRegeditEngine(beanObject.getClass().getName());
            this.configAop(engine, aopDefine);//ÿ�ζ�����configΪ�˱�֤ÿ��Bean�м�
            aopBean = beanObject;
        } else {
            //2.Propxy��ʽAop
            Class<?> beanType = beanObject.getClass();
            engine = new ClassEngine();
            engine.setRootClassLoader(this.loader);
            engine.setBuilderMode(BuilderMode.Propxy);
            engine.setSuperClass(beanType);
            engine.generateName();
            this.configAop(engine, aopDefine);//
            engineMap.put(beanType, engine);//����Ҫ����aop�����͡�
            aopBean = engine.newInstance(beanObject);
        }
        //2.ִ��aop����ע��
        if (engine.isConfig(aopBean) == false)
            engine.configBean(aopBean);//���û�����ù���������ã���Ŀ����Ϊ�˼����ظ������á�
        return aopBean;
    }
};