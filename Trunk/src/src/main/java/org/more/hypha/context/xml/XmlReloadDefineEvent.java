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
package org.more.hypha.context.xml;
import org.more.core.log.Log;
import org.more.core.log.LogFactory;
import org.more.hypha.Event;
/**
 * ����װ���¼�������������������װ��ʱ���������¼���
 * @version 2010-10-10
 * @author ������ (zyc@byshell.org)
 */
public class XmlReloadDefineEvent extends Event {
    private static Log log = LogFactory.getLog(XmlReloadDefineEvent.class);
    public class XmlReloadDefineEvent_Params extends Event.Params {
        public XmlDefineResource xmlDefineResource = null;
    };
    public XmlReloadDefineEvent_Params toParams(Sequence eventSequence) {
        Object[] params = eventSequence.getParams();
        log.debug("Sequence to Params ,params = {%0}", params);
        XmlReloadDefineEvent_Params p = new XmlReloadDefineEvent_Params();
        p.xmlDefineResource = (XmlDefineResource) params[0];
        return p;
    }
};