package org.more.webui.components;
import org.more.util.StringConvertUtil;
import org.more.webui.context.ViewContext;
/**
 * 
 * @version : 2012-5-11
 * @author ������ (zyc@byshell.org)
 */
public abstract class ValueHolder {
    private Object newValue = null;
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
        return this.newValue;
    }
    /**д������ֵ����д�������ֵ���ڵ���{@link #updateModule(ViewContext)}��*/
    public void value(Object newValue) {
        this.newValue = newValue;
    }
    /**��д��{@link ValueHolder}�����Ե�ֵ���µ�ģ���С�*/
    public abstract void updateModule(ViewContext viewContext);
}