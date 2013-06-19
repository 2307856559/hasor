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
import org.more.webui.context.RenderType;
import org.more.webui.context.ViewContext;
import org.more.webui.lifestyle.Phase;
import org.more.webui.lifestyle.PhaseID;
/**
 * ��7�׶Σ���ִ�����UI��Ϣ��Ⱦ���ͻ����С�
 * @version : 2011-8-4
 * @author ������ (zyc@byshell.org)
 */
public class Render_Phase extends Phase {
    public static class Render_PhaseID extends PhaseID {
        public String getPhaseID() {
            return "Render";
        };
    };
    private static Render_PhaseID PhaseID = new Render_PhaseID();
    public PhaseID getPhaseID() {
        return PhaseID;
    };
    public void execute(ViewContext viewContext) throws Throwable {
        HttpServletResponse response = viewContext.getHttpResponse();
        if (response.isCommitted() == true)
            return;
        //ȷ����Ⱦ��Χ��������Ⱦ
        RenderType renderType = viewContext.getRenderType();
        if (renderType == RenderType.No)
            return;
        else if (renderType == RenderType.Part) {//TODO : �������� �п��ܲ�֧��
            System.out.println("�ֲ���Ⱦ::�ݲ�֧��.");
            return;
        } else if (renderType == RenderType.ALL)
            viewContext.getTemplate().process(viewContext.getViewELContext(), response.getWriter());
    };
};
