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
package org.more.classcode.objects;
import java.lang.reflect.Method;
import org.more.classcode.AopBeforeListener;
import org.more.classcode.AopInvokeFilter;
import org.more.classcode.AopReturningListener;
import org.more.classcode.AopStrategy;
import org.more.classcode.AopThrowingListener;
import org.more.classcode.ClassEngine;
/**
 * �ӿ�{@link AopStrategy}��Ĭ��ʵ�֡�
 * @version 2010-9-3
 * @author ������ (zyc@byshell.org)
 */
public class DefaultAopStrategy implements AopStrategy {
    public void initStrategy(ClassEngine classEngine) {}
    /**������Aop��*/
    public boolean isIgnore(Class<?> superClass, Method ignoreMethod) {
        return false;
    }
    /**�����Ͳ���{@link AopBeforeListener}�������ء�*/
    public AopBeforeListener[] filterAopBeforeListener(Object target, Method method, AopBeforeListener[] beforeListener) {
        return beforeListener;
    }
    /**�����Ͳ���{@link AopReturningListener}�������ء�*/
    public AopReturningListener[] filterAopReturningListener(Object target, Method method, AopReturningListener[] returningListener) {
        return returningListener;
    }
    /**�����Ͳ���{@link AopThrowingListener}�������ء�*/
    public AopThrowingListener[] filterAopThrowingListener(Object target, Method method, AopThrowingListener[] throwingListener) {
        return throwingListener;
    }
    /**�����Ͳ���{@link AopInvokeFilter}�������ء�*/
    public AopInvokeFilter[] filterAopInvokeFilter(Object target, Method method, AopInvokeFilter[] invokeFilter) {
        return invokeFilter;
    }
}