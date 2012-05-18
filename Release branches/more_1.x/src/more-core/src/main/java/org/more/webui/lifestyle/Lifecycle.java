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
import java.util.ArrayList;
import java.util.List;
import org.more.webui.UILifecycleException;
import org.more.webui.context.ViewContext;
/**
 * ��������ִ�з���
 * @version : 2011-8-3
 * @author ������ (zyc@byshell.org)
 */
public abstract class Lifecycle {
    private List<PhaseListener> listeners = new ArrayList<PhaseListener>();
    private List<Phase>         phase     = new ArrayList<Phase>();
    /**���һ���׶μ�����*/
    public void addPhaseListener(PhaseListener listener) {
        if (this.listeners.contains(listener) == false)
            this.listeners.add(listener);
    };
    /**ɾ��һ���׶μ�����*/
    public void removePhaseListener(PhaseListener listener) {
        if (this.listeners.contains(listener) == false)
            this.listeners.remove(listener);
    };
    /**��ȡ���н׶εļ�����*/
    public PhaseListener[] getPhaseListeners() {
        PhaseListener[] list = new PhaseListener[listeners.size()];
        this.listeners.toArray(list);
        return list;
    };
    /**��ʼ��������ui�������ڷ����� */
    public void execute(ViewContext uiContext) throws UILifecycleException {
        for (Phase phase : this.getPhases())
            try {
                phase.doPhase(uiContext, this.listeners);
            } catch (Throwable e) {
                throw new UILifecycleException("���������쳣����ִ��" + phase.getPhaseID() + "�׶��ڼ䷢���쳣��", e);
            }
    };
    /**��ȡ���������еĸ����׶ζ���*/
    public Phase[] getPhases() {
        Phase[] phase = new Phase[this.phase.size()];
        this.phase.toArray(phase);
        return phase;
    };
    /**���һ���׶�*/
    protected void addPhase(Phase phase) {
        this.phase.add(phase);
    };
};