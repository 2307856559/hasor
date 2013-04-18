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
public interface PlatformConfigEnum {
    /**װ�ص�class�����룬���ż��.*/
    public static final String Platform_LoadPackages                = "framework.loadPackages";
    /**�쳣��������ܵ�������(����Code).*/
    public static final String Binder_ErrorCaseCount                = "framework.httpServlet.errorCaseCount";
    //
    //
    /**�������ռ����Ŀ¼�����Ե�ַ��*/
    public static final String Workspace_WorkDir                    = "workspace.workDir";
    /** ������ļ�����Ŀ¼��Ĭ�����workDir��ַ������ͨ������absolute����Ϊtrue��ʾһ�����Ե�ַ��*/
    public static final String Workspace_DataDir                    = "workspace.dataDir";
    public static final String Workspace_DataDir_Absolute           = "workspace.dataDir.absolute";
    /** ���������ڼ��������ʱ���ݴ�ŵ�ַ��Ĭ�����baseDir��ַ������ͨ������absolute����Ϊtrue��ʾһ�����Ե�ַ��*/
    public static final String Workspace_TempDir                    = "workspace.tempDir";
    public static final String Workspace_TempDir_Absolute           = "workspace.tempDir.absolute";
    /** ��������ʱ���ɵĻ������ݴ��λ�ã�Ĭ�����baseDir��ַ������ͨ������absolute����Ϊtrue��ʾһ�����Ե�ַ��*/
    public static final String Workspace_CacheDir                   = "workspace.cacheDir";
    public static final String Workspace_CacheDir_Absolute          = "workspace.cacheDir.absolute";
    //
    //
    /**�����ַ*/
    public static final String Security_LoginURL                    = "security.loginURL";
    /**�ǳ���ַ*/
    public static final String Security_LogoutURL                   = "security.logoutURL";
    /**��������û���*/
    public static final String Security_LoginFormData_AccountField  = "security.loginFormData.accountField";
    /**�����������*/
    public static final String Security_LoginFormData_PasswordField = "security.loginFormData.passwordField";
    /**request�������Ȩ�޼���URL*/
    public static final String Security_Rules_Includes              = "security.rules.includes";
    /**request�����ų�Ȩ�޼���URL*/
    public static final String Security_Rules_Excludes              = "security.rules.excludes";
    /**�ӳ�����ƥ�������·��Ĭ�����ã�NeedCheck��NoCheck*/
    public static final String Security_Rules_DefaultRuleModel      = "security.rules.defaultRuleModel";
}