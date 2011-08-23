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
import org.more.submit.ActionInvoke;
import org.more.submit.ActionObject;
import org.more.submit.ActionStack;
import org.more.submit.Result;
import org.more.submit.ResultProcess;
/**
 *  Ĭ��{@link ActionObject}�ӿ�ʵ�֡�
 * @version : 2011-7-15
 * @author ������ (zyc@byshell.org)
 */
public class DefaultActionObject implements ActionObject {
    private URI                  uri           = null;
    private ActionInvoke         invoke        = null;
    private DefaultSubmitService submitService = null;
    //
    public DefaultActionObject(ActionInvoke invoke, DefaultSubmitService submitService, URI uri) {
        this.invoke = invoke;
        this.submitService = submitService;
        this.uri = uri;
    };
    public String getNameSpace() {
        return this.uri.getScheme();
    };
    public String getActionString() {
        return this.uri.toString();
    };
    /*-----------------------------------------*/
    public Object doAction(Object... objects) throws Throwable {
        if (objects == null || objects.length == 0)
            return this.doAction((Map<String, ?>) null);
        Map<String, Object> params = new HashMap<String, Object>();
        if (objects != null)
            for (int i = 0; i < objects.length; i++)
                params.put(String.valueOf(i), objects[i]);
        return this.doAction(params);
    };
    public Object doAction(Map<String, ?> params) throws Throwable {
        return this.doAction(null, params);
    };
    public Object doAction(ActionStack stack, Object... objects) throws Throwable {
        if (objects == null || objects.length == 0)
            return this.doAction(stack, (Map<String, ?>) null);
        Map<String, Object> params = new HashMap<String, Object>();
        if (objects != null)
            for (int i = 0; i < objects.length; i++)
                params.put(String.valueOf(i), objects[i]);
        return this.doAction(stack, params);
    };
    public Object doAction(ActionStack stack, Map<String, ?> params) throws Throwable {
        DefaultActionStack onStack = this.submitService.createStack(this.uri, stack, params);
        return this.callBack(onStack, this.invoke.invoke(onStack));
    };
    /*ִ�к�������*/
    private Object callBack(DefaultActionStack onStack, Object var) throws Throwable {
        if (var instanceof DefaultResultImpl == false)
            return var;
        DefaultResultImpl<?> result = (DefaultResultImpl<?>) var;
        ResultProcess<Result<?>> rp = this.submitService.getResultProcess(result.getName());
        if (rp == null)
            return var;
        return rp.invoke(onStack, result);
    };
};