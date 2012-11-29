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
import org.more.core.xml.XmlStackDecorator;
import org.more.core.xml.stream.StartElementEvent;
import org.more.hypha.define.aop.AopPointcut;
import org.more.hypha.xml.XmlDefineResource;
/**
 * ���ڽ���aop:pointcut��ǩ
 * @version 2010-9-22
 * @author ������ (zyc@byshell.org)
 */
public class TagAop_Pointcut extends TagAop_AbstractPointcut<AopPointcut> {
    /**����{@link TagAop_Pointcut}����*/
    public TagAop_Pointcut(XmlDefineResource configuration) {
        super(configuration);
    }
    /**����{@link AopPointcut}���Ͷ���*/
    protected AopPointcut createDefine() {
        return new AopPointcut();
    }
    /**��ʼ��ǩ����expression���ԡ�*/
    public void beginElement(XmlStackDecorator<Object> context, String xpath, StartElementEvent event) {
        super.beginElement(context, xpath, event);
        AopPointcut define = this.getDefine(context);
        define.setExpression(event.getAttributeValue("expression"));
    }
}