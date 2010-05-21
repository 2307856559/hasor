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
import org.more.core.ognl.Ognl;
import org.more.core.ognl.OgnlContext;
import org.more.core.ognl.OgnlException;
import org.more.workflow.context.ELContext;
/**
 * ���ʽ��������ͨ��������Խ���workflow�г��ֵ�EL���ʽ������ȡ��������ʽ��ֵ��
 * workflow��EL���ʽ��Ҫ��ѭOgnl���ʽ�﷨��
 * Date : 2010-5-21
 * @author ������
 */
public class ValueExpression {
    //========================================================================================Field
    private String expressionString = null;
    private Object thisValue        = null;
    //==================================================================================Constructor
    /**����һ�����ʽ��������*/
    public ValueExpression(String expressionString) throws OgnlException {
        this.expressionString = expressionString;
    };
    //==========================================================================================Job
    /**��ȡValueExpression��������el�ַ�����*/
    public String getExpressionString() {
        return this.expressionString;
    };
    /**���㲢�ҷ��ص�ǰ���ʽ��ֵ�����elContext����Ϊ���������NullPointerException�����쳣��*/
    public Object eval(ELContext elContext) throws OgnlException, NullPointerException {
        if (elContext == null)
            throw new NullPointerException("��ָ��ELContext���Ͳ�����");
        //
        OgnlContext ognlContext = elContext.getOgnlContext();
        ognlContext.setCurrentObject(this.thisValue);
        Object result = Ognl.getValue(this.expressionString, ognlContext);
        ognlContext.setCurrentObject(null);
        return result;
    };
    /**��һ��������뵽������ʽ�У���������������ELContext���ظ�����putLocal��������Ķ������ȼ�����ELContext��*/
    public void putLocalThis(Object value) {
        this.thisValue = value;
    };
};