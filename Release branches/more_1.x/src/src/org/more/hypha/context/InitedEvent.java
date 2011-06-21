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
package org.more.hypha.context;
import org.more.hypha.ApplicationContext;
import org.more.hypha.Event;
import org.more.log.ILog;
import org.more.log.LogFactory;
/**
 * {@link ApplicationContext}��ʼ��������϶������¼���
 * @version 2011-2-25
 * @author ������ (zyc@byshell.org)
 */
public class InitedEvent extends Event {
    private static ILog log = LogFactory.getLog(InitedEvent.class);
    public class Params extends Event.Params {
        public ApplicationContext applicationContext = null;
    };
    public Params toParams(Sequence eventSequence) {
        Object[] params = eventSequence.getParams();
        log.debug("Sequence to Params ,params = {%0}", params);
        Params p = new Params();
        p.applicationContext = (ApplicationContext) params[0];
        return p;
    }
};