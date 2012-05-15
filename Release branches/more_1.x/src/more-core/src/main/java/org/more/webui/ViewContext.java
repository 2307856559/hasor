package org.more.webui;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.more.core.error.InvokeException;
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
    public UIViewRoot getViewRoot() {
        //A.����UIViewRoot
        if (this.viewRoot == null)
            try {
                this.viewRoot = UIViewRoot.createViewRoot(this);
                //C.�����������
                Map<String, String[]> reqParams = this.getHttpRequest().getParameterMap();
                for (String key : reqParams.keySet()) {
                    String[] var = reqParams.get(key);
                    if (var == null)
                        this.viewRoot.addParamter(key, null);
                    else if (var.length == 1)
                        this.viewRoot.addParamter(key, var[0]);
                    else
                        this.viewRoot.addParamter(key, var);
                }
            } catch (Throwable e) {
                throw new InvokeException(e);
            }
        //B.����UIViewRoot
        return this.viewRoot;
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
}