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
package org.hasor.security;
/**
 * �û�Ȩ�޲��Խӿڣ��ýӿ����û�м����κβ�����������Խ��Ϊtrue��
 * @version : 2013-4-20
 * @author ������ (zyc@byshell.org)
 */
public interface SecurityQuery extends SecurityNode {
    /*-*/
    public SecurityQuery and(String permissionCode);
    public SecurityQuery and(Permission permission);
    public SecurityQuery and(SecurityQuery testNode);
    /*-*/
    public SecurityQuery or(String permissionCode);
    public SecurityQuery or(Permission permission);
    public SecurityQuery or(SecurityQuery testNode);
    /*-*/
    /**������ѯ���ȡ��*/
    public SecurityQuery not();
    /**����б��벻����.*/
    public SecurityQuery not(String permissionCode);
    /**����б��벻����.*/
    public SecurityQuery not(Permission permission);
    /**����б��벻����.*/
    public SecurityQuery not(SecurityQuery testNode);
    /*-*/
    /**��Ҫ����ϵͳ*/
    public SecurityQuery andLogin();
    public SecurityQuery orLogin();
    /**��Ҫ�ǳ�ϵͳ*/
    public SecurityQuery andLogout();
    public SecurityQuery orLogout();
    /**��Ҫ�������*/
    public SecurityQuery andGuest();
    /**����Ϊ�������*/
    public SecurityQuery orGuest();
    /**���������*/
    public SecurityQuery notGuest();
    /**�Զ����ⷽʽ��*/
    public SecurityQuery andCustomer(SecurityNode customerTest);
    public SecurityQuery orCustomer(SecurityNode customerTest);
    public SecurityQuery notCustomer(SecurityNode customerTest);
};