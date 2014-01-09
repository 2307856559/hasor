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
package net.test.project.common.hessian;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import net.hasor.core.AppContext;
import net.hasor.core.AppContextAware;
import net.hasor.core.plugin.Plugin;
import net.hasor.web.WebApiBinder;
import net.hasor.web.plugin.AbstractWebHasorPlugin;
import org.more.util.StringUtils;
import com.caucho.hessian.client.HessianProxyFactory;
import com.caucho.hessian.server.HessianServlet;
/**
 * 
 * @version : 2013-12-25
 * @author ������(zyc@hasor.net)
 */
@Plugin
public class HessianPlugin extends AbstractWebHasorPlugin {
    public void loadPlugin(WebApiBinder apiBinder) {
        Set<Class<?>> servicesSet = apiBinder.findClass(HessianService.class);
        /*1.ע��*/
        final Map<Class<?>, HessianServlet> serviceMap = new HashMap<Class<?>, HessianServlet>();
        for (Class<?> serviceType : servicesSet) {
            //
            HessianService hessAnno = serviceType.getAnnotation(HessianService.class);
            if (hessAnno == null || StringUtils.isBlank(hessAnno.value()))
                continue;
            String pushPath = hessAnno.value();
            pushPath = (pushPath.charAt(0) != '/') ? ("/" + pushPath) : pushPath;
            //
            HessianServlet serviceServlet = new HessianServlet();
            serviceMap.put(serviceType, serviceServlet);
            apiBinder.serve(pushPath).with(serviceServlet);
        }
        /*2.��ʼ��*/
        apiBinder.registerAware(new AppContextAware() {
            public void setAppContext(AppContext appContext) {
                for (Entry<Class<?>, HessianServlet> ent : serviceMap.entrySet()) {
                    Class<?> serviceType = ent.getKey();
                    HessianServlet serviceServlet = ent.getValue();
                    serviceServlet.setAPIClass(serviceType);
                    /*ͨ�� AppContext ����*/
                    serviceServlet.setHome(appContext.getInstance(serviceType));
                }
            }
        });
    }
    //
    /**���� Hessian �ͻ��˵���*/
    public static <T> T getPropxy(Class<T> propxyFaces) throws MalformedURLException {
        HessianClient hessAnno = propxyFaces.getAnnotation(HessianClient.class);
        if (hessAnno == null || StringUtils.isBlank(hessAnno.value()))
            return null;
        return getPropxy(propxyFaces, hessAnno.value());
    }
    /**���� Hessian �ͻ��˵���*/
    public static <T> T getPropxy(Class<T> propxyFaces, String url) throws MalformedURLException {
        if (propxyFaces.isInterface() == false)
            throw new ClassCastException("propxyFaces is not Interface.");
        HessianProxyFactory factory = new HessianProxyFactory();
        return (T) factory.create(propxyFaces, url);
    }
}