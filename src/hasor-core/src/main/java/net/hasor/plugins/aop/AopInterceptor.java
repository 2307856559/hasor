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
package net.hasor.plugins.aop;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import net.hasor.core.ApiBinder;
import net.hasor.core.AppContext;
import net.hasor.core.AppContextAware;
import net.hasor.plugins.aop.GlobalAop.RegType;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.more.util.MatchUtils;
/**
 * Aop������
 * @version : 2013-11-8
 * @author ������(zyc@hasor.net)
 */
class AopInterceptor implements MethodInterceptor, AppContextAware {
    private AppContext                                                    appContext           = null;
    private List<Class<? extends MethodInterceptor>>                      globalList           = null;
    private WeakHashMap<Method, List<Class<? extends MethodInterceptor>>> methodInterceptorMap = new WeakHashMap<Method, List<Class<? extends MethodInterceptor>>>();
    //
    public AopInterceptor(List<Class<? extends MethodInterceptor>> globalList, ApiBinder apiBinder) {
        this.globalList = globalList;
        apiBinder.registerAware(this);
    }
    //
    public void setAppContext(AppContext appContext) {
        this.appContext = appContext;
    }
    //
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method targetMethod = invocation.getMethod();
        List<Class<? extends MethodInterceptor>> list = this.methodInterceptorMap.get(targetMethod);
        //1.ȡ����������
        if (list == null) {
            list = new ArrayList<Class<? extends MethodInterceptor>>();
            //a.ȫ��������
            String fullName = targetMethod.toGenericString();
            for (Class<? extends MethodInterceptor> global : globalList) {
                GlobalAop gaop = global.getAnnotation(GlobalAop.class);
                boolean match = false;
                if (RegType.Regexp == gaop.regType())
                    match = fullName.matches(gaop.value());//ʹ��������ʽ
                else if (RegType.Wildcard == gaop.regType())
                    match = MatchUtils.matchWild(gaop.value(), fullName);//ʹ��ͨ���
                //
                if (match)
                    list.add(global);
            }
            //b.�༶������
            Aop beforeAnno = targetMethod.getDeclaringClass().getAnnotation(Aop.class);
            if (beforeAnno != null) {
                for (Class<? extends MethodInterceptor> interType : beforeAnno.value())
                    if (interType != null)
                        list.add(interType);
            }
            //c.������������
            beforeAnno = targetMethod.getAnnotation(Aop.class);
            if (beforeAnno != null) {
                for (Class<? extends MethodInterceptor> interType : beforeAnno.value())
                    if (interType != null)
                        list.add(interType);
            }
            //d.������
            this.methodInterceptorMap.put(targetMethod, list);
        }
        //2.��������
        return new AopChainInvocation(appContext, list, invocation).invoke(invocation);
    }
}