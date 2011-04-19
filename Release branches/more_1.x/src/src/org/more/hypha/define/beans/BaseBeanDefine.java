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
package org.more.hypha.define.beans;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.more.RepeateException;
import org.more.hypha.AbstractBeanDefine;
import org.more.hypha.AbstractMethodDefine;
import org.more.hypha.define.AbstractDefine;
/**
 * TemplateBeanDefine�����ڶ���һ��bean��ģ�塣
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public abstract class BaseBeanDefine extends AbstractDefine<AbstractBeanDefine> implements AbstractBeanDefine {
    private String                                id            = null;                                       //id
    private String                                name          = null;                                       //����
    private String                                logicPackage  = null;                                       //�߼���
    private boolean                               boolAbstract  = false;                                      //�����־
    private boolean                               boolInterface = false;                                      //�ӿڱ�־
    private boolean                               boolSingleton = false;                                      //��̬��־
    private boolean                               boolLazyInit  = true;                                       //�ӳ�װ�ر�־
    private String                                description   = null;                                       //������Ϣ
    private AbstractMethodDefine                  factoryMethod = null;                                       //����������������
    //
    private ArrayList<ConstructorDefine>          initParams    = new ArrayList<ConstructorDefine>();         //��ʼ������
    private List<String>                          propertyNames = new ArrayList<String>();
    private HashMap<String, PropertyDefine>       propertys     = new HashMap<String, PropertyDefine>();      //����
    private List<String>                          methodNames   = new ArrayList<String>();
    private HashMap<String, AbstractMethodDefine> methods       = new HashMap<String, AbstractMethodDefine>(); //����
    private String                                initMethod    = null;                                       //��ʼ������
    private String                                destroyMethod = null;                                       //���ٷ���
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
    public String getFullName() {
        if (this.logicPackage != null)
            return this.logicPackage + "." + this.name;
        else
            return this.getName();
    }
    /**����һ��booleanֵ����ʾ���Ƿ�Ϊһ�������ࡣ*/
    public boolean isAbstract() {
        return this.boolAbstract;
    };
    /**����һ��booleanֵ����ʾ���Ƿ�Ϊһ���ӿڡ�*/
    public boolean isInterface() {
        return this.boolInterface;
    };
    /**����һ��booleanֵ����ʾ���bean�Ƿ�Ϊ��̬�ġ�*/
    public boolean isSingleton() {
        return this.boolSingleton;
    };
    /**����һ��booleanֵ����ʾ���bean�Ƿ�Ϊ�ӳ�װ�صġ�*/
    public boolean isLazyInit() {
        return this.boolLazyInit;
    };
    /**����bean��������Ϣ��*/
    public String getDescription() {
        return this.description;
    };
    /**�÷�����factoryName()�����ǳɶԳ��ֵģ��÷�������Ŀ�귽���Ĵ������ơ�*/
    public AbstractMethodDefine factoryMethod() {
        return this.factoryMethod;
    };
    /**������������������bean�ϵ�һЩ���������صļ�����һ��ֻ�����ϡ�*/
    public Collection<? extends AbstractMethodDefine> getMethods() {
        return Collections.unmodifiableCollection((Collection<AbstractMethodDefine>) this.methods.values());
    };
    /**���ݷ������������ƻ�ȡ�䷽�����塣*/
    public AbstractMethodDefine getMethod(String name) {
        return this.methods.get(name);
    }
    /**��ȡ��ʼ����������*/
    public String getInitMethod() {
        return this.initMethod;
    };
    /**��ȡ���ٷ�������*/
    public String getDestroyMethod() {
        return this.destroyMethod;
    };
    /**
     * �����Զ����˵��������beanʱ����Ҫ������������
     * ��������ͨ����ָ���췽�����������ڹ�����ʽ�����������������˹��������Ĳ����б�
     * ���صļ�����һ��ֻ�����ϡ�
     */
    public Collection<ConstructorDefine> getInitParams() {
        return Collections.unmodifiableCollection((Collection<ConstructorDefine>) this.initParams);
    };
    /**����bean�Ķ������Լ��ϣ����صļ�����һ��ֻ�����ϡ�*/
    public Collection<PropertyDefine> getPropertys() {
        return Collections.unmodifiableCollection((Collection<PropertyDefine>) this.propertys.values());
    };
    /**���ؾ����������ַ�����*/
    public String toString() {
        return this.getClass().getSimpleName() + "@" + this.hashCode() + " name=" + this.getName();
    };
    /**���һ����������������ӵ������������Զ���������*/
    public void addInitParam(ConstructorDefine constructorParam) {
        this.initParams.add(constructorParam);
        final BaseBeanDefine define = this;
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
    /**���ô�����Beanʱʹ�õĹ���bean�ķ���������*/
    public void setFactoryMethod(AbstractMethodDefine factoryMethod) {
        this.factoryMethod = factoryMethod;
    }
    /**���ø�bean�Ƿ�Ϊһ������ġ�*/
    public void setBoolAbstract(boolean boolAbstract) {
        this.boolAbstract = boolAbstract;
    }
    /**���ø�bean�Ƿ�Ϊһ���ӿڡ�*/
    public void setBoolInterface(boolean boolInterface) {
        this.boolInterface = boolInterface;
    }
    /**���ø�bean�Ƿ�Ϊһ����̬�ġ�*/
    public void setBoolSingleton(boolean boolSingleton) {
        this.boolSingleton = boolSingleton;
    }
    /**���ø�bean�Ƿ�Ϊһ���ӳٳ�ʼ���ġ�*/
    public void setBoolLazyInit(boolean boolLazyInit) {
        this.boolLazyInit = boolLazyInit;
    }
    /**����bean��ʼ��������*/
    public void setInitMethod(String initMethod) {
        this.initMethod = initMethod;
    }
    /**����bean���ٷ�����*/
    public void setDestroyMethod(String destroyMethod) {
        this.destroyMethod = destroyMethod;
    }
}