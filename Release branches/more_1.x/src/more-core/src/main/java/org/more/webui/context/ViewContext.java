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
package org.more.webui.context;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.more.core.iatt.DecSequenceMap;
import org.more.util.StringConvertUtil;
import org.more.webui.UIInitException;
import org.more.webui.support.UIComponent;
import org.more.webui.support.UIViewRoot;
import org.more.webui.web.PostFormEnum;
import freemarker.template.Template;
/**
 * �������ͼ��������ÿ������һ����ͼ��
 * @version : 2012-4-25
 * @author ������ (zyc@byshell.org)
 */
public class ViewContext extends HashMap<String, Object> {
    private static final long   serialVersionUID = 994771756771520847L;
    private HttpServletRequest  req              = null;               //req
    private HttpServletResponse res              = null;               //res
    private String              facePath         = null;               //��ͼģ��λ��
    private FacesContext        uiContext        = null;               //����������
    private long                comClientID      = 0;                  //�齨�Ŀͻ���ID
    //
    public ViewContext(HttpServletRequest req, HttpServletResponse res, FacesContext uiContext) {
        this.req = req;
        this.res = res;
        this.facePath = this.req.getRequestURI().substring(req.getContextPath().length());
        this.uiContext = uiContext;
    }
    /**��ȡҪ��Ⱦ��ҳ��*/
    public String getFacePath() {
        return facePath;
    }
    /**������Ⱦ��ҳ��*/
    public void setFacePath(String facePath) {
        this.facePath = facePath;
    }
    /**��ȡһ������������Ψһ�Ŀͻ���ID */
    public String getComClientID(UIComponent component) {
        return String.valueOf(comClientID++);
    }
    //
    //
    private UIViewRoot viewRoot = null;
    /**��ȡ��ʾ����ͼ��{@link UIViewRoot}����*/
    public UIViewRoot getViewRoot() throws UIInitException, IOException {
        //A.����UIViewRoot
        if (this.viewRoot == null) {
            Template tempRoot = this.getTemplate();
            String reqURI = this.req.getRequestURI();
            String templateFile = this.req.getSession().getServletContext().getRealPath(reqURI);
            this.viewRoot = this.uiContext.getFacesConfig().createViewRoot(tempRoot, templateFile);
        }
        //B.����UIViewRoot
        return this.viewRoot;
    }
    /**��ȡ�������*/
    public HttpServletRequest getHttpRequest() {
        return this.req;
    }
    /**��ȡ��Ӧ����*/
    public HttpServletResponse getHttpResponse() {
        return this.res;
    }
    //
    //
    private DecSequenceMap<Object> seq = null;
    /**��ȡ�뵱ǰ��ͼ��ص�EL������*/
    public Map<String, Object> getViewELContext() {
        if (this.seq == null) {
            this.seq = new DecSequenceMap<Object>();
            /*0.��ǩ����*/
            //            RenderKit kit = this.getUIContext().getRenderKit();
            //            this.seq.addMap(kit.getTags());//��WebUI�������ڣ���Ⱦ��ʱ��ʹ�á�
            //ע��ԭ��:��FacesContextFactory��ʼ��ʱ���Ѿ�����ǩ���õ�ȫ��services�С�
            /*1.��ͼ����*/
            this.seq.addMap(this);
            /*2.��������*/
            this.seq.addMap(this.getUIContext().getAttribute());
            /*3.��������*/
            this.seq.addMap(FreemarkerInit.initModelMap);
        }
        return this.seq;
    }
    /**��ȡ{@link FacesContext}����*/
    public FacesContext getUIContext() {
        return this.uiContext;
    }
    /**��ȡʹ�õı���*/
    public String getEncoding() {
        return this.uiContext.getEncoding();
    }
    /**��ȡ��ͼģ�����������Ⱦ*/
    public Template getTemplate() throws IOException {
        return this.uiContext.getFreemarker().getTemplate(this.facePath, this.getEncoding());
    }
    /*--------------*/
    //  ThreadLocal
    /*--------------*/
    private static ThreadLocal<ViewContext> threadLocal = new ThreadLocal<ViewContext>();
    public static ViewContext getCurrentViewContext() {
        return threadLocal.get();
    }
    public static void setCurrentViewContext(ViewContext viewContext) {
        if (threadLocal.get() != null)
            threadLocal.remove();
        threadLocal.set(viewContext);
    }
    /*--------------*/
    //   PostForm
    /*--------------*/
    /**��ȡ����������Դ���Ǹ��齨��*/
    public String getTarget() {
        return this.getHttpRequest().getParameter(PostFormEnum.PostForm_TargetParamKey.value());
    };
    /**��ȡ�ͻ����������¼���*/
    public String getEvent() {
        String event = this.getHttpRequest().getParameter(PostFormEnum.PostForm_EventKey.value());
        return (event == null || event.equals("")) ? null : event;
    };
    /**��ȡ��Ⱦ���ͣ�Ĭ����Ⱦȫ��*/
    public RenderType getRenderType() {
        String renderKey = PostFormEnum.PostForm_RenderParamKey.value();
        String renderType = this.getHttpRequest().getParameter(renderKey);
        RenderType render = StringConvertUtil.changeType(renderType, RenderType.class, RenderType.ALL);
        if (render == null)
            return RenderType.ALL;
        else
            return render;
    }
    /**��ȡ�����״̬���ݡ�*/
    public String getStateData() {
        String stateDataKey = PostFormEnum.PostForm_StateDataParamKey.value();
        return this.getHttpRequest().getParameter(stateDataKey);
    }
}