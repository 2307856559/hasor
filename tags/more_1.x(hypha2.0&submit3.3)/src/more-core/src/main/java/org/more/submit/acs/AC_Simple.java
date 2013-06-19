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
package org.more.submit.acs;
import org.more.core.error.LoadException;
import org.more.submit.ActionContext;
import org.more.submit.ActionInvoke;
/**
 * �򵥵�ACʵ�֣���AC��ֱ�Ӵ���class����
 * @version : 2011-7-14
 * @author ������ (zyc@byshell.org)
 */
public class AC_Simple implements ActionContext {
    private ClassLoader actionLoader   = null;
    private String      defaultPackage = null;
    //
    public AC_Simple() {
        this("");
    }
    public AC_Simple(String defaultPackage) {
        this(defaultPackage, null);
    }
    public AC_Simple(String defaultPackage, ClassLoader actionLoader) {
        this.setDefaultPackage(defaultPackage);
        if (actionLoader == null)
            actionLoader = Thread.currentThread().getContextClassLoader();
        this.actionLoader = actionLoader;
    }
    /**��ȡһ���������ð�����{@link AC_Simple}��Ѱ��action��Ĭ�ϰ���*/
    public String getDefaultPackage() {
        return this.defaultPackage;
    }
    /**����һ���������ð�����{@link AC_Simple}��Ѱ��action��Ĭ�ϰ���*/
    public void setDefaultPackage(String defaultPackage) {
        if (defaultPackage.equals("") || defaultPackage == null) {
            this.defaultPackage = "";
            return;
        }
        if (defaultPackage.endsWith(".") == true)
            this.defaultPackage = defaultPackage;
        else
            this.defaultPackage = defaultPackage + ".";
    }
    public ClassLoader getActionLoader() {
        return this.actionLoader;
    }
    public ActionInvoke getActionInvoke(String classKey, String methodKey) throws Throwable {
        classKey = this.defaultPackage + classKey;
        Class<?> type = this.actionLoader.loadClass(classKey);
        //
        Object obj = this.getBean(type);
        if (obj == null)
            throw new LoadException("װ��action�����쳣��");
        return new DefaultActionInvoke(obj, methodKey);
    };
    /**����������ָ���Ķ���*/
    protected Object getBean(Class<?> type) throws Throwable {
        return type.newInstance();
    }
};