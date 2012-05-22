package org.more.webui.render;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import org.more.webui.components.UIComponent;
import org.more.webui.context.ViewContext;
/**
 * �齨��Ⱦ��
 * @version : 2012-5-18
 * @author ������ (zyc@byshell.org)
 */
public interface Render {
    /**��ʼ��Ⱦ�齨*/
    public void beginRender(ViewContext viewContext, UIComponent component, Map params, Writer writer) throws IOException;
    /**�齨��Ⱦ����*/
    public void endRender(ViewContext viewContext, UIComponent component, Map params, Writer writer) throws IOException;
}