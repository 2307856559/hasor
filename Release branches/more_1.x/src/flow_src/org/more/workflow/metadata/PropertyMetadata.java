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
import java.util.Iterator;
import org.more.core.ognl.OgnlException;
import org.more.workflow.context.ELContext;
import org.more.workflow.el.PropertyBinding;
import org.more.workflow.el.PropertyBindingHolder;
import org.more.workflow.el.ValueExpression;
import org.more.workflow.event.EventListener;
import org.more.workflow.event.EventPhase;
import org.more.workflow.event.object.UpdataPropertyEvnet;
/**
 * ����Ԫ��Ϣ���󣬸�����{@link AbstractMetadata}���һ����������������һ��ģ�͵�������Ϣ��workFlowϵͳ����PropertyMetadata����������ģ��ִ������ע�������
 * PropertyMetadata������������һ��·������<b>form.role.name��</b>�����workFlow��ע������Բ���������Ե����������<br/>
 * PropertyMetadata������������Ե���������ɶ����ֵ���ġ���������ڶ����Ե�����;������ֵ�����������Ognl�쳣��<br/>���Ա���Ϊ������ɲ��֣�
 * (1)����EL��(2)����ֵEL����һ�����ʽҪ��һ�����Ե���·�������ڶ������ʽ������һ���Ϸ���ognl�﷨���ʽ,����ȷ�����Ե�ֵ��
 * ��ʾ���ڶ�������ֵELʱ����ͨ��ʹ�� this�ؼ�����ȷ��ģ�ͱ���������磺<br/>
 * propertyEL="account"<br/>
 * valueEL="this.account + 'hello Word'"
 * Date : 2010-6-15
 * @author ������
 */
public final class PropertyMetadata extends AbstractMetadata implements PropertyBindingHolder {
    private String          propertyEL   = null; //����EL
    private String          valueEL      = null; //����ֵEL
    private PropertyBinding bindingCache = null; //����������Բ�����
    /**����һ������Ԫ��Ϣ����propertyEL�����������Եĵ���·����valueEL�������������Ե�ֵ��*/
    public PropertyMetadata(String propertyEL, String valueEL) {
        super(propertyEL);
        if (propertyEL == null)
            throw new NullPointerException("propertyELֵΪ��,PropertyMetadata���ܱ����������Ԫ��Ϣ��");
        this.propertyEL = propertyEL;
        this.valueEL = valueEL;
    };
    /**�ڵ�ǰ������������һ���¼���*/
    protected void event(EventPhase event) {
        Iterator<EventListener> iterator = this.getListeners();
        while (iterator.hasNext())
            iterator.next().doListener(event);
    };
    /**ʹ������Ԫ��Ϣ����ģ�͵�������Ϣ��*/
    public void updataProperty(Object mode, ELContext elContext) throws Throwable {
        //�������Ա��ʽ��ȡValueBinding
        if (this.bindingCache == null)
            this.bindingCache = this.getPropertyBinding(this.propertyEL, mode);
        if (this.bindingCache.isReadOnly() == true)
            return;
        //����elContext����ֵ���ʽ�������õ������С�
        Object oldValue = null;
        Object newValue = null;
        oldValue = this.bindingCache.getValue();
        elContext.putLocalThis(mode);
        ValueExpression ve = new ValueExpression(this.valueEL);
        newValue = ve.eval(elContext);
        elContext.putLocalThis(null);
        //
        UpdataPropertyEvnet event = new UpdataPropertyEvnet(mode, this.propertyEL, oldValue, newValue, this);
        this.event(event.getEventPhase()[0]);//before
        this.bindingCache.setValue(event.getNewValue());
        this.event(event.getEventPhase()[1]);//after
    };
    @Override
    public PropertyBinding getPropertyBinding(String propertyEL, Object object) throws OgnlException {
        return new PropertyBinding(propertyEL, object);
    }
};