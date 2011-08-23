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
package org.more.submit.result;
import java.io.Writer;
import java.util.Map;
import org.more.submit.impl.DefaultResultImpl;
import org.more.util.attribute.AttBase;
import org.more.util.attribute.IAttribute;
import freemarker.template.Configuration;
import freemarker.template.Template;
/**
 * ftl
 * @version : 2011-7-25
 * @author ������ (zyc@byshell.org)
 */
public class FTLResult extends DefaultResultImpl<Object> implements IAttribute {
    private Writer  writer       = null;
    private String  templatePath = null;
    private AttBase att          = new AttBase();
    //
    public FTLResult(String templatePath, Writer writer, Object returnValue) {
        super("ftl", returnValue);
        this.writer = writer;
        this.templatePath = templatePath;
    }
    public FTLResult(String templatePath, Writer writer) {
        super("ftl", templatePath);
        this.writer = writer;
    }
    public FTLResult(String templatePath) {
        super("ftl", templatePath);
    }
    /**��ȡҪִ�е�ģ�塣*/
    public String getTemplatePath() {
        return this.templatePath;
    }
    /**����ʹ��{@link FreeMarkerConfig}���{@link Configuration}��������֮�⣬Ҳ����ͨ����д�÷����������á������÷������ûᱻ���ִ�С�*/
    public void applyConfiguration(Configuration configuration) {};
    /**����{@link Template}�Ա����ṩ���߼��Ĺ��ܡ�*/
    public void applyTemplate(Template template) {}
    /**��ȡģ��ִ�н�������ַ��*/
    public Writer getWriter() {
        return this.writer;
    };
    //
    public boolean contains(String name) {
        return this.att.contains(name);
    };
    public void setAttribute(String name, Object value) {
        this.att.setAttribute(name, value);
    };
    public Object getAttribute(String name) {
        return this.att.getAttribute(name);
    };
    public void removeAttribute(String name) {
        this.att.removeAttribute(name);
    };
    public String[] getAttributeNames() {
        return this.att.getAttributeNames();
    };
    public void clearAttribute() {
        this.att.clearAttribute();
    };
    public Map<String, Object> toMap() {
        return this.att.toMap();
    }
}