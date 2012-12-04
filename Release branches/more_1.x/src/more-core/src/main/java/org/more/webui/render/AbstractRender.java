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
package org.more.webui.render;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.more.util.BeanUtil;
import org.more.util.CommonCodeUtil;
import org.more.webui.component.UIComponent;
import org.more.webui.component.values.AbstractValueHolder;
import org.more.webui.context.ViewContext;
import org.more.webui.tag.TemplateBody;
import com.alibaba.fastjson.JSON;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;
/**
 * 
 * @version : 2012-5-18
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractRender<T extends UIComponent> implements Render<T> {
    private String tagString = null;
    /**��ȡ��Ⱦ�Ŀͻ����齨ģ������ÿһ���ͻ����齨�����Ӧһ��js�ű���*/
    public abstract String getClientType();
    /**Ҫʹ�õı�ǩ*/
    public abstract String tagName(ViewContext viewContext, T component);
    @Override
    public void beginRender(ViewContext viewContext, T component, TemplateBody arg3, Writer writer) throws IOException, TemplateModelException {
        this.tagString = this.tagName(viewContext, component);
        if (this.tagString == null)
            throw new NullPointerException("tagName is null.");
        writer.write("<" + this.tagString);
        /*-------------------------------------------------*/
        //Core Atts
        /*-------------------------------------------------*/
        writer.write(" id='" + component.getClientID(viewContext) + "'");
        writer.write(" comID='" + component.getComponentID() + "'");
        writer.write(" sMode='" + component.getComponentType() + "'");
        writer.write(" cMode='" + this.getClientType() + "'");
        writer.write(" comPath='" + component.getComponentPath() + "'");
        if (this.isSaveState(viewContext, component) == true) {
            String jsonData = JSON.toJSONString(component.saveState());
            String base64 = CommonCodeUtil.Base64.base64Encode(jsonData);
            writer.write(" uiState='" + base64 + "'");
        }
        /*-------------------------------------------------*/
        //Atts
        /*-------------------------------------------------*/
        Map<String, Object> atts = this.tagAttributes(viewContext, component);
        //class
        if (atts.containsKey("class") == false)
            atts.put("class", "WebUI_" + this.getClientType());
        //put atts
        for (Entry<String, Object> ent : atts.entrySet()) {
            Object value = ent.getValue();
            if (value == null)
                continue;
            String var = null;
            if (value instanceof AbstractValueHolder == true)
                var = ((AbstractValueHolder) value).valueTo(String.class);
            else
                var = String.valueOf(value);
            if (var != null)
                writer.write(" " + ent.getKey() + "=\"" + var + "\"");
        }
        writer.write(">");
    };
    @Override
    public void render(ViewContext viewContext, T component, TemplateBody arg3, Writer writer) throws IOException, TemplateException {
        if (arg3 != null)
            arg3.render(writer);
    }
    @Override
    public void endRender(ViewContext viewContext, T component, TemplateBody arg3, Writer writer) throws IOException, TemplateModelException {
        writer.write("</" + this.tagString + ">");
    };
    /**λ�ڱ�ǩ�ϵ�������������������������������ͻ�����������ԡ�*/
    public Map<String, Object> tagAttributes(ViewContext viewContext, T component) {
        HashMap<String, Object> mineState = new HashMap<String, Object>();
        Map<String, AbstractValueHolder> comProp = component.getPropertys();
        for (String propName : comProp.keySet()) {
            //a.��ȡ���Ե�get/set����
            Method rm = BeanUtil.getReadMethod(propName, component.getClass());
            Method wm = BeanUtil.getWriteMethod(propName, component.getClass());
            //b.ֻ��û�ж���get/set���������ԲŻᱻѡ�С�
            if (rm == null && wm == null) {
                AbstractValueHolder vh = comProp.get(propName);
                mineState.put(propName.toLowerCase(), vh.value());
            }
        }
        AbstractValueHolder avh = comProp.get("class");
        if (avh != null)
            mineState.put("class", avh);
        return mineState;
    };
    /**�Ƿ񱣴�״̬��*/
    public boolean isSaveState(ViewContext viewContext, T component) {
        return true;
    }
}