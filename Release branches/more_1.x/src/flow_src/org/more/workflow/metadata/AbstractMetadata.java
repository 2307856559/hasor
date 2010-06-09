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
import org.more.core.ognl.OgnlException;
import org.more.util.attribute.AttBase;
import org.more.util.attribute.IAttribute;
import org.more.workflow.context.ELContext;
import org.more.workflow.context.RunContext;
import org.more.workflow.el.PropertyBinding;
import org.more.workflow.el.PropertyBindingHolder;
import org.more.workflow.event.AbstractHolderListener;
import org.more.workflow.event.object.UpdataModeEvnet;
/**
 * �����Ԫ��Ϣ�࣬�κ�Ԫ��Ϣ�඼��Ҫ�̳и��ࡣ�����ṩ��Ԫ��Ϣ����ģ�͵Ľӿڡ����������ͨ����д��ط�������д����ʵ�֡�
 * AbstractMetadata�ṩupdataMode��newInstance�������������ºʹ���Ԫ��Ϣ�������ģ�͡�<br/>
 * ���������ͨ��{@link PropertyBindingHolder}�ӿ�������һ��ognl���ʽ��������{@link PropertyBinding}��
 * ͨ��{@link PropertyBinding}������ʽִ��ognl���ʽ��ģ�͵�ioc������Ҫ�����˽ӿ�����ɡ�
 * ����AbstractMetadata�����ṩ��ע���Ƴ�����Ԫ��Ϣ�ķ�����ͨ����Щ���������޸�AbstractMetadataԪ��Ϣ�����е����ԡ�
 * Date : 2010-5-16
 * @author ������
 */
public abstract class AbstractMetadata extends AbstractHolderListener implements IAttribute, ModeUpdataHolder, PropertyBindingHolder {
    //========================================================================================Field
    private String                              metadataID           = null;                                   //Ԫ��Ϣ����ID
    private final Map<String, PropertyMetadata> propertyMap          = new HashMap<String, PropertyMetadata>(); //�������ڸ���ģ��ʱʹ�õ�El���Լ���
    private final Map<String, PropertyBinding>  propertyBindingCache = new HashMap<String, PropertyBinding>(); //���ڱ����¼������Զ���
    private final AttBase                       attributeMap         = new AttBase();                          //���ڱ����¼������Զ���
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
    /**��������EL��ȡ��{@link PropertyBinding}���������ͼ��ȡ�����Բ������򷵻�ֵΪnull��*/
    @Override
    public PropertyBinding getPropertyBinding(String propertyEL, Object object) throws OgnlException {
        PropertyBinding pb = null;
        if (this.propertyBindingCache.containsKey(propertyEL) == false) {
            pb = new PropertyBinding(propertyEL, object);
            this.propertyBindingCache.put(propertyEL, pb);
        } else
            pb = this.propertyBindingCache.get(propertyEL);
        return pb;
    };
    /**����ģ����Ϣ���ڸ���ģ����ϢʱAbstractMetadata�����ε���ÿ��PropertyMetadataԪ��Ϣ��updataMode�ӿڣ�����÷���������UpdataModeEvnet�¼���*/
    @Override
    public void updataMode(Object mode, ELContext elContext) throws Throwable {
        UpdataModeEvnet event = new UpdataModeEvnet(mode, this);
        this.event(event.getEventPhase()[0]);//before
        for (PropertyMetadata item : this.propertyMap.values())
            item.updataMode(mode, elContext);
        this.event(event.getEventPhase()[1]);//after
    };
    /**����Ԫ��Ϣ�������Ķ�������������˴����ľ������Ͷ����û�����ͨ����չ�÷������Զ�����󴴽����̡� */
    public abstract Object newInstance(RunContext runContext) throws Throwable;
    /**
     * ���һ������Ԫ��Ϣ���÷�������ָ����ǰform��һ���������ƣ���ͬʱָ����һ�����ʽ��
     * ������{@link ModeUpdataHolder}�ӿڷ���ʱFormMetadata�����ע��������б��Ԥ����ģ��ִ�и��²�����
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
     * ���һ������Ԫ��Ϣ���÷�������ָ����ǰform��һ���������ƣ���ͬʱָ����һ�����ʽ��������
     * {@link ModeUpdataHolder}�ӿڷ���ʱFormMetadata�����ע��������б��Ԥ����ģ��ִ�и��²�����
     * ע�ⲻ���ظ�ע��ͬһ�����ԣ����������{@link RepeateException}�쳣��
     * @param propertyItem Ҫ��ӵ����Զ�������Զ�Ӧ��{@link PropertyBinding ���ʽ}��
     */
    public void addProperty(PropertyMetadata propertyItem) {
        if (this.propertyMap.containsKey(propertyItem.getMetadataID()) == true)
            throw new RepeateException("����ע���ظ�������Ԫ��Ϣ�� ");
        this.propertyMap.put(propertyItem.getMetadataID(), propertyItem);
    };
    /**ȡ��һ������Ԫ��Ϣ����ӣ�����ֻ��Ҫ�������������ɣ�����FormItemMetadata����������������metadataID��*/
    public void removeProperty(String propertyName) {
        if (this.propertyMap.containsKey(propertyName) == true)
            this.propertyMap.remove(propertyName);
    };
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