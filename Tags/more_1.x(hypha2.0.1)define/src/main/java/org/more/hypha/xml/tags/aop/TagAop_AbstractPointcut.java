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
import org.more.core.error.RepeateException;
import org.more.core.xml.XmlElementHook;
import org.more.core.xml.XmlStackDecorator;
import org.more.core.xml.stream.EndElementEvent;
import org.more.core.xml.stream.StartElementEvent;
import org.more.hypha.aop.AopService;
import org.more.hypha.define.aop.AopPointcut;
import org.more.hypha.define.aop.AopConfig;
import org.more.hypha.define.aop.AopPointcutGroupDefine;
import org.more.hypha.xml.XmlDefineResource;
/**
 * ���ڽ����е��ǩ�Ļ��࣬��������name���ԡ�
 * @version 2010-9-24
 * @author ������ (zyc@byshell.org)
 */
@SuppressWarnings("unchecked")
public abstract class TagAop_AbstractPointcut<T extends AopPointcut> extends TagAop_NS implements XmlElementHook {
    private static final String PointcutDefine = "$more_Aop_PointcutDefine";
    /**����{@link TagAop_AbstractPointcut}���Ͷ���*/
    public TagAop_AbstractPointcut(XmlDefineResource configuration) {
        super(configuration);
    }
    /**����һ��{@link AopPointcut}�������*/
    protected abstract T createDefine();
    /**��ȡ������{@link AopPointcut}�������*/
    protected final T getDefine(XmlStackDecorator<?> context) {
        return (T) context.getAttribute(PointcutDefine);
    };
    /**��ʼ�����ǩ*/
    public void beginElement(XmlStackDecorator<Object> context, String xpath, StartElementEvent event) {
        context.createStack();
        T define = this.createDefine();
        String name = event.getAttributeValue("name");
        if (name != null)
            define.setName(name);// or this.putAttribute(define, "name", name);
        context.setAttribute(PointcutDefine, (Object) define);
    }
    /**���������ǩ*/
    public void endElement(XmlStackDecorator<Object> context, String xpath, EndElementEvent event) {
        T define = this.getDefine(context);
        boolean isReg = false;
        //1.Pointcut������Group����
        T parentDefine = (T) context.getParentStack().getAttribute(PointcutDefine);
        if (parentDefine != null) {
            if (parentDefine instanceof AopPointcutGroupDefine) {
                ((AopPointcutGroupDefine) parentDefine).addPointcutDefine(define);
                isReg = true;
            }
        }
        //2.Pointcut������Config���� 
        AopConfig parentConfig = (AopConfig) context.getAttribute(TagAop_Config.ConfigDefine);
        if (isReg == false && parentConfig != null) {
            if (parentConfig != null) {
                if (parentConfig.getDefaultPointcutDefine() != null)
                    throw new RepeateException("���ܶ�AopConfigDefine���͵�[" + parentConfig.getName() + "]���еڶ��ζ���aop�е㡣");
                parentConfig.setDefaultPointcutDefine(define);
                isReg = true;
            }
        }
        //3.ע�ᵽ������
        if (isReg == false && define.getName() != null) {
            AopService service = this.getAopConfig();
            if (service.containPointcutDefine(define.getName()) == true)
                throw new RepeateException("�����ظ�����[" + define.getName() + "]��������");
            service.addPointcutDefine(define);
        }
        //
        context.removeAttribute(PointcutDefine);
        context.dropStack();
    }
}