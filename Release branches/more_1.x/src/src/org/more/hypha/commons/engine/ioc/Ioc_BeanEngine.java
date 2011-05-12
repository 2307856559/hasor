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
package org.more.hypha.commons.engine.ioc;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import org.more.hypha.AbstractBeanDefine;
import org.more.hypha.AbstractMethodDefine;
import org.more.hypha.AbstractPropertyDefine;
import org.more.hypha.PropertyBinding;
import org.more.hypha.ValueMetaData;
import org.more.hypha.commons.AbstractExpandPointManager;
import org.more.hypha.commons.engine.AbstractBeanBuilder;
/**
* �����ְ���Ǹ���{@link AbstractBeanDefine}ת�������ͻ���Beanʵ�����
* ������һ�������࣬��ʹ��ʱ��Ҫͨ�����������{@link AbstractExpandPointManager}����
* @version 2011-1-13
* @author ������ (zyc@byshell.org)
*/
public class Ioc_BeanEngine extends IocEngine {
    public static final String EngineName = "Ioc";
    /**
     * �������洴������ȥ����bean���롣
     * @param define Ҫװ�����͵�bean���塣
     * @param params ����beanʱ���ݵĲ�����
     * @return ���ش�����bean����
     */
    public final Object builderBean(AbstractBeanDefine define, Object[] params) throws Throwable {
        String defineID = define.getID();
        //--------------------------------------------------------------------------------------------------------------
        AbstractBeanBuilder<AbstractBeanDefine> builder = this.beanBuilderMap.get(define.getBeanType());
        Class<?> beanType = this.builderType(define, params);
        //1.����
        Object obj = this.applicationContext.getExpandPointManager().exePointOnReturn(BeforeCreatePoint.class, //Ԥ����Bean
                new Object[] { beanType, params, define, this.applicationContext });
        if (obj == null)
            if (builder.ifDefaultBeanCreateMode() == false)
                //------------------------������Ĭ�ϲ��ԣ�
                obj = builder.createBean(define, params);
            else {
                //------------------------��ʹ��Ĭ�ϲ��ԣ�
                // create begin
                AbstractMethodDefine factory = define.factoryMethod();
                Collection<? extends AbstractPropertyDefine> initParam = null;
                if (factory != null) {
                    //1.������ʽ
                    initParam = factory.getParams();
                    Class<?>[] types = transform_toTypes(initParam, params);
                    Object[] objects = transform_toObjects(initParam, params);
                    String factoryBeanID = factory.getForBeanDefine().getID();
                    if (factory.isStatic() == true) {
                        Class<?> factoryType = this.applicationContext.getBeanType(factoryBeanID);
                        Method factoryMethod = factoryType.getMethod(factory.getCodeName(), types);
                        obj = factoryMethod.invoke(null, objects);
                    } else {
                        Object factoryObject = this.applicationContext.getBean(factoryBeanID, params);/*params�����ᱻ˳�ƴ��빤��bean�С�*/
                        Method factoryMethod = factoryObject.getClass().getMethod(factory.getCodeName(), types);
                        obj = factoryMethod.invoke(factoryObject, objects);
                    }
                } else {
                    //2.���췽��
                    initParam = define.getInitParams();
                    Class<?>[] types = transform_toTypes(initParam, params);
                    Object[] objects = transform_toObjects(initParam, params);
                    //
                    Constructor<?> constructor = beanType.getConstructor(types);
                    obj = constructor.newInstance(objects);
                }
                // create end//
            }
        //2.����ע��
        for (AbstractPropertyDefine propDefine : define.getPropertys()) {
            PropertyDefine prop = (PropertyDefine) propDefine;
            PropertyBinding eval = this.applicationContext.getELContext().getPropertyBinding(prop.getName(), obj);
            eval.setValue(this.transform_toObject(propDefine, params));//ELע��
        }
        //--------------------------------------------------------------------------------------------------------------��ʼ���׶�
        //3.��̬����&����ƥ��
        obj = this.cast(beanType, obj);
        if (define.isSingleton() == true)
            this.singleBeanCache.put(defineID, obj);
        //4.�������ٷ���
        {
            //TODO  ProxyFinalizeClassEngine ce = new ProxyFinalizeClassEngine(this);
            //TODO  ce.setBuilderMode(BuilderMode.Propxy);
            //TODO  ce.setSuperClass(objType);
            //TODO  obj = ce.newInstance(obj);
        }
        //5.ִ����չ��
        obj = this.applicationContext.getExpandPointManager().exePointOnSequence(AfterCreatePoint.class,//Beanװ��
                new Object[] { obj, params, define, this.applicationContext });
        //6.ִ����������init����
        {
            Class<?> objType = obj.getClass();
            String initMethodName = define.getInitMethod();
            if (initMethodName != null) {
                Method m = objType.getMethod(initMethodName, Object[].class);
                m.invoke(obj, params);
            }
        }
        return obj;
    };
    /**�����������Ƿ�ƥ�䶨�����ͣ����û��ָ��beanType������ֱ�ӷ��ء�*/
    private Object cast(Class<?> beanType, Object obj) throws ClassCastException {
        if (beanType != null)
            return beanType.cast(obj);
        return obj;
    };
    /*��һ������ת�������͡�*/
    private Class<?>[] transform_toTypes(Collection<? extends AbstractPropertyDefine> pds, Object[] params) {
        System.out.println();
        return null;//TODO
    };
    /*��һ������ת���ɶ���*/
    private Object[] transform_toObjects(Collection<? extends AbstractPropertyDefine> pds, Object[] params) {
        System.out.println();
        return null;//TODO
    };
    /*������ת���ɶ���*/
    private Object transform_toObject(AbstractPropertyDefine prop, Object[] params) {
        System.out.println();
        return null;//TODO
    };
    private Collection<? extends ValueMetaData> transform(Collection<? extends AbstractPropertyDefine> pds) {
        ArrayList<ValueMetaData> vms = new ArrayList<ValueMetaData>(pds.size());
        for (AbstractPropertyDefine pd : pds)
            vms.add(pd.getMetaData());
        return vms;
    }
};