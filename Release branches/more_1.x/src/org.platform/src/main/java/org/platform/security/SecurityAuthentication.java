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
 * ����Ȩ��ϵͳ����֤��������֤ģ����ϵͳ�п��Դ��ڶ�ݡ���Щģ��ᰴ��˳���ų�һ��������֤ģ������
 * ÿ����֤�������󶼻���������֤ģ�����ϴ���һ�飬����ͨ��{@link AuthorResult#Exit}ö��ֵ��ǿ���жϡ�
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
public interface SecurityAuthentication {
    /**��ʼ����Ȩģ�顣*/
    public void initAuthor(AppContext appContext);
    /**ִ�е�½������ֵΪ{@link AuthorResult}����ö�١�*/
    public AuthorResult doLogin(AuthSession authSession);
    /**ִ���˳�������ֵΪ{@link AuthorResult}����ö�١�*/
    public AuthorResult doLogout(AuthSession authSession);
    /**
     * ��ö�پ�������ν�����һ����֤ģ�����Ķ�����
     * @version : 2013-3-26
     * @author ������ (zyc@byshell.org)
     */
    public static enum AuthorResult {
        /**����ִ����֤ģ������*/
        Continue,
        /**����֤ģ�������˳���������*/
        Exit
    }
}s