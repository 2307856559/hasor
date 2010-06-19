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
package org.more.workflow.runtime;
import org.more.workflow.context.RunContext;
import org.more.workflow.event.object.RuntimeEvent;
import org.more.workflow.metadata.AbstractObject;
import org.more.workflow.util.Config;
/**
 * ������{@link Runtime}�ӿڵ�һ��ʵ�֣���Ҫ���ڴ���{@link Runtime}����
 * �����ṩԪ��Ϣ��{@link RuntimeStateHolder}�İ󶨡����Ҷ�ÿ���׶ε��¼���������.
 * Date : 2010-6-14
 * @author ������
 */
public class RuntimeProxy extends AbstractObject implements Runtime {
    private Runtime runtime = null;
    /**����һ��RuntimeProxy����*/
    protected RuntimeProxy(String runtimeID, Runtime runtime, RuntimeStateHolder runtimeStateHolder) {
        super(runtimeID, runtimeStateHolder);
        if (runtime == null)
            throw new NullPointerException("��������runtime�����쳣�����ܴ���һ����runtime���õĴ���");
        this.runtime = runtime;
    };
    @Override
    public void init(Config config) throws Throwable {
        RuntimeEvent event = new RuntimeEvent(this.runtime);
        event.setAttribute("config", config);
        this.event(event.getEventPhase()[0]);
        this.runtime.init(config);
    };
    @Override
    public void beforeRun(Config config, RunContext runContext) throws Throwable {
        RuntimeEvent event = new RuntimeEvent(this.runtime);
        event.setAttribute("config", config);
        event.setAttribute("runContext", runContext);
        this.event(event.getEventPhase()[1]);
        this.runtime.beforeRun(config, runContext);
    };
    @Override
    public Object doRun(Config config, RunContext runContext) throws Throwable {
        RuntimeEvent event = new RuntimeEvent(this.runtime);
        event.setAttribute("config", config);
        event.setAttribute("runContext", runContext);
        this.event(event.getEventPhase()[2]);
        return this.runtime.doRun(config, runContext);
    };
    @Override
    public void afterRun(Config config, RunContext runContext) throws Throwable {
        RuntimeEvent event = new RuntimeEvent(this.runtime);
        event.setAttribute("config", config);
        event.setAttribute("runContext", runContext);
        this.event(event.getEventPhase()[3]);
        this.runtime.afterRun(config, runContext);
    };
    @Override
    public void destroy() {
        RuntimeEvent event = new RuntimeEvent(this.runtime);
        this.event(event.getEventPhase()[4]);
        this.runtime.destroy();
    };
    /** ��ȡ�˴�����������ľ���Runtime����*/
    public Runtime getTargetBean() {
        return this.runtime;
    };
};