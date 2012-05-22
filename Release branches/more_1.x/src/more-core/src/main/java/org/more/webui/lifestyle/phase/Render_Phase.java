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
import javax.servlet.http.HttpServletResponse;
import org.more.core.iatt.DecSequenceAttribute;
import org.more.webui.context.FacesContext;
import org.more.webui.context.RenderType;
import org.more.webui.context.ViewContext;
import org.more.webui.lifestyle.Phase;
import org.more.webui.lifestyle.PhaseID;
import org.more.webui.render.RenderKit;
/**
 * ��7�׶Σ���ִ�����UI��Ϣ��Ⱦ���ͻ����С�
 * @version : 2011-8-4
 * @author ������ (zyc@byshell.org)
 */
public class Render_Phase extends Phase {
    private Render_PhaseID phaseID = new Render_PhaseID();
    public PhaseID getPhaseID() {
        return this.phaseID;
    };
    public void execute(ViewContext viewContext) throws Throwable {
        HttpServletResponse response = viewContext.getHttpResponse();
        if (response.isCommitted() == true)
            return;
        //A.ȷ����Ⱦ��Χ
        RenderType renderType = viewContext.isRender();
        if (renderType == RenderType.No)
            return;
        else if (renderType == RenderType.Part)//TODO : �������� �п��ܲ�֧��
            return;
        else if (renderType == RenderType.ALL)
            return;
        //B.׼������
        FacesContext uiContext = viewContext.getUIContext();
        RenderKit kit = uiContext.getFacesConfig().getRenderKit(viewContext.getRenderKitName());
        DecSequenceAttribute seq = new DecSequenceAttribute();
        seq.putMap(kit.getTags());
        seq.putMap(uiContext.getAttribute());
        //C.ִ����Ⱦ
        viewContext.getTemplate().process(seq.toMap(), response.getWriter());
    };
};
class Render_PhaseID extends PhaseID {
    public String getPhaseID() {
        return "Render";
    };
};