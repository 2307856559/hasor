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
/**
 * ����Ȩ��ϵͳ�е��û��Ự���û��Ự�б������û���½֮���Ȩ�����ݡ�
 * @version : 2013-3-26
 * @author ������ (zyc@byshell.org)
 */
public abstract class AuthSession {
    /**��ȡ��½���û��������δ��¼ϵͳ���������������ʺ���᷵�������ʺš�*/
    public abstract IUser getUserObject();
    /**��ָ�����û������½��Ȩ��ϵͳ��*/
    public abstract void doLogin(IUser user);
    /**��ָ�����û��ʺ�����ϵͳ��*/
    public abstract void doLogin(String account, String password);
    /**ִ���˳���*/
    public abstract void doLogout();
    /**��ӻỰ������ʱȨ�ޣ����Ự��ʧȨ����ʧ��*/
    public abstract void addTempPermission(Permission permission);
    /**�Ƿ��Ѿ���½*/
    public abstract boolean isLogin();
    /**�ж��Ƿ�Ϊ�����ʺţ��������������ʺţ�������δ��½ϵͳ������¸�ֵ����Ϊtrue��*/
    public abstract boolean isGuest();
    /**�رջỰ*/
    public abstract void close();
}