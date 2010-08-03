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
package org.more.submit;
import org.more.FormatException;
/**
 * submit�Ĺ����ࡣ
 * @version : 2010-7-27
 * @author ������(zyc@byshell.org)
 */
public class Util {
    /**��ʽ�������ַ�������������ַ���û��ָ��Ҫ���õ�Ŀ�귽�����ʽ��֮��ʹ��execute��ΪĬ�Ϸ�����*/
    public static String formatInvokeString(String invokeString) {
        String[] is = invokeString.split("\\.");
        String actionName = null;
        String actionMethod = null;
        if (is.length > 2)
            throw new FormatException("�����invokeString�ַ�����ʽ");
        if (is.length >= 1)
            actionName = is[0];
        if (is.length == 2)
            actionMethod = is[1];
        else
            actionMethod = "execute";
        return actionName + "." + actionMethod;
    };
    /**���������ַ������ҽ�����֮�������ע�뵽ActionStack�����У���󷵻ء�actionName, actionMethod*/
    public static String[] splitInvokeString(String invokeString) {
        return formatInvokeString(invokeString).split("\\.");
    };
    /**���������ַ������ҽ�����֮�������ע�뵽ActionStack�����У���󷵻ء�actionName, actionMethod*/
    public static String getActionString(String invokeString) {
        return splitInvokeString(invokeString)[0];
    };
    /**���������ַ������ҽ�����֮�������ע�뵽ActionStack�����У���󷵻ء�actionName, actionMethod*/
    public static String getMethodString(String invokeString) {
        return splitInvokeString(invokeString)[1];
    };
};