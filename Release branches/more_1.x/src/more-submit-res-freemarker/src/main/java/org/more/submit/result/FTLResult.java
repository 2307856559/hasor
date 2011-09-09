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
import org.more.util.attribute.ParentDecorator;
import freemarker.template.Configuration;
import freemarker.template.Template;
/**
 * ftl
 * @version : 2011-7-25
 * @author ������ (zyc@byshell.org)
 */
public class FTLResult extends DefaultResultImpl<Object> implements IAttribute<Object> {
    private Writer             writer       = null;
    private String             templatePath = null;
    private IAttribute<Object> parentAtt    = null;
    private AttBase<Object>    thisAtt      = new AttBase<Object>();
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
    public Configuration applyConfiguration(Configuration configuration) {
        return configuration;
    };
    /**����{@link Template}�Ա����ṩ���߼��Ĺ��ܡ�*/
    public Template applyTemplate(Template template) {
        return template;
    }
    /**��ȡģ��ִ�н�������ַ��*/
    public Writer getWriter() {
        return this.writer;
    };
    //
    /**���ø�{@link IAttribute}�ӿڵ����Լ��������Լ��������{@link IAttribute}�ӿ�set,remote,clean����ֻ����Ե�ǰ������Լ�������Ӱ�쵽�����Լ���*/
    public void setParentAtt(IAttribute<Object> parent) {
        this.parentAtt = new ParentDecorator<Object>(this.thisAtt, parent);
    };
    private IAttribute<Object> getParent() {
        if (this.parentAtt == null)
            return this.thisAtt;
        return this.parentAtt;
    };
    public boolean contains(String name) {
        return this.getParent().contains(name);
    };
    public Object getAttribute(String name) {
        return this.getParent().getAttribute(name);
    };
    public String[] getAttributeNames() {
        return this.getParent().getAttributeNames();
    };
    public Map<String, Object> toMap() {
        return this.getParent().toMap();
    };
    public void setAttribute(String name, Object value) {
        this.thisAtt.setAttribute(name, value);
    };
    public void removeAttribute(String name) {
        this.thisAtt.removeAttribute(name);
    };
    public void clearAttribute() {
        this.thisAtt.clearAttribute();
    };
}