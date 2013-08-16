/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
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
package org.hasor.security;
import org.hasor.context.AppContext;
/**
 * ����Ȩ��ϵͳ����֤��������֤ģ����ϵͳ�п��Դ��ڶ�ݡ���Щģ��ᰴ��˳���ų�һ��������֤ģ������
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
public interface SecurityAuth {
    /**��ʼ����Ȩģ�顣*/
    public void initAuth(AppContext appContext);
    /**ͨ��userCode��½ϵͳ*/
    public UserInfo getUserInfo(String userCode);
    /**ͨ���ʺ������½ϵͳ*/
    public UserInfo getUserInfo(String account, String password);
    /**������Ȩģ�顣*/
    public void destroyAuth(AppContext appContext);
}