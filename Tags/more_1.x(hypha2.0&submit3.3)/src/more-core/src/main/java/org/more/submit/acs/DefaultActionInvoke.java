package org.more.submit.acs;
import java.lang.reflect.Method;
import org.more.core.error.InvokeException;
import org.more.submit.ActionInvoke;
import org.more.submit.ActionStack;
/**
 * 
 * @version : 2012-4-10
 * @author ������ (zyc@byshell.org)
 */
public class DefaultActionInvoke implements ActionInvoke {
    private Object target = null;
    private String method = null;
    public DefaultActionInvoke(Object target, String method) {
        this.target = target;
        this.method = method;
    }
    public Object invoke(ActionStack stack) throws Throwable {
        Class<?> type = this.target.getClass();
        Method[] m = type.getMethods();
        Method method = null;
        for (int i = 0; i < m.length; i++) {
            if (m[i].getName().equals(this.method) == false)
                continue; //���Ʋ�һ�º���
            if (m[i].getParameterTypes().length != 1)
                continue; //�������Ȳ�һ�º���
            if (ActionStack.class.isAssignableFrom(m[i].getParameterTypes()[0]) == true) {
                method = m[i];//��������
                break;
            }
        }
        if (method == null)//����Ҳ��������������쳣
            throw new InvokeException("�޷�ִ��[" + this.method + "]���Ҳ���ƥ��ķ�����");
        return method.invoke(target, stack);
    }
}