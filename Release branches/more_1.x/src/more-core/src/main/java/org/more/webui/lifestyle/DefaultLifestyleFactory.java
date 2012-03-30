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
import org.more.webui.lifestyle.phase.ApplyRequestValue_Phase;
import org.more.webui.lifestyle.phase.InitView_Phase;
import org.more.webui.lifestyle.phase.InvokeApplication_Phase;
import org.more.webui.lifestyle.phase.Render_Phase;
import org.more.webui.lifestyle.phase.RestoreView_Phase;
import org.more.webui.lifestyle.phase.UpdateModules_Phase;
import org.more.webui.lifestyle.phase.Validation_Phase;
/**
 * �������ڹ����࣬�������ڴ����������ڶ���
 * @version : 2012-3-29
 * @author ������ (zyc@byshell.org)
 */
public class DefaultLifestyleFactory extends LifecycleFactory {
    /**�����������ڶ���*/
    public Lifecycle createLifestyle() {
        /*�����������ڶ���*/
        Lifecycle lifestyle = new Lifecycle() {};
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
}