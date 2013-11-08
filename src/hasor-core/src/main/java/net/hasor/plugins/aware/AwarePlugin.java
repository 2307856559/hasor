/*
 * Copyright 2008-2009 the original author or authors.
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
package net.hasor.plugins.aware;
import static net.hasor.core.AppContext.ContextEvent_Start;
import net.hasor.core.ApiBinder;
import net.hasor.core.AppContext;
import net.hasor.core.EventListener;
import net.hasor.core.plugin.AbstractPluginFace;
import net.hasor.core.plugin.Plugin;
import net.hasor.plugins.aware.AppContextAware.AwareUtil;
/**
 * ���� AppContextAware �ӿڵĵ��á�
 * @version : 2013-11-8
 * @author ������(zyc@hasor.net)
 */
@Plugin
public class AwarePlugin extends AbstractPluginFace implements EventListener {
    public void loadPlugin(ApiBinder apiBinder) {
        apiBinder.getEnvironment().getEventManager().pushEventListener(ContextEvent_Start, this);
    }
    public void onEvent(String event, Object[] params) throws Throwable {
        AppContext appContext = (AppContext) params[0];
        new AppAwareUtil().doAware(appContext);
    }
    /*����ע��� AppContextAware �ӿ�*/
    private class AppAwareUtil extends AwareUtil {
        public void doAware(AppContext appContext) {
            super.doAware(appContext);
        }
    }
}