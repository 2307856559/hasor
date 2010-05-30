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
package org.more.workflow.el;
import org.more.core.ognl.NoSuchPropertyException;
import org.more.core.ognl.Node;
import org.more.core.ognl.Ognl;
import org.more.core.ognl.OgnlContext;
import org.more.core.ognl.OgnlException;
import org.more.workflow.event.AbstractHolderListener;
import org.more.workflow.event.ListenerHolder;
import org.more.workflow.event.object.GetValueEvent;
import org.more.workflow.event.object.SetValueEvent;
import org.more.workflow.form.FormMetadata;
/**
 * ���԰�����{@link FormMetadata}ͨ�������ȡ���������ԡ�����getValue������setValue����������ʱ
 * ������GetValueEvent��SetValueEvent�¼�����ʾ�������PropertyBinding������ע���¼������������������
 * ����������������Ե�����RW�¼���
 * Date : 2010-5-20
 * @author ������
 */
public class PropertyBinding extends AbstractHolderListener implements ListenerHolder {
    //========================================================================================Field 
    private String      propertyEL   = null;             //����EL
    private Node        propertyNode = null;             //��������Զ�ȡ��
    /**�������ͨ�����ֶ����ı����ԵĿɶ�д״̬������Ϳո��ֶε�setValue����isReadOnly������ʱ�������³�ʼ������*/
    protected Boolean   readOnly     = null;
    private OgnlContext ognlContext  = new OgnlContext();
    private Object      object       = null;             //���������Ķ���
    //==================================================================================Constructor
    public PropertyBinding(String propertyEL, Object object) throws OgnlException {
        this.propertyEL = propertyEL;
        this.propertyNode = (Node) Ognl.parseExpression(propertyEL);//��������
        this.object = object;
    };
    //==========================================================================================Job
    /**��������EL�����һ�ȡ����֮�������ֵ��*/
    public synchronized Object getValue() throws OgnlException {
        Object result = this.propertyNode.getValue(this.ognlContext, this.object);
        GetValueEvent event = new GetValueEvent(this, result);
        this.event(event.getEventPhase()[0]);
        return event.getResult();
    };
    /**
     * ��������EL����һ���µ�ֵ�滻ԭ������ֵ���÷������������۳ɹ���񶼽�
     * ����ֻ���������������д��ɹ���isReadOnly��������Ϊfalse�����򷵻�true��
     */
    public synchronized void setValue(Object value) throws OgnlException {
        try {
            SetValueEvent event = new SetValueEvent(this, value);
            this.event(event.getEventPhase()[0]);
            this.propertyNode.setValue(this.ognlContext, this.object, event.getNewValue());
            this.changeReadOnly(false);
        } catch (OgnlException e) {
            this.changeReadOnly(true);
            throw e;
        }
    };
    private void changeReadOnly(boolean readOnly) {
        if (this.readOnly == null)
            this.readOnly = readOnly;
    };
    /**��ȡ���ڱ�ʾ���Ե�EL���ʽ��*/
    public String getPropertyEL() {
        return this.propertyEL;
    };
    /**
     * ���������Ƿ�Ϊֻ�����ԣ�PropertyBinding��ͨ������getValue��setValue�����������Ƿ�Ϊֻ�����ԡ�
     * �����ֻ��������setValue����������NoSuchPropertyException�쳣��isReadOnly��������ͨ������쳣��
     * �϶��������Ƿ�Ϊֻ����ֵ��ע�������isReadOnly��������ֻ������һ��PropertyBinding�Ὣ���Խ������������
     * ������ù�setValue������isReadOnly����Ҳ�ᱻ˳��������,�ѱ����������������Բ��ԡ�
     */
    public boolean isReadOnly() throws OgnlException {
        if (this.readOnly != null)
            return readOnly;
        try {
            this.setValue(this.getValue());
            return (this.readOnly = false);
        } catch (NoSuchPropertyException e) {
            return (this.readOnly = true);
        }
    };
};