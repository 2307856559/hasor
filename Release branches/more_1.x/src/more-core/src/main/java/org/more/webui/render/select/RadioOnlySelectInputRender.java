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
package org.more.webui.render.select;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import org.more.util.BeanUtil;
import org.more.webui.component.UISelectInput;
import org.more.webui.context.ViewContext;
import org.more.webui.tag.TemplateBody;
import freemarker.template.TemplateException;
/**
 * ��ѡ�������齨��Ⱦ��һ��radio��
 * <br><b>�ͻ���ģ��</b>��UIRadioSelectOnlyInput��UIRadioSelectOnlyInput.js��
 * @version : 2012-5-18
 * @author ������ (zyc@byshell.org)
 */
public class RadioOnlySelectInputRender<T extends UISelectInput> extends AbstractSelectInputRender<T> {
    @Override
    public String getClientType() {
        return "UIRadioOnlySelectInput";
    }
    @Override
    public String tagName(ViewContext viewContext, T component) {
        return "ul";
    }
    @Override
    public void render(ViewContext viewContext, T component, TemplateBody arg3, Writer writer) throws IOException, TemplateException {
        List<?> listData = component.getValueList();
        String keyField = component.getKeyField();
        String varField = component.getVarField();
        Object[] selectVar = component.getSelectValues();
        int dataIndex = 0;
        if (listData != null)
            for (Object obj : listData) {
                if (obj == null)
                    continue;
                Object keyValue = null;
                Object varValue = null;
                if (obj instanceof Map) {
                    Map mapData = (Map) obj;
                    keyValue = mapData.get(keyField);
                    varValue = mapData.get(varField);
                } else {
                    keyValue = BeanUtil.readPropertyOrField(obj, keyField);
                    varValue = BeanUtil.readPropertyOrField(obj, varField);
                }
                //���
                String cid = component.getComponentID() + "_" + dataIndex;
                writer.write("<li><label id='" + cid + "_Label'>");
                RenderType renderType = this.getRenderType(component);
                if (renderType == RenderType.atFirst) {
                    //��ǰ������ı�
                    this.renderSpan(component, cid, writer, varValue, selectVar);
                    this.renderInput(component, cid, writer, keyValue, varValue, selectVar, true);
                } else if (renderType == RenderType.atLast) {
                    //�ں�������ı�
                    this.renderInput(component, cid, writer, keyValue, varValue, selectVar, true);
                    this.renderSpan(component, cid, writer, varValue, selectVar);
                } else if (renderType == RenderType.onlyTitle) {
                    //������ı�
                    this.renderInput(component, cid, writer, keyValue, varValue, selectVar, false);
                    this.renderSpan(component, cid, writer, varValue, selectVar);
                } else
                    throw new NullPointerException("getRenderType ����û����ȷ����ֵ.");
                writer.write("</label></li>");
                dataIndex++;
            }
    }
    public void renderInput(T component, String cid, Writer writer, Object keyValue, Object varValue, Object[] selectVar, boolean isShow) throws IOException, TemplateException {
        writer.write("<input id='" + cid + "_Input' type='radio' forComID='" + component.getComponentID() + "'");
        String name = component.getName();
        if (name == null || name.equals("") == true) {} else
            writer.write(" name='" + name + "'");
        if (isShow == false)
            writer.write(" style='display:none'");
        writer.write(" value='" + keyValue + "'");
        writer.write(" varValue='" + varValue + "'");
        for (Object obj : selectVar)
            if ((keyValue == null && obj == null) == true || keyValue.equals(obj) == true)
                writer.write(" checked='checked'");
        writer.write("/>");
    }
    public void renderSpan(T component, String cid, Writer writer, Object varValue, Object[] selectVar) throws IOException, TemplateException {
        writer.write("<a id='" + cid + "_Span' href='javascript:void(0)'>" + varValue + "</a>");
    }
    /*----------------------------------------------------------------*/
    public static enum RenderType {
        /**ѡ���ǰ�����ѡ���ı�����ѡ��򣩡�*/
        atFirst,
        /**ѡ���������ѡ���ı�����ѡ��򣩡�*/
        atLast,
        /**ѡ���ֻ�����ı���������ѡ��򣩡�*/
        onlyTitle,
    }
    protected RenderType getRenderType(T component) {
        return RenderType.atFirst;
    }
}