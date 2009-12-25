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
package org.more.submit.support;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;
/**
 * ҳ��Ԥ�����ǩ�Ĳ�����ǩ���ò�����ǩ��������ҳ��Ԥ�����ǩ��ActionManager���ݻ�������������
 * Date : 2009-8-10
 * @author ������
 */
public class ActionParamTag extends BodyTagSupport {
    private static final long serialVersionUID = 5847549188323147281L;
    private String            key              = null;
    private Object            value            = null;
    @Override
    public int doStartTag() throws JspException {
        ActionTag aTag = (ActionTag) this.getParent();
        aTag.params.put(key, value);
        return Tag.SKIP_BODY;
    }
    @Override
    public int doEndTag() {
        return Tag.EVAL_PAGE;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public Object getValue() {
        return value;
    }
    public void setValue(Object value) {
        this.value = value;
    }
}