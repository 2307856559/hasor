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
package org.web;
import org.more.submit.ActionStack;
/**
 * ��ΪAction������ǹ��еķǳ���ģ�ͬʱҲ�����ǽӿڡ�
 * Date : 2009-12-11
 */
public class LoginAction {
    /**���ʺź�����һ��ʱ��½�ɹ�*/
    public String login(ActionStack stack) {
        String account = stack.getParamString("account");//��ȡ�ʺ�
        String password = stack.getParamString("password");//��ȡ����
        if (account.equals(password) == true)
            return "��½�ɹ���";
        else
            return "��½ʧ�ܣ��ʺź����벻һ�£�";
    }
}