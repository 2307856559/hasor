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
package org.more.hypha.configuration;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import org.more.NoDefinitionException;
import org.more.RepeateException;
import org.more.StateException;
import org.more.core.xml.XmlParserKit;
import org.more.core.xml.XmlParserKitManager;
import org.more.core.xml.stream.XmlReader;
import org.more.hypha.DefineResource;
import org.more.hypha.DefineResourcePlugin;
import org.more.hypha.EventManager;
import org.more.hypha.beans.AbstractBeanDefine;
import org.more.hypha.beans.TypeManager;
import org.more.hypha.beans.support.TagBeans_Beans;
import org.more.hypha.event.AddBeanDefineEvent;
import org.more.hypha.event.AddPluginEvent;
import org.more.hypha.event.ClearDefineEvent;
import org.more.hypha.event.LoadingDefineEvent;
import org.more.hypha.event.ReloadDefineEvent;
import org.more.util.attribute.AttBase;
import org.more.util.attribute.IAttribute;
/**
 * ������{@link DefineResource}��һ��ʵ���ࡣ
 * �Լ�����ֵԪ��Ϣ���������ɡ�
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public class DefineResourceImpl implements DefineResource {
    /**  */
    private static final long                 serialVersionUID = -2907262416329013610L;
    //
    private XmlConfiguration                  configuration    = null;
    private boolean                           inited           = false;                                    //�Ƿ��ʼ��
    private String                            sourceName       = null;                                     //��Դ��
    private ArrayList<String>                 pluginNames      = new ArrayList<String>();                  //������Ƽ���
    private Map<String, DefineResourcePlugin> pluginList       = null;                                     //�������
    private ArrayList<String>                 defineNames      = new ArrayList<String>();                  //bean�������Ƽ���
    private Map<String, AbstractBeanDefine>   defineMap        = new HashMap<String, AbstractBeanDefine>(); //bean����Map
    private EventManager                      eventManager     = null;                                     //�¼�������
    private IAttribute                        attributeManager = null;                                     //���Թ�����
    //
    private TypeManager                       typeManager      = new TypeManager();                        //���ͽ���
    private XmlParserKitManager               manager          = new XmlParserKitManager();                //xml������
    private ClassLoader                       classLoader      = null;
    //========================================================================================���췽��
    /**˽�л�*/
    DefineResourceImpl(XmlConfiguration configuration) {
        this.configuration = configuration;
        this.eventManager = configuration.getEventManager();
    }
    //========================================================================================DefineResourcePluginSet�ӿ�
    /**������չDefine����������*/
    public DefineResourcePlugin getPlugin(String name) {
        if (this.pluginList == null)
            return null;
        if (this.pluginNames.contains(name) == false)
            return null;
        return this.pluginList.get(name);
    };
    /**����һ��������������������滻�����Ĳ��ע�ᡣ*/
    public synchronized void setPlugin(String name, DefineResourcePlugin plugin) {
        if (this.pluginList == null)
            this.pluginList = new HashMap<String, DefineResourcePlugin>();
        this.getEventManager().doEvent(new AddPluginEvent(this, plugin));//TODO �²��
        this.pluginNames.add(name);
        this.pluginList.put(name, plugin);
    };
    /**ɾ��һ�����еĲ��ע�ᡣ*/
    public synchronized void removePlugin(String name) {
        this.pluginNames.remove(name);
        this.pluginList.remove(name);
    };
    //========================================================================================
    /**��ȡһ��{@link AbstractBeanDefine}���塣*/
    public AbstractBeanDefine getBeanDefine(String id) throws NoDefinitionException {
        if (this.defineNames.contains(id) == false)
            throw new NoDefinitionException("������idΪ[" + id + "]��Bean���塣");
        return this.defineMap.get(id);
    };
    /**����ĳ��id��bean�����Ƿ���ڡ�*/
    public boolean containsBeanDefine(String id) {
        return this.defineNames.contains(id);
    }
    /**���һ��Bean���壬����ӵ�Bean����ᱻִ�м�⡣*/
    public synchronized void addBeanDefine(AbstractBeanDefine define) {
        if (this.defineNames.contains(define.getID()) == true)
            throw new RepeateException("[" + define.getID() + "]Bean�����ظ���");
        this.getEventManager().doEvent(new AddBeanDefineEvent(this, define));//TODO ��Bean���壬ʹ�ö�����ʽ��
        this.defineNames.add(define.getID());
        this.defineMap.put(define.getID(), define);
    };
    /**���������ļ�����*/
    protected DefineResourceImpl passerXml(InputStream in) throws XMLStreamException {
        XmlReader reader = new XmlReader(in);
        this.manager.getContext().setAttribute(TagBeans_Beans.BeanDefineManager, this);
        reader.reader(this.manager, null);
        return this;
    };
    /**��ȡ{@link XmlParserKitManager}*/
    protected XmlParserKitManager getManager() {
        return this.manager;
    }
    //========================================================================================
    /**��ȡ���ͽ�����*/
    public TypeManager getTypeManager() {
        return this.typeManager;
    }
    /**ע��һ����ǩ�������߼���*/
    public void regeditXmlParserKit(String namespace, XmlParserKit kit) {
        this.manager.regeditKit(namespace, kit);
    }
    /**ȡ��һ����ǩ�������߼���ע�ᡣ*/
    public void unRegeditXmlParserKit(String namespace, XmlParserKit kit) {
        this.manager.unRegeditKit(namespace, kit);
    }
    /**�ֶ�ִ������װ�ض���������ظ�װ�ؿ��ܲ����쳣���ö�����������{@link LoadingDefineEvent}�¼�*/
    public synchronized void loadDefine() throws IOException, XMLStreamException {
        if (this.inited == true)
            throw new StateException(this.sourceName + "�����ظ���ʼ��!");
        this.configuration.loadConfig(this);
        this.inited = true;
    };
    /**�������װ�ص�Bean������󣬸÷�����������{@link ClearDefineEvent}�¼���*/
    public synchronized void clearDefine() {
        this.getEventManager().doEvent(new ClearDefineEvent(this));//TODO ����
        this.defineNames.clear();
        this.defineMap.clear();
        this.pluginNames.clear();
        this.pluginList.clear();
        this.inited = false;
    }
    /**����װ�����ã��÷���������ִ��clearDefine()���������ִ��loadDefine()����ִ��֮ǰ�÷���������{@link ReloadDefineEvent}�¼���*/
    public synchronized void reloadDefine() throws IOException, XMLStreamException {
        this.getEventManager().doEvent(new ReloadDefineEvent(this));//TODO ����
        this.clearDefine();
        this.loadDefine();
    }
    /**������Դ��*/
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
    public String getSourceName() {
        return this.sourceName;
    }
    /**��ȡ{@link DefineResource}�ӿ�ʹ�õ���װ������*/
    public ClassLoader getClassLoader() {
        return this.classLoader;
    };
    /**����ClassLoader��ͨ���ڳ�ʼ��֮ǰ�������á�*/
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    public IAttribute getAttribute() {
        if (this.attributeManager == null)
            this.attributeManager = new AttBase();
        return this.attributeManager;
    }
    public EventManager getEventManager() {
        return this.eventManager;
    }
    public List<String> getBeanDefineNames() {
        return Collections.unmodifiableList((List<String>) this.defineNames);
    }
    public boolean isPrototype(String id) throws NoDefinitionException {
        AbstractBeanDefine define = this.getBeanDefine(id);
        return (define.factoryName() == null) ? false : true;
    }
    public boolean isSingleton(String id) throws NoDefinitionException {
        AbstractBeanDefine define = this.getBeanDefine(id);
        return define.isSingleton();
    }
    public boolean isFactory(String id) throws NoDefinitionException {
        AbstractBeanDefine define = this.getBeanDefine(id);
        return (define.factoryName() == null) ? false : true;
    }
}