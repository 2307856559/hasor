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
package org.more.core.classcode;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
/**
 * �������������������{@link ClassBuilder}���ɵ����ࡣ
 * @version 2010-9-3
 * @author ������ (zyc@byshell.org)
 */
public class ClassConfiguration {
    private ClassBuilder      classBuilder             = null;
    //
    private ArrayList<String> renderMethodList         = null; //����ί�нӿڵķ������飬�Ǿ���˳��ġ�
    private ArrayList<String> renderDelegateList       = null; //ί�нӿ����飬�Ǿ���˳��ġ�
    private ArrayList<String> renderDelegatePropxyList = null; //ί���������飬�Ǿ���˳��ġ�
    private ArrayList<String> renderAopMethodList      = null; //����Aop���Եķ������飬�Ǿ���˳��ġ�
    //
    /**����ClassConfiguration����*/
    ClassConfiguration(ClassBuilder classBuilder, BuilderClassAdapter builderAdapter, AopClassAdapter aopAdapter) {
        this.classBuilder = classBuilder;
        //��ȡ��ִ����������ʱ��Ĳ�����Ϣ��
        this.renderMethodList = builderAdapter.getRenderMethodList();
        this.renderDelegateList = builderAdapter.getRenderDelegateList();
        this.renderDelegatePropxyList = builderAdapter.getRenderDelegatePropxyList();
        //��ȡaop������Ϣ��
        if (aopAdapter != null)
            this.renderAopMethodList = aopAdapter.getRenderAopMethodList();
    }
    /**����Bean����*/
    public Object configBean(Object obj) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        ClassEngine classEngine = classBuilder.getClassEngine();
        Class<?> beanClass = obj.getClass();
        //
        //1.ע��DelegateArrayName��DelegateMethodArrayName
        if (this.classBuilder.isAddDelegate() == true) {
            //1.��ȡMethodDelegate��delegateTypes���������˳����renderDelegateList������
            Class<?>[] delegateTypes = new Class<?>[renderDelegateList.size()];//����ʵ����Ⱦ�Ľӿ�ʵ����Ŀ������Ҫע��Ĵ������顣
            MethodDelegate[] methodDelegates = new MethodDelegate[delegateTypes.length];
            for (Class<?> delType : this.classBuilder.getDelegateType()) {
                int index = renderDelegateList.indexOf(EngineToos.replaceClassName(delType.getName()));
                if (index != -1) {
                    delegateTypes[index] = delType;
                    methodDelegates[index] = classEngine.getDelegate(delType);
                }
            }
            //2.��ȡmethod�������顣
            Method[] methods = new Method[this.renderMethodList.size()];//����ʵ����Ⱦ�Ĵ�����ʵ����Ŀ���������顣
            for (int i = 0; i < delegateTypes.length; i++) {
                methodDelegates[i] = classEngine.getDelegate(delegateTypes[i]);
                for (Method method : delegateTypes[i].getMethods()) {
                    String m_name = method.getName();
                    String m_desc = EngineToos.toAsmType(method.getParameterTypes());
                    String m_return = EngineToos.toAsmType(method.getReturnType());
                    String fullDesc = m_name + "(" + m_desc + ")" + m_return;
                    int index = this.renderMethodList.indexOf(fullDesc);
                    if (index != -1)
                        methods[index] = method;
                }
            }
            //3.ִ��ע��
            Method method_1 = beanClass.getMethod("set" + BuilderClassAdapter.DelegateArrayName, MethodDelegate[].class);
            method_1.invoke(obj, new Object[] { methodDelegates });//ע�����
            Method method_2 = beanClass.getMethod("set" + BuilderClassAdapter.DelegateMethodArrayName, Method[].class);
            method_2.invoke(obj, new Object[] { methods });//ע�뷽��
        }
        //2.ע���ֶ�
        if (this.classBuilder.isAddFields() == true) {
            String[] delegateFields = this.classBuilder.getDelegateFields();
            if (delegateFields != null) {
                PropertyDelegate<?>[] delegateProperty = new PropertyDelegate<?>[this.renderDelegatePropxyList.size()];
                for (String field : delegateFields) {
                    int index = this.renderDelegatePropxyList.indexOf(field);
                    if (index != -1)
                        delegateProperty[index] = classEngine.getDelegateProperty(field);
                }
                Method method = beanClass.getMethod("set" + BuilderClassAdapter.PropertyArrayName, PropertyDelegate[].class);
                method.invoke(obj, new Object[] { delegateProperty });//ע���������
            }//end if
        }
        //3.ע��AOP
        if (this.classBuilder.isRenderAop() == true) {
            AopStrategy aopStrategy = classEngine.getAopStrategy();
            //(1)׼������
            Method[] aopMethodArray = new Method[this.renderAopMethodList.size()];//����ʵ����Ⱦ��aop������Ŀ���������顣
            AopFilterChain_Start[] aopFilterChain = new AopFilterChain_Start[this.renderAopMethodList.size()];//����ʵ����Ⱦ��aop������Ŀ���������顣
            //
            AopBeforeListener[] aopBeforeListener = classEngine.getAopBeforeListeners();
            AopReturningListener[] aopReturningListener = classEngine.getAopReturningListeners();
            AopThrowingListener[] aopThrowingListener = classEngine.getAopThrowingListeners();
            AopInvokeFilter[] aopInvokeFilter = classEngine.getAopFilters();
            //(3)����ע������
            for (int i = 0; i < this.renderAopMethodList.size(); i++) {
                ArrayList<Method> methodArrays = EngineToos.findAllMethod(beanClass);
                for (Method m : methodArrays) {
                    String m_name = m.getName();
                    String m_desc = EngineToos.toAsmType(m.getParameterTypes());
                    String m_return = EngineToos.toAsmType(m.getReturnType());
                    String fullDesc = m_name + "(" + m_desc + ")" + m_return;
                    int index = this.renderAopMethodList.indexOf(fullDesc);
                    if (index != -1) {
                        aopMethodArray[index] = m;
                        //ִ�з�����aop���ԡ�
                        AopBeforeListener[] _aopBeforeListener = aopStrategy.filterAopBeforeListener(obj, m, aopBeforeListener);
                        AopReturningListener[] _aopReturningListener = aopStrategy.filterAopReturningListener(obj, m, aopReturningListener);
                        AopThrowingListener[] _aopThrowingListener = aopStrategy.filterAopThrowingListener(obj, m, aopThrowingListener);
                        AopInvokeFilter[] _aopInvokeFilter = aopStrategy.filterAopInvokeFilter(obj, m, aopInvokeFilter);
                        //�����������
                        AopFilterChain nextFilterChain = new AopFilterChain_End() {};
                        if (_aopInvokeFilter != null)
                            for (int j = 0; j < _aopInvokeFilter.length; j++)
                                nextFilterChain = new AopFilterChain_Impl(_aopInvokeFilter[j], nextFilterChain);
                        //�������������󻷽ڡ�
                        aopFilterChain[i] = new AopFilterChain_Start(nextFilterChain, _aopBeforeListener, _aopReturningListener, _aopThrowingListener);
                    }
                }
                //
            }
            //(4)ע��
            Method method_1 = beanClass.getMethod("set" + AopClassAdapter.AopFilterChainName, AopFilterChain_Start[].class);
            method_1.invoke(obj, new Object[] { aopFilterChain });//ע�����
            Method method_2 = beanClass.getMethod("set" + AopClassAdapter.AopMethodArrayName, Method[].class);
            method_2.invoke(obj, new Object[] { aopMethodArray });//ע�뷽��
        }
        return obj;
    }
}