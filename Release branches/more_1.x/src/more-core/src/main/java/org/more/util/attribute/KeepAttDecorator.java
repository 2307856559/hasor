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
import java.util.HashMap;
import java.util.Map;
import org.more.core.error.DefineException;
import org.more.core.error.MoreStateException;
/**
*    �������Խӿڡ��ýӿ���չ��Attribute�ӿڣ����ҽ�������Ա��������⡣ʹ�������ڱ�ɾ������
* ���滻ʱ����ȷ������Ϊ��ͨ���ýӿڿ��Խ���������Ϊ�������Ի�������ԡ�
*    �������ԣ�����������ָ�����óɱ��ֵ������Ƿ���Ը���������ֵ����һ����ExtAttribute�ӿڵ�
* ReplaceMode_Throw���Ժ��񡣲�ͬ����ExtAttribute�ӿ�������������ԣ���KeepAttribute�ӿ�
* �����һ�����ԡ������ó�Ϊ���ֵ����Կ���ͨ��removeAttribute����ɾ��������ɾ��һ��ӵ�б���
* ���Ե�����ʱ����ͬʱɾ���䱣�����ԡ�
*    �������ԣ�����������ָ�����Կ��Ա��������õ��ǲ����Ա�ɾ�������ñ������Կ��Ա�֤ĳЩ�����
* ϵͳ�����ܹ����ʵ�ĳЩ���ԣ�������Щ���Ա��벻�ܲ����ڡ�
*    ��ʾ���������Ժͱ������Կ���ͬʱ���õ�һ�������ϡ�
* @version 2009-12-3
* @author ������ (zyc@byshell.org)
*/
public class KeepAttDecorator<T> extends AbstractAttDecorator<T> {
    //========================================================================================Field
    /** �����������Ե�Map */
    private Map<String, KeepAttDecorator.KeepAttStruct> map = null;
    /**
     * ���������ڲ��࣬���������ڱ��������������ݡ�
     * Date : 2009-4-29
     * @author ������
     */
    private static class KeepAttStruct {
        /**��������*/
        private boolean master = false;
        /**��������*/
        private boolean keep   = false;
    }
    //==================================================================================Constructor
    /**
     * ����һ������װ��������װ��������Ҫ�������������Զ�����滻���Ե�֧�֡��滻��������IExtAttribute�ӿڶ��塣
     * �÷���������Ĭ�ϲ���IExtAttribute.ReplaceMode_Replace��
     * @param source Ҫװ�ε�Ŀ�����Զ���
     */
    public KeepAttDecorator(IAttribute<T> source) {
        super(source);
        this.map = new HashMap<String, KeepAttDecorator.KeepAttStruct>();
    }
    //==========================================================================================Job
    /**
     * �������ԣ���������Ѿ�������Ϊ���������������{@link MoreStateException}�쳣��
     * @param name Ҫ�������������
     * @param value Ҫ���������ֵ��
     */
    public void setAttribute(String name, T value) {
        if (this.getSource().contains(name) == true)
            if (this.map.get(name).keep == true)
                throw new MoreStateException("���� " + name + " �߱��������Բ��ܽ����µ�����ֵ��");
        this.getSource().setAttribute(name, value);
        this.map.put(name, new KeepAttStruct());
    }
    /**
     * ���������Լ�����ɾ��ָ�����ԡ������ɾ��������ӵ�б��������������MustException�쳣��
     * @param name Ҫɾ�����������ơ�
     * @throws UnsupportedOperationException �����ɾ��������ӵ�б����������������쳣��
     */
    public void removeAttribute(String name) throws UnsupportedOperationException {
        if (this.getSource().contains(name) == true)
            if (this.map.get(name).master == true)
                throw new UnsupportedOperationException("����ɾ������ " + name + " �������Ծ߱��������ԡ�");
        this.getSource().removeAttribute(name);
        this.map.remove(name);
    }
    /**
     * ���������Ƿ��Ǳ��ֵġ������ͼ��һ�������ڵ��������ñ�������ʱ����NoDefinitionException�쳣��
     * @param attName Ҫ���ñ������Ե���������
     * @param isKeep �������ֵΪtrue���ʾ�����䱣�����ԣ������ȡ���䱣�����ԡ�
     * @throws DefineException δ�����쳣�������ͼ��һ�������ڵ��������ñ�������ʱ������
     */
    public void setKeep(String attName, boolean isKeep) throws DefineException {
        if (this.getSource().contains(attName) == false)
            throw new DefineException("���ܸ������ڵ����� " + attName + " ���ñ�������");
        else
            this.map.get(attName).keep = isKeep;
    }
    /**
     * ����ĳ�������Ƿ�ӵ�б������ԡ��������һ�������ڵ����Ը÷�����ʼ�շ���false��
     * @param attName �����Ե���������
     * @return ����ĳ�������Ƿ�ӵ�б������ԡ�
     */
    public boolean isKeep(String attName) {
        return (this.getSource().contains(attName) == false) ? false : this.map.get(attName).keep;
    }
    /**
     * ���������Ƿ��Ǳ���ġ������ͼ��һ�������ڵ��������ñ�������ʱ����NoDefinitionException�쳣��
     * @param attName Ҫ���ñ������Ե���������
     * @param isMaster �������ֵΪtrue���ʾ������������ԣ������ȡ����������ԡ�
     * @throws DefineException δ�����쳣�������ͼ��һ�������ڵ��������ñ�������ʱ������
     */
    public void setMaster(String attName, boolean isMaster) throws DefineException {
        if (this.getSource().contains(attName) == false)
            throw new DefineException("���ܸ������ڵ����� " + attName + " ���ñ�������");
        else
            this.map.get(attName).master = isMaster;
    }
    /**
     * ����ĳ�������Ƿ�ӵ�б������ԡ��������һ�������ڵ����Ը÷�����ʼ�շ���false��
     * @param attName �����Ե���������
     * @return ����ĳ�������Ƿ�ӵ�б������ԡ�
     */
    public boolean isMaster(String attName) {
        return (this.getSource().contains(attName) == false) ? false : this.map.get(attName).master;
    }
}