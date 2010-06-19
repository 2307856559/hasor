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
package org.test.more.classcodeTest;
import java.util.Date;
import org.more.core.classcode.AOPFilterChain;
import org.more.core.classcode.AOPInvokeFilter;
import org.more.core.classcode.AOPMethods;
import org.more.core.classcode.ClassEngine;
import org.more.log.LogFactory;
/**
 * classcode
 * Date : 2009-11-2
 * @author Administrator
 */
class ImplAOPInvokeFilter implements AOPInvokeFilter {
    @Override
    public Object doFilter(Object target, AOPMethods methods, Object[] args, AOPFilterChain chain) throws Throwable {
        return chain.doInvokeFilter(target, methods, args);
    }
}
////������ cglib
//class TestCallbackFilter implements CallbackFilter {
//    // ֻ�о�������������0�ķ����Żر�,����������
//    public int accept(Method method) {
//        return 1;
//    }
//}
////������,��������ʵ��AOP
//class TestAuthorizationInterceptor implements MethodInterceptor {
//    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
//        return methodProxy.invokeSuper(object, args);
//    }
//}
/**
 * 
 * Date : 2009-10-30
 * @author Administrator
 */
public class MainTest {
    public static void main(String[] args) throws Exception {
        LogFactory.nowMode = LogFactory.Mode_Run;
        /*                  ���󴴽����ܲ���*/
        //        ClassEngine ce = new ClassEngine();
        //        ce.setSuperClass(MethodPropxyMap.class);
        //        ce.setCallBacks(new AOPInvokeFilter[] { new ImplAOPInvokeFilter() });
        //        ce.setEnableAOP(false);
        //                long start = new Date().getTime();
        //                for (int i = 0; i < 500000; i++) {
        //                    //new MethodPropxyMap();
        //                    Object obj1 = ce.newInstance(null);
        //                }
        //                long end = new Date().getTime();
        //                System.out.println("time:" + (end - start) + "\tClassEngine Create");
        //        //=================================================================
        //        long start_1 = new Date().getTime();
        //        for (int i = 0; i < 1000; i++) {
        //            Enhancer enhancer = new Enhancer();// CGLib�����õĻ�����
        //            enhancer.setSuperclass(MethodPropxyMap.class);// Ҫ�������
        //            CallbackFilter callbackFilter = new TestCallbackFilter();// ������,�������Ĺ����ǹ��˵�����Ҫ���������صķ���.��ȷ�����������صĶ�����ҪAOP�ķ���
        //            enhancer.setCallbackFilter(callbackFilter);//���ù�����
        //            Callback saveCallback = new TestAuthorizationInterceptor();// ������,���������ص��ķ����Ǿ���'������'����֮���,���������з������ᱻ����������
        //            Callback loadCallback = NoOp.INSTANCE;
        //            Callback[] callbacks = { loadCallback, saveCallback };
        //            enhancer.setCallbacks(callbacks);
        //            Object obj2 = enhancer.create();
        //        }
        //        long end_1 = new Date().getTime();
        //        System.out.println("time:" + (end_1 - start_1) + "\tCgLib Create");
        //=================================================================
        /*                  AOP�������ܲ���*/
        //=================================================================
        ClassEngine ce = new ClassEngine();
        ce.setSuperClass(MethodPropxyMap.class);
        ce.setCallBacks(new AOPInvokeFilter[] { new ImplAOPInvokeFilter() });
        ce.setEnableAOP(true);
        Object obj1 = ce.newInstance(null);
        long start_1 = new Date().getTime();
        MethodPropxyMap map_1 = (MethodPropxyMap) obj1;
        for (int i = 0; i < 100000; i++)
            map_1.aaa();
        long end_1 = new Date().getTime();
        System.out.println("time:" + (end_1 - start_1) + "\tClassEngine AOP");
        //=================================================================
        //        Enhancer enhancer = new Enhancer();// CGLib�����õĻ�����
        //        enhancer.setSuperclass(MethodPropxyMap.class);// Ҫ�������
        //        CallbackFilter callbackFilter = new TestCallbackFilter();// ������,�������Ĺ����ǹ��˵�����Ҫ���������صķ���.��ȷ�����������صĶ�����ҪAOP�ķ���
        //        enhancer.setCallbackFilter(callbackFilter);//���ù�����
        //        Callback saveCallback = new TestAuthorizationInterceptor();// ������,���������ص��ķ����Ǿ���'������'����֮���,���������з������ᱻ����������
        //        Callback loadCallback = NoOp.INSTANCE;
        //        Callback[] callbacks = { loadCallback, saveCallback, saveCallback };
        //        enhancer.setCallbacks(callbacks);
        //        Object obj2 = enhancer.create();
        //        long start_2 = new Date().getTime();
        //        MethodPropxyMap map_2 = (MethodPropxyMap) obj2;
        //        for (int i = 0; i < 100000; i++)
        //            map_2.aaa();
        //        long end_2 = new Date().getTime();
        //        System.out.println("time:" + (end_2 - start_2) + "\tCGLib AOP");
        //=================================================================
        /*                  ����*/
        //=================================================================
        //                ClassEngine ce = new ClassEngine();
        //                ce.setSuperClass(MethodPropxyMap.class);
        //                ce.setCallBacks(new AOPInvokeFilter[] { new ImplAOPInvokeFilter() });
        //                ce.setEnableAOP(true);
        //                ce.builderClass();
        //                //������ļ�
        //                FileOutputStream fso = new FileOutputStream(ce.getSimpleName() + ".class");
        //                fso.write(ce.toBytes());
        //                fso.flush();
        //                fso.close();
        //                Object obj1 = ce.newInstance(null);
        //                MethodPropxyMap m = (MethodPropxyMap) obj1;
        //                m.aaa(0, 0, 0, 0, null, 0);
        //                java.lang.reflect.Method m2 = ce.toClass().getMethod("set$More_AOPFilterChain", ImplAOPFilterChain.class);
    }
}