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
package net.hasor.core.services;
import static net.hasor.core.AppContext.ContextEvent_Start;
import static net.hasor.core.AppContext.ContextEvent_Stoped;
import static net.hasor.core.AppContext.ModuleEvent_Start;
import static net.hasor.core.AppContext.ModuleEvent_Stoped;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.hasor.Hasor;
import net.hasor.core.AppContext;
import net.hasor.core.EventListener;
import net.hasor.core.EventManager;
import com.google.inject.Key;
/**
 * 
 * @version : 2013-10-29
 * @author ������(zyc@hasor.net)
 */
public class HandlerHub implements EventListener {
    private AppContext                                           appContext;
    private Map<Class<?>, ServicesRegisterHandlerDefine<Object>> handlerDefine;
    //
    public HandlerHub(final AppContext appContext) {
        this.appContext = appContext;
        this.handlerDefine = new HashMap<Class<?>, ServicesRegisterHandlerDefine<Object>>();
        EventManager eventMsg = this.appContext.getEventManager();
        eventMsg.addEventListener(ContextEvent_Start, this);
        eventMsg.addEventListener(ContextEvent_Stoped, this);
        eventMsg.addEventListener(ModuleEvent_Start, this);
        eventMsg.addEventListener(ModuleEvent_Stoped, this);
        eventMsg.pushEventListener(ContextEvent_Start, new EventListener() {
            public void onEvent(String event, Object[] params) throws Throwable {
                List<ServicesRegisterHandlerDefine> defineList = appContext.getInstanceByBindingType(ServicesRegisterHandlerDefine.class);
                for (ServicesRegisterHandlerDefine handler : defineList)
                    handlerDefine.put(handler.getServiceType(), handler);
            }
        });
    }
    /***/
    public void onEvent(String event, Object[] params) throws Throwable {
        /*  */if (event.equals(ContextEvent_Start)) {
            /*���䣬ע�����*/
        } else if (event.equals(ContextEvent_Stoped)) {
            /*���䣬���ע��*/
        } else if (event.equals(ModuleEvent_Start)) {
            /*���䣬ע�����*/
        } else if (event.equals(ModuleEvent_Stoped)) {
            /*���䣬���ע��*/
        }
    }
    //
    //
    //
    private static class MappingItem {
        public Class<?> type;
        public Object   target;
    }
    private static MappingItem createMappingItem(Class<?> type, Object target) {
        MappingItem e = new MappingItem();
        e.type = type;
        e.target = target;
        return e;
    }
    private Map<Object, MappingItem> serviceBeanMapping = new HashMap<Object, MappingItem>();
    /*ע�����*/
    public synchronized void registerService(Class<?> type, Class<?> serviceType, Object... objects) {
        Hasor.assertIsLegal(!serviceBeanMapping.containsKey(serviceType), "Repeat service registry at : " + serviceType);
        //
        Object serviceBean = this.appContext.getInstance(serviceType);
        MappingItem regItem = createMappingItem(type, serviceBean);
        this.serviceBeanMapping.put(serviceType, regItem);
        this._registerServiceObject(type, regItem, objects);
    };
    /*ע�����*/
    public synchronized void registerService(Class<?> type, Key<?> serviceKey, Object... objects) {
        Hasor.assertIsLegal(!serviceBeanMapping.containsKey(serviceKey), "Repeat service registry at : " + serviceKey);
        //
        Object serviceBean = this.appContext.getGuice().getInstance(serviceKey);
        MappingItem regItem = createMappingItem(type, serviceBean);
        this.serviceBeanMapping.put(serviceKey, regItem);
        this._registerServiceObject(type, regItem, objects);
    };
    /*ע�����*/
    public synchronized void registerServiceObject(Class<?> type, Object serviceBean, Object... objects) {
        Hasor.assertIsLegal(!serviceBeanMapping.containsKey(serviceBean), "Repeat service registry at : " + serviceBean);
        //
        MappingItem regItem = createMappingItem(type, serviceBean);
        this.serviceBeanMapping.put(serviceBean, regItem);
        this._registerServiceObject(type, regItem, objects);
    };
    /*���ע�����*/
    public synchronized void unRegisterService(Class<?> type, Class<?> serviceType) {
        if (!serviceBeanMapping.containsKey(serviceType))
            return;
        this._unRegisterServiceObject(type, serviceBeanMapping.get(serviceType));
        serviceBeanMapping.remove(serviceType);
    };
    /*���ע�����*/
    public synchronized void unRegisterService(Class<?> type, Key<?> serviceKey) {
        if (!serviceBeanMapping.containsKey(serviceKey))
            return;
        this._unRegisterServiceObject(type, serviceBeanMapping.get(serviceKey));
        serviceBeanMapping.remove(serviceKey);
    };
    /*���ע�����*/
    public synchronized void unRegisterServiceObject(Class<?> type, Object serviceBean) {
        if (!serviceBeanMapping.containsKey(serviceBean))
            return;
        this._unRegisterServiceObject(type, serviceBeanMapping.get(serviceBean));
        serviceBeanMapping.remove(serviceBean);
    };
    /*ע�����*/
    private void _registerServiceObject(Class<?> type, MappingItem serviceBean, Object... objects) {
        ServicesRegisterHandlerDefine define = this.handlerDefine.get(type);
        if (define == null)
            return;
        define.registerService(serviceBean.target);
    };
    /*���ע�����*/
    private void _unRegisterServiceObject(Class<?> type, MappingItem serviceBean) {
        ServicesRegisterHandlerDefine define = this.handlerDefine.get(type);
        if (define == null)
            return;
        define.unRegisterService(serviceBean.target);
    };
    //
    //    private Map<Object, List<MappingItem>> serviceGroup = new HashMap<Object, List<MappingItem>>();
    //    private List<MappingItem> getServiceList(Object groupBy) {
    //        List<MappingItem> serviceList = this.serviceGroup.get(groupBy);
    //        if (serviceList == null) {
    //            serviceList = new ArrayList<MappingItem>();
    //            this.serviceGroup.put(groupBy, serviceList);
    //        }
    //        return serviceList;
    //    }
    //    private Object getCurrentGroupKey() {
    //        Object currentModule = ModulePropxy.getLocalModuleInfo(appContext);
    //        currentModule = (currentModule == null) ? appContext : currentModule;
    //        return currentModule;
    //    }
}