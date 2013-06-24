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
package org.more.webui.lifestyle;
import java.util.Iterator;
import java.util.List;
import org.more.webui.context.ViewContext;
/**
 * ��ʾ���������е�һ���׶Ρ�
 * @version : 2011-8-4
 * @author ������ (zyc@byshell.org)
 */
public abstract class Phase {
    //    private IAttribute flash = new AttBase();
    //    /**��ȡһ��flash���flash���Դ�Խ��ǰ�׶εĸ��������֡�*/
    //    public IAttribute getFlash() {
    //        return this.flash;
    //    };
    /**��ʼ����֮ǰ������׶��¼�*/
    public void doBefore(ViewContext uiContext, List<PhaseListener> listeners, PhaseID phaseID) {
        Iterator<PhaseListener> iterator = listeners.iterator();
        while (iterator.hasNext() == true) {
            PhaseListener listener = iterator.next();
            if (listener.getPhaseID().equals(phaseID) == true)
                listener.beforePhase(uiContext, this);
        }
    };
    /**����ʱ */
    public void doPhase(ViewContext uiContext, List<PhaseListener> listeners) throws Throwable {
        this.doBefore(uiContext, listeners, this.getPhaseID());
        this.execute(uiContext);
        this.doAfter(uiContext, listeners, this.getPhaseID());
    };
    /**�������������׶��¼�*/
    public void doAfter(ViewContext uiContext, List<PhaseListener> listeners, PhaseID phaseID) {
        Iterator<PhaseListener> iterator = listeners.iterator();
        while (iterator.hasNext() == true) {
            PhaseListener listener = iterator.next();
            if (listener.getPhaseID().equals(phaseID) == true)
                listener.afterPhase(uiContext, this);
        }
    };
    /**ִ�н׶� */
    public abstract void execute(ViewContext uiContext) throws Throwable;
    /**��ȡ�׶�ID*/
    public abstract PhaseID getPhaseID();
};