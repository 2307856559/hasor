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
import java.util.Map;
import org.more.hypha.ApplicationContext;
import org.more.submit.ActionInvoke;
import org.more.submit.ActionStack;
import org.more.util.attribute.IAttribute;
/**
 * Ĭ��{@link SubmitService�ӿ�ʵ��}����submit v4.0�ķ������ṩ�ࡣ
 * @version : 2011-7-14
 * @author ������ (zyc@byshell.org)
 */
public class DefaultSubmitService extends AbstractSubmitService {
    private static final long serialVersionUID = 8665931474234786465L;
    //
    public void start(ApplicationContext context, IAttribute flash) {};
    public void stop(ApplicationContext context, IAttribute flash) {}
    //
    protected DefaultActionObject createActionObject(URI uri, ActionInvoke invoke) {
        /*�պ����չ�����￪��*/
        return new DefaultActionObject(invoke, this, uri);
    };
    protected DefaultActionStack createStack(URI uri, ActionStack onStack, Map<String, ?> params) {
        DefaultActionStack stack = new DefaultActionStack(uri, onStack, this);
        stack.putALL(params);
        return stack;
    };
};