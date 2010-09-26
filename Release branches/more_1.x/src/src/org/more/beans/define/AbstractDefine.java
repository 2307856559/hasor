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
package org.more.beans.define;
import java.util.HashMap;
import java.util.Map;
import org.more.util.attribute.AttBase;
import org.more.util.attribute.IAttribute;
/**
 * �ýӿ����ڶ���һ��beans�齨�е�bean������
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractDefine implements IAttribute, ExpandDefineSet {
    private IAttribute                attribute    = new AttBase(); //����
    private Map<String, ExpandDefine> expandDefine = null;         //��չ��������
    /**������չDefine����������*/
    public ExpandDefine getExpandDefine(String name) {
        return null;
    }
    /**����һ����չ����*/
    public void addExpandDefine(String name, ExpandDefine extDefine) {
        if (this.expandDefine == null)
            this.expandDefine = new HashMap<String, ExpandDefine>();
        this.expandDefine.put(name, extDefine);
    }
    //-------------------------------------------------------------
    public boolean contains(String name) {
        return this.attribute.contains(name);
    };
    public void setAttribute(String name, Object value) {
        this.attribute.setAttribute(name, value);
    };
    public Object getAttribute(String name) {
        return this.attribute.getAttribute(name);
    };
    public void removeAttribute(String name) {
        this.attribute.removeAttribute(name);
    };
    public String[] getAttributeNames() {
        return this.attribute.getAttributeNames();
    };
    public void clearAttribute() {
        this.attribute.clearAttribute();
    };
}