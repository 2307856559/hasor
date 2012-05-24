package org.more.webui.components;
import org.more.core.ognl.OgnlException;
import org.more.util.StringConvertUtil;
import org.more.webui.context.ViewContext;
/**
 * ����ֵ�����࣬����ʹ���̸߳���ķ�ʽ����ÿһ�����ԡ�
 * @version : 2012-5-11
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractValueHolder {
    private Object metaValue = null;
    public static class Value {
        boolean needUpdate = false;
        Object  newValue   = null;
    }
    /**�÷�ʽ��Ϊ�˱��ⲻͬ�߳�֮��ĸ���*/
    private ThreadLocal<Value> newValue = new ThreadLocal<Value>();
    @Override
    public String toString() {
        Object var = this.value();
        return (var == null) ? "null" : var.toString();
    }
    public <T> T valueTo(Class<T> toType) {
        return StringConvertUtil.changeType(this.value(), toType);
    }
    /**����ģ���ϵ�����ֵ��*/
    public Object value() {
        Value v = this.newValue.get();
        return (v != null) ? v.newValue : null;
    }
    /**д������ֵ����д�������ֵ���ڵ���{@link #updateModule(ViewContext)}��д�뵽Bean�С�*/
    public void value(Object newValue) {
        Value v = this.newValue.get();
        if (v == null) {
            v = new Value();
            this.newValue.set(v);
        }
        v.newValue = newValue;
        v.needUpdate = true;
    }
    /**����ԭʼ��Ϣֵ��ÿ���̶߳���һ��������value����ĳһ���̵߳�����reset����ʱ����ֵ�ᱻ�ָ����Ǹ��߳��ϡ���*/
    public Object getMetaValue() {
        return this.metaValue;
    }
    /**����ԭʼ��Ϣֵ��ÿ���̶߳���һ��������value����ĳһ���̵߳�����reset����ʱ����ֵ�ᱻ�ָ����Ǹ��߳��ϡ���*/
    public void setMetaValue(Object metaValue) {
        this.metaValue = metaValue;
    }
    /**����һ��boolean����ֵ�����������Ƿ�Ϊֻ��ģʽ��*/
    public abstract boolean isReadOnly();
    /**����һ��boolean����ֵ�����Ƿ���Ҫִ��updateModule���²�����*/
    public boolean isUpdate() {
        Value v = this.newValue.get();
        return (v != null) ? v.needUpdate : false;
    };
    protected Value getValue() {
        return this.newValue.get();
    };
    /**����������Ϊ��ʼ��״̬*/
    public void reset() {
        Value v = this.newValue.get();
        if (v == null) {
            v = new Value();
            this.newValue.set(v);
        }
        v.newValue = this.metaValue;
        v.needUpdate = false;
    };
    /**��д��{@link AbstractValueHolder}�����Ե�ֵ���µ�ģ���С�*/
    public abstract void updateModule(UIComponent component, ViewContext viewContext) throws OgnlException;
}