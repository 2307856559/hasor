package org.more.webui.context;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterConfig;
import org.more.webui.UIInitException;
import org.more.webui.components.UIComponent;
import org.more.webui.components.UIViewRoot;
import org.more.webui.freemarker.loader.ITemplateLoader;
import org.more.webui.freemarker.loader.template.ClassPathTemplateLoader;
import org.more.webui.freemarker.loader.template.DirTemplateLoader;
import org.more.webui.freemarker.loader.template.MultiTemplateLoader;
import org.more.webui.freemarker.parser.ElementHook;
import org.more.webui.freemarker.parser.Hook_UserTag;
import org.more.webui.freemarker.parser.TemplateScanner;
import org.more.webui.render.Render;
import org.more.webui.render.RenderKit;
import freemarker.template.Template;
/**
 * 
 * @version : 2012-5-22
 * @author ������ (zyc@byshell.org)
 */
public class FacesConfig {
    private String                     encoding           = "utf-8";                         //�ַ�����
    private String                     numberFormat       = null;                            //���ָ�ʽ��
    private String                     booleanFormat      = null;                            //�������͸�ʽ��
    private String                     dateTimeFormat     = null;                            //ʱ�����ڸ�ʽ��
    private boolean                    localizedLookup    = false;                           //�Ƿ����ù��ʻ��Ķ�֧��
    private String                     facesSuffix        = ".xhtml";
    private FilterConfig               initConfig         = null;
    private ArrayList<ITemplateLoader> templateLoaderList = new ArrayList<ITemplateLoader>();
    /**�齨�ı�ǩ���Ͷ�Ӧ���齨����*/
    private Map<String, Class<?>>      componentMap       = new HashMap<String, Class<?>>();
    /**�齨�ı�ǩ���Ͷ�Ӧ����Ⱦ��*/
    private Map<String, RenderKit>     renderKitMap       = new HashMap<String, RenderKit>();
    /*----------------------------------------------------------------*/
    public FacesConfig(FilterConfig initConfig) {
        this.initConfig = initConfig;
    }
    /*----------------------------------------------------------------*/
    private static long genID = 0;
    /**����������ͣ����ɸ����ID*/
    public static String generateID(Class<? extends UIComponent> compClass) {
        return "com_" + (genID++);
    }
    /**��ȡ���ڽ���ģ��ʱ�����������͵ĸ�ʽ��*/
    public String getNumberFormat() {
        return this.numberFormat;
    }
    public void setNumberFormat(String numberFormat) {
        this.numberFormat = numberFormat;
    }
    /**��ȡ���ڽ���ģ��ʱ�����������͵ĸ�ʽ��*/
    public String getBooleanFormat() {
        return this.booleanFormat;
    }
    public void setBooleanFormat(String booleanFormat) {
        this.booleanFormat = booleanFormat;
    }
    /**��ȡ���ڽ���ģ��ʱ����ʱ���������͵ĸ�ʽ��*/
    public String getDateTimeFormat() {
        return this.dateTimeFormat;
    }
    public void setDateTimeFormat(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }
    /**��ȡһ��booleanֵ��ֵ������ģ���Ƿ�֧�ֹ��ʻ���*/
    public boolean isLocalizedLookup() {
        return this.localizedLookup;
    }
    public void setLocalizedLookup(boolean localizedLookup) {
        this.localizedLookup = localizedLookup;
    }
    /**��ȡҳ��ʹ�õ��ַ�����*/
    public String getEncoding() {
        return this.encoding;
    };
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
    /**��ȡһ����չ�������Ǿ߱�����չ�����ļ�������ΪUI�ļ���*/
    public String getFacesSuffix() {
        return this.facesSuffix;
    }
    public void setFacesSuffix(String facesSuffix) {
        this.facesSuffix = facesSuffix;
    }
    public String getInitConfig(String key) {
        return this.initConfig.getInitParameter(key);
    }
    /*----------------------------------------------------------------*/
    /**���{@link ITemplateLoader}����*/
    public void addLoader(ITemplateLoader loader) {
        this.templateLoaderList.add(loader);
    }
    /**���һ������ΪLoader��·����*/
    public void addLoader(String packageName) {
        ClassPathTemplateLoader loader = new ClassPathTemplateLoader(packageName);
        this.addLoader(loader);
    }
    /**���һ��·����ΪLoader��·����*/
    public void addTemplateLoader(File templateDir) throws IOException {
        DirTemplateLoader loader = new DirTemplateLoader(templateDir);
        this.addLoader(loader);
    }
    /**��������ӵ�Loader���س�һ��{@link MultiTemplateLoader}����*/
    public MultiTemplateLoader getMultiTemplateLoader() {
        MultiTemplateLoader multiLoader = new MultiTemplateLoader();
        for (ITemplateLoader loader : this.templateLoaderList)
            multiLoader.addTemplateLoader(loader);
        return multiLoader;
    }
    /**
     * ���һ���齨��ע�ᡣ
     * @param tagName �齨�ı�ǩ����
     * @param componentClass �齨class���͡�
     */
    public void addComponent(String tagName, Class<?> componentClass) {
        this.componentMap.put(tagName, componentClass);
    }
    /**����һ��{@link RenderKit}*/
    public RenderKit getRenderKit(String scope) {
        RenderKit kit = this.renderKitMap.get(scope);
        if (kit == null) {
            kit = new RenderKit();
            this.renderKitMap.put(scope, kit);
        }
        return kit;
    }
    /**��ӱ�ǩ��ӳ���ϵ��*/
    public void addRender(String scope, String tagName, Class<? extends Render> renderClass) {
        this.getRenderKit(scope).addRender(tagName, renderClass);
    }
    /**���ڴ���һ��{@link UIViewRoot}���� */
    public UIViewRoot createViewRoot(Template template) throws UIInitException, IOException {
        //A.����ɨ����
        ElementHook hook = new Hook_UserTag(this);/*UnifiedCall��@add*/
        TemplateScanner scanner = new TemplateScanner();
        scanner.addElementHook("UnifiedCall", hook);
        //B.����ģ���ȡUIViewRoot
        return (UIViewRoot) scanner.parser(template, new UIViewRoot());
    }
    /**�����齨�ı�ǩ���������齨*/
    public UIComponent createComponent(String tagName) throws UIInitException {
        try {
            Class<?> comClass = this.componentMap.get(tagName);
            UIComponent com = (UIComponent) comClass.newInstance();
            com.setId(FacesConfig.generateID(com.getClass()));//����ID
            return com;
        } catch (InstantiationException e) {
            throw new UIInitException("�齨���� ��" + tagName + "�����ܱ�����.", e);
        } catch (IllegalAccessException e) {
            throw new UIInitException("�齨�����ڴ��� ��" + tagName + "���ڼ�����һ������ķ���Ȩ��.", e);
        }
    }
}