package org.more.webui.components;
import org.more.webui.context.ViewContext;
/**
 * ��ֵ̬
 * @version : 2012-5-11
 * @author ������ (zyc@byshell.org)
 */
public class ExpressionValueHolder extends ValueHolder {
    private String expressionString = null;
    public ExpressionValueHolder(String expressionString) {
        this.expressionString = expressionString;
    }
    @Override
    public void updateModule(ViewContext viewContext) {
        //TODO ��newValue��ֵ���µ�expressionString��ʾ��ģ����
    }
}