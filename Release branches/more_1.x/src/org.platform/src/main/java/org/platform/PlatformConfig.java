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
package org.platform;
/**
 * ������ϢKey
 * @version : 2013-4-18
 * @author ������ (zyc@byshell.org)
 */
public interface PlatformConfig {
    //
    //
    /**װ�ص�class�����룬���ż��.*/
    public static final String Platform_LoadPackages                       = "framework.loadPackages";
    //
    //
    /**�쳣��������ܵ�������(����Code).*/
    public static final String HttpServlet_ErrorCaseCount                  = "httpServlet.errorCaseCount";
    //
    //
    /**�������ռ����Ŀ¼�����Ե�ַ��*/
    public static final String Workspace_WorkDir                           = "workspace.workDir";
    /** ������ļ�����Ŀ¼��Ĭ�����workDir��ַ������ͨ������absolute����Ϊtrue��ʾһ�����Ե�ַ��*/
    public static final String Workspace_DataDir                           = "workspace.dataDir";
    public static final String Workspace_DataDir_Absolute                  = "workspace.dataDir.absolute";
    /** ���������ڼ��������ʱ���ݴ�ŵ�ַ��Ĭ�����baseDir��ַ������ͨ������absolute����Ϊtrue��ʾһ�����Ե�ַ��*/
    public static final String Workspace_TempDir                           = "workspace.tempDir";
    public static final String Workspace_TempDir_Absolute                  = "workspace.tempDir.absolute";
    /** ��������ʱ���ɵĻ������ݴ��λ�ã�Ĭ�����baseDir��ַ������ͨ������absolute����Ϊtrue��ʾһ�����Ե�ַ��*/
    public static final String Workspace_CacheDir                          = "workspace.cacheDir";
    public static final String Workspace_CacheDir_Absolute                 = "workspace.cacheDir.absolute";
    //
    //
    /**�Ƿ�����Ȩ��ϵͳ*/
    public static final String Security_Enable                             = "security.enable";
    /**��Security_Enable����֮�󣬸�ֵ�����Ƿ��������URL���ֵ�Ȩ�޹��ˡ�*/
    public static final String Security_EnableURL                          = "security.enableURL";
    /**��Security_Enable����֮�󣬸�ֵ�����Ƿ�������Է��������е�Ȩ�޹��ˡ�*/
    public static final String Security_EnableMethod                       = "security.enableMethod";
    /**AuthSession���ݻ���*/
    public static final String Security_AuthSessionCache                   = "security.authSessionCacheName";
    /**AuthSessionCache�����õĳ�ʱʱ��.*/
    public static final String Security_AuthSessionCache_Timeout           = "security.authSessionMapCache.timeout";
    /**AuthSessionCache�������Ƿ���Զ������.*/
    public static final String Security_AuthSessionCache_Eternal           = "security.authSessionMapCache.eternal";
    /**AuthSessionCache��ÿ�����ʻ������ʱ�Ƿ��Զ�������Լ����Լʱ��ͬ����ʱ���泬ʱʱ�䣩.*/
    public static final String Security_AuthSessionCache_AutoRenewal       = "security.authSessionMapCache.autoRenewal";
    /**AuthSessionCache����������̹߳�����ʱ��Ƶ��(����).*/
    public static final String Security_AuthSessionCache_ThreadSeep        = "security.authSessionMapCache.threadSeep";
    /**�����ַ*/
    public static final String Security_LoginURL                           = "security.loginURL";
    /**�ǳ���ַ*/
    public static final String Security_LogoutURL                          = "security.logoutURL";
    /**��������û���*/
    public static final String Security_LoginFormData_AccountField         = "security.loginFormData.accountField";
    /**�����������*/
    public static final String Security_LoginFormData_PasswordField        = "security.loginFormData.passwordField";
    /**URLȨ�޼��Ĭ�ϲ������ã�Login|Logout|Guest|Permission|None*/
    public static final String Security_Rules_DefaultModel                 = "security.rules.defaultRule";
    /**������Ȩ�޼�鷶���URL*/
    public static final String Security_Rules_Includes                     = "security.rules.includes";
    /**�ų�Ȩ�޼�鷶���URL*/
    public static final String Security_Rules_Excludes                     = "security.rules.excludes";
    /**ת������*/
    public static final String Security_Forwards                           = "security.forwards";
    /**�Ƿ����������ʺţ����������ʺ�֮���û���δ��½ʱ��ȡ��AuthSession���ȡ������AuthSession.*/
    public static final String Security_Guest_Enable                       = "security.guest.enable";
    /**�����û��ʺ���*/
    public static final String Security_Guest_ClassType                    = "security.guest.classType";
    /**�����ʺ�Ȩ��*/
    public static final String Security_Guest_Permissions                  = "security.guest.permissions";
    /**�Ƿ����ÿͻ���cookie��Э����֤��*/
    public static final String Security_ClientCookie_Enable                = "security.clientCookie.enable";
    /**��ϵͳ����ʱ�Ƿ�ǿ�����пͻ����Ѿ���½����Cookie��ϢʧЧ*/
    public static final String Security_ClientCookie_LoseCookieOnStart     = "security.clientCookie.loseCookieOnStart";
    /**�ͻ���cookie����*/
    public static final String Security_ClientCookie_CookieName            = "security.clientCookie.cookieName";
    /**cookie��ʱʱ�䣬��λ����*/
    public static final String Security_ClientCookie_Timeout               = "security.clientCookie.timeout";
    /**cookie��Domain���ã����������������֧�ֿ������cookie����Ĭ��Ϊ�ղ��Ը�ֵ�������ã�*/
    public static final String Security_ClientCookie_Domain                = "security.clientCookie.cookieDomain";
    /**cookie��path���ԣ�Ĭ��Ϊ�ղ��Ը�ֵ�������ã�*/
    public static final String Security_ClientCookie_Path                  = "security.clientCookie.cookiePath";
    /**�Ƿ����cookie����*/
    public static final String Security_ClientCookie_Encryption_Enable     = "security.clientCookie.encryption.enable";
    /**cookie���ݼ��ܷ�ʽ��DES,BAS64�ȵ�.*/
    public static final String Security_ClientCookie_Encryption_EncodeType = "security.clientCookie.encryption.encodeType";
    /**cookie���ݼ���ʱʹ�õ�Key*/
    public static final String Security_ClientCookie_Encryption_Key        = "security.clientCookie.encryption.key";
    /**�����㷨����*/
    public static final String Security_EncryptionDigestSet                = "security.encryptionDigestSet";
    //
    //
    /**�Ƿ����û���ϵͳ.*/
    public static final String CacheConfig_Enable                          = "cacheConfig.enable";
    /**MapCache�����õĳ�ʱʱ��.*/
    public static final String CacheConfig_MapCache_Timeout                = "cacheConfig.mapCache.timeout";
    /**MapCache�������Ƿ���Զ������.*/
    public static final String CacheConfig_MapCache_Eternal                = "cacheConfig.mapCache.eternal";
    /**MapCache��ÿ�����ʻ������ʱ�Ƿ��Զ�������Լ����Լʱ��ͬ����ʱ���泬ʱʱ�䣩.*/
    public static final String CacheConfig_MapCache_AutoRenewal            = "cacheConfig.mapCache.autoRenewal";
    /**MapCache����������̹߳�����ʱ��Ƶ��(����).*/
    public static final String CacheConfig_MapCache_ThreadSeep             = "cacheConfig.mapCache.threadSeep";
}