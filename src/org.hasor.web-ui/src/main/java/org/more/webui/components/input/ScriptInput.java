/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
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
import org.more.webui.component.support.NoState;
import org.more.webui.component.support.UICom;
import org.more.webui.components.UIInput;
import org.more.webui.context.ViewContext;
/**
 * <b>����</b>�����齨��valueֵ�󶨵�һ���ͻ��˽ű��ķ���ֵ�ϡ�
 * <br><b>�齨����</b>��ui_ScriptInput
 * <br><b>��ǩ</b>��@ui_ScriptInput
 * <br><b>������¼�</b>����
 * <br><b>��Ⱦ��</b>��{@link ScriptInputRender}
 * @version : 2012-5-15
 * @author ������ (zyc@byshell.org)
 */
@UICom(tagName = "ui_ScriptInput", renderType = ScriptInputRender.class)
public class ScriptInput extends UIInput {
    @Override
    public String getComponentType() {
        return "ui_ScriptInput";
    };
    /**ͨ�����Ա�*/
    public enum Propertys {
        /**�󶨵Ŀͻ��˽ű�,����R��*/
        varRScript,
        /**�󶨵Ŀͻ��˽ű�,д��R��*/
        varWScript,
    };
    @Override
    protected void initUIComponent(ViewContext viewContext) {
        super.initUIComponent(viewContext);
        this.setPropertyMetaValue(Propertys.varRScript.name(), null);
        this.setPropertyMetaValue(Propertys.varWScript.name(), null);
    }
    /**��ȡ,�󶨵Ŀͻ��˽ű�,��*/
    public String getVarRScript() {
        return this.getProperty(Propertys.varRScript.name()).valueTo(String.class);
    };
    @NoState
    /**����,�󶨵Ŀͻ��˽ű�,��*/
    public void setVarRScript(String varRScript) {
        this.getProperty(Propertys.varRScript.name()).value(varRScript);
    };
    /**��ȡ,�󶨵Ŀͻ��˽ű�,д*/
    public String getVarWScript() {
        return this.getProperty(Propertys.varWScript.name()).valueTo(String.class);
    };
    @NoState
    /**����,�󶨵Ŀͻ��˽ű�,д*/
    public void setVarWScript(String varWScript) {
        this.getProperty(Propertys.varWScript.name()).value(varWScript);
    };
}