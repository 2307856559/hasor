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
package org.more.webui.components.select;
import org.more.webui.component.support.UICom;
import org.more.webui.components.UISelectInput;
import org.more.webui.context.ViewContext;
import org.more.webui.render.select.CheckManySelectInputRender;
/**
 * <b>����</b>��ѡ����ѡ�齨��
 * <br><b>�齨����</b>��ui_ManySelect
 * <br><b>��ǩ</b>��@ui_ManySelect
 * <br><b>������¼�</b>����
 * <br><b>��Ⱦ��</b>��{@link CheckManySelectInputRender}
 * @version : 2012-5-15
 * @author ������ (zyc@byshell.org)
 */
@UICom(tagName = "ui_ManySelect", renderType = CheckManySelectInputRender.class)
public class ManyCheck extends UISelectInput {
    @Override
    public String getComponentType() {
        return "ui_ManySelect";
    }
    /**ͨ�����Ա�*/
    public static enum Propertys {
        /** ���ݣ�-��*/
        listData,
        /**��ʾ�����ֶΣ�R��*/
        keyField,
        /**ֵ�ֶΣ�R��*/
        varField,
    }
    @Override
    protected void initUIComponent(ViewContext viewContext) {
        super.initUIComponent(viewContext);
    }
}