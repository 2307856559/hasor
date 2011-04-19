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
package org.more.hypha.define;
import java.util.Map;
import org.more.util.attribute.AttBase;
import org.more.util.attribute.IAttribute;
/**
 * ����������������Ϣ��Ҫ���ɵĸ��࣬�����ṩ��{@link IAttribute}�ӿ�ʵ�֡�
 * ����ζ�ſ�������Щ������Ϣ�ϸ����Զ������ԡ�{@link #getAppendAttribute()}�������ṩ��һ���ڲ������Լ���
 * ��hypha�в���ֱ�ӷ��ʵ�������Լ��ϣ�����Ϊ����ṹ����չ��Ŀ�ġ�
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractDefine<T> implements IAttribute {
    private IAttribute attribute = null; //���ԣ�Ϊ���ṩIAttribute�ӿڹ��ܡ�
    private IAttribute flash     = null; //��������
    //
    /**��ȡ���ڴ����ʱ�������Եĸ��ӵ����Լ���*/
    public IAttribute getFlash() {
        if (this.flash == null)
            this.flash = new AttBase();
        return this.flash;
    };
    protected IAttribute getAttribute() {
        if (this.attribute == null)
            this.attribute = new AttBase();
        return this.attribute;
    }
    public boolean contains(String name) {
        return this.getAttribute().contains(name);
    };
    public void setAttribute(String name, Object value) {
        this.getAttribute().setAttribute(name, value);
    };
    public Object getAttribute(String name) {
        return this.getAttribute().getAttribute(name);
    };
    public void removeAttribute(String name) {
        this.getAttribute().removeAttribute(name);
    };
    public String[] getAttributeNames() {
        return this.getAttribute().getAttributeNames();
    };
    public void clearAttribute() {
        this.getAttribute().clearAttribute();
    }
    public Map<String, Object> toMap() {
        return this.getAttribute().toMap();
    };
}