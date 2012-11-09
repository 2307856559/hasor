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
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.more.core.map.DecSequenceMap;
import org.more.util.StringConvertUtil;
import org.more.webui.component.UIComponent;
import org.more.webui.component.UIViewRoot;
import org.more.webui.freemarker.parser.TemplateScanner;
import org.more.webui.lifestyle.Lifecycle;
import org.more.webui.lifestyle.Phase;
import org.more.webui.lifestyle.PhaseID;
import org.more.webui.web.PostFormEnum;
import com.alibaba.fastjson.JSONObject;
import freemarker.template.Template;
import freemarker.template.TemplateException;
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
    private Lifecycle           lifecycle        = null;               //����������
    private long                comClientID      = 0;                  //�齨�Ŀͻ���ID
    //
    public ViewContext(HttpServletRequest req, HttpServletResponse res, Lifecycle lifecycle) {
        this.req = req;
        this.res = res;
        this.facePath = this.req.getRequestURI();
        if (this.facePath.startsWith(req.getContextPath()) == true)
            this.facePath = this.facePath.substring(req.getContextPath().length());
        this.lifecycle = lifecycle;
        Map<String, String[]> reqMap = req.getParameterMap();
        for (String key : reqMap.keySet()) {
            String[] value = reqMap.get(key);
            if (value.length == 0)
                this.put(key, null);
            else if (value.length == 1)
                this.put(key, value[0]);
            else
                this.put(key, value);
        }
    };
    /**�����������������*/
    public Lifecycle getLifecycle() {
        return this.lifecycle;
    }
    /**��ȡҪ��Ⱦ��ҳ�档*/
    public String getFacePath() {
        return facePath;
    };
    /**������Ⱦ��ҳ�档*/
    public void setFacePath(String facePath) {
        this.facePath = facePath;
    };
    /**��ȡһ������������Ψһ�Ŀͻ���ID��*/
    public String getComClientID(UIComponent component) {
        return String.valueOf(comClientID++);
    };
    public String newClientID() {
        return "Com_" + String.valueOf(comClientID++);
    }
    /**��ȡ��ǰ�������������ڽ׶Ρ�*/
    public PhaseID getPhaseID() {
        Phase phase = this.lifecycle.getCurrentPhase();
        if (phase != null)
            return phase.getPhaseID();
        return null;
    }
    /**ִ��ģ���ַ�����*/
    public String processTemplateString(String templateString) throws TemplateException, IOException {
        Map<String, Object> elContext = this.getViewELContext();
        CharArrayWriter charWrite = new CharArrayWriter();
        this.getUIContext().processTemplateString(templateString, charWrite, elContext);
        return charWrite.toString();
    };
    private UIViewRoot viewRoot = null;
    /**��ȡ��ʾ����ͼ��{@link UIViewRoot}����*/
    public UIViewRoot getViewRoot() throws IOException {
        //A.����UIViewRoot
        if (this.viewRoot == null) {
            Template tempRoot = this.getTemplate();
            String reqURI = this.req.getRequestURI();
            String templateFile = this.req.getSession().getServletContext().getRealPath(reqURI);
            //
            TemplateScanner scanner = this.getUIContext().getEnvironment().getTemplateScanner();
            //B.����ģ���ȡUIViewRoot
            this.viewRoot = (UIViewRoot) scanner.parser(tempRoot, new UIViewRoot(), this.getUIContext());
        }
        //B.����UIViewRoot
        return this.viewRoot;
    };
    /**��ȡ�������*/
    public HttpServletRequest getHttpRequest() {
        return this.req;
    };
    /**��ȡ��Ӧ����*/
    public HttpServletResponse getHttpResponse() {
        return this.res;
    };
    private DecSequenceMap<String, Object> seq = null;
    /**��ȡ�뵱ǰ��ͼ��ص�EL�����ġ�*/
    public Map<String, Object> getViewELContext() {
        if (this.seq == null) {
            this.seq = new DecSequenceMap<String, Object>();
            /*0.��ǩ��*/
            String KitScope = this.getRenderKitScope();
            this.seq.addMap(this.getUIContext().getRenderKit(KitScope).getTags());
            /*1.��ͼ����*/
            this.seq.addMap(this);
            /*2.Bean��������*/
            if (this.getUIContext().getBeanContext() instanceof Map)
                this.seq.addMap((Map) this.getUIContext().getBeanContext());
            /*3.��������*/
            this.seq.addMap(this.getUIContext().getAttribute());
        }
        return this.seq;
    };
    /**��ȡ{@link FacesContext}����*/
    public FacesContext getUIContext() {
        return this.lifecycle.getFacesContext();
    };
    /**��ȡ��ͼģ�����������Ⱦ��*/
    public Template getTemplate() throws IOException {
        String pageEncoding = this.getUIContext().getEnvironment().getPageEncoding();
        return this.getUIContext().getFreemarker().getTemplate(this.facePath, pageEncoding);
    };
    /**��ȡ��Ⱦ��KIT����*/
    public String getRenderKitScope() {
        return "default";
    };
    /*--------------*/
    //  ThreadLocal
    /*--------------*/
    private static ThreadLocal<ViewContext> threadLocal = new ThreadLocal<ViewContext>();
    public static ViewContext getCurrentViewContext() {
        return threadLocal.get();
    };
    public static void setCurrentViewContext(ViewContext viewContext) {
        if (threadLocal.get() != null)
            threadLocal.remove();
        threadLocal.set(viewContext);
    };
    /*--------------*/
    //   PostForm
    /*--------------*/
    /**��ȡ����������Դ���Ǹ��齨��*/
    public String getTarget() {
        return this.getHttpRequest().getParameter(PostFormEnum.PostForm_TargetParamKey.value());
    };
    /**��ȡ�����齨��·����*/
    public String getTargetPath() {
        return this.getHttpRequest().getParameter(PostFormEnum.PostForm_TargetPathKey.value());
    };
    /**��ȡ�ͻ����������¼���*/
    public boolean isAjax() {
        String isAjax = this.getHttpRequest().getParameter(PostFormEnum.PostForm_IsAjaxKey.value());
        return StringConvertUtil.parseBoolean(isAjax, false);
    };
    /**��ȡ�ͻ����������¼���*/
    public String getEvent() {
        String event = this.getHttpRequest().getParameter(PostFormEnum.PostForm_EventKey.value());
        return (event == null || event.equals("")) ? null : event;
    };
    /**��ȡ��Ⱦ���ͣ�Ĭ����Ⱦȫ����*/
    public RenderType getRenderType() {
        String renderKey = PostFormEnum.PostForm_RenderParamKey.value();
        String renderType = this.getHttpRequest().getParameter(renderKey);
        RenderType render = StringConvertUtil.changeType(renderType, RenderType.class, RenderType.ALL);
        if (render == null)
            return RenderType.ALL;
        else
            return render;
    };
    /**��ȡ�����״̬���ݡ�*/
    public String getStateData() {
        String stateDataKey = PostFormEnum.PostForm_StateDataParamKey.value();
        return this.getHttpRequest().getParameter(stateDataKey);
    };
    /**��ͻ��˷�����Ӧ���ݡ�*/
    private void pushClient(int stateNumber, Object returnData) throws IOException {
        if (this.getEvent() != null) {
            String sendDataStr = JSONObject.toJSONString(returnData);
            this.getHttpResponse().setStatus(stateNumber);
            this.getHttpResponse().getWriter().write(sendDataStr);
        }
    };
    /**��ͻ��˷�����Ӧ���ݣ���Ӧ״̬:200.*/
    public void sendObject(Object returnData) throws IOException {
        this.pushClient(200, returnData);
    };
    /**��ͻ��˷�����Ӧ���ݡ���Ӧ״̬:500.*/
    public void sendError(String returnError) throws IOException {
        this.sendError(500, new Exception(returnError));
    };
    /**��ͻ��˷�����Ӧ���ݡ���Ӧ״̬:500.*/
    public void sendError(Exception returnError) throws IOException {
        this.sendError(500, returnError);
    };
    /**��ͻ��˷�����Ӧ���ݡ���Ӧ״̬��errorID����ָ��.*/
    public void sendError(int errorID, String returnError) throws IOException {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        returnData.put("errorID", errorID);
        returnData.put("message", returnError);
        returnData.put("trace", null);
        this.pushClient(20, returnData);
    };
    /**��ͻ��˷�����Ӧ���ݡ���Ӧ״̬��errorID����ָ��.*/
    public void sendError(int errorID, Exception returnError) throws IOException {
        if (returnError == null)
            throw new NullPointerException();
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        CharArrayWriter caw = new CharArrayWriter();
        PrintWriter printWriter = new PrintWriter(caw);
        returnError.printStackTrace(printWriter);
        returnData.put("errorID", errorID);
        returnData.put("message", returnError.getLocalizedMessage());
        returnData.put("trace", caw.toString());
        this.pushClient(20, returnData);
    };
}