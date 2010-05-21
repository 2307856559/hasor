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
import org.more.core.ognl.OgnlException;
import org.more.util.attribute.AttBase;
import org.more.util.attribute.IAttribute;
import org.more.workflow.context.ELContext;
import org.more.workflow.el.PropertyBinding;
import org.more.workflow.el.PropertyBindingHolder;
import org.more.workflow.event.AbstractHolderListener;
import org.more.workflow.event.EventType;
import org.more.workflow.event.object.UpdataModeEvnet;
/**
 * ����Ԫ��Ϣ����ģ�͵�Ԫ��Ϣ������Ҫ���ɸ��ࡣ�����ṩ�˸���ģ�͵Ľӿ��������������ģ�͵ľ���ʵ�֡�
 * �������ͨ��Ԫ��Ϣʵ�ֵ�{@link PropertyBindingHolder}�ӿ�������һ��ognl���ʽ��������{@link PropertyBinding}��
 * ͨ��{@link PropertyBinding}������ʽִ��ognl���ʽ��ģ�͵�ioc������Ҫ�����˽ӿ�����ɡ�
 * Date : 2010-5-16
 * @author ������
 */
public abstract class AbstractMetadata extends AbstractHolderListener implements IAttribute, ModeUpdataHolder, PropertyBindingHolder {
    //========================================================================================Field
    private String                             metadataID   = null;                                  //Ԫ��Ϣ����ID
    private final Map<String, PropertyBinding> propertyMap  = new HashMap<String, PropertyBinding>(); //���ڱ����¼������Զ���
    private final AttBase                      attributeMap = new AttBase();                         //���ڱ����¼������Զ���
    //==================================================================================Constructor
    /**����һ��Ԫ��Ϣ���󣬲���������Ԫ��Ϣ��ID�����id����ͨ��getMetadataID������ȡ��*/
    public AbstractMetadata(String metadataID) {
        this.metadataID = metadataID;
    };
    //==========================================================================================Job
    /**��ȡԪ��Ϣ����ID����id���ڴ���AbstractMetadata����ʱָ���ģ����Ҳ����޸ġ�*/
    public String getMetadataID() {
        return this.metadataID;
    };
    @Override
    public PropertyBinding getPropertyBinding(String propertyEL, Object object) throws OgnlException {
        PropertyBinding pb = null;
        if (this.propertyMap.containsKey(propertyEL) == false) {
            pb = new PropertyBinding(propertyEL, object);
            this.propertyMap.put(propertyEL, pb);
        } else
            pb = this.propertyMap.get(propertyEL);
        return pb;
    };
    @Override
    public void updataMode(Object mode, ELContext elContext) throws Throwable {
        this.event(new UpdataModeEvnet(EventType.UpdataModeEvnet, mode));
    };
    /**����Ԫ��Ϣ�������Ķ�������������˴����ľ������Ͷ����û�����ͨ����չ�÷������Զ�����󴴽����̡� */
    public abstract Object newInstance(ELContext elContext) throws Throwable;
    //==========================================================================================Job
    @Override
    public void clearAttribute() {
        this.attributeMap.clearAttribute();
    };
    @Override
    public boolean contains(String name) {
        return this.attributeMap.contains(name);
    };
    @Override
    public Object getAttribute(String name) {
        return this.attributeMap.getAttribute(name);
    };
    @Override
    public String[] getAttributeNames() {
        return this.attributeMap.getAttributeNames();
    };
    @Override
    public void removeAttribute(String name) {
        this.attributeMap.removeAttribute(name);
    };
    @Override
    public void setAttribute(String name, Object value) {
        this.attributeMap.setAttribute(name, value);
    };
};