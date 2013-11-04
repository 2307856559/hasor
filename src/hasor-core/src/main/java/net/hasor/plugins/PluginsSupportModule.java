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
package net.hasor.plugins;
import java.util.Set;
import net.hasor.Hasor;
import net.hasor.core.ApiBinder;
import net.hasor.core.AppContext;
import net.hasor.core.Module;
import net.hasor.core.context.AnnoModule;
/**
 * �����ϵ֧��
 * @version : 2013-4-8
 * @author ������ (zyc@hasor.net)
 */
@AnnoModule()
public class PluginsSupportModule implements Module {
    /**��ʼ��.*/
    public void init(ApiBinder apiBinder) {
        Set<Class<?>> pluginSet = apiBinder.getClassSet(Plugin.class);
        if (pluginSet == null)
            return;
        Hasor.info("find Plugin : " + Hasor.logString(pluginSet));
        for (Class<?> pluginClass : pluginSet) {
            if (PluginFace.class.isAssignableFrom(pluginClass) == false) {
                Hasor.warning("not implemented PluginFace :%s", pluginClass);
                continue;
            }
            try {
                PluginFace pluginFace = (PluginFace) pluginClass.newInstance();
                Hasor.info("loadPlugin %s.", pluginClass);
                pluginFace.loadPlugin(apiBinder);
            } catch (Throwable e) {
                Hasor.error("config Plugin error at %s.%s", pluginClass, e);
            }
        }
    }
    /***/
    public void start(AppContext appContext) {}
    /***/
    public void stop(AppContext appContext) {}
}