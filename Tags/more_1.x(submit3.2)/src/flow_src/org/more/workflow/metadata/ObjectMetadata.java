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
package org.more.workflow.metadata;
import java.util.HashMap;
import java.util.Map;
import org.more.RepeateException;
import org.more.workflow.el.PropertyBinding;
/**
 * ������ģ�Ͷ����Ԫ��Ϣ��ͨ��������Ա���ģ�͵�����map���ϡ�
 * Date : 2010-6-15
 * @author ������
 */
public class ObjectMetadata extends AbstractMetadata {
    private final Map<String, PropertyMetadata> propertyMap = new HashMap<String, PropertyMetadata>(); //�������ڸ���ģ��ʱʹ�õ�El���Լ���
    /**����һ��Ԫ��Ϣ���󣬲���������Ԫ��Ϣ��ID�����id����ͨ��getMetadataID������ȡ��*/
    public ObjectMetadata(String metadataID) {
        super(metadataID);
    };
    /**
    * ���һ������Ԫ��Ϣ���÷�������ָ����ǰobject��һ���������ƣ���ͬʱָ����һ�����ʽ��
    * ������{@link ModeUpdataHolder}�ӿڷ���ʱ{@link AbstractStateHolder}�����ע��������б��Ԥ����ģ��ִ�и��²�����
    * ע�ⲻ���ظ�ע��ͬһ�����ԣ����������{@link RepeateException}�쳣��
    * @param propertyName Ҫ��ӵ���������
    * @param expressionString �����Զ�Ӧ��{@link PropertyBinding ���ʽ}��
    */
    public void addProperty(String propertyName, String expressionString) {
        if (this.propertyMap.containsKey(propertyName) == true)
            throw new RepeateException("����ע���ظ�������Ԫ��Ϣ�� ");
        this.addProperty(new PropertyMetadata(propertyName, expressionString));
    };
    /**
     * ���һ������Ԫ��Ϣ���÷�������ָ����ǰobject��һ���������ƣ���ͬʱָ����һ�����ʽ��������
     * {@link ModeUpdataHolder}�ӿڷ���ʱ{@link AbstractStateHolder}�����ע��������б��Ԥ����ģ��ִ�и��²�����
     * ע�ⲻ���ظ�ע��ͬһ�����ԣ����������{@link RepeateException}�쳣��
     * @param propertyItem Ҫ��ӵ����Զ�������Զ�Ӧ��{@link PropertyBinding ���ʽ}��
     */
    public void addProperty(PropertyMetadata propertyItem) {
        if (this.propertyMap.containsKey(propertyItem.getMetadataID()) == true)
            throw new RepeateException("����ע���ظ�������Ԫ��Ϣ�� ");
        this.propertyMap.put(propertyItem.getMetadataID(), propertyItem);
    };
    /**ȡ��һ������Ԫ��Ϣ����ӣ�����ֻ��Ҫ����������el���ɡ�*/
    public void removeProperty(String propertyName) {
        if (this.propertyMap.containsKey(propertyName) == true)
            this.propertyMap.remove(propertyName);
    };
    /** ��ȡ�������������Ԫ��Ϣ���� */
    public PropertyMetadata[] getPropertys() {
        PropertyMetadata[] pm = new PropertyMetadata[this.propertyMap.size()];
        pm = this.propertyMap.values().toArray(pm);
        return pm;
    };
    /** ��ȡĳһ���Ե�Ԫ��Ϣ���� */
    public PropertyMetadata getProperty(String name) {
        return this.propertyMap.get(name);
    };
};