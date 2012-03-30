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
import org.more.webui.ViewContext;
import org.more.webui.lifestyle.Phase;
import org.more.webui.lifestyle.PhaseID;
/**
 * ��1�׶Σ����ڳ�ʼ����ͼ�е����ģ������
 * @version : 2012-3-29
 * @author ������ (zyc@byshell.org)
 */
public class InitView_Phase extends Phase {
    private InitView_PhaseID phaseID = new InitView_PhaseID();
    public PhaseID getPhaseID() {
        return this.phaseID;
    };
    public void execute(ViewContext viewContext) throws Throwable {
        viewContext.getViewRoot().processInit(viewContext);
    }
};
class InitView_PhaseID extends PhaseID {
    public String getPhaseID() {
        return "InitView";
    };
};