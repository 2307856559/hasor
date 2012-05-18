package org.more.webui.context;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.more.webui.UIInitException;
import org.more.webui.components.UIViewRoot;
import freemarker.template.Template;
/**
 * ��ͼ����
 * @version : 2012-4-25
 * @author ������ (zyc@byshell.org)
 */
public class ViewContext {
    private HttpServletRequest  req       = null; //req
    private HttpServletResponse res       = null; //res
    private String              facePath  = null; //��ͼģ��λ��
    private UIContext           uiContext = null; //����������
    private UIViewRoot          viewRoot  = null;
    //
    public ViewContext(HttpServletRequest req, HttpServletResponse res, UIContext uiContext) {
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
            this.viewRoot = this.uiContext.getRegister().createViewRoot(temp);
        }
        //B.����UIViewRoot
        return this.viewRoot;
    }
    public boolean isRender() {
        // TODO Auto-generated method stub
        return true;
    }
    /**��ȡ�����״̬���ݡ�*/
    public String getStateJsonData() {
        String key = this.uiContext.getGlobal().getString(UIContext.Request_StateKEY);
        return this.getHttpRequest().getParameter(key);
    }
    /**��ȡ�������*/
    public HttpServletRequest getHttpRequest() {
        return this.req;
    }
    /**��ȡ��Ӧ����*/
    public HttpServletResponse getHttpResponse() {
        return this.res;
    }
    /**��ȡ{@link UIContext}����*/
    public UIContext getUIContext() {
        return this.uiContext;
    }
    /**��ȡʹ�õı���*/
    public String getEncoding() {
        return this.uiContext.getEncoding();
    }
    /**��ȡ��ͼģ�����*/
    public Template getTemplate() throws IOException {
        return this.uiContext.getFreemarkerConfiguration().getTemplate(this.facePath, this.getEncoding());
    }
    /*-------------*/
    //
    //
    //
    //
    //
    /*-------------*/
    /*----------------------------------------------------------------------------------*/
    //    private static ThreadLocal<UIViewRoot> currentViewRoot = new ThreadLocal<UIViewRoot>();
    //    public static UIViewRoot getCurrentUIViewRoot() {
    //        return currentViewRoot.get();
    //    }
    //    public static void setCurrentViewRoot(UIViewRoot viewRoot) {
    //        if (getCurrentUIViewRoot() != null) {
    //            currentViewRoot.remove();
    //        }
    //        currentViewRoot.set(viewRoot);
    //    }
    /**��ȡʹ�õ���Ⱦ������*/
    public String getRenderKitName() {
        return "default";
    }
    //    private static ThreadLocal<ViewContext> threadLocal = new ThreadLocal<ViewContext>();
    //    public static ViewContext getCurrentViewContext() {
    //        return threadLocal.get();
    //    }
    //    public static void setCurrentViewContext(ViewContext viewContext) {
    //        if (threadLocal.get() != null)
    //            threadLocal.remove();
    //        threadLocal.set(viewContext);
    //    }
    private static ViewContext threadLocal = null;
    public static ViewContext getCurrentViewContext() {
        return threadLocal;
    }
    public static void setCurrentViewContext(ViewContext viewContext) {
        threadLocal = viewContext;
    }
}