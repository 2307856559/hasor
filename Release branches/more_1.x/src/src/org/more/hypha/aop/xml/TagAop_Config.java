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
package org.more.hypha.aop.xml;
import org.more.RepeateException;
import org.more.core.classcode.BuilderMode;
import org.more.core.xml.XmlElementHook;
import org.more.core.xml.XmlStackDecorator;
import org.more.core.xml.stream.EndElementEvent;
import org.more.core.xml.stream.StartElementEvent;
import org.more.hypha.aop.AopInfoConfig;
import org.more.hypha.aop.define.AbstractInformed;
import org.more.hypha.aop.define.AbstractPointcutDefine;
import org.more.hypha.aop.define.AopConfigDefine;
import org.more.hypha.beans.define.BaseBeanDefine;
import org.more.hypha.beans.xml.TagBeans_AbstractBeanDefine;
import org.more.hypha.context.xml.XmlDefineResource;
import org.more.util.StringConvert;
/**
 * ���ڽ���aop:config��ǩ
 * @version 2010-10-9
 * @author ������ (zyc@byshell.org)
 */
public class TagAop_Config extends TagAop_NS implements XmlElementHook {
    public static final String ConfigDefine = "$more_Aop_Config";
    /**����{@link TagAop_Config}����*/
    public TagAop_Config(XmlDefineResource configuration) {
        super(configuration);
    }
    /**��ʼ��ǩ����*/
    public void beginElement(XmlStackDecorator context, String xpath, StartElementEvent event) {
        AopConfigDefine config = new AopConfigDefine();
        //att :name
        String name = event.getAttributeValue("name");
        //att :aopMode
        String aopMode = event.getAttributeValue("aopMode");
        config.setName(name);
        BuilderMode mode = (BuilderMode) StringConvert.changeType(aopMode, BuilderMode.class, BuilderMode.Super);
        config.setAopMode(mode);
        context.setAttribute(ConfigDefine, config);
    }
    /**������ǩ����*/
    public void endElement(XmlStackDecorator context, String xpath, EndElementEvent event) {
        BaseBeanDefine bean = (BaseBeanDefine) context.getAttribute(TagBeans_AbstractBeanDefine.BeanDefine);
        AopConfigDefine config = (AopConfigDefine) context.getAttribute(ConfigDefine);
        //1.����ڲ���Informed
        AbstractPointcutDefine defaultPointcutDefine = config.getDefaultPointcutDefine();
        for (AbstractInformed informed : config.getAopInformedList())
            if (informed.getRefPointcut() == null)
                informed.setRefPointcut(defaultPointcutDefine);
        //3.ע�ᵽBean�ϡ�
        AopInfoConfig plugin = (AopInfoConfig) this.getDefineResource().getFlash().getAttribute(AopInfoConfig.ServiceName);
        plugin.setAop(bean, config);
        String name = config.getName();
        if (name != null)
            if (plugin.containAopDefine(name) == false)
                plugin.addAopDefine(config);
            else
                throw new RepeateException("���ܶ�ͬһ������[" + name + "]AopConfigDefine�����ظ����塣");
    }
}