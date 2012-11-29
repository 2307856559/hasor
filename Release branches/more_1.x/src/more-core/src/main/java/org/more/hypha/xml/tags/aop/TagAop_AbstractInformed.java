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
package org.more.hypha.xml.tags.aop;
import org.more.core.error.LostException;
import org.more.core.xml.XmlElementHook;
import org.more.core.xml.XmlStackDecorator;
import org.more.core.xml.stream.EndElementEvent;
import org.more.core.xml.stream.StartElementEvent;
import org.more.hypha.aop.AopService;
import org.more.hypha.define.aop.AopProcessor;
import org.more.hypha.define.aop.AopPointcut;
import org.more.hypha.define.aop.AopConfig;
import org.more.hypha.xml.XmlDefineResource;
/**
 * ����informed���ͱ�ǩ��refBean���ԡ�
 * @version 2010-10-9
 * @author ������ (zyc@byshell.org)
 */
@SuppressWarnings("unchecked")
public abstract class TagAop_AbstractInformed<T extends AopProcessor> extends TagAop_NS implements XmlElementHook {
    public static final String AopInformedDefine = "$more_aop_AopInformedDefine";
    public TagAop_AbstractInformed(XmlDefineResource configuration) {
        super(configuration);
    }
    /**����һ��{@link AopProcessor}�������*/
    protected abstract T createDefine(StartElementEvent event);
    /**��ȡ������{@link AopProcessor}�������*/
    protected final T getDefine(XmlStackDecorator<?> context) {
        return (T) context.getAttribute(AopInformedDefine);
    };
    /**��ʼ��ǩ����*/
    public void beginElement(XmlStackDecorator<Object> context, String xpath, StartElementEvent event) {
        T define = this.createDefine(event);
        context.setAttribute(AopInformedDefine, define);
        //1.��ȡ����config
        AopConfig config = (AopConfig) context.getAttribute(TagAop_Config.ConfigDefine);
        //2.������ǩ
        String refBean = event.getAttributeValue("refBean");
        if (refBean == null)
            throw new LostException("[" + config.getName() + "]����informed��before��returning��throwing��filter��ǩʱû�ж���refBean���ԡ�");
        if (this.getDefineResource().containsBeanDefine(refBean) == false)
            throw new LostException("[" + config.getName() + "]����informed��before��returning��throwing��filter��ǩʱ�޷��ҵ������[" + refBean + "]Bean��");
        define.setRefBean(refBean);
        String pointcutRef = event.getAttributeValue("pointcut-ref");
        //3.��Informed��ӵ������config�С�
        if (pointcutRef != null) {
            AopService service = this.getAopConfig();
            AopPointcut pointcutDefine = service.getPointcutDefine(pointcutRef);
            config.addInformed(define, pointcutDefine);
        } else
            config.addInformed(define);
    }
    /**������ǩ������ɾ��ʹ�õı�����*/
    public void endElement(XmlStackDecorator<Object> context, String xpath, EndElementEvent event) {
        context.removeAttribute(AopInformedDefine);
    }
}