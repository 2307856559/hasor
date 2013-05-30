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
package org.platform.context.support;
import static org.platform.Platform.Platform_LoadPackages;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.more.util.ClassUtils;
import org.platform.Assert;
import org.platform.binder.BeanInfo;
import org.platform.context.AppContext;
import org.platform.context.Settings;
import org.platform.context.WorkSpace;
import com.google.inject.Binding;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
/**
 * {@link AppContext}�ӿڵĳ���ʵ���ࡣ
 * @version : 2013-4-9
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractAppContext implements AppContext {
    private long                  startTime   = System.currentTimeMillis(); //ϵͳ����ʱ��
    private Map<String, BeanInfo> beanInfoMap = null;
    private WorkSpace             workSpace   = null;
    //
    /**����*/
    public abstract void start(Module... modules);
    /**���ٷ�����*/
    public abstract void destroyed();
    @Override
    public WorkSpace getWorkSpace() {
        if (this.workSpace == null) {
            this.workSpace = new AbstractWorkSpace() {
                @Override
                public Settings getSettings() {
                    return AbstractAppContext.this.getSettings();
                }
            };
        }
        return this.workSpace;
    }
    @Override
    public long getAppStartTime() {
        return this.startTime;
    };
    @Override
    public Set<Class<?>> getClassSet(Class<?> featureType) {
        if (featureType == null)
            return null;
        String loadPackages = this.getSettings().getString(Platform_LoadPackages);
        String[] spanPackage = loadPackages.split(",");
        return ClassUtils.getClassSet(spanPackage, featureType);
    }
    @Override
    public <T> T getInstance(Class<T> beanType) {
        return this.getGuice().getInstance(beanType);
    };
    @Override
    public <T> Class<T> getBeanType(String name) {
        Assert.isNotNull(name, "bean name is null.");
        if (this.beanInfoMap == null)
            this.collectBeanInfos();
        BeanInfo info = this.beanInfoMap.get(name);
        if (info != null)
            return (Class<T>) info.getBeanType();
        return null;
    }
    @Override
    public String getBeanName(Class<?> targetClass) {
        Assert.isNotNull(targetClass, "targetClass is null.");
        if (this.beanInfoMap == null)
            this.collectBeanInfos();
        for (Entry<String, BeanInfo> ent : this.beanInfoMap.entrySet()) {
            if (ent.getValue().getBeanType() == targetClass)
                return ent.getKey();
        }
        return null;
    }
    @Override
    public String[] getBeanNames() {
        if (this.beanInfoMap == null)
            this.collectBeanInfos();
        return this.beanInfoMap.keySet().toArray(new String[this.beanInfoMap.size()]);
    }
    @Override
    public BeanInfo getBeanInfo(String name) {
        if (this.beanInfoMap == null)
            this.collectBeanInfos();
        return this.beanInfoMap.get(name);
    }
    private void collectBeanInfos() {
        this.beanInfoMap = new HashMap<String, BeanInfo>();
        TypeLiteral<BeanInfo> INFO_DEFS = TypeLiteral.get(BeanInfo.class);
        for (Binding<BeanInfo> entry : this.getGuice().findBindingsByType(INFO_DEFS)) {
            BeanInfo beanInfo = entry.getProvider().get();
            this.beanInfoMap.put(beanInfo.getName(), beanInfo);
        }
    }
    @Override
    public <T> T getBean(String name) {
        BeanInfo beanInfo = this.getBeanInfo(name);
        if (beanInfo == null)
            return null;
        return (T) this.getGuice().getInstance(beanInfo.getKey());
    };
}