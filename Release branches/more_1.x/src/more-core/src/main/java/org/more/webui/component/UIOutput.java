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
package org.more.webui.component;
import org.more.webui.context.ViewContext;
import org.more.webui.render.OutputRender;
/**
 * <b>�齨ģ��</b>�����ڱ���������ܵ��齨��
 * <br><b>������¼�</b>����
 * <br><b>��Ⱦ��</b>��{@link OutputRender}
 * @version : 2012-5-15
 * @author ������ (zyc@byshell.org)
 */
public abstract class UIOutput extends UIComponent {
    /**ͨ�����Ա�*/
    public enum Propertys {
        /**ֵ��RW��*/
        value,
    }
    @Override
    protected void initUIComponent(ViewContext viewContext) {
        super.initUIComponent(viewContext);
        this.setPropertyMetaValue(Propertys.value.name(), null);
    }
    /**��ȡ�齨value����*/
    public Object getValue() {
        return this.getProperty(Propertys.value.name()).valueTo(Object.class);
    }
    /**�����齨value����*/
    public void setValue(Object value) {
        this.getProperty(Propertys.value.name()).value(value);
    }
}