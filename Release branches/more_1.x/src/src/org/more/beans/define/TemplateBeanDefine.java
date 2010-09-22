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
package org.more.beans.define;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.more.RepeateException;
import org.more.beans.AbstractBeanDefine;
import org.more.beans.AbstractPropertyDefine;
import org.more.beans.IocTypeEnum;
import org.more.util.attribute.AttBase;
import org.more.util.attribute.IAttribute;
/**
 * TemplateBeanDefine�����ڶ���һ��bean��ģ�塣
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public class TemplateBeanDefine implements AbstractBeanDefine {
    private String                            name          = null;                                   //����
    private IocTypeEnum                       iocType       = null;                                   //Ĭ������ע�뷽ʽ
    private String                            scope         = null;                                   //bean������
    private boolean                           boolAbstract  = false;                                  //�����־
    private boolean                           boolInterface = false;                                  //�ӿڱ�־
    private boolean                           boolSingleton = false;                                  //��̬��־
    private boolean                           boolLazyInit  = false;                                  //�ӳ�װ�ر�־
    private String                            description   = null;                                   //������Ϣ
    private String                            factoryName   = null;                                   //����������
    private String                            factoryMethod = null;                                   //��������������
    private TemplateBeanDefine                useTemplate   = null;                                   //Ӧ�õ�ģ��
    private ArrayList<AbstractPropertyDefine> initParams    = new ArrayList<AbstractPropertyDefine>(); //��ʼ������
    private ArrayList<AbstractPropertyDefine> propertys     = new ArrayList<AbstractPropertyDefine>(); //����
    private IAttribute                        attribute     = new AttBase();                          //Ԫ��Ϣ����
    private IAttribute                        defineconfig  = new AttBase();                          //��չ��������
    //-------------------------------------------------------------
    /**����bean�����ƣ���ͬһ��Factory��name��Ψһ�ġ�*/
    public String getName() {
        return this.name;
    };
    /**��ȡbean�ĸ������������ע�뵽Bean�еġ�*/
    public IocTypeEnum getIocType() {
        return this.iocType;
    };
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
    /**����Ŀ�귽���ķ�������������*/
    public String factoryMethod() {
        return this.factoryMethod;
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
        this.propertys.toArray(define);
        return define;
    };
    /**������չDefine����������*/
    public IAttribute getDefineConfig() {
        return this.defineconfig;
    }
    /**���ؾ����������ַ�����*/
    public String toString() {
        return this.getClass().getSimpleName() + "@" + this.hashCode() + " name=" + this.getName();
    };
    /**���һ������������*/
    public void addInitParam(AbstractPropertyDefine property) {
        this.initParams.add(property);
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
    public void addProperty(AbstractPropertyDefine property) {
        this.propertys.add(property);
    };
    //-------------------------------------------------------------
    /**����Bean����*/
    public void setName(String name) {
        this.name = name;
    }
    /**����Bean���Ե�Ioc��ʽ��*/
    public void setIocType(IocTypeEnum iocType) {
        this.iocType = iocType;
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
    /**���ô�����Beanʱʹ�õĹ���bean�ķ�������*/
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
    //-------------------------------------------------------------
    public boolean contains(String name) {
        return this.attribute.contains(name);
    };
    public void setAttribute(String name, Object value) {
        this.attribute.setAttribute(name, value);
    };
    public Object getAttribute(String name) {
        return this.attribute.getAttribute(name);
    };
    public void removeAttribute(String name) {
        this.attribute.removeAttribute(name);
    };
    public String[] getAttributeNames() {
        return this.attribute.getAttributeNames();
    };
    public void clearAttribute() {
        this.attribute.clearAttribute();
    }
}