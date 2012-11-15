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
import org.more.core.xml.stream.StartElementEvent;
import org.more.hypha.define.AopDefineInformed;
import org.more.hypha.define.AopPointcutType;
import org.more.hypha.xml.XmlDefineResource;
/**
 * ���ڽ���aop:filter��ǩ
 * @version 2010-9-22
 * @author ������ (zyc@byshell.org)
 */
public class TagAop_Filter extends TagAop_AbstractInformed<AopDefineInformed> {
    /**����{@link TagAop_Filter}����*/
    public TagAop_Filter(XmlDefineResource configuration) {
        super(configuration);
    }
    /**����AopDefineInformed���Ͷ��󣬸ö����pointcutType����ֵΪ{@link AopPointcutType#Filter}��*/
    protected AopDefineInformed createDefine(StartElementEvent event) {
        AopDefineInformed informed = new AopDefineInformed();
        informed.setPointcutType(AopPointcutType.Filter);
        return informed;
    }
}