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
package org.more.webui.lifestyle.phase;
import org.more.webui.context.ViewContext;
import org.more.webui.lifestyle.Phase;
import org.more.webui.lifestyle.PhaseID;
/**
 * ��3�׶Σ��ý׶ν����������Ҫ����������ֵ���뵽�����ϡ�
 * @version : 2011-8-4
 * @author ������ (zyc@byshell.org)
 */
public class ApplyRequestValue_Phase extends Phase {
    public static class ApplyRequestValue_PhaseID extends PhaseID {
        public String getPhaseID() {
            return "ApplyRequestValue";
        };
    };
    public final static ApplyRequestValue_PhaseID PhaseID = new ApplyRequestValue_PhaseID();
    //
    public PhaseID getPhaseID() {
        return PhaseID;
    };
    public void execute(ViewContext uiContext) throws Throwable {
        uiContext.getViewRoot().processApplyRequest(uiContext); //Ӧ���������
    };
};