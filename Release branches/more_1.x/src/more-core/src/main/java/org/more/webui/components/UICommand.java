package org.more.webui.components;
/**
 * 
 * @version : 2012-5-15
 * @author ������ (zyc@byshell.org)
 */
public class UICommand extends UIComponent implements ActionSource {
    public String getTagName() {
        return "Command";
    }
    /**ͨ�����Ա�*/
    public enum Propertys {
        /**Action����*/
        action,
    }
    /**��ȡAction EL�ַ���*/
    public String getAction() {
        return this.getProperty(Propertys.action.name()).valueTo(String.class);
    }
    /**����Action EL�ַ���*/
    public void setAction(String action) {
        this.getProperty(Propertys.action.name()).value(action);
        this.methodExp = null;
    }
    private MethodExpression methodExp = null;
    public MethodExpression getActionExpression() {
        if (this.methodExp == null) {
            String actionString = this.getAction();
            if (actionString == null || actionString.equals("")) {} else
                this.methodExp = new MethodExpression(actionString);
        }
        return this.methodExp;
    }
}