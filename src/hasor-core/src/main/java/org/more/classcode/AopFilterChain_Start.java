/*
 * Copyright 2008-2009 the original 赵永春(zyc@hasor.net).
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
/**
* 过滤器链的第一个，负责在生成的aop类中调用。以产生aop链调用。
 * @version 2009-10-30
 * @author 赵永春 (zyc@hasor.net)
 */
public class AopFilterChain_Start {
    private AopFilterChain         nextFilterChain      = null; //过滤器链的下一个过滤器。
    //
    private AopBeforeListener[]    aopBeforeListener    = null; //before切面事件接受者。
    private AopReturningListener[] aopReturningListener = null; //returning切面事件接受者。
    private AopThrowingListener[]  aopThrowingListener  = null; //throwing切面事件接受者。
    /***/
    AopFilterChain_Start(AopFilterChain nextFilterChain, AopBeforeListener[] aopBeforeListener, AopReturningListener[] aopReturningListener, AopThrowingListener[] aopThrowingListener) {
        this.nextFilterChain = nextFilterChain;
        this.aopBeforeListener = aopBeforeListener;
        this.aopReturningListener = aopReturningListener;
        this.aopThrowingListener = aopThrowingListener;
    }
    public Object doInvokeFilter(Object target, Method method, Object[] args) {
        try {
            //1.下一环节检测，除非发生内部错误否则不会出现环节丢失。
            if (this.nextFilterChain == null)
                throw new LostException("丢失Aop链第一环节。");
            //2.通知before切面。
            if (this.aopBeforeListener != null)
                for (int i = 0; i < this.aopBeforeListener.length; i++)
                    this.aopBeforeListener[i].beforeInvoke(target, method, args);
            //3.执行调用。
            Object result = this.nextFilterChain.doInvokeFilter(target, method, args);
            //4.通知returning切面。
            if (this.aopReturningListener != null)
                for (int i = 0; i < this.aopReturningListener.length; i++)
                    this.aopReturningListener[i].returningInvoke(target, method, args, result);
            //5.返回结果。
            return result;
        } catch (Throwable e) {
            //6.通知throwing切面。
            Throwable throwObj = e;
            if (this.aopThrowingListener != null)
                for (int i = 0; i < this.aopThrowingListener.length; i++)
                    throwObj = this.aopThrowingListener[i].throwsException(target, method, args, throwObj);
            //7.如果抛出的异常属于RuntimeException那么直接转类型抛出。
            if (e instanceof RuntimeException == true)
                throw (RuntimeException) throwObj;
            else
                throw new InvokeException(throwObj);
        }
    }
}