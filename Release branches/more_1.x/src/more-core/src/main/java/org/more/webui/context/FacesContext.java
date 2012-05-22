package org.more.webui.context;
import org.more.core.iatt.Attribute;
import freemarker.template.Configuration;
/**
 * 
 * @version : 2012-4-25
 * @author ������ (zyc@byshell.org)
 */
public abstract class FacesContext {
    private FacesConfig facesConfig = null;
    //
    public FacesContext(FacesConfig facesConfig) {
        this.facesConfig = facesConfig;
    };
    /**��ȡ���ö���*/
    public FacesConfig getFacesConfig() {
        return this.facesConfig;
    };
    /**��ȡҳ��ʹ�õ��ַ�����*/
    public String getEncoding() {
        return this.facesConfig.getEncoding();
    };
    /**��ȡfreemarker�����ö���*/
    public abstract Configuration getFreemarker();
    /**��ȡ���Լ���*/
    public abstract Attribute<Object> getAttribute();
}