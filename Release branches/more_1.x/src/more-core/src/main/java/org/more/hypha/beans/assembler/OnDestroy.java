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
package org.more.hypha.beans.assembler;
import org.more.core.log.Log;
import org.more.core.log.LogFactory;
import org.more.hypha.Event.Sequence;
import org.more.hypha.EventListener;
import org.more.hypha.context.DestroyEvent;
/**
 * ���¼���Ŀ�����������Bean�����档
 * @version : 2011-4-22
 * @author ������ (zyc@byshell.org)
 */
class OnDestroy implements EventListener<DestroyEvent> {
    private static Log log = LogFactory.getLog(OnDestroy.class);
    public void onEvent(DestroyEvent event, Sequence sequence) {
        log.debug("hypha.beans On Destroy!");
    }
}