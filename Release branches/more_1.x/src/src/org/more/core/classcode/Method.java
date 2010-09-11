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
/**
 * classcodeʹ�õ�Method���󣬸����з�װ��aop����������ʵ��Ŀ�귽����
 * @version 2010-9-11
 * @author ������ (zyc@byshell.org)
 */
public class Method {
    private java.lang.reflect.Method proxyMethod  = null;
    private java.lang.reflect.Method targetMeyhod = null;
    /**����Method���Ͷ���*/
    Method(java.lang.reflect.Method proxyMethod, java.lang.reflect.Method targetMeyhod) {
        this.proxyMethod = proxyMethod;
        this.targetMeyhod = targetMeyhod;
    }
    /**��ȡaop�������������aop�ڼ��ٴε��ø÷�������������ѭ����*/
    public java.lang.reflect.Method getProxyMethod() {
        return this.proxyMethod;
    }
    /**��ȡaop����ķ�����Ŀ�귽����*/
    public java.lang.reflect.Method getTargetMeyhod() {
        return this.targetMeyhod;
    }
}