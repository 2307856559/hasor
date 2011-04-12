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
package org.more.hypha;
import org.more.core.ognl.NoSuchPropertyException;
import org.more.core.ognl.Node;
import org.more.core.ognl.Ognl;
import org.more.core.ognl.OgnlContext;
import org.more.core.ognl.OgnlException;
/**
 * ���԰��������ͨ��������Զ����Խ��ж�д������
 * Date : 2011-4-11
 * @author ������ (zyc@byshell.org)
 */
public class PropertyBinding {
    private String      propertyEL   = null;             //����EL
    private Node        propertyNode = null;             //��������Զ�ȡ��
    /**�������ͨ�����ֶ����ı����ԵĿɶ�д״̬������Ϳո��ֶε�setValue����isReadOnly������ʱ�������³�ʼ������*/
    protected Boolean   readOnly     = null;
    private OgnlContext ognlContext  = new OgnlContext();
    private Object      object       = null;             //���������Ķ���
    public PropertyBinding(String propertyEL, Object object) throws OgnlException {
        this.propertyEL = propertyEL;
        this.propertyNode = (Node) Ognl.parseExpression(propertyEL);//��������
        this.object = object;
    };
    /**��������EL�����һ�ȡ����֮�������ֵ��*/
    public synchronized Object getValue() throws OgnlException {
        return this.propertyNode.getValue(this.ognlContext, this.object);
    };
    /**
     * ��������EL����һ���µ�ֵ�滻ԭ������ֵ���÷������������۳ɹ���񶼽�
     * ����ֻ���������������д��ɹ���isReadOnly��������Ϊfalse�����򷵻�true��
     */
    public synchronized void setValue(Object value) throws OgnlException {
        try {
            this.propertyNode.setValue(this.ognlContext, this.object, value);
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