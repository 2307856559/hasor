package org.noe.platform.modules.freemarker.install;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;
import javax.servlet.ServletContext;
import org.noe.platform.Noe;
import org.noe.platform.context.AppContext;
import org.noe.platform.context.Module;
import org.noe.platform.context.ModuleSettings;
import org.noe.platform.context.WebAppContext;
import org.noe.platform.context.module.AbstractWebNoeModule;
import org.noe.platform.modules.freemarker.FmMethod;
import org.noe.platform.modules.freemarker.FmTag;
import org.noe.platform.modules.freemarker.FreemarkerService;
import org.noe.platform.modules.freemarker.Tag;
import org.noe.platform.modules.freemarker.loader.FmTemplateLoader;
import org.noe.platform.modules.freemarker.loader.loader.ClassPathTemplateLoader;
import org.noe.platform.modules.freemarker.loader.loader.DirTemplateLoader;
import org.noe.platform.modules.freemarker.loader.loader.MultiTemplateLoader;
import org.noe.platform.modules.freemarker.support.FmService;
import org.noe.platform.modules.freemarker.support.FmServlet;
import org.noe.platform.modules.freemarker.support.InternalMethodObject;
import org.noe.platform.modules.freemarker.support.InternalTagObject;
import org.noe.platform.modules.servlet30.WebFilterMod;
import org.noe.platform.modules.servlet30.WebServletMod;
import com.google.inject.Binder;
import com.google.inject.Provider;
import freemarker.cache.NullCacheStorage;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
/**
 * 
 * @version : 2013-7-31
 * @author ������ (zyc@byshell.org)
 */
@Module()
public class FreemarkerMod extends AbstractWebNoeModule implements Provider<Configuration> {
    private Configuration freemarkerConfig = new Configuration();
    @Override
    public Configuration get() {
        return freemarkerConfig;
    }
    //
    @Override
    public void readyModule(ModuleSettings info) {
        //������
        info.beforeMe(WebServletMod.class);
        info.beforeMe(WebFilterMod.class);
    }
    @Override
    public void init(Binder guice) {
        guice.bind(Configuration.class).toProvider(this);
        guice.bind(FreemarkerService.class).to(FmService.class).asEagerSingleton();
        //
        this.serve("*.ftl").with(FmServlet.class);
        this.serve("*.htm").with(FmServlet.class);
    }
    //
    /**װ��FmTag*/
    protected void loadFmTag(AppContext appContext, Configuration fmConfiguration) {
        //1.��ȡ
        Set<Class<?>> fmTagSet = appContext.getClassSet(FmTag.class);
        if (fmTagSet == null)
            return;
        for (Class<?> cls : fmTagSet) {
            if (Tag.class.isAssignableFrom(cls) == false) {
                Noe.logWarning("loadFmTag : not implemented IFmTag or IFmTag2. class=%s", cls);
            } else {
                FmTag fmTagAnno = cls.getAnnotation(FmTag.class);
                String tagName = fmTagAnno.value();
                Tag tagBody = (Tag) appContext.getInstance(cls);
                InternalTagObject internalTag = new InternalTagObject(tagBody);
                Noe.logInfo("loadFmTag %s at %s.", tagName, cls);
                fmConfiguration.setSharedVariable(tagName, internalTag);
            }
        }
    }
    //
    /**װ��FmMethod*/
    protected void loadFmMethod(AppContext appContext, Configuration fmConfiguration) {
        Set<Class<?>> fmMethodSet = appContext.getClassSet(Object.class);
        if (fmMethodSet == null)
            return;
        for (Class<?> fmMethodType : fmMethodSet) {
            try {
                Method[] m1s = fmMethodType.getMethods();
                Object targetMethodObject = null;
                for (Method fmMethod : m1s) {
                    if (fmMethod.isAnnotationPresent(FmMethod.class) == true) {
                        FmMethod fmMethodAnno = fmMethod.getAnnotation(FmMethod.class);
                        String funName = fmMethodAnno.value();
                        if (targetMethodObject == null)
                            targetMethodObject = appContext.getInstance(fmMethodType);
                        InternalMethodObject internalMethod = new InternalMethodObject(fmMethod, targetMethodObject);
                        Noe.logInfo("loadFmMethod %s at %s.", funName, fmMethod);
                        fmConfiguration.setSharedVariable(funName, internalMethod);
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }
    //
    /**װ��Services*/
    protected void applyBean(AppContext appContext, Configuration configuration) {
        String[] names = appContext.getBeanNames();
        if (names == null || names.length == 0)
            return;
        Noe.logInfo("Registration Beans %s", new Object[] { names });
        for (String key : names)
            try {
                configuration.setSharedVariable(key, appContext.getBean(key));
            } catch (Exception e) {
                Noe.logError("%s Bean Registration failed!%s", key, e);
            }
    }
    //
    /***/
    @Override
    public void start(WebAppContext appContext) {
        this.freemarkerConfig.setObjectWrapper(new DefaultObjectWrapper());
        this.freemarkerConfig.setCacheStorage(new NullCacheStorage());//���û���Ϊ��
        this.freemarkerConfig.setEncoding(Locale.CHINA, "utf-8");
        this.freemarkerConfig.setOutputEncoding("utf-8");
        this.freemarkerConfig.setDefaultEncoding("utf-8");
        this.freemarkerConfig.setTemplateUpdateDelay(0);
        this.freemarkerConfig.setClassicCompatible(true);
        this.freemarkerConfig.setNumberFormat("#");
        this.freemarkerConfig.setDateFormat("yyyy-MM-dd");
        this.freemarkerConfig.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
        this.freemarkerConfig.setLocalizedLookup(false);
        //
        try {
            ArrayList<FmTemplateLoader> tempLoaderArray = new ArrayList<FmTemplateLoader>();
            //
            File fmPath = new File(appContext.getWorkSpace().getWorkDir(), "webapps");
            fmPath.mkdirs();
            tempLoaderArray.add(new DirTemplateLoader(fmPath));
            Noe.logInfo("workSpacePath = %s", fmPath);
            //
            //this.getServletContext() <-- ����ֻ����Init�׶β�������
            File webPath = new File(((ServletContext) appContext.getContext()).getRealPath("/"));
            webPath.mkdirs();
            tempLoaderArray.add(new DirTemplateLoader(webPath));
            //            File devPath=new File("src/main/resources/META-INF/webapp");
            //            System.out.println("@"+devPath);
            //            if(devPath.exists()){
            //            	  
            //            	 tempLoaderArray.add(new DirTemplateLoader(devPath));	
            //            }
            Noe.logInfo("webRoot = %s", webPath);
            //
            tempLoaderArray.add(new ClassPathTemplateLoader("/META-INF/webapp"));
            Noe.logInfo("classpath = %s.", "/META-INF/webapp");
            //
            FmTemplateLoader[] loaderList = tempLoaderArray.toArray(new FmTemplateLoader[tempLoaderArray.size()]);
            this.freemarkerConfig.setTemplateLoader(new MultiTemplateLoader(loaderList));
            this.loadFmTag(appContext, this.get());
            this.loadFmMethod(appContext, this.get());
            this.applyBean(appContext, this.get());
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}