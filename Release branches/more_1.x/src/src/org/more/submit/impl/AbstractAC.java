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
package org.more.submit.impl;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.more.core.error.FormatException;
import org.more.core.error.ResourceException;
import org.more.submit.ActionContext;
import org.more.submit.ActionInvoke;
import org.more.submit.ActionPackage;
/**
 * ����ACʵ�֡�
 * @version : 2011-7-14
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractAC implements ActionContext {
    private Map<String, ActionPackage> packages = new HashMap<String, ActionPackage>();
    //
    public ActionPackage definePackage(String packageName) {
        if (packageName == null)
            packageName = "";
        if (packageName.indexOf('/') > 0)
            throw new FormatException("�����в��ܴ����ַ���/��");
        ActionPackage pack = this.packages.get(packageName);
        if (pack == null) {
            pack = new DefaultPackage(packageName);
            this.packages.put(packageName, pack);
        }
        return pack;
    };
    public ActionInvoke getAction(URI uri) {
        //�ȴ�Ĭ�ϰ��л�ȡactio·����
        String name = uri.getAuthority();
        String actionPath = this.definePackage("").getActionPath(name);
        if (actionPath == null)
            //name��Ϊ������ȡaction��ַ��
            actionPath = this.definePackage(name).getActionPath(uri.getPath());
        if (actionPath == null)
            throw new ResourceException("Ĭ�ϰ��в����ڸ�actionӳ�䣬ͬʱҲû�ж�������Ƶİ���[" + name + "]");
        //
        return this.findAction(actionPath, uri.getQuery());
    };
    public abstract ActionInvoke findAction(String actionPath, String queryInfo);
};