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
import org.more.DoesSupportException;
import org.more.workflow.context.ELContext;
import org.more.workflow.context.RunContext;
import org.more.workflow.el.PropertyBinding;
import org.more.workflow.el.ValueExpression;
/**
 * ����Ԫ��Ϣ����AbstractMetadata������������һ��ģ�͵���Ϣ����PropertyMetadata���������������ģ�͵�������Ϣ
 * workFlowϵͳ����PropertyMetadata���������Ϣ����ģ��ִ������ע�������PropertyMetadata������������һ��·������
 * <b>form.role.name��</b>�����AbstractMetadata��ע������Բ���������Ե����������PropertyMetadata������
 * ������Ե���������ɶ����ֵ���ġ���������ڶ����Ե�����;������ֵ�����������Ognl�쳣��<br/>���Ա���Ϊ������ɲ��֣�
 * (1)����EL��(2)����ֵEL����һ�����ʽҪ��һ�����Ե���·�������ڶ������ʽ������һ���Ϸ���ognl�﷨���ʽ��
 * ��ʾ���ڶ�������ֵELʱ����ͨ��ʹ�� this�ؼ�����ȷ��ģ�ͱ���������磺<br/>
 * propertyEL="account"<br/>
 * valueEL="this.account + 'hello Word'"
 * Date : 2010-5-20
 * @author ������
 */
public class PropertyMetadata extends AbstractMetadata {
    //========================================================================================Field
    private String          propertyEL   = null; //����EL
    private String          valueEL      = null; //����ֵEL
    private PropertyBinding bindingCache = null;
    //==================================================================================Constructor
    public PropertyMetadata(String propertyEL, String valueEL) {
        super(propertyEL);
        this.propertyEL = propertyEL;
        this.valueEL = valueEL;
    };
    //==========================================================================================Job
    /**FormPropertyMetadata���Ͳ�֧�ָ÷�����������ø÷���������һ��DoesSupportException�쳣��*/
    @Override
    public Object newInstance(RunContext runContext) {
        throw new DoesSupportException("FormPropertyMetadata���Ͳ�֧�ָ÷�����");
    };
    @Override
    public void updataMode(Object mode, ELContext elContext) throws Throwable {
        super.updataMode(mode, elContext);
        //�������Ա��ʽ��ȡValueBinding
        if (this.bindingCache == null)
            this.bindingCache = this.getPropertyBinding(this.propertyEL, mode);
        if (this.bindingCache.isReadOnly() == true)
            return;
        //����elContext����ֵ���ʽ�������õ������С�
        ValueExpression ve = new ValueExpression(this.valueEL);
        elContext.putLocalThis(mode);
        this.bindingCache.setValue(ve.eval(elContext));
        elContext.putLocalThis(null);
    };
};