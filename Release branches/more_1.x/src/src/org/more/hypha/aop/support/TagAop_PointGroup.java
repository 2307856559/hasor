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
import org.more.core.xml.XmlStackDecorator;
import org.more.core.xml.stream.StartElementEvent;
import org.more.hypha.DefineResource;
import org.more.hypha.aop.define.AopPointcutGroupDefine;
import org.more.hypha.aop.define.AopPointcutGroupDefine.RelEnum;
import org.more.util.StringConvert;
/**
 * ���ڽ���aop:pointGroup��ǩ
 * @version 2010-9-22
 * @author ������ (zyc@byshell.org)
 */
public class TagAop_PointGroup extends TagAop_AbstractPointcut<AopPointcutGroupDefine> {
    /**����{@link TagAop_PointGroup}����*/
    public TagAop_PointGroup(DefineResource configuration) {
        super(configuration);
    }
    /**����{@link AopPointcutGroupDefine}���Ͷ���*/
    protected AopPointcutGroupDefine createDefine() {
        return new AopPointcutGroupDefine();
    }
    /**��ʼ��ǩ�����ڽ���rel���ԡ�*/
    public void beginElement(XmlStackDecorator context, String xpath, StartElementEvent event) {
        super.beginElement(context, xpath, event);
        AopPointcutGroupDefine define = this.getDefine(context);
        String rel = event.getAttributeValue("rel");
        RelEnum relEnum = (RelEnum) StringConvert.changeType(rel, RelEnum.class, RelEnum.And);
        define.setRelation(relEnum);
    }
}