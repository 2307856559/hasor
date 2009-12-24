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
public class HelloAction {
    /**Action�����������һ������ΪActionStack�Ĳ�����Submit����Action�ķ���ֵ�����κ����ơ�*/
    public Object hello(ActionStack stack) {
        System.out.println("Hello More!");
        return "����Ƿ��ؽ����";
    }
    /**���ʺź�����һ��ʱ��½�ɹ�*/
    public boolean login(ActionStack stack) {
        //getParamString������getParam(...).toString()�ļ�д��ʽ��
        String account = stack.getParamString("account");//��ȡ�ʺ�
        String password = stack.getParamString("password");//��ȡ����
        boolean res = account.equals(password);
        System.out.println(res);
        return res;
    }
    /**��ȡ���ͬ��������*/
    public int names(ActionStack stack) {
        String[] names = (String[]) stack.getParam("name");
        for (String n : names)
            System.out.println(n);
        return names.length;
    }
    /**ת����ʽ����*/
    public void forward(ActionStack stack) {
        if (stack.getParamString("go").equals("forward") == true)
            stack.setResultsScript("jspforward", "/error.html", "server");
        else
            stack.setResultsScript("jspforward", "/error.html");
    }
}