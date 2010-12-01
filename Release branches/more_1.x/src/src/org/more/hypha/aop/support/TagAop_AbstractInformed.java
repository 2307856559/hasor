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
package org.more.hypha.aop.support;
import org.more.NoDefinitionException;
import org.more.NotFoundException;
import org.more.core.xml.XmlElementHook;
import org.more.core.xml.XmlStackDecorator;
import org.more.core.xml.stream.EndElementEvent;
import org.more.core.xml.stream.StartElementEvent;
import org.more.hypha.aop.AopDefineResourcePlugin;
import org.more.hypha.aop.define.AbstractInformed;
import org.more.hypha.aop.define.AbstractPointcutDefine;
import org.more.hypha.aop.define.AopConfigDefine;
import org.more.hypha.aop.define.AopDefineInformed;
import org.more.hypha.context.Tag_Abstract;
import org.more.hypha.context.XmlDefineResource;
/**
 * ����informed���ͱ�ǩ��refBean���ԡ�
 * @version 2010-10-9
 * @author ������ (zyc@byshell.org)
 */
@SuppressWarnings("unchecked")
public abstract class TagAop_AbstractInformed<T extends AopDefineInformed> extends Tag_Abstract implements XmlElementHook {
    public static final String AopInformedDefine = "$more_aop_AopInformedDefine";
    public TagAop_AbstractInformed(XmlDefineResource configuration) {
        super(configuration);
    }
    /**����һ��{@link AbstractInformed}�������*/
    protected abstract T createDefine(StartElementEvent event);
    /**��ȡ������{@link AbstractInformed}�������*/
    protected final T getDefine(XmlStackDecorator context) {
        return (T) context.getAttribute(AopInformedDefine);
    };
    /**��ʼ��ǩ����*/
    public void beginElement(XmlStackDecorator context, String xpath, StartElementEvent event) throws NoDefinitionException, NotFoundException {
        T define = this.createDefine(event);
        context.setAttribute(AopInformedDefine, define);
        //1.��ȡ����config
        AopConfigDefine config = (AopConfigDefine) context.getAttribute(TagAop_Config.ConfigDefine);
        //2.������ǩ
        String refBean = event.getAttributeValue("refBean");
        if (refBean == null)
            throw new NoDefinitionException("[" + config.getName() + "]����informed��before��returning��throwing��filter��ǩʱû�ж���refBean���ԡ�");
        if (this.getDefineResource().containsBeanDefine(refBean) == false)
            throw new NotFoundException("[" + config.getName() + "]����informed��before��returning��throwing��filter��ǩʱ�޷��ҵ������[" + refBean + "]Bean��");
        define.setRefBean(refBean);
        String pointcutRef = event.getAttributeValue("pointcut-ref");
        //3.��Informed��ӵ������config�С�
        if (pointcutRef != null) {
            AopDefineResourcePlugin plugin = (AopDefineResourcePlugin) this.getDefineResource().getPlugin(AopDefineResourcePlugin.AopDefineResourcePluginName);
            AbstractPointcutDefine pointcutDefine = plugin.getPointcutDefine(pointcutRef);
            config.addInformed(define, pointcutDefine);
        } else
            config.addInformed(define);
    }
    /**������ǩ������ɾ��ʹ�õı�����*/
    public void endElement(XmlStackDecorator context, String xpath, EndElementEvent event) {
        context.removeAttribute(AopInformedDefine);
    }
}