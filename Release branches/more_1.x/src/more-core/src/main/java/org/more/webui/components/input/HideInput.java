package org.more.webui.components.input;
import org.more.webui.component.UIInput;
import org.more.webui.component.support.UICom;
import org.more.webui.render.inputs.HideInputRender;
/**
 * <b>����</b>������һ�����ر���
 * <br><b>�齨����</b>��ui_HideInput
 * <br><b>��ǩ</b>��@ui_HideInput
 * <br><b>������¼�</b>����
 * <br><b>��Ⱦ��</b>��{@link HideInputRender}
 * @version : 2012-5-15
 * @author ������ (zyc@byshell.org)
 */
@UICom(tagName = "ui_HideInput", renderType = HideInputRender.class)
public class HideInput extends UIInput {
    @Override
    public String getComponentType() {
        return "ui_HideInput";
    };
}