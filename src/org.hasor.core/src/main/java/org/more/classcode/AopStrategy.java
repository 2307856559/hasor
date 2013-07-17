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
package org.more.classcode;
import java.lang.reflect.Method;
/**
 * Aop���ɲ��ԣ�����ͨ���ýӿ���ȷ��aop��������ɲ��ԡ�ֻ��{@link ClassEngine}����������
 * {@link AopBeforeListener}��{@link AopReturningListener}��{@link AopThrowingListener}
 * ��������ӿڻ���{@link AopInvokeFilter}�������ӿ�ʱ�Ż�����������ڼ�����aop��֧�֡�<br/>
 * ͨ���ýӿ��еķ�������ȷ��ĳ�������Ƿ����aop�����ɣ�ͬʱ�����Կ������ɵ�aop��������������aop���������󼯺ϡ�
 * @version 2010-9-3
 * @author ������ (zyc@byshell.org)
 */
public interface AopStrategy extends BaseStrategy {
    /**
     * ��{@link ClassEngine}ɨ�����ڼ䣬���������aop������ÿ���ͨ���÷�����ȷ����������������Ƿ����aop��װ��
     * �����Ҫ���Է���true���򷵻�false��
     * @param superClass �ò������ڱ�ʾ�������ֵĸ÷������������͡�
     * @param ignoreMethod �ò������ڱ�ʾ�����ֵķ�����
     * @return �����Ƿ�������������aop��װ�������Ҫ���Է���true���򷵻�false��
     */
    public boolean isIgnore(Class<?> superClass, Method ignoreMethod);
    /**
     * �÷���������bean��ʱ��ᱻ���á�ͨ���÷������Ծ���������������Ч{@link AopBeforeListener}���ϡ�
     * @param target ��װ������
     * @param method ��Ҫaop����ķ�����
     * @param beforeListener ������Ч��{@link AopBeforeListener}���ϣ�ͨ���÷������Խ��ı����������Ч�Ķ��󼯡�
     * @return ����������Ч��{@link AopBeforeListener}���󼯺ϡ�
     */
    public AopBeforeListener[] filterAopBeforeListener(Object target, Method method, AopBeforeListener[] beforeListener);
    /**
     * �÷���������bean��ʱ��ᱻ���á�ͨ���÷������Ծ���������������Ч{@link AopReturningListener}���ϡ�
     * @param target ��װ������
     * @param method ��Ҫaop����ķ�����
     * @param returningListener ������Ч��{@link AopReturningListener}���ϣ�ͨ���÷������Խ��ı����������Ч�Ķ��󼯡�
     * @return ����������Ч��{@link AopReturningListener}���󼯺ϡ�
     */
    public AopReturningListener[] filterAopReturningListener(Object target, Method method, AopReturningListener[] returningListener);
    /**
     * �÷���������bean��ʱ��ᱻ���á�ͨ���÷������Ծ���������������Ч{@link AopThrowingListener}���ϡ�
     * @param target ��װ������
     * @param method ��Ҫaop����ķ�����
     * @param throwingListener ������Ч��{@link AopThrowingListener}���ϣ�ͨ���÷������Խ��ı����������Ч�Ķ��󼯡�
     * @return ����������Ч��{@link AopThrowingListener}���󼯺ϡ�
     */
    public AopThrowingListener[] filterAopThrowingListener(Object target, Method method, AopThrowingListener[] throwingListener);
    /**
     * �÷���������bean��ʱ��ᱻ���á�ͨ���÷������Ծ���������������Ч{@link AopInvokeFilter}���ϡ�
     * @param target ��װ������
     * @param method ��Ҫaop����ķ�����
     * @param invokeFilter ������Ч��{@link AopInvokeFilter}���ϣ�ͨ���÷������Խ��ı����������Ч�Ķ��󼯡�
     * @return ����������Ч��{@link AopInvokeFilter}���󼯺ϡ�
     */
    public AopInvokeFilter[] filterAopInvokeFilter(Object target, Method method, AopInvokeFilter[] invokeFilter);
}