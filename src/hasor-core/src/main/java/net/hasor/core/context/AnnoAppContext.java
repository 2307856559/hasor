/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.hasor.core.context;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import net.hasor.Hasor;
import net.hasor.core.ApiBinder.ModuleSettings;
import net.hasor.core.Module;
import net.hasor.core.ModuleInfo;
import org.more.util.StringUtils;
/**
 * 
 * @version : 2013-7-16
 * @author ������ (zyc@hasor.net)
 */
public class AnnoAppContext extends StandardAppContext {
    /***/
    public AnnoAppContext() {
        super();
    }
    /***/
    public AnnoAppContext(String mainSettings) throws IOException {
        super(mainSettings);
    }
    /***/
    public AnnoAppContext(File mainSettings) {
        super(mainSettings);
    }
    /***/
    public AnnoAppContext(URI mainSettings) {
        super(mainSettings);
    }
    /***/
    public AnnoAppContext(String mainSettings, Object context) throws IOException {
        super(mainSettings, context);
    }
    /***/
    public AnnoAppContext(File mainSettings, Object context) {
        super(mainSettings, context);
    }
    /***/
    public AnnoAppContext(URI mainSettings, Object context) {
        super(mainSettings, context);
    }
    //
    protected void initContext() {
        this.loadModule();
        super.initContext();
    }
    /**װ��ģ��*/
    protected void loadModule() {
        //1.ɨ��classpath��
        Set<Class<?>> initHookSet = this.getEnvironment().getClassSet(AnnoModule.class);
        Hasor.info("find Module : " + Hasor.logString(initHookSet));
        //2.����δʵ��HasorModule�ӿڵ���
        for (Class<?> modClass : initHookSet) {
            if (!Module.class.isAssignableFrom(modClass)) {
                Hasor.warning("not implemented HasorModule or Module :%s", modClass);
                continue;/*����*/
            }
            /*Hasor ģ��*/
            Module modObject = this.createModule(modClass);
            ModuleInfo moduleInfo = this.addModule(modObject);
            //
            if (moduleInfo instanceof ModuleSettings) {
                ModuleSettings infoCfg = (ModuleSettings) moduleInfo;
                AnnoModule modAnno = modClass.getAnnotation(AnnoModule.class);
                String dispName = StringUtils.isBlank(modAnno.displayName()) ? modClass.getSimpleName() : modAnno.displayName();
                infoCfg.setDisplayName(dispName);
                infoCfg.setDescription(modAnno.description());
            }
        }
    }
    private <T> T createModule(Class<?> listenerClass) {
        try {
            return (T) listenerClass.newInstance();
        } catch (Exception e) {
            Hasor.error("create %s an error!%s", listenerClass, e);
            return null;
        }
    }
}