package org.more.webui.context;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.more.util.StringConvertUtil;
import org.more.webui.UIInitException;
import org.more.webui.components.UIViewRoot;
import org.more.webui.web.PostFormEnum;
import freemarker.template.Template;
/**
 * �������ͼ��������
 * @version : 2012-4-25
 * @author ������ (zyc@byshell.org)
 */
public class ViewContext {
    private HttpServletRequest  req       = null; //req
    private HttpServletResponse res       = null; //res
    private String              facePath  = null; //��ͼģ��λ��
    private FacesContext        uiContext = null; //����������
    private UIViewRoot          viewRoot  = null;
    //
    public ViewContext(HttpServletRequest req, HttpServletResponse res, FacesContext uiContext) {
        this.req = req;
        this.res = res;
        this.facePath = this.req.getRequestURI();
        this.uiContext = uiContext;
    }
    /**��ȡ��ʾ����ͼ��{@link UIViewRoot}����*/
    public UIViewRoot getViewRoot() throws UIInitException, IOException {
        //A.����UIViewRoot
        if (this.viewRoot == null) {
            Template temp = this.getTemplate();
            this.viewRoot = this.uiContext.getFacesConfig().createViewRoot(temp);
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
    /**��ȡ{@link FacesContext}����*/
    public FacesContext getUIContext() {
        return this.uiContext;
    }
    /**��ȡʹ�õı���*/
    public String getEncoding() {
        return this.uiContext.getEncoding();
    }
    /**��ȡ��ͼģ�����*/
    public Template getTemplate() throws IOException {
        return this.uiContext.getFreemarker().getTemplate(this.facePath, this.getEncoding());
    }
    /**��ȡʹ�õ���Ⱦ������*/
    public String getRenderKitName() {
        return "default";
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
    //    /**��ȡ�ͻ����������¼���*/
    //    public Event getEvent() {};
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