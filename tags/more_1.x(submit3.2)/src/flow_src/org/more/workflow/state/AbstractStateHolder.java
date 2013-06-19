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
package org.more.workflow.state;
import java.util.Iterator;
import org.more.util.attribute.IAttribute;
import org.more.workflow.context.ApplicationContext;
import org.more.workflow.context.ELContext;
import org.more.workflow.context.FlashSession;
import org.more.workflow.context.RunContext;
import org.more.workflow.event.EventListener;
import org.more.workflow.event.EventPhase;
import org.more.workflow.event.ListenerHolder;
import org.more.workflow.event.object.LoadStateEvent;
import org.more.workflow.event.object.SaveStateEvent;
import org.more.workflow.event.object.UpdataModeEvnet;
import org.more.workflow.metadata.AbstractObject;
import org.more.workflow.metadata.MetadataHolder;
import org.more.workflow.metadata.ModeUpdataHolder;
import org.more.workflow.metadata.ObjectMetadata;
import org.more.workflow.metadata.PropertyMetadata;
/**
 * �������״̬�����ӿڣ�ͨ���ýӿڿ��Զ�ͬһ��AbstractMetadata�������״̬���棬״̬�ָ��Ȳ�����
 * �����໹�����ṩ����Ĳ��������������ṩ{@link ModeUpdataHolder}��{@link MetadataHolder}�ӿ�ʵ�֡�<br/>
 * ����AbstractStateHolder����Բ�����Щ���Ͷ�����Ҫͨ������ʵ��getMetadata��������ȷ�ϡ�<br/>
 * saveState���������ڽ�ģ�͵�״̬��Ϣ���浽SoftSession�У���loadState���ǽ�ģ��״̬��SoftSession�лָ�������
 * ���ģ����Ҫ�߱����������Ҫʵ��{@link StateCache}�ӿڡ�
 * Date : 2010-6-15
 * @author ������
 */
public abstract class AbstractStateHolder implements ListenerHolder, ModeUpdataHolder, MetadataHolder {
    @Override
    public abstract ObjectMetadata getMetadata();
    /**�����������Ķ�������������˴����ľ������Ͷ����û�����ͨ����չ�÷������Զ�����󴴽����̡� */
    public abstract Object newInstance(RunContext runContext) throws Throwable;
    @Override
    public Iterator<EventListener> getListeners() {
        return this.getMetadata().getListeners();
    };
    /**�ڵ�ǰ������������һ���¼���*/
    protected void event(EventPhase event) {
        Iterator<EventListener> iterator = this.getListeners();
        while (iterator.hasNext())
            iterator.next().doListener(event);
    };
    @Override
    public void updataMode(AbstractObject object, ELContext elContext) throws Throwable {
        ObjectMetadata am = this.getMetadata();
        if (am == null)
            throw new NullPointerException("ͨ��getMetadata������ȡԪ��Ϣʱ����ķ���null��");
        //
        Object obj = object.getTargetBean();
        UpdataModeEvnet event = new UpdataModeEvnet(obj, this);
        this.event(event.getEventPhase()[0]);//before
        for (PropertyMetadata property : am.getPropertys())
            property.updataProperty(obj, elContext);
        this.event(event.getEventPhase()[1]);//after
    };
    /**��������װ��ģ��״̬��*/
    public void loadState(AbstractObject object, ApplicationContext appContext) {
        Object obj = object.getTargetBean();
        if (obj instanceof StateCache == false)
            return;
        FlashSession flashSession = appContext.getSoftSession();
        IAttribute states = flashSession.getFlash(object.getID());
        if (states == null)
            return;
        StateCache chache = (StateCache) obj;
        //
        LoadStateEvent event = new LoadStateEvent(obj, states);
        this.event(event.getEventPhase()[0]);//before
        chache.recoverState(states);
        this.event(event.getEventPhase()[1]);//after
    };
    /**��ģ��״̬���浽�����С�*/
    public void saveState(AbstractObject object, ApplicationContext appContext) {
        Object obj = object.getTargetBean();
        if (obj instanceof StateCache == false)
            return;
        StateCache chache = (StateCache) obj;
        IAttribute flash = appContext.getSoftSession().getFlash(object.getID());
        //
        SaveStateEvent event = new SaveStateEvent(chache, flash);
        this.event(event.getEventPhase()[0]);//before
        chache.saveState(flash);
        appContext.getSoftSession().setFlash(object.getID(), flash);
        this.event(event.getEventPhase()[1]);//after
    };
};