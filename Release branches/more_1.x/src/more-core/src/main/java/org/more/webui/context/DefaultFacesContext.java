package org.more.webui.context;
import java.util.HashMap;
import java.util.Map;
import freemarker.cache.MruCacheStorage;
import freemarker.template.Configuration;
/**
 * 
 * @version : 2012-4-25
 * @author ������ (zyc@byshell.org)
 */
public class DefaultFacesContext extends FacesContext {
    private Configuration       cfg = null;
    private Map<String, Object> att = null;
    /*------------------------------------------------*/
    public DefaultFacesContext(FacesConfig config) {
        super(config);
    }
    @Override
    public Map<String, Object> getAttribute() {
        if (this.att == null)
            this.att = new HashMap<String, Object>();
        return this.att;
    }
    @Override
    public Configuration getFreemarker() {
        if (this.cfg == null) {
            Configuration cfg = new Configuration();
            String config = this.getFacesConfig().getEncoding();
            if (config != null)
                cfg.setDefaultEncoding(config);
            /*��������ӣ���Ϊû�л������ģ���������붪ʧ�����⡣
             * ������������ԭ����webui��Ҫ��ģ���еı�ǩд��id�ļ���*/
            cfg.setCacheStorage(new MruCacheStorage(0, Integer.MAX_VALUE));
            //
            cfg.setLocalizedLookup(this.getFacesConfig().isLocalizedLookup());
            cfg.setTemplateLoader(this.getFacesConfig().getMultiTemplateLoader());
            this.cfg = cfg;
        }
        return this.cfg;
    }
}