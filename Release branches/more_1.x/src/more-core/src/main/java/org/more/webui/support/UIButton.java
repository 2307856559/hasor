package org.more.webui.support;
import org.more.webui.context.ViewContext;
/**
 * Button
 * @version : 2012-5-15
 * @author ������ (zyc@byshell.org)
 */
public class UIButton extends UIComponent {
    /**ͨ�����Ա�*/
    public enum Propertys {
        /**��ʾ��Ⱦʱ���Ƿ�ʹ��a��ǩ����input��ǩ��Ĭ�ϣ���*/
        useLink,
        /**��ʾ������*/
        title
    }
    @Override
    protected void initUIComponent(ViewContext viewContext) {
        super.initUIComponent(viewContext);
        this.setProperty(Propertys.useLink.name(), true);
        this.setProperty(Propertys.title.name(), "");
    }
    public boolean isUseLink() {
        return this.getProperty(Propertys.useLink.name()).valueTo(Boolean.TYPE);
    }
    public void setUseLink(boolean useLink) {
        this.getProperty(Propertys.useLink.name()).value(useLink);
    }
    public String getTitle() {
        return this.getProperty(Propertys.title.name()).valueTo(String.class);
    }
    public void setTitle(String title) {
        this.getProperty(Propertys.title.name()).value(title);
    }
}