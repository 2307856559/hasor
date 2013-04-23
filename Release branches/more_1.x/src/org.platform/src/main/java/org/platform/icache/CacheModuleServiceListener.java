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
package org.platform.icache;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.more.util.StringUtil;
import org.platform.Platform;
import org.platform.binder.ApiBinder;
import org.platform.context.AbstractModuleListener;
import org.platform.context.AppContext;
import org.platform.context.InitListener;
import org.platform.context.setting.Config;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.internal.UniqueAnnotations;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.name.Names;
/**
 * �������
 * @version : 2013-4-8
 * @author ������ (zyc@byshell.org)
 */
@InitListener(displayName = "CacheModuleServiceListener", description = "org.platform.icache���������֧�֡�", startIndex = 0)
public class CacheModuleServiceListener extends AbstractModuleListener {
    private CacheManager  cacheManager = null;
    private CacheSettings settings     = new CacheSettings();
    /**��ʼ��.*/
    @Override
    public void initialize(ApiBinder event) {
        //1.����Aop
        event.getGuiceBinder().bindInterceptor(new ClassNeedCacheMatcher(), new MethodPowerMatcher(), new CacheInterceptor());
        /*�����ļ�������*/
        Config systemConfig = event.getInitContext().getConfig();
        systemConfig.addSettingsListener(this.settings);
        this.settings.loadConfig(systemConfig.getSettings());
        //2.���뻺������
        this.loadCache(event);
        this.loadKeyBuilder(event);
        //3.ע��Manager
        event.getGuiceBinder().bind(CacheManager.class).to(DefaultCacheManager.class).asEagerSingleton();
    }
    @Override
    public void initialized(AppContext appContext) {
        this.cacheManager = appContext.getBean(CacheManager.class);
        this.cacheManager.initManager(appContext);
        Platform.info("online ->> cache is " + (this.settings.isCacheEnable() ? "enable." : "disable."));
    }
    @Override
    public void destroy(AppContext appContext) {
        Config systemConfig = appContext.getInitContext().getConfig();
        systemConfig.removeSettingsListener(this.settings);
        this.cacheManager.destroyManager(appContext);
    }
    //
    /*װ��KeyBuilder*/
    protected void loadKeyBuilder(ApiBinder event) {
        Platform.info("begin loadKeyBuilder...");
        //1.��ȡ
        Set<Class<?>> iKeyBuilderSet = event.getClassSet(KeyBuilder.class);
        List<Class<? extends IKeyBuilder>> iKeyBuilderList = new ArrayList<Class<? extends IKeyBuilder>>();
        for (Class<?> cls : iKeyBuilderSet) {
            if (IKeyBuilder.class.isAssignableFrom(cls) == false) {
                Platform.warning("loadKeyBuilder : not implemented IKeyBuilder of type " + Platform.logString(cls));
            } else {
                Platform.info("at KeyBuilder of type " + Platform.logString(cls));
                iKeyBuilderList.add((Class<? extends IKeyBuilder>) cls);
            }
        }
        //2.����
        Collections.sort(iKeyBuilderList, new Comparator<Class<?>>() {
            @Override
            public int compare(Class<?> o1, Class<?> o2) {
                KeyBuilder o1Anno = o1.getAnnotation(KeyBuilder.class);
                KeyBuilder o2Anno = o2.getAnnotation(KeyBuilder.class);
                int o1AnnoIndex = o1Anno.sort();
                int o2AnnoIndex = o2Anno.sort();
                return (o1AnnoIndex < o2AnnoIndex ? -1 : (o1AnnoIndex == o2AnnoIndex ? 0 : 1));
            }
        });
        //3.ע�����
        long defaultKeyBuilderIndex = Long.MAX_VALUE;
        Binder binder = event.getGuiceBinder();
        for (Class<? extends IKeyBuilder> keyBuildertype : iKeyBuilderList) {
            KeyBuilder keyBuilderAnno = keyBuildertype.getAnnotation(KeyBuilder.class);
            Key<? extends IKeyBuilder> keyBuilderKey = Key.get(keyBuildertype);
            Map<String, String> initParams = this.toMap(keyBuilderAnno.initParams());
            KeyBuilderDefinition keyBuilderDefine = new KeyBuilderDefinition(keyBuilderAnno.value(), keyBuilderKey, initParams);
            binder.bind(KeyBuilderDefinition.class).annotatedWith(UniqueAnnotations.create()).toInstance(keyBuilderDefine);
            //ȷ���Ƿ�Ϊdefaut
            if (keyBuildertype.isAnnotationPresent(DefaultKeyBuilder.class) == true) {
                Platform.info("at DefaultKeyBuilder of type " + Platform.logString(keyBuildertype));
                DefaultKeyBuilder defaultKeyBuilder = keyBuildertype.getAnnotation(DefaultKeyBuilder.class);
                if (defaultKeyBuilder.value() < defaultKeyBuilderIndex/*��ԽСԽ����*/) {
                    defaultKeyBuilderIndex = defaultKeyBuilder.value();
                    binder.bind(IKeyBuilder.class).toProvider(keyBuilderDefine);
                }
            }
        }
    }
    //
    /*װ��Cache*/
    protected void loadCache(ApiBinder event) {
        Platform.info("begin loadCache...");
        //1.��ȡ
        Set<Class<?>> cacheSet = event.getClassSet(Cache.class);
        List<Class<? extends ICache>> cacheList = new ArrayList<Class<? extends ICache>>();
        for (Class<?> cls : cacheSet) {
            if (ICache.class.isAssignableFrom(cls) == false) {
                Platform.warning("loadCache : not implemented ICache of type " + Platform.logString(cls));
            } else {
                Platform.info("at Cache of type " + Platform.logString(cls));
                cacheList.add((Class<? extends ICache>) cls);
            }
        }
        //3.ע�����
        long defaultCacheIndex = Long.MAX_VALUE;
        Binder binder = event.getGuiceBinder();
        for (Class<? extends ICache> cacheType : cacheList) {
            Cache cacheAnno = cacheType.getAnnotation(Cache.class);
            Key<? extends ICache> cacheKey = Key.get(cacheType);
            Map<String, String> initParams = this.toMap(cacheAnno.initParams());
            CacheDefinition cacheDefine = new CacheDefinition(cacheAnno.value(), cacheKey, initParams);
            for (String cacheName : cacheAnno.value()) {
                binder.bind(CacheDefinition.class).annotatedWith(Names.named(cacheName)).toInstance(cacheDefine);
                binder.bind(ICache.class).annotatedWith(Names.named(cacheName)).toProvider(cacheDefine);
            }
            //ȷ���Ƿ�Ϊdefaut
            if (cacheType.isAnnotationPresent(DefaultCache.class) == true) {
                Platform.info("at DefaultCache of type " + Platform.logString(cacheType));
                DefaultCache defaultCache = cacheType.getAnnotation(DefaultCache.class);
                if (defaultCache.value() < defaultCacheIndex/*��ԽСԽ����*/) {
                    defaultCacheIndex = defaultCache.value();
                    binder.bind(ICache.class).toProvider(cacheDefine);
                }
            }
        }
    }
    //
    /*ת������*/
    protected Map<String, String> toMap(InitParam[] initParams) {
        Map<String, String> initMap = new HashMap<String, String>();
        if (initParams != null)
            for (InitParam param : initParams)
                if (StringUtil.isBlank(param.name()) == false)
                    initMap.put(param.name(), param.value());
        return initMap;
    }
    /*-------------------------------------------------------------------------------------*/
    /*���������Ƿ�ƥ�䡣����ֻҪ���ͻ򷽷��ϱ����@NeedCache��*/
    private class ClassNeedCacheMatcher extends AbstractMatcher<Class<?>> {
        @Override
        public boolean matches(Class<?> matcherType) {
            /*������ڽ���״̬����Ի�����*/
            if (settings.isCacheEnable() == false)
                return false;
            /*----------------------------*/
            if (matcherType.isAnnotationPresent(NeedCache.class) == true)
                return true;
            Method[] m1s = matcherType.getMethods();
            Method[] m2s = matcherType.getDeclaredMethods();
            for (Method m1 : m1s) {
                if (m1.isAnnotationPresent(NeedCache.class) == true)
                    return true;
            }
            for (Method m2 : m2s) {
                if (m2.isAnnotationPresent(NeedCache.class) == true)
                    return true;
            }
            return false;
        }
    }
    /*�����ⷽ���Ƿ�ƥ�䡣���򣺷����򷽷��������ϱ����@NeedCache��*/
    private class MethodPowerMatcher extends AbstractMatcher<Method> {
        @Override
        public boolean matches(Method matcherType) {
            /*������ڽ���״̬����Ի�����*/
            if (settings.isCacheEnable() == false)
                return false;
            /*----------------------------*/
            if (matcherType.isAnnotationPresent(NeedCache.class) == true)
                return true;
            if (matcherType.getDeclaringClass().isAnnotationPresent(NeedCache.class) == true)
                return true;
            return false;
        }
    }
    /*������*/
    private class CacheInterceptor implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            /*������ڽ���״̬����Ի�����*/
            if (settings.isCacheEnable() == false)
                return invocation.proceed();
            /*----------------------------*/
            //1.��ȡ��������
            Method targetMethod = invocation.getMethod();
            NeedCache cacheAnno = targetMethod.getAnnotation(NeedCache.class);
            if (cacheAnno == null)
                cacheAnno = targetMethod.getDeclaringClass().getAnnotation(NeedCache.class);
            if (cacheAnno == null)
                return invocation.proceed();
            //2.��ȡKey
            StringBuilder cacheKey = new StringBuilder(targetMethod.toString());
            Object[] args = invocation.getArguments();
            if (args != null)
                for (Object arg : args) {
                    if (arg == null) {
                        cacheKey.append("NULL");
                        continue;
                    }
                    /*��֤arg������Ϊ��*/
                    IKeyBuilder keyBuilder = cacheManager.getKeyBuilder(arg.getClass());
                    cacheKey.append(keyBuilder.serializeKey(arg));
                }
            Platform.debug("MethodInterceptor Method : " + targetMethod.toString());
            Platform.debug("MethodInterceptor Cache key :" + Platform.logString(cacheKey.toString()));
            //3.��������
            ICache cacheObject = null;
            if (StringUtil.isBlank(cacheAnno.cacheName()) == true)
                cacheObject = cacheManager.getDefaultCache();
            else
                cacheObject = cacheManager.getCache(cacheAnno.cacheName());
            //
            String key = cacheKey.toString();
            if (cacheObject.hasCache(key) == true) {
                Platform.debug("the method return data is from Cache.");
                return cacheObject.fromCache(key);
            } else {
                Platform.debug("set data to Cache key :" + key);
                Object res = invocation.proceed();
                cacheObject.toCache(key, res, cacheAnno.timeout());
                return res;
            }
        }
    }
}