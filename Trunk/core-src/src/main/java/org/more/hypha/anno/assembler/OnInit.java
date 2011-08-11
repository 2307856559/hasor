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
package org.more.hypha.anno.assembler;
import org.more.core.error.InitializationException;
import org.more.core.log.Log;
import org.more.core.log.LogFactory;
import org.more.hypha.Event.Sequence;
import org.more.hypha.EventListener;
import org.more.hypha.anno.AnnoService;
import org.more.hypha.context.AbstractApplicationContext;
import org.more.hypha.context.InitEvent;
/**
 * aop�ĳ�ʼ��EventException
 * @version : 2011-4-22
 * @author ������ (zyc@byshell.org)
 */
class OnInit implements EventListener<InitEvent> {
    private static Log  log     = LogFactory.getLog(OnInit.class);
    private AnnoService service = null;
    public OnInit(AnnoService service) {
        this.service = service;
    }
    public void onEvent(InitEvent event, Sequence sequence) throws Throwable {
        AbstractApplicationContext context = (AbstractApplicationContext) event.toParams(sequence).applicationContext;
        if (this.service == null)
            throw new InitializationException("ע��anno�������!");
        context.regeditService(AnnoService.class, this.service);
        log.info("hypha.anno init OK!");
    };
}