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
import org.more.webui.context.FacesConfig;
import org.more.webui.context.FacesContext;
import org.more.webui.context.ViewContext;
import org.more.webui.lifestyle.phase.ApplyRequestValue_Phase;
import org.more.webui.lifestyle.phase.InitView_Phase;
import org.more.webui.lifestyle.phase.InvokeApplication_Phase;
import org.more.webui.lifestyle.phase.Render_Phase;
import org.more.webui.lifestyle.phase.RestoreView_Phase;
import org.more.webui.lifestyle.phase.UpdateModules_Phase;
import org.more.webui.lifestyle.phase.Validation_Phase;
/**
 * ��������ִ�з���
 * @version : 2011-8-3
 * @author ������ (zyc@byshell.org)
 */
public abstract class Lifecycle {
    private List<PhaseListener> listeners = new ArrayList<PhaseListener>();
    private List<Phase>         phase     = new ArrayList<Phase>();
    private FacesConfig         config    = null;
    public Lifecycle(FacesConfig config) {
        this.config = config;
    }
    protected FacesConfig getEnvironment() {
        return config;
    }
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
                //                long t = System.currentTimeMillis();
                phase.doPhase(uiContext, this.listeners);
                //                System.out.println("$$$$\t" + phase + "\t" + (System.currentTimeMillis() - t));
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
    public void addPhase(Phase phase) {
        this.phase.add(phase);
    };
    /**����Ĭ�ϵ��������ڶ���*/
    public static Lifecycle getDefault(FacesConfig config, FacesContext context) {
        /*�����������ڶ���*/
        Lifecycle lifestyle = new Lifecycle(config) {};
        {
            //��1�׶Σ����ڳ�ʼ����ͼ�е����ģ������
            lifestyle.addPhase(new InitView_Phase());
            //��2�׶Σ�����UI���״̬��
            lifestyle.addPhase(new RestoreView_Phase());
            //��3�׶Σ������������Ҫ����������ֵ���뵽�����ϡ�
            lifestyle.addPhase(new ApplyRequestValue_Phase());
            //��4�׶Σ������ģ���е����ݽ�����֤��
            lifestyle.addPhase(new Validation_Phase());
            //��5�׶Σ������ģ���е�ֵ���õ�ӳ���bean�С�
            lifestyle.addPhase(new UpdateModules_Phase());
            //��6�׶Σ�����������Ϣ������Command��Click�¼����ȶ�����
            lifestyle.addPhase(new InvokeApplication_Phase());
            //��7�׶Σ���ִ�����UI��Ϣ��Ⱦ���ͻ����С�
            lifestyle.addPhase(new Render_Phase());
        }
        return lifestyle;
    }
};