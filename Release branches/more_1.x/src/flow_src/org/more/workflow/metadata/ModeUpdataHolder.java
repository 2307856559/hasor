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
package org.more.workflow.metadata;
import org.more.workflow.context.ELContext;
import org.more.workflow.state.StateHolder;
/**
 * ����ģ�ͽӿڣ��ýӿ�����ģ�Ͷ���һЩ��ֵ���µ������ϡ�<br/>
 * ����ģ�Ͷ�ֱ�ӻ��Ӽ̳�{@link AbstractObject}�����࣬ģ�Ϳ���ͨ�������ṩ��getMetadata����
 * ��ȡ��ģ��metadata�������ģ��{@link AbstractMetadata metadata}�б�������������ӳ����Ϣ����
 * {@link AbstractMetadata metadata}�����ṩ��{@link ModeUpdataHolder}�ӿڵľ���ʵ�֡�<br/>
 * ��Ȼ��Ҳ����ͨ����ȡģ�͵�{@link StateHolder}Ȼ����{@link StateHolder}�ϵ���updataMode��
 * ������{@link StateHolder}�����updataMode����{@link AbstractMetadata metadata}��updataMode
 * ���ǵ�������һ���ġ�ֻ����{@link StateHolder}��updataModeʹ�õ���metadata��updataMode����ʵ�֡�<br/>
 * Date : 2010-5-16
 * @author ������
 */
public interface ModeUpdataHolder {
    /**����ģ�͵���Ϣ��mode����������Ҫ���µ�ģ�Ͷ��󣬶�elContext����������˵�����ģ��ʱ��������elContext��
     * @throws Throwable */
    public void updataMode(Object mode, ELContext elContext) throws Throwable;
};