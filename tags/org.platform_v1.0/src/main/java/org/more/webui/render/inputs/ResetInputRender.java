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
package org.more.webui.render.inputs;
import org.more.webui.components.UIInput;
import org.more.webui.context.ViewContext;
/**
 * �������齨��Ⱦ��input[type=reset]��
 * <br><b>�ͻ���ģ��</b>��UIButton��UIButton.js��
 * @version : 2012-5-18
 * @author ������ (zyc@byshell.org)
 */
public class ResetInputRender<T extends UIInput> extends AbstractInputRender<T> {
    @Override
    public String getClientType() {
        return "UIButton";
    }
    @Override
    public InputType getInputType(ViewContext viewContext, T component) {
        return InputType.reset;
    }
}