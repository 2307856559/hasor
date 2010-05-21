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
package org.more.workflow.form;
import org.more.DoesSupportException;
import org.more.core.ognl.OgnlException;
import org.more.workflow.context.ELContext;
import org.more.workflow.el.PropertyBinding;
import org.more.workflow.el.ValueExpression;
import org.more.workflow.metadata.AbstractMetadata;
/**
 * ���̱�����Ԫ��Ϣ�����������Կ��������̱���һ�����������ֶ�Ҳ�����Ǹ��������ֶΡ�������ioc���Ե���
 * FormPropertyMetadata����Ա�ʾһ�����Ե�Ѱ��·�����磺form.role.name��<br/>
 * �����FormMetadata��ע������Բ�������Ӽ����ԣ�FormPropertyMetadata����Զ�����������Ե���������ɶ����
 * ֵ���ġ���������ڶ����Ե�����;������ֵ�����������Ognl�쳣��<br/>
 * ���Ա���Ϊ������ɲ��֣�(1)����EL��(2)����ֵEL����һ�����ʽҪ��һ�����Ե���·�������ڶ������ʽ������һ���Ϸ���ognl�﷨���ʽ��
 * Date : 2010-5-20
 * @author ������
 */
public class FormPropertyMetadata extends AbstractMetadata {
    //========================================================================================Field
    private String propertyEL = null; //����EL
    private String valueEL    = null; //����ֵEL
    //==================================================================================Constructor
    public FormPropertyMetadata(String propertyEL, String valueEL) {
        super(propertyEL);
        this.propertyEL = propertyEL;
        this.valueEL = valueEL;
    };
    //==========================================================================================Job
    /**FormPropertyMetadata���Ͳ�֧�ָ÷�����������ø÷���������һ��DoesSupportException�쳣��*/
    @Override
    public Object newInstance(ELContext elContext) {
        throw new DoesSupportException("FormPropertyMetadata���Ͳ�֧�ָ÷�����");
    };
    @Override
    public void updataMode(Object mode, ELContext elContext) throws OgnlException {
        //�������Ա��ʽ��ȡValueBinding
        PropertyBinding vp = this.getPropertyBinding(this.propertyEL, mode);
        if (vp.isReadOnly() == true)
            return;
        //����elContext����ֵ���ʽ�������õ������С�
        ValueExpression ve = new ValueExpression(this.valueEL);
        ve.putLocalThis(mode);
        vp.setValue(ve.eval(elContext));
    };
};