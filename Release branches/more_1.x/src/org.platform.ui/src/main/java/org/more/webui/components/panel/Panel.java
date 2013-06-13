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
package org.more.webui.components.panel;
import org.more.webui.component.UIComponent;
import org.more.webui.component.support.UICom;
import org.more.webui.context.ViewContext;
/**
 * <b>����</b>��ҳ����壬����������������ҳ�档
 * <br><b>�齨����</b>��ui_Panel
 * <br><b>��ǩ</b>��@ui_Panel
 * <br><b>������¼�</b>��OnLoadPage���޴���
 * <br><b>��Ⱦ��</b>��{@link PanelRender}
 * @version : 2012-5-15
 * @author ������ (zyc@byshell.org)
 */
@UICom(tagName = "ui_Panel", renderType = PanelRender.class)
public class Panel extends UIComponent {
    /**ͨ�����Ա�*/
    public static enum Propertys {
        /**Ҫ����ĵ�ַ��RW��*/
        pageURL,
    }
    @Override
    public String getComponentType() {
        return "ui_Panel";
    }
    @Override
    protected void initUIComponent(ViewContext viewContext) {
        super.initUIComponent(viewContext);
        this.setPropertyMetaValue(Propertys.pageURL.name(), null);
    }
    public String getPageURL() {
        return this.getProperty(Propertys.pageURL.name()).valueTo(String.class);
    }
    public void setPageURL(String pageURL) {
        this.getProperty(Propertys.pageURL.name()).value(pageURL);
    }
}