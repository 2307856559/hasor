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
import org.more.hypha.AbstractBeanDefine;
import org.more.hypha.DefineResource;
import org.more.hypha.Event;
import org.more.hypha.EventManager;
import org.more.hypha.ExpandPointManager;
import org.more.hypha.commons.AbstractEventManager;
import org.more.hypha.commons.AbstractExpandPointManager;
import org.more.util.attribute.AttBase;
import org.more.util.attribute.IAttribute;
/**
 * ����{@link DefineResource}�ӿڻ���ʵ���࣬��������Ҫʵ���й�{@link AbstractBeanDefine}���͵Ĳ������ܡ�
 * @version 2010-11-30
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractDefineResource implements DefineResource {
    private static final long          serialVersionUID   = 1420351981612281917L;
    private String                     sourceName         = null;                //��Դ��
    private IAttribute                 flashContext       = null;                //ȫ�����棬ͨ����д�ܱ����ķ���createFlash���ﵽֲ���Ŀ�ġ�
    private ThreadLocal<IAttribute>    threadFlash        = null;                //ȫ�����棬ͨ����д�ܱ����ķ���createFlash���ﵽֲ���Ŀ�ġ�
    private IAttribute                 attributeManager   = null;                //���Թ�����
    //�����ֶζ�����ͨ����д��Ӧ�����ﵽ��д��Ŀ�ġ�
    private AbstractEventManager       eventManager       = null;                //�¼�������
    private AbstractExpandPointManager expandPointManager = null;                //��չ�������
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
        return this.attributeManager;
    }
    /**��ȡFlash�����flash��һ���ڲ���ϢЯ���塣�����Թᴩ����hypha�����н׶Ρ�
     * �õ�flash�����ְ취һ����������ȡ������һ�������ض���λ����hypha�ṩ�������߳����ơ�*/
    public final IAttribute getFlash() {
        if (this.flashContext == null)
            this.flashContext = this.createFlash("Public");
        return this.flashContext;
    };
    /**��ȡFlash�����flash��һ���ڲ���ϢЯ���塣�����Թᴩ����hypha�����н׶Ρ�
     * �õ�flash�����ְ취һ����������ȡ������һ�������ض���λ����hypha�ṩ���̼߳������*/
    public final IAttribute getThreadFlash() {
        if (this.threadFlash == null) {
            this.threadFlash = new ThreadLocal<IAttribute>();
            IAttribute flash = this.createFlash("Thread");
            this.threadFlash.set(flash);
            return flash;
        } else {
            IAttribute flash = this.threadFlash.get();
            if (flash == null) {
                flash = this.createFlash("Thread");
                this.threadFlash.set(flash);
            }
            return flash;
        }
    }
    public final EventManager getEventManager() {
        if (this.eventManager == null)
            this.eventManager = this.createEventManager();
        return this.eventManager;
    }
    public final ExpandPointManager getExpandPointManager() {
        if (this.expandPointManager == null)
            this.expandPointManager = this.createExpandPointManager();
        return this.expandPointManager;
    }
    /**�׳�һ���¼�������¼��жϻ�����ִ�д��������*/
    protected void throwEvent(Event event, Object... params) {
        this.getEventManager().doEvent(event, params);
    }
    //========================================================================================================================
    /**����һ�����Թ����������¸÷��������滻{@link DefineResource}�ӿ�ʹ�õ�Attribute����*/
    protected IAttribute createAttribute() {
        return new AttBase();
    };
    /**����һ��Flash�����¸÷��������滻{@link DefineResource}�ӿ�ʹ�õ�Flash����
     * �������ֵΪ��Public�����ʾ������һ�����Կ�Խ�����̵߳�FLASH��
     * ���Ϊ��Thread�����ʾ��������һ��ֻ�ڵ�ǰ�߳�����Ч��FLASH*/
    protected IAttribute createFlash(String type) {
        return new AttBase();
    };
    /**����һ��{@link EventManager}�����¸÷��������滻{@link DefineResource}�ӿ�ʹ�õ�{@link EventManager}����*/
    protected AbstractEventManager createEventManager() {
        AbstractEventManager em = new AbstractEventManager() {};
        em.init(this);
        return em;
    };
    /**����һ��{@link ExpandPointManager}�����¸÷��������滻{@link DefineResource}�ӿ�ʹ�õ�{@link ExpandPointManager}����*/
    protected AbstractExpandPointManager createExpandPointManager() {
        AbstractExpandPointManager epm = new AbstractExpandPointManager() {};
        epm.init(this);
        return epm;
    };
};