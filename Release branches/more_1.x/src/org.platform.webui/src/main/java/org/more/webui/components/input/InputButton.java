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
import org.more.webui.components.UIButton;
import org.more.webui.render.inputs.ButtonInputRender;
/**
 * <b>����</b>��input���Ͱ�ť
 * <br><b>�齨����</b>��ui_Button
 * <br><b>��ǩ</b>��@ui_Button
 * <br><b>������¼�</b>��OnAction
 * <br><b>��Ⱦ��</b>��{@link ButtonInputRender}
 * @version : 2012-5-15
 * @author ������ (zyc@byshell.org)
 */
@UICom(tagName = "ui_Button", renderType = ButtonInputRender.class)
public class InputButton extends UIButton {
    @Override
    public String getComponentType() {
        return "ui_Button";
    }
}