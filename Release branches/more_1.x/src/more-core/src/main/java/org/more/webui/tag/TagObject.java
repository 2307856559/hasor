package org.more.webui.tag;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import org.more.core.json.JsonUtil;
import org.more.webui.components.UIComponent;
import org.more.webui.context.Register;
import org.more.webui.context.ViewContext;
import org.more.webui.render.Render;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
/**
 * ͨ�ñ�ǩ����
 * @version : 2012-5-13
 * @author ������ (zyc@byshell.org)
 */
public class TagObject implements TemplateDirectiveModel {
    @SuppressWarnings("rawtypes")
    public void execute(Environment arg0, Map arg1, TemplateModel[] arg2, TemplateDirectiveBody arg3) throws TemplateException, IOException {
        //A.��ȡ�齨
        ViewContext viewContext = ViewContext.getCurrentViewContext();
        UIComponent component = null;
        try {
            String componentID = (String) arg1.get("id").toString();
            component = viewContext.getViewRoot().getChildByID(componentID);
        } catch (Exception e) {
            throw new TemplateException(e, arg0);
        }
        //B.�ж�ʱ����Ҫִ����Ⱦ
        System.out.println(JsonUtil.transformToJson(arg1));
        if (component.isRender() == false)
            return;
        //C.������Ⱦ
        Register register = viewContext.getUIContext().getRegister();
        String renderID = register.getMappingRendererByComponent(viewContext.getRenderKitName(), component.getComponentType());
        Render renderer = register.createRenderer(renderID);
        Writer writer = arg0.getOut();
        renderer.beginRender(viewContext, component, writer);
        if (component.isRenderChildren() == true && arg3 != null)
            arg3.render(writer);
        renderer.endRender(viewContext, component, writer);
    }
}