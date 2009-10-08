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
import org.more.NoDefinitionException;
import org.more.ReadOnlyException;
/**
 * ���Ա���װ��������װ����ʵ����IKeepAttribute�ӿڵĹ��ܡ�
 * Date : 2009-4-30
 * @author ������
 */
public class KeepAttDecorator extends AbstractAttDecorator implements IKeepAttribute {
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
    /**
     * ����һ������װ��������װ��������Ҫ�������������Զ�����滻���Ե�֧�֡��滻��������IExtAttribute�ӿڶ��塣
     * �÷���������Ĭ�ϲ���IExtAttribute.ReplaceMode_Replace��
     * @param source Ҫװ�ε�Ŀ�����Զ���
     */
    public KeepAttDecorator(IAttribute source) {
        super(source);
        this.map = new HashMap<String, KeepAttDecorator.KeepAttStruct>();
    }
    @Override
    public void removeAttribute(String name) throws UnsupportedOperationException {
        if (this.getSource().contains(name) == true)
            if (this.map.get(name).master == true)
                throw new UnsupportedOperationException("����ɾ������ " + name + " �������Ծ߱��������ԡ�");
        this.getSource().removeAttribute(name);
        this.map.remove(name);
    }
    @Override
    public void setAttribute(String name, Object value) throws ReadOnlyException {
        if (this.getSource().contains(name) == true)
            if (this.map.get(name).keep == true)
                throw new ReadOnlyException("���� " + name + " �߱��������Բ��ܽ����µ�����ֵ��");
        this.getSource().setAttribute(name, value);
        this.map.put(name, new KeepAttStruct());
    }
    @Override
    public boolean isKeep(String attName) {
        return (this.getSource().contains(attName) == false) ? false : this.map.get(attName).keep;
    }
    @Override
    public boolean isMaster(String attName) {
        return (this.getSource().contains(attName) == false) ? false : this.map.get(attName).master;
    }
    @Override
    public void setKeep(String attName, boolean isKeep) throws NoDefinitionException {
        if (this.getSource().contains(attName) == false)
            throw new NoDefinitionException("���ܸ������ڵ����� " + attName + " ���ñ�������");
        else
            this.map.get(attName).keep = isKeep;
    }
    @Override
    public void setMaster(String attName, boolean isMaster) throws NoDefinitionException {
        if (this.getSource().contains(attName) == false)
            throw new NoDefinitionException("���ܸ������ڵ����� " + attName + " ���ñ�������");
        else
            this.map.get(attName).master = isMaster;
    }
}
