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
package org.more.util.attribute;
import java.util.ArrayList;
import java.util.Collections;
/**
 * ParentDecorator����װ��������װ�����������ǻ���ԭ��{@link IAttribute}���Լ�֮������һ�㸸���Լ���
 * ��������������ͬ������ʱ�µ����Լ�����Ӱ�츸���Լ���
 * @version 2010-9-11
 * @author ������ (zyc@byshell.org)
 */
public class ParentDecorator extends AbstractAttDecorator {
    //========================================================================================Field
    private IAttribute parent = null;
    //==================================================================================Constructor
    /**
     * ����һ��ParentDecorator����װ������ʹ�ø÷���������װ�����丸���Լ�����Դ(source)��
     * @param source ָ��װ����Ҫװ�ε����Լ���
     * @throws NullPointerException ���source����Ϊ������������쳣��
     */
    public ParentDecorator(IAttribute source) throws NullPointerException {
        super(source);
    }
    /**
     * ����һ��ParentDecorator����װ������ʹ�ø÷���������װ�����丸���Լ�����Դ(source)��
     * @param source ָ��װ����Ҫװ�ε����Լ���
     * @param parent ��ʹ�õĸ����Լ���
     * @throws NullPointerException ���source����Ϊ������������쳣��
     */
    public ParentDecorator(IAttribute source, IAttribute parent) throws NullPointerException {
        super(source);
        this.parent = parent;
    }
    //==========================================================================================JOB
    /**��ȡ��ǰ���Լ��еĸ����Լ������ʹ�õ���ParentDecorator(IAttribute)���췽��������ParentDecorator�������֧��������ֵ��getSource()��������ֵһ����*/
    public IAttribute getParent() {
        return this.parent;
    }
    /**�滻�����Լ���*/
    protected void setParent(IAttribute parent) {
        this.parent = parent;
    }
    /**���ȴӵ�ǰ���Լ���Ѱ�ң�����ҵ�����true�����򵽸����Լ���ȥ�Ҳ��ҷ��ز��ҽ����*/
    public boolean contains(String name) {
        if (super.contains(name) == false)
            return this.parent.contains(name);
        return true;
    }
    /**���ȴӵ�ǰ���Լ���Ѱ�ң�����ҵ�����������󡣷��򵽸����Լ���ȥ�Ҳ��ҷ��ز��ҽ����*/
    public Object getAttribute(String name) {
        Object obj = super.getAttribute(name);
        if (obj == null)
            return this.parent.getAttribute(name);
        return obj;
    }
    /**���ص�ǰ���Լ��Լ������Լ��п��Է��ʵ��������������������ǰ���Լ��ж���������ڸ����Լ����ظ�����÷���ֻ�ᱣ��һ���������ơ�*/
    public String[] getAttributeNames() {
        ArrayList<String> ns = new ArrayList<String>();
        Collections.addAll(ns, super.getAttributeNames());
        for (String n : this.parent.getAttributeNames())
            if (ns.contains(n) == false)
                ns.add(n);
        //
        String[] array = new String[ns.size()];
        ns.toArray(array);
        return array;
    }
}