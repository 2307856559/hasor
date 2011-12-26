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
package org.more.core.copybean.rw;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.more.core.copybean.PropertyReader;
import org.more.core.copybean.PropertyWrite;
/**
 * HttpSession���д����ʹ�ø�����Ϊ��д������ʵ�ִ�HttpSession�п������Ի�����HttpSession�п������ԡ�
 * @version 2009-5-15
 * @author ������ (zyc@byshell.org)
 */
public class WebSessionRW implements PropertyReader<HttpSession>, PropertyWrite<HttpSession> {
    public List<String> getPropertyNames(HttpSession target) {
        ArrayList<String> names = new ArrayList<String>();
        Enumeration<String> attNames = target.getAttributeNames();
        while (attNames.hasMoreElements())
            names.add(attNames.nextElement());
        return names;
    };
    public boolean canWrite(String propertyName, HttpSession target, Object newValue) {
        return true;
    };
    public boolean writeProperty(String propertyName, HttpSession target, Object newValue) {
        target.setAttribute(propertyName, newValue);
        return true;
    };
    public boolean canReader(String propertyName, HttpSession target) {
        return true;
    };
    public Object readProperty(String propertyName, HttpSession target) {
        return target.getAttribute(propertyName);
    };
    public Class<?> getTargetClass() {
        return HttpSession.class;
    };
};