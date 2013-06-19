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
package org.more.workflow.runtime.object;
import org.more.submit.SubmitContext;
import org.more.workflow.context.RunContext;
import org.more.workflow.runtime.Runtime;
import org.more.workflow.util.Config;
/**
 * ����ִ��Submit�е�action.
 * Date : 2010-6-19
 * @author ������
 */
public class ActionRuntime implements Runtime {
    @Override
    public void init(Config config) throws Throwable {};
    @Override
    public void beforeRun(Config param, RunContext runContext) throws Throwable {};
    @Override
    public Object doRun(Config param, RunContext runContext) throws Throwable {
        String invokeString = (String) param.getParam("action");
        if (invokeString == null)
            throw new NullPointerException("����ʱActionRuntime,����actionΪ��.");
        SubmitContext submit = runContext.getApplication().getSubmitContext();
        if (submit == null)
            throw new NullPointerException("���ش���:ActionRuntime��ȡ��SubmitContextΪ��.");
        return submit.doAction(invokeString);
    };
    @Override
    public void afterRun(Config param, RunContext runContext) throws Throwable {};
    @Override
    public void destroy() {}
};