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
package org.platform.security;
import org.platform.context.AppContext;
/**
 * Ȩ���жϣ�����ͨ���ýӿ���ʵ�ָ��ֲ��õ�Ȩ��ģ�͡�
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
public interface SecurityPolicy {
    /**��ʼ�����Զ���*/
    public void initPolicy(AppContext appContext);
    /**
     * ���в��Լ�顣
     * @param userInfo �û���Ϣ����
     * @param powerCode Ҫ����Ȩ�޵㡣
     */
    public boolean testPermission(AuthSession authSession, Permission[] powerCode);
}a