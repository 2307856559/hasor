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
import java.util.Map;
import org.more.hypha.DefineResource;
import org.more.hypha.EventManager;
import org.more.hypha.ExpandPoint;
import org.more.hypha.ExpandPointManager;
import org.more.hypha.event.AbstractEventManager;
import org.more.hypha.expandpoint.AbstractExpandPointManager;
import org.more.util.attribute.AttBase;
import org.more.util.attribute.IAttribute;
/**
 * ����{@link DefineResource}�ӿڻ���ʵ���࣬��������Ҫʵ���й�{@link AbstractBeanDefine}���͵Ĳ������ܡ�
 * @version 2010-11-30
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractDefineResource implements DefineResource {
    private static final long  serialVersionUID   = 1420351981612281917L;
    private String             sourceName         = null;                //��Դ��
    private IAttribute         flashContext       = null;                //ȫ�����棬ͨ����д�ܱ����ķ���createFlash���ﵽֲ���Ŀ�ġ�
    private IAttribute         attributeManager   = null;                //���Թ�����
    //�����ֶζ�����ͨ����д��Ӧ�����ﵽ��д��Ŀ�ġ�
    private EventManager       eventManager       = null;                //�¼�������
    private ExpandPointManager expandPointManager = null;                //��չ�������
    //========================================================================================================================IAttribute
    public boolean contains(String name) {
        return this.getAttribute().contains(name);
    }
    public void setAttribute(String name, Object value) {
        this.getAttribute().setAttribute(name, value);
    }
    public Object getAttribute(String name) {
        return this.getAttribute().getAttribute(name);
    }
    public void removeAttribute(String name) {
        this.getAttribute().removeAttribute(name);
    }
    public String[] getAttributeNames() {
        return this.getAttribute().getAttributeNames();
    }
    public void clearAttribute() {
        this.getAttribute().clearAttribute();
    }
    public Map<String, Object> toMap() {
        return this.getAttribute().toMap();
    };
    //========================================================================================================================Base
    /**��ȡһ��״̬��״̬�����Ƿ��Ѿ�׼���ã�{@link AbstractDefineResource}�����и÷���ʼ�շ���true��*/
    public boolean isReady() {
        return true;
    };
    /**������Դ����*/
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
    public String getSourceName() {
        return this.sourceName;
    }
    //========================================================================================================================GET
    /**��ȡDefineResource�����Է��ʽӿڡ��������ͨ����д�÷������ı����Թ�������*/
    public final IAttribute getAttribute() {
        if (this.attributeManager == null)
            this.attributeManager = this.createAttribute();
        if (this.attributeManager == null)
            this.attributeManager = new AttBase();
        return this.attributeManager;
    }
    /**��ȡFlash�����flash��һ���ڲ���ϢЯ���塣�����Թᴩ����hypha�����н׶Ρ��õ�flash�����ְ취һ����������ȡ������һ�������ض���λ����hypha�ṩ��*/
    public final IAttribute getFlash() {
        if (this.flashContext == null)
            this.flashContext = this.createFlash();
        if (this.flashContext == null)
            this.flashContext = new AttBase();
        return this.flashContext;
    };
    /**��ȡ�¼���������ͨ���ù��������Է����¼����¼��ļ���Ҳ��ͨ������ӿڶ�����ɵġ��������ͨ����д�÷������ı��¼���������*/
    public final EventManager getEventManager() {
        if (this.eventManager == null)
            this.eventManager = this.getEventManager();
        if (this.eventManager == null)
            try {
                this.eventManager = new AbstractEventManager(this) {};
                this.eventManager.init(this.getFlash());
            } catch (Throwable e) {/*TODO ���������κ��쳣*/}
        return this.eventManager;
    }
    /**��ȡ��չ���������ͨ����չ����������Լ�����ע����߽��ע����չ�㡣�й���չ��Ĺ�����μ�{@link ExpandPoint}���������ͨ����д�÷������ı���չ���������*/
    public final ExpandPointManager getExpandPointManager() {
        if (this.expandPointManager == null)
            this.expandPointManager = this.createExpandPointManager();
        if (this.expandPointManager == null)
            try {
                this.expandPointManager = new AbstractExpandPointManager(this) {};
                this.expandPointManager.init(this.getFlash());
            } catch (Throwable e) {/*TODO ���������κ��쳣*/}
        return this.expandPointManager;
    }
    //========================================================================================================================
    /**����һ�����Թ�������������ؿ��򴴽�Ĭ�ϵ�һ�����Թ�������*/
    protected IAttribute createAttribute() {
        return null;
    };
    /**����һ��Flash��������ؿ��򴴽�Ĭ�ϵ�һ��Flash��*/
    protected IAttribute createFlash() {
        return null;
    };
    /**����һ��{@link EventManager}��������ؿ��򴴽�Ĭ�ϵ�һ����������*/
    protected EventManager createEventManager() {
        return null;
    };
    /**����һ��{@link ExpandPointManager}��������ؿ��򴴽�Ĭ�ϵ�һ����������*/
    protected ExpandPointManager createExpandPointManager() {
        return null;
    };
};