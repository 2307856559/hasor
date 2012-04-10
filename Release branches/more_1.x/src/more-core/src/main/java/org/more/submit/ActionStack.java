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
package org.more.submit;
import java.net.URI;
import org.more.util.attribute.Attribute;
import org.more.util.attribute.DecParentAttribute;
import org.more.util.attribute.IAttribute;
/**
 * ����һ��actionִ��ʱ�Ĳ�����ջ��ÿ������ִ��Action����ʱ��submit�����Զ�����һ���µĶ�ջ��
 * @version : 2011-7-14
 * @author ������ (zyc@byshell.org)
 */
public class ActionStack extends DecParentAttribute<Object> {
    private SubmitService service = null;
    public ActionStack(URI uri, IAttribute<Object> parent, SubmitService service) {
        super(parent);
        this.service = service;
    };
    public ActionStack(URI uri, SubmitService service) {
        super(new Attribute<Object>());
        this.service = service;
    };
    /**��ȡ{@link SubmitService}�ӿڶ���*/
    public SubmitService getSubmitService() {
        return this.service;
    };
    public ActionStack getParent() {
        IAttribute<Object> parent = super.getParent();
        if (parent instanceof ActionStack == true)
            return (ActionStack) parent;
        return null;
    };
};