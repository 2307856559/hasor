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
package org.more.webui.components.input;
import org.more.webui.component.support.UICom;
import org.more.webui.components.UIInput;
import org.more.webui.context.ViewContext;
import org.more.webui.render.inputs.TextInputRender;
/**
 * <b>����</b>��Text�����
 * <br><b>�齨����</b>��ui_Text
 * <br><b>��ǩ</b>��@ui_Text
 * <br><b>������¼�</b>����
 * <br><b>��Ⱦ��</b>��{@link TextInputRender}
 * @version : 2012-5-15
 * @author ������ (zyc@byshell.org)
 */
@UICom(tagName = "ui_Text", renderType = TextInputRender.class)
public class TextInput extends UIInput {
    /**ͨ�����Ա�*/
    public enum Propertys {
        /**��ֵ�ǵ�valueû������ֵʱ���ø�ֵ�����ʾ��RW��*/
        tipTitle,
    }
    @Override
    public String getComponentType() {
        return "ui_Text";
    }
    @Override
    protected void initUIComponent(ViewContext viewContext) {
        super.initUIComponent(viewContext);
        this.setPropertyMetaValue(Propertys.tipTitle.name(), null);
    }
    public String getTipTitle() {
        return this.getProperty(Propertys.tipTitle.name()).valueTo(String.class);
    }
    public void setTipTitle(String tipTitle) {
        this.getProperty(Propertys.tipTitle.name()).value(tipTitle);
    }
}