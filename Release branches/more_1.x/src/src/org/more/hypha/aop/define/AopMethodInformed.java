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
package org.more.hypha.aop.define;
/**
 * ������aop������Զ���һ���޲����ķ�����Ϊaop���յ��÷�����aop״̬������Ҫͨ��aop����ʱ��ȡ��
 * �����Ͳ�֧��{@link PointcutType#Filter}��ʽ��aop��
 * @version 2010-9-27
 * @author ������ (zyc@byshell.org)
 */
public class AopMethodInformed extends AopDefineInformed {
    private String method = null; //����ķ�����
    /**��ȡ����ķ�������*/
    public String getMethod() {
        return this.method;
    }
    /**���ö���ķ�������*/
    public void setMethod(String method) {
        this.method = method;
    }
}