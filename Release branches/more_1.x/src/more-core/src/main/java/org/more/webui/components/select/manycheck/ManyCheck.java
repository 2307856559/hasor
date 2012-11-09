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
package org.more.webui.components.select.manycheck;
import org.more.webui.component.UISelectInput;
import org.more.webui.component.support.NoState;
import org.more.webui.component.support.UICom;
import org.more.webui.context.ViewContext;
/**
 * <b>����</b>��ѡ����ѡ�齨��
 * <br><b>�齨����</b>��ui_ManySelect
 * <br><b>��ǩ</b>��@ui_ManySelect
 * <br><b>������¼�</b>����
 * <br><b>��Ⱦ��</b>��{@link ManyCheckRender}
 * @version : 2012-5-15
 * @author ������ (zyc@byshell.org)
 */
@UICom(tagName = "ui_ManySelect", renderType = ManyCheckRender.class)
public class ManyCheck extends UISelectInput {
    /**ͨ�����Ա�*/
    public static enum Propertys {
        /**�Ƿ񽫱�������ѡ���֮ǰ��Ĭ��false��R��*/
        titleFirst,
    }
    @Override
    public String getComponentType() {
        return "ui_ManySelect";
    }
    @Override
    protected void initUIComponent(ViewContext viewContext) {
        super.initUIComponent(viewContext);
        this.setPropertyMetaValue(Propertys.titleFirst.name(), false);
    }
    public boolean isTitleFirst() {
        return this.getProperty(Propertys.titleFirst.name()).valueTo(Boolean.TYPE);
    }
    @NoState
    public void setTitleFirst(boolean titleFirst) {
        this.getProperty(Propertys.titleFirst.name()).value(titleFirst);
    }
}