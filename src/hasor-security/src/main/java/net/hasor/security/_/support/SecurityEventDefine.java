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
package net.hasor.security._.support;
/**
 * 
 * @version : 2013-5-8
 * @author ������ (zyc@byshell.org)
 */
public interface SecurityEventDefine {
    /**��������URLȨ��ʱ�����¼��п��Բٿ�AuthSession��ʱ����·������Ȩ�ޡ�*/
    public static final String TestURLPermission        = "Security_TestURLPermission";
    /**�����¼�*/
    public static final String Login                    = "Security_Login";
    /**�ǳ��¼�*/
    public static final String Logout                   = "Security_Logout";
    /**AuthSession���ر�*/
    public static final String AuthSession_Close        = "Security_AuthSession_Close";
    /**AuthSession������*/
    public static final String AuthSession_New          = "Security_AuthSession_New";
    /**��ǰ�̼߳����AuthSession��*/
    public static final String AuthSession_Activate     = "Security_AuthSession_Activate";
    /**��ǰ�̶߳ۻ���AuthSession��*/
    public static final String AuthSession_Inactivation = "Security_AuthSession_Inactivation";
}