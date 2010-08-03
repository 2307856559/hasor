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
import org.more.submit.web.WebActionStack;
public class Action {
    public String hello(WebActionStack stack) {
        System.out.println(stack.getParam("msg"));
        String acc = stack.getParamString("acc");
        String pwd = stack.getParamString("pwd");
        if (acc == null)
            return "�������ʺź������Ե�½ϵͳ��";
        else
            return (acc.equals(pwd) == true) ? "<font color='#00ff00'>��½�ɹ���</font>" : "<font color='#ff0000'>�ʺź����벻һ����½ʧ��!</font>";
    }
}