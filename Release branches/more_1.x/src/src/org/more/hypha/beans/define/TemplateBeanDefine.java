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
package org.more.hypha.beans.define;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import org.more.RepeateException;
import org.more.hypha.AbstractDefine;
import org.more.hypha.beans.AbstractBeanDefine;
import org.more.hypha.beans.AbstractMethodDefine;
/**
 * TemplateBeanDefine�����ڶ���һ��bean��ģ�塣
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public class TemplateBeanDefine extends AbstractDefine implements AbstractBeanDefine {
    private String                                id            = null;                                       //id
    private String                                name          = null;                                       //����
    private String                                logicPackage  = null;                                       //�߼���
    private String                                scope         = null;                                       //bean������
    private boolean                               boolAbstract  = false;                                      //�����־
    private boolean                               boolInterface = false;                                      //�ӿڱ�־
    private boolean                               boolSingleton = false;                                      //��̬��־
    private boolean                               boolLazyInit  = false;                                      //�ӳ�װ�ر�־
    private String                                description   = null;                                       //������Ϣ
    private String                                factoryName   = null;                                       //����������
    private String                                factoryMethod = null;                                       //����������������
    private TemplateBeanDefine                    useTemplate   = null;                                       //Ӧ�õ�ģ��
    private ArrayList<ConstructorDefine>          initParams    = new ArrayList<ConstructorDefine>();         //��ʼ������
    private HashMap<String, PropertyDefine>       propertys     = new HashMap<String, PropertyDefine>();      //����
    private HashMap<String, AbstractMethodDefine> methods       = new HashMap<String, AbstractMethodDefine>(); //����
    //-------------------------------------------------------------
    /**���ء�TemplateBean����*/
    public String getBeanType() {
        return "TemplateBean";
    }
    /**����bean��Ψһ��ţ����û��ָ��id������idֵ����fullName����ֵ��*/
    public String getID() {
        if (this.id == null)
            return this.getFullName();
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
    /**��ȡbean���������������֧�ֶ���������*/
    public String getScope() {
        return this.scope;
    };
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
    /**����bean�Ĺ�������*/
    public String factoryName() {
        return this.factoryName;
    };
    /**�÷�����factoryName()�����ǳɶԳ��ֵģ��÷�������Ŀ�귽���Ĵ������ơ�*/
    public String factoryMethod() {
        return this.factoryMethod;
    };
    /**������������������bean�ϵ�һЩ������*/
    public AbstractMethodDefine[] getMethods() {
        AbstractMethodDefine[] define = new AbstractMethodDefine[this.methods.size()];
        this.methods.values().toArray(define);
        return define;
    };
    /**��ȡbeanʹ�õ�ģ�塣*/
    public TemplateBeanDefine getUseTemplate() {
        return useTemplate;
    }
    /**��ȡ���������beanʱ����Ҫ������������*/
    public AbstractPropertyDefine[] getInitParams() {
        AbstractPropertyDefine[] define = new AbstractPropertyDefine[this.initParams.size()];
        this.initParams.toArray(define);
        return define;
    };
    /**��ȡ���������beanʱ����Ҫ������������*/
    public AbstractPropertyDefine[] getPropertys() {
        AbstractPropertyDefine[] define = new AbstractPropertyDefine[this.propertys.size()];
        this.propertys.values().toArray(define);
        return define;
    };
    /**���ؾ����������ַ�����*/
    public String toString() {
        return this.getClass().getSimpleName() + "@" + this.hashCode() + " name=" + this.getName();
    };
    /**���һ������������*/
    public void addInitParam(ConstructorDefine constructorParam) {
        this.initParams.add(constructorParam);
        final TemplateBeanDefine define = this;
        Collections.sort(this.initParams, new Comparator<AbstractPropertyDefine>() {
            public int compare(AbstractPropertyDefine arg0, AbstractPropertyDefine arg1) {
                int cdefine_1 = ((ConstructorDefine) arg0).getIndex();
                int cdefine_2 = ((ConstructorDefine) arg1).getIndex();
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
        this.propertys.put(property.getName(), property);
    };
    /**���һ������������*/
    public void addMethod(AbstractMethodDefine method) {
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
    /**����bean��Ч������*/
    public void setScope(String scope) {
        this.scope = scope;
    }
    /**����������Ϣ��*/
    public void setDescription(String description) {
        this.description = description;
    }
    /**���ô�����Beanʱʹ�õĹ���bean����*/
    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }
    /**���ô�����Beanʱʹ�õĹ���bean�ķ���������*/
    public void setFactoryMethod(String factoryMethod) {
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
    /**����beanʹ�õ�ģ�塣*/
    public void setUseTemplate(TemplateBeanDefine useTemplate) {
        this.useTemplate = useTemplate;
    }
}