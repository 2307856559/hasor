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
package net.hasor.mvc.controller;
import java.lang.reflect.Method;
import net.hasor.context.AppContext;
/**
 * Action����
 * @version : 2013-8-12
 * @author ������ (zyc@hasor.net)
 */
public interface ActionDefine {
    /**�ڵ���Action֮ǰ�������¼�*/
    public static String Event_BeforeInvoke = "ActionInvoke_Event_BeforeInvoke";
    /**�ڵ���Action֮���������¼�*/
    public static String Event_AfterInvoke  = "ActionInvoke_Event_AfterInvoke";
    //
    /**��ȡAction���Խ��յķ���*/
    public String[] getHttpMethod();
    /**��ȡĿ�귽����*/
    public Method getTargetMethod();
    /**�жϸ�Action�Ƿ�������RESTfulӳ���ַ��*/
    public boolean isRESTful();
    /**��ȡӳ���ַ���*/
    public String getRestfulMapping();
    /**��ȡӳ���ַ�������ƥ��ı��ʽ�ַ���*/
    public String getRestfulMappingMatches();
    /**��ȡ��Ӧ����*/
    public String getMimeType();
    /**��ȡAppContext*/
    public AppContext getAppContext();
}