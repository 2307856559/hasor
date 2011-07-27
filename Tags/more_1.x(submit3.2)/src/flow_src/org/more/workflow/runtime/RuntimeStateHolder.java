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
import org.more.workflow.context.RuntimeFactory;
import org.more.workflow.event.object.NewInstanceEvent;
import org.more.workflow.metadata.ObjectMetadata;
import org.more.workflow.state.AbstractStateHolder;
/**
 * ����ʱ״̬��������ͨ���ö�����Է���ı������������ʱ״̬���κ��й�����ʱ�Ĳ������ᱻ��װ�ڸ����С�
 * Date : 2010-6-16
 * @author ������
 */
public class RuntimeStateHolder extends AbstractStateHolder {
    private RuntimeMetadata runtimeMetadata = null;
    public RuntimeStateHolder(RuntimeMetadata runtimeMetadata) {
        this.runtimeMetadata = runtimeMetadata;
    }
    @Override
    public ObjectMetadata getMetadata() {
        return this.runtimeMetadata;
    }
    /**
     * ����{@link Runtime}��ע��÷���ֻ�ᴴ��һ��Runtime���Ͷ��������ȥ�������Runtime�����ԡ�
     * �����Ҫ������Ը�����ִ��updataMode������
     */
    @Override
    public Runtime newInstance(RunContext runContext) throws Throwable {
        RuntimeFactory factory = runContext.getApplication().getRuntimeFactory();
        Runtime obj = factory.getRuntime(runContext, this.runtimeMetadata);
        String runtimeID = factory.generateID(runContext, obj);
        //
        obj = new RuntimeProxy(runtimeID, obj, new RuntimeStateHolder(this.runtimeMetadata));
        NewInstanceEvent event = new NewInstanceEvent(obj, this);
        this.event(event.getEventPhase()[0]);
        return obj;
    }
};