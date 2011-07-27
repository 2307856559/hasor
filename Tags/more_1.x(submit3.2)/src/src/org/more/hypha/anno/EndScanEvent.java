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
package org.more.hypha.anno;
import org.more.core.log.ILog;
import org.more.core.log.LogFactory;
import org.more.hypha.Event;
import org.more.hypha.context.xml.XmlDefineResource;
/**
 * ���¼�����Xmlװ�ؽ����ڼ���������־��ע��ɨ�������
 * @version 2011-2-25
 * @author ������ (zyc@byshell.org)
 */
public class EndScanEvent extends Event {
    private static ILog log = LogFactory.getLog(EndScanEvent.class);
    public class EndScanEvent_Params extends Event.Params {
        public XmlDefineResource resource    = null;
        public String            packageName = null;
        public AnnoService       annoService = null;
    };
    public EndScanEvent_Params toParams(Sequence eventSequence) {
        Object[] params = eventSequence.getParams();
        log.debug("Sequence to Params ,params = {%0}", params);
        EndScanEvent_Params p = new EndScanEvent_Params();
        p.resource = (XmlDefineResource) params[0];
        p.packageName = (String) params[1];
        p.annoService = (AnnoService) params[2];
        return p;
    }
};