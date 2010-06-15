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
package org.more.workflow.event.object;
import org.more.util.attribute.IAttribute;
import org.more.workflow.event.Event;
import org.more.workflow.event.EventPhase;
/**
 * ������״̬����ʱ��
 * Date : 2010-5-21
 * @author ������
 */
public class SaveStateEvent extends Event {
    /**  */
    private static final long serialVersionUID = -4046949021700949565L;
    private IAttribute        flash            = null;
    /**������״̬����ʱ��*/
    public SaveStateEvent(Object targetMode, IAttribute flash) {
        super("SaveStateEvent", targetMode);
        this.flash = flash;
    };
    @Override
    protected EventPhase[] createEventPhase() {
        return new EventPhase[] { new Event.BeforeEventPhase(), new Event.AfterEventPhase() };
    };
    public IAttribute getFlash() {
        return this.flash;
    };
};