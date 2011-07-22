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
import java.net.URI;
import org.more.hypha.Service;
import org.more.util.attribute.IAttribute;
/**
 * Submit 4.0�ķ���ӿڡ�
 * @version : 2011-7-14
 * @author ������ (zyc@byshell.org)
 */
public interface SubmitService extends IAttribute, Service {
    /**ע�������ռ䡣*/
    public void regeditNameSpace(String prefix, ActionContext context);
    /**���ĳ�������ռ��ע�ᡣ*/
    public void unRegeditNameSpace(String prefix);
    /**�ı�Ĭ�������ռ䡣*/
    public void changeDefaultNameSpace(String prefix);
    /**���������ռ�ǰ׺��ȡ��{@link ActionContext}����*/
    public ActionContext getNameSpace(String prefix);
    /**��ȡĬ�������ռ�����*/
    public String getDefaultNameSpaceString();
    /**��ȡĬ�������ռ����*/
    public ActionContext getDefaultNameSpace();
    /**��ȡһ��action����ͨ��action�������ִ��action���á����ַ�����ʽ�������£�ac://package.package.package.action/param/param */
    public ActionObject getActionObject(String url) throws Throwable;
    /**��ȡһ��action����ͨ��action�������ִ��action���á����ַ�����ʽ�������£�ac://package.package.package.action/param/param*/
    public ActionObject getActionObject(URI uri) throws Throwable;
    /**ע��һ���������ظ�ע��ͬһ�����Ƶ��������������滻��*/
    public void regeditScope(String scopeName, IAttribute scope);
    /**��ȡ�Ѿ�ע�����������������ڸ��������򷵻�һ��null��*/
    public IAttribute getScope(String scopeName);
    /**��ȡһ��{@link IAttribute}�ӿڶ��󣬸ýӿڶ�����һ����װ�˶��������Ľӿڶ���*/
    public IAttribute getScopeStack();
    /*-----------------------------------*/
    //    public Session getSession(String sessionID);
    //    public Session getSession();
    //    public SessionContext getSessionContext();
    //
    //    public Sate getState(String stateID);
    //    public Sate getState();
    //    public SateContext getStateContext();
    // 
    //    public FormContext getFormContext();
    // 
    //    public Route getRoute(String match);
    //    public Route setRoute(Route route, String match);
}