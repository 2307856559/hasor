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
package org.more.submit.acs.simple.xml;
import org.more.core.error.InitializationException;
import org.more.core.log.Log;
import org.more.core.log.LogFactory;
import org.more.hypha.Event.Sequence;
import org.more.hypha.EventListener;
import org.more.hypha.context.AbstractApplicationContext;
import org.more.hypha.context.StartingServicesEvent;
import org.more.submit.ActionContext;
import org.more.submit.ActionPackage;
import org.more.submit.SubmitService;
/**
 * ���ฺ��build{@link HyphaSubmitService}�ӿڶ���
 * @version : 2011-4-22
 * @author ������ (zyc@byshell.org)
 */
class OnStartingServices implements EventListener<StartingServicesEvent> {
    private static Log log    = LogFactory.getLog(OnStartingServices.class);
    private B_Config   config = null;
    // 
    public OnStartingServices(B_Config config) {
        this.config = config;
    }
    public void onEvent(StartingServicesEvent event, Sequence sequence) throws Throwable {
        AbstractApplicationContext context = (AbstractApplicationContext) event.toParams(sequence).applicationContext;
        //1.����service
        this.config.build.setContext(context);//����������
        //2.������������
        for (String arrName : context.getAttributeNames())
            this.config.build.setAttribute(arrName, context.getAttribute(arrName));
        SubmitService service = this.config.build.build();
        if (service == null)
            throw new InitializationException("SubmitService ����build���Ϊ��!");
        //3.ע��AC
        if (this.config.acList != null)
            for (B_AC acb : this.config.acList) {
                String prefix = acb.getNamespace();
                ActionContext ac = new AC_Propxy(context, acb.getRefBean());;
                service.regeditNameSpace(prefix, ac);
            }
        //4.ע��Mapping
        if (this.config.acMappingList != null)
            for (B_AnnoActionInfo am : this.config.acMappingList) {
                ActionContext defaultAC = service.getDefaultNameSpace();
                if (defaultAC == null)
                    throw new NullPointerException("�޷���ȡĬ�ϵ�submit�����ռ䡣");
                ActionPackage pack = defaultAC.definePackage(am.packageString);
                pack.addRouteMapping(am.mappingPath, am.actionPath);
            }
        //5.ע�����
        context.regeditService(SubmitService.class, service);
        log.info("hypha.submit start OK!");
    };
}