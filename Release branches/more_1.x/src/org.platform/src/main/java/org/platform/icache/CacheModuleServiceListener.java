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
import static org.platform.PlatformConfigEnum.CacheConfig_Enable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.more.global.Global;
import org.more.util.StringUtil;
import org.platform.Platform;
import org.platform.binder.ApiBinder;
import org.platform.context.AbstractModuleListener;
import org.platform.context.AppContext;
import org.platform.context.InitListener;
import org.platform.context.SettingListener;
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
    private boolean      enable       = false; /*Ĭ�Ϲر�״̬*/
    private CacheManager cacheManager = null;
    /**��ʼ��.*/
    @Override
    public void initialize(ApiBinder event) {
        //1.����Aop
        event.getGuiceBinder().bindInterceptor(new ClassNeedCacheMatcher(), new MethodPowerMatcher(), new CacheInterceptor());
        /*�����ļ�������*/
        event.getInitContext().getConfig().addSettingsListener(new SettingListener() {
            @Override
            public void reLoadConfig(Global oldConfig, Global newConfig) {
                enable = newConfig.getBoolean(CacheConfig_Enable, false);
            }
        });
        //2.���뻺������
        this.loadCache(event);
        this.loadKeyBuilder(event);
        //3.ע��Manager
        event.getGuiceBinder().bind(CacheManager.class).to(DefaultCacheManager.class);
    }
    @Override
    public void initialized(AppContext appContext) {
        this.cacheManager = appContext.getBean(CacheManager.class);
        this.cacheManager.initManager(appContext);
    }
    @Override
    public void destroy() {
        this.cacheManager.destroyManager();
    }
    //
    /*װ��KeyBuilder*/
    protected void loadKeyBuilder(ApiBinder event) {
        Platform.warning("cache ->> begin loadKeyBuilder...");
        //1.��ȡ
        Set<Class<?>> iKeyBuilderSet = event.getClassSet(KeyBuilder.class);
        List<Class<? extends IKeyBuilder>> iKeyBuilderList = new ArrayList<Class<? extends IKeyBuilder>>();
        for (Class<?> cls : iKeyBuilderSet) {
            if (IKeyBuilder.class.isAssignableFrom(cls) == false) {
                Platform.warning("cache ->>loadKeyBuilder : not implemented IKeyBuilder of type " + Platform.logString(cls));
            } else {
                Platform.info("cache ->> at KeyBuilder of type " + Platform.logString(cls));
                iKeyBuilderList.add((Class<? extends IKeyBuilder>) cls);
            }
        }
        //3.ע�����
        Binder binder = event.getGuiceBinder();
        for (Class<? extends IKeyBuilder> keyBuildertype : iKeyBuilderList) {
            KeyBuilder keyBuilderAnno = keyBuildertype.getAnnotation(KeyBuilder.class);
            Key<? extends IKeyBuilder> keyBuilderKey = Key.get(keyBuildertype);
            Map<String, String> initParams = this.toMap(keyBuilderAnno.initParams());
            KeyBuilderDefinition keyBuilderDefine = new KeyBuilderDefinition(keyBuilderAnno.value(), keyBuilderKey, initParams);
            //
            binder.bind(KeyBuilderDefinition.class).annotatedWith(UniqueAnnotations.create()).toInstance(keyBuilderDefine);
            //ȷ���Ƿ�Ϊdefaut
            if (keyBuildertype.isAnnotationPresent(DefaultKeyBuilder.class) == true) {
                Platform.info("cache ->> at DefaultKeyBuilder of type " + Platform.logString(keyBuildertype));
                binder.bind(IKeyBuilder.class).annotatedWith(DefaultKeyBuilder.class).toProvider(keyBuilderDefine);
            }
        }
    }
    //
    /*װ��Cache*/
    protected void loadCache(ApiBinder event) {
        Platform.warning("cache ->> begin loadCache...");
        //1.��ȡ
        Set<Class<?>> cacheSet = event.getClassSet(Cache.class);
        List<Class<? extends ICache>> cacheList = new ArrayList<Class<? extends ICache>>();
        for (Class<?> cls : cacheSet) {
            if (ICache.class.isAssignableFrom(cls) == false) {
                Platform.warning("cache ->> loadCache : not implemented ICache of type " + Platform.logString(cls));
            } else {
                Platform.info("cache ->> at Cache of type " + Platform.logString(cls));
                cacheList.add((Class<? extends ICache>) cls);
            }
        }
        //3.ע�����
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
                Platform.info("cache ->> at DefaultCache of type " + Platform.logString(cacheType));
                binder.bind(ICache.class).annotatedWith(DefaultCache.class).toProvider(cacheDefine);
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
            if (enable == false)
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
            if (enable == false)
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
            if (enable == false)
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
                    IKeyBuilder keyBuilder = cacheManager.getKeyBuilder((arg != null) ? arg.getClass() : null);
                    cacheKey.append(keyBuilder.serializeKey(arg));
                }
            Platform.debug("cache ->> MethodInterceptor Method : " + targetMethod.toString());
            Platform.debug("cache ->> MethodInterceptor Cache key :" + Platform.logString(cacheKey.toString()));
            //3.��������
            ICache cacheObject = cacheManager.getCache(cacheAnno.cacheName());
            String key = cacheKey.toString();
            if (cacheObject.hasCache(key) == true) {
                Platform.debug("cache ->> the method return data is from Cache.");
                return cacheObject.fromCache(key);
            } else {
                Platform.debug("cache ->> set data to Cache key :" + key);
                Object res = invocation.proceed();
                cacheObject.toCache(key, res, cacheAnno.timeout());
                return res;
            }
        }
    }
}