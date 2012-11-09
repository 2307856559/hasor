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
import org.more.webui.render.inputs.ButtonInputRender;
import org.more.webui.render.inputs.CheckboxInputRender;
import org.more.webui.render.inputs.FileInputRender;
import org.more.webui.render.inputs.HideInputRender;
import org.more.webui.render.inputs.ImageInputRender;
import org.more.webui.render.inputs.PasswordInputRender;
import org.more.webui.render.inputs.RadioInputRender;
import org.more.webui.render.inputs.ResetInputRender;
import org.more.webui.render.inputs.SubmitInputRender;
import org.more.webui.render.inputs.TextInputRender;
/**
 * <b>�齨ģ��</b>�����ڱ�����������������ܵ��齨ģ�ͣ���Ԫ�أ���
 * <br><b>������¼�</b>����
 * <br><b>��Ⱦ��</b>��{@link ButtonInputRender}��{@link CheckboxInputRender}��{@link FileInputRender}��
 * {@link HideInputRender}��{@link ImageInputRender}��{@link PasswordInputRender}��{@link RadioInputRender}��
 * {@link ResetInputRender}��{@link SubmitInputRender}��{@link TextInputRender}
 * @version : 2012-5-15
 * @author ������ (zyc@byshell.org)
 */
public abstract class UIInput extends UIOutput {
    /**ͨ�����Ա�*/
    public enum Propertys {
        /**������RW��*/
        name,
        /**��֤�������ݵ�������ʽ��RW��*/
        verification,
    }
    @Override
    protected void initUIComponent(ViewContext viewContext) {
        super.initUIComponent(viewContext);
        this.setPropertyMetaValue(Propertys.name.name(), null);
        this.setPropertyMetaValue(Propertys.verification.name(), null);
    }
    /*-------------------------------------------------------------------------------*/
    /**��ȡ�齨����*/
    public String getName() {
        return this.getProperty(Propertys.name.name()).valueTo(String.class);
    }
    /**�����齨����*/
    public void setName(String name) {
        this.getProperty(Propertys.name.name()).value(name);
    }
    /**��֤,������ʽ��������ã�*/
    public String getVerification() {
        return this.getProperty(Propertys.verification.name()).valueTo(String.class);
    }
    /**��֤,������ʽ��������ã�*/
    public void setVerification(String verification) {
        this.getProperty(Propertys.verification.name()).value(verification);
    }
}