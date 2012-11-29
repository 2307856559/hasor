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
import java.lang.annotation.Annotation;
import org.more.core.error.LostException;
import org.more.hypha.AbstractBeanDefine;
import org.more.hypha.anno.KeepWatchParser;
import org.more.hypha.aop.AopService;
import org.more.hypha.aop.assembler.AopService_Impl;
import org.more.hypha.define.anno.Aop;
import org.more.hypha.define.anno.AopInformed;
import org.more.hypha.define.anno.Bean;
import org.more.hypha.define.aop.AopConfig;
import org.more.hypha.define.aop.AopDefineInformed;
import org.more.hypha.define.aop.AopPointcut;
import org.more.hypha.define.aop.AopPointcutType;
import org.more.hypha.xml.XmlDefineResource;
/**
 * ��bean���ڽ���aop��ע�����ã�ȡ�����಻��Ӱ�쵽anno�ķ����ṩ��ֻ�����ᵼ���޷�����bean��aopע��������
 * @version 2010-10-14
 * @author ������ (zyc@byshell.org)
 */
class Watch_Aop implements KeepWatchParser {
    public void process(Object target, Annotation annoData, XmlDefineResource resource) {
        Class<?> beanType = (Class<?>) target;
        Bean bean = beanType.getAnnotation(Bean.class);
        if (bean == null)
            return;
        AopService aopPlugin = (AopService) resource.getFlash().getAttribute(AopService_Impl.ServiceName);
        // ID
        String id = bean.id();
        if (id.equals("") == true) {
            StringBuffer idb = new StringBuffer();
            String logicPackage = bean.logicPackage();
            if (logicPackage.equals("") == true)
                logicPackage = beanType.getPackage().getName();
            idb.append(logicPackage);
            idb.append(".");
            String name = bean.name();
            if (name.equals("") == true)
                name = beanType.getSimpleName();
            idb.append(name);
            id = idb.toString();
        }
        //1.��ȡaopע��
        BeanDefine define = resource.getBeanDefine(id);
        Aop aop = beanType.getAnnotation(Aop.class);
        if (aop == null)
            return;
        //2.���useConfig���ԡ�
        AopConfig aopConfig = null;
        String var = aop.useConfig();
        if (var.equals("") == false) {
            aopConfig = aopPlugin.getAopDefine(var);
            if (aopConfig == null)
                throw new LostException("�Ҳ�������Ϊ[" + var + "]��Aop���á�");
            aopPlugin.setAop(define, aopConfig);
            return;
        }
        //3.����aopע��
        aopConfig = new AopConfig();
        aopConfig.setAopMode(aop.mode());
        //defaultPointcut
        AopPointcut aoppoint = new AopPointcut();
        aoppoint.setExpression(aop.defaultPointcut());
        aopConfig.setDefaultPointcutDefine(aoppoint);
        //informeds
        AopInformed[] informeds = aop.informeds();
        for (AopInformed informed : informeds) {
            String refBean = informed.refBean();
            String pointcut = informed.pointcut();
            AopPointcutType pointType = informed.type();
            //����㶨��
            AopPointcut aoppoint_item = new AopPointcut();
            aoppoint.setExpression(pointcut);
            //����������
            AopDefineInformed informedDefine = new AopDefineInformed();
            informedDefine.setRefBean(refBean);
            informedDefine.setPointcutType(pointType);
            informedDefine.setRefPointcut(aoppoint_item);
            //
            aopConfig.addInformed(informedDefine);
        }
        //4.ע��
        aopPlugin.setAop(define, aopConfig);
    }
}