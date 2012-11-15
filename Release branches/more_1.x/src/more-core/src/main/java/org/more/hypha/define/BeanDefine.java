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
package org.more.hypha.define;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.more.core.error.RepeateException;
/**
 * TemplateBeanDefine�����ڶ���һ��bean��ģ�塣
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public abstract class BeanDefine extends AbstractDefine<BeanDefine> {
    private String                          id            = null;                                       //id
    private String                          name          = null;                                       //����
    private String                          logicPackage  = null;                                       //�߼���
    private String                          iocEngine     = "Ioc";                                      //����Bean��ʹ�õ���������
    private boolean                         boolAbstract  = false;                                      //�����־
    private boolean                         boolSingleton = false;                                      //��̬��־
    private boolean                         boolLazyInit  = true;                                       //�ӳ�װ�ر�־
    private boolean                         boolCheckType = true;                                       //�Ƿ�Ҫ��ǿ�����ͼ��
    private String                          description   = null;                                       //������Ϣ
    /**��ȡ����bean��*/
    private BeanDefine                      factoryBean   = null;
    private MethodDefine                    factoryMethod = null;
    /**��ȡbean��Aop����*/
    private AopConfigDefine                 aopConfig     = null;
    //
    private ArrayList<ConstructorDefine>    initParams    = new ArrayList<ConstructorDefine>();         //��ʼ������
    private List<String>                    propertyNames = new ArrayList<String>();
    private HashMap<String, PropertyDefine> propertys     = new LinkedHashMap<String, PropertyDefine>(); //����
    private List<String>                    methodNames   = new ArrayList<String>();
    private HashMap<String, MethodDefine>   methods       = new HashMap<String, MethodDefine>();        //����
    private String                          initMethod    = null;                                       //��ʼ������
    private String                          destroyMethod = null;                                       //���ٷ���
    private BeanDefine                      useTemplate   = null;                                       //Ӧ�õ�ģ��
    //-------------------------------------------------------------
    /**����bean��Ψһ��ţ����û��ָ��id������idֵ����fullName����ֵ��*/
    public String getID() {
        if (this.id == null)
            this.id = this.getFullName();
        return this.id;
    };
    /**����bean�����ƣ����ָ����package������ôname��ֵ���Գ����ظ���*/
    public String getName() {
        return this.name;
    };
    /**��ȡBean���߼������壬��������������ʵ����������ͬ��������Ϊһ�����ڵ��߼�������ʽ��*/
    public String getPackage() {
        return this.logicPackage;
    };
    /**��ȡ����package��bean���������������޶�����*/
    public String getFullName() {
        if (this.logicPackage != null)
            return this.logicPackage + "." + this.name;
        else
            return this.getName();
    }
    /**����ע����ʹ�õ�ע�뷽ʽ��Fact��Ioc��User*/
    public String getIocEngine() {
        return this.iocEngine;
    }
    /**����ע����ʹ�õ�ע�뷽ʽ��Fact��Ioc��User*/
    public void setIocEngine(String iocEngine) {
        this.iocEngine = iocEngine;
    }
    /**����һ��booleanֵ����ʾ���Ƿ�Ϊһ�������ࡣ*/
    public boolean isAbstract() {
        return this.boolAbstract;
    };
    /**����һ��booleanֵ����ʾ���bean�Ƿ�Ϊ��̬�ġ�*/
    public boolean isSingleton() {
        return this.boolSingleton;
    };
    /**����һ��booleanֵ����ʾ���bean�Ƿ�Ϊ�ӳ�װ�صġ�*/
    public boolean isLazyInit() {
        return this.boolLazyInit;
    };
    /**�Ƿ�Ҫ��ǿ�����ͼ��*/
    public boolean isCheck() {
        return this.boolCheckType;
    };
    /**����bean��������Ϣ��*/
    public String getDescription() {
        return this.description;
    };
    /**��ȡ����bean��*/
    public BeanDefine factoryBean() {
        return this.factoryBean;
    };
    /**�÷�����factoryName()�����ǳɶԳ��ֵģ��÷�������Ŀ�귽���Ĵ������ơ�*/
    public MethodDefine factoryMethod() {
        return this.factoryMethod;
    };
    /**��ȡ�����Ķ��壬�����ǰ������û���������Զ���ʹ�õ�ģ���в��ҡ���������ֱ��ģ�巵��Ϊ�ա�*/
    public MethodDefine getMethod(String name) {
        MethodDefine md = this.getDeclaredMethod(name);
        if (md == null && this.useTemplate != null)
            return this.useTemplate.getMethod(name);
        return md;
    };
    /**��ȡ�����Ķ��壬�÷���ֻ���ڵ�ǰ�����в��ҡ�*/
    public MethodDefine getDeclaredMethod(String name) {
        return this.methods.get(name);
    };
    /**��ȡ��ǰ�����п��õķ����������ϡ�*/
    public Collection<? extends MethodDefine> getMethods() {
        HashMap<String, MethodDefine> ms = new HashMap<String, MethodDefine>();
        ms.putAll(this.methods);
        if (this.useTemplate != null)
            for (MethodDefine m : this.useTemplate.getMethods())
                if (ms.containsKey(m.getName()) == false)
                    ms.put(m.getName(), m);
        return Collections.unmodifiableCollection((Collection<MethodDefine>) ms.values());
    };
    /**��ȡ��ǰ�����������ķ����б����صĽ��������ʹ�õ�ģ���еķ���������*/
    public Collection<? extends MethodDefine> getDeclaredMethods() {
        return Collections.unmodifiableCollection((Collection<MethodDefine>) this.methods.values());
    };
    /**��ȡ���Զ��壬�����ǰ������û���������Զ���ʹ�õ�ģ���в��ҡ���������ֱ��ģ�巵��Ϊ�ա�*/
    public PropertyDefine getProperty(String name) {
        PropertyDefine define = this.getDeclaredProperty(name);
        if (define == null && this.useTemplate != null)
            return this.useTemplate.getProperty(name);
        return define;
    };
    /**��ȡ���Զ��壬�÷���ֻ���ڵ�ǰ�����в��ҡ�*/
    public PropertyDefine getDeclaredProperty(String name) {
        return this.propertys.get(name);
    };
    /**����bean�Ķ������Լ��ϣ����صļ�����һ��ֻ�����ϡ�*/
    public Collection<? extends PropertyDefine> getPropertys() {
        HashMap<String, PropertyDefine> ps = new HashMap<String, PropertyDefine>();
        ps.putAll(this.propertys);
        if (this.useTemplate != null)
            for (PropertyDefine p : this.useTemplate.getPropertys())
                if (ps.containsKey(p.getName()) == false)
                    ps.put(p.getName(), p);
        return Collections.unmodifiableCollection((Collection<PropertyDefine>) ps.values());
    };
    /**��ȡ��ǰ�����������������б����صĽ��������ʹ�õ�ģ���е�����������*/
    public Collection<? extends PropertyDefine> getDeclaredPropertys() {
        return Collections.unmodifiableCollection((Collection<PropertyDefine>) this.propertys.values());
    };
    /**��ȡ��ʼ����������*/
    public String getInitMethod() {
        return this.initMethod;
    };
    /**��ȡ���ٷ�������*/
    public String getDestroyMethod() {
        return this.destroyMethod;
    };
    /**��ȡbeanʹ�õ�ģ�塣*/
    public BeanDefine getUseTemplate() {
        return this.useTemplate;
    };
    /**
     * �����Զ����˵��������beanʱ����Ҫ������������
     * ��������ͨ����ָ���췽�����������ڹ�����ʽ�����������������˹��������Ĳ����б�
     * ���صļ�����һ��ֻ�����ϡ�
     */
    public Collection<ConstructorDefine> getInitParams() {
        return Collections.unmodifiableCollection((Collection<ConstructorDefine>) this.initParams);
    };
    /**���ؾ����������ַ�����*/
    public String toString() {
        return this.getClass().getSimpleName() + "@" + this.hashCode() + " name=" + this.getName();
    };
    /**���һ����������������ӵ������������Զ���������*/
    public void addInitParam(ConstructorDefine constructorParam) {
        if (constructorParam.getIndex() == -1)
            constructorParam.setIndex(this.initParams.size());
        this.initParams.add(constructorParam);
        final BeanDefine define = this;
        Collections.sort(this.initParams, new Comparator<ConstructorDefine>() {
            public int compare(ConstructorDefine arg0, ConstructorDefine arg1) {
                int cdefine_1 = arg0.getIndex();
                int cdefine_2 = arg1.getIndex();
                if (cdefine_1 > cdefine_2)
                    return 1;
                else if (cdefine_1 < cdefine_2)
                    return -1;
                else
                    throw new RepeateException(define + "[" + arg0 + "]��[" + arg1 + "]������������ظ�.");
            }
        });
    };
    /**���һ�����ԡ�*/
    public void addProperty(PropertyDefine property) {
        this.propertyNames.add(property.getName());
        this.propertys.put(property.getName(), property);
    };
    /**���һ������������*/
    public void addMethod(MethodDefine method) {
        this.methodNames.add(method.getName());
        this.methods.put(method.getName(), method);
    };
    /**��ȡbean��Aop����*/
    public AopConfigDefine getAopConfig() {
        return aopConfig;
    }
    //-------------------------------------------------------------
    /**����id��*/
    public void setId(String id) {
        this.id = id;
    }
    /**����Bean����*/
    public void setName(String name) {
        this.name = name;
    }
    /**�����߼�������*/
    public void setLogicPackage(String logicPackage) {
        this.logicPackage = logicPackage;
    }
    /**����������Ϣ��*/
    public void setDescription(String description) {
        this.description = description;
    }
    public void setFactoryBean(BeanDefine factoryBean) {
        this.factoryBean = factoryBean;
    }
    /**���ô�����Beanʱʹ�õĹ���bean�ķ���������*/
    public void setFactoryMethod(MethodDefine factoryMethod) {
        this.factoryMethod = factoryMethod;
    }
    /**���ø�bean�Ƿ�Ϊһ������ġ�*/
    public void setBoolAbstract(boolean boolAbstract) {
        this.boolAbstract = boolAbstract;
    }
    /**���ø�bean�Ƿ�Ϊһ����̬�ġ�*/
    public void setBoolSingleton(boolean boolSingleton) {
        this.boolSingleton = boolSingleton;
    }
    /**���ø�bean�Ƿ�Ϊһ���ӳٳ�ʼ���ġ�*/
    public void setBoolLazyInit(boolean boolLazyInit) {
        this.boolLazyInit = boolLazyInit;
    }
    /**�����Ƿ�ִ��ǿ�Ƽ�顣*/
    public void setBoolCheckType(boolean boolCheckType) {
        this.boolCheckType = boolCheckType;
    }
    /**����bean��ʼ��������*/
    public void setInitMethod(String initMethod) {
        this.initMethod = initMethod;
    }
    /**����bean���ٷ�����*/
    public void setDestroyMethod(String destroyMethod) {
        this.destroyMethod = destroyMethod;
    }
    /**����beanʹ�õ�ģ�塣*/
    public void setUseTemplate(BeanDefine useTemplate) {
        this.useTemplate = useTemplate;
    }
    /**����bean��Aop����*/
    public void setAopConfig(AopConfigDefine aopConfig) {
        this.aopConfig = aopConfig;
    }
}