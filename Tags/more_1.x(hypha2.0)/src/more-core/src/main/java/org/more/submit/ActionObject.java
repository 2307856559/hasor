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
import java.util.HashMap;
import java.util.Map;
/**
 * �ýӿ���һ���ɵ��õ�action����ͨ���ӿڷ������Զ�action���е��á�
 * @version : 2011-7-14
 * @author ������ (zyc@byshell.org)
 */
public class ActionObject {
    private URI           uri           = null;
    private ActionInvoke  invoke        = null;
    private SubmitService submitService = null;
    //
    public ActionObject(ActionInvoke invoke, SubmitService submitService, URI uri) {
        this.invoke = invoke;
        this.submitService = submitService;
        this.uri = uri;
    };
    /**��ȡaction���������ռ䡣*/
    public String getNameSpace() {
        return this.uri.getScheme();
    };
    /**��ȡaction�ַ�����*/
    public String getActionString() {
        return this.uri.toString();
    };
    /*-----------------------------------------*/
    /**ִ��action�����ҷ���ִ�н����*/
    public Object doAction(Object... objects) throws Throwable {
        if (objects == null || objects.length == 0)
            return this.doAction((ActionStack) null);
        Map<String, Object> params = new HashMap<String, Object>();
        if (objects != null)
            for (int i = 0; i < objects.length; i++)
                params.put(String.valueOf(i), objects[i]);
        return this.doAction(params);
    };
    /**ִ��action�����ҷ���ִ�н�����µ�{@link ActionStack}������ڲ�������ʾ��{@link ActionStack}֮�ϡ�*/
    public Object doAction(ActionStack stack, Object... objects) throws Throwable {
        if (objects == null || objects.length == 0)
            return this.doAction(stack, (Map<String, Object>) null);
        Map<String, Object> params = new HashMap<String, Object>();
        if (objects != null)
            for (int i = 0; i < objects.length; i++)
                params.put(String.valueOf(i), objects[i]);
        return this.doAction(stack, params);
    };
    /**ִ��action�����ҷ���ִ�н����*/
    public Object doAction(Map<String, Object> params) throws Throwable {
        return this.doAction(null, params);
    };
    /**ִ��action�����ҷ���ִ�н�����µ�{@link ActionStack}������ڲ�������ʾ��{@link ActionStack}֮�ϡ�*/
    public Object doAction(ActionStack stack, Map<String, Object> params) throws Throwable {
        ActionStack onStack = this.submitService.createStack(this.uri, stack, params);
        return this.callBack(onStack, this.invoke.invoke(onStack));
    };
    /*ִ�к�������*/
    private Object callBack(ActionStack onStack, Object res) throws Throwable {
        if (res instanceof String == false)
            return res;
        String str = (String) res;
        ResultProcess rp = this.submitService.getResultProcess(str);
        if (rp == null)
            return res;
        return rp.invoke(onStack, res);
    };
};