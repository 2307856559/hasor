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
package org.platform.icache.support;
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
import org.platform.context.AppContext;
import org.platform.context.Config;
import org.platform.context.InitListener;
import org.platform.context.support.AbstractModuleListener;
import org.platform.icache.Cache;
import org.platform.icache.CacheManager;
import org.platform.icache.DefaultCache;
import org.platform.icache.DefaultKeyBuilder;
import org.platform.icache.ICache;
import org.platform.icache.IKeyBuilder;
import org.platform.icache.KeyBuilder;
import org.platform.icache.NeedCache;
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
@InitListener(displayName = "CacheModuleServiceListener", description = "org.platform.icache���������֧�֡�", startIndex = -100)
public class CacheModuleServiceListener extends AbstractModuleListener {
    private CacheManager  cacheManager = null;
    private CacheSettings settings     = null;
    /**��ʼ��.*/
    @Override
    public void initialize(ApiBinder event) {
        //1.����Aop
        event.getGuiceBinder().bindInterceptor(new ClassNeedCacheMatcher(), new MethodPowerMatcher(), new CacheInterceptor());
        /*�����ļ�������*/
        this.settings = new CacheSettings();
        this.settings.loadConfig(event.getSettings());
        event.getGuiceBinder().bind(CacheSettings.class).toInstance(this.settings);
        //2.���뻺������
        this.loadCache(event);
        this.loadKeyBuilder(event);
        //3.ע��Manager
        event.getGuiceBinder().bind(CacheManager.class).to(DefaultCacheManager.class).asEagerSingleton();
    }
    @Override
    public void initialized(AppContext appContext) {
        Config systemConfig = appContext.getInitContext().getConfig();
        systemConfig.addSettingsListener(this.settings);
        //
        this.cacheManager = appContext.getInstance(CacheManager.class);
        this.cacheManager.initManager(appContext);
        Platform.info("online ->> cache is %s", (this.settings.isCacheEnable() ? "enable." : "disable."));
    }
    @Override
    public void destroy(AppContext appContext) {
        Config systemConfig = appContext.getInitContext().getConfig();
        systemConfig.removeSettingsListener(this.settings);
        //
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
                Platform.warning("loadKeyBuilder : not implemented IKeyBuilder of type %s.", cls);
            } else
                iKeyBuilderList.add((Class<? extends IKeyBuilder>) cls);
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
            KeyBuilderDefinition keyBuilderDefine = new KeyBuilderDefinition(keyBuilderAnno.value(), keyBuilderKey);
            binder.bind(KeyBuilderDefinition.class).annotatedWith(UniqueAnnotations.create()).toInstance(keyBuilderDefine);
            Platform.info("KeyBuilder type:" + Platform.logString(keyBuildertype) + " mapping " + Platform.logString(keyBuilderAnno.value()));
            //ȷ���Ƿ�Ϊdefaut
            if (keyBuildertype.isAnnotationPresent(DefaultKeyBuilder.class) == true) {
                Platform.warning("KeyBuilder type:" + Platform.logString(keyBuildertype) + " is DefaultKeyBuilder on " + Platform.logString(keyBuilderAnno.value()));
                DefaultKeyBuilder defaultKeyBuilder = keyBuildertype.getAnnotation(DefaultKeyBuilder.class);
                if (defaultKeyBuilder.value() <= defaultKeyBuilderIndex/*��ԽСԽ����*/) {
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
        List<Class<ICache>> cacheList = new ArrayList<Class<ICache>>();
        for (Class<?> cls : cacheSet) {
            if (ICache.class.isAssignableFrom(cls) == false) {
                Platform.warning("loadCache : not implemented ICache of type %s", cls);
            } else
                cacheList.add((Class<ICache>) cls);
        }
        //3.ע�����
        long defaultCacheIndex = Long.MAX_VALUE;
        Binder binder = event.getGuiceBinder();
        Map<String, Integer> cacheIndex = new HashMap<String, Integer>();
        for (Class<ICache> cacheType : cacheList) {
            Cache cacheAnno = cacheType.getAnnotation(Cache.class);
            for (String cacheName : cacheAnno.value()) {
                Platform.info(cacheName + " at Cache of type " + Platform.logString(cacheType));
                //
                int maxIndex = (cacheIndex.containsKey(cacheName) == false) ? Integer.MAX_VALUE : cacheIndex.get(cacheName);
                if (cacheAnno.sort() <= maxIndex/*��ԽСԽ����*/) {
                    cacheIndex.put(cacheName, cacheAnno.sort());
                    //
                    CacheDefinition cacheDefine = new CacheDefinition(cacheName, cacheType);
                    binder.bind(CacheDefinition.class).annotatedWith(Names.named(cacheName)).toInstance(cacheDefine);
                    binder.bind(ICache.class).annotatedWith(Names.named(cacheName)).toProvider(cacheDefine);
                    //ȷ���Ƿ�Ϊdefaut
                    if (cacheType.isAnnotationPresent(DefaultCache.class) == true) {
                        Platform.warning(cacheName + " is DefaultCache!");
                        DefaultCache defaultCache = cacheType.getAnnotation(DefaultCache.class);
                        if (defaultCache.value() <= defaultCacheIndex/*��ԽСԽ����*/) {
                            defaultCacheIndex = defaultCache.value();
                            binder.bind(ICache.class).toProvider(cacheDefine);
                        }
                    }
                }
            }
        }
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
            Platform.debug("MethodInterceptor Method : %s", targetMethod);
            Platform.debug("MethodInterceptor Cache key :%s", cacheKey.toString());
            //3.��ȡ����
            ICache<Object> cacheObject = null;
            if (StringUtil.isBlank(cacheAnno.cacheName()) == true)
                cacheObject = cacheManager.getDefaultCache();
            else
                cacheObject = cacheManager.getCache(cacheAnno.cacheName());
            //4.��������
            String key = cacheKey.toString();
            Object returnData = null;
            if (cacheObject.hasCache(key) == true) {
                Platform.debug("the method return data is from Cache.");
                returnData = cacheObject.fromCache(key);
            } else {
                Platform.debug("set data to Cache key :" + key);
                returnData = invocation.proceed();
                cacheObject.toCache(key, returnData, cacheAnno.timeout());
            }
            return returnData;
        }
    }
}