/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
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
package org.more.webui.context.scope;
import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.ServletContext;
import org.more.webui.context.ViewContext;
/**
 * �����ṩHttpSession������Ķ�ȡ����֧��ɾ���޸Ĳ�����
 * @version 2009-12-28
 * @author ������ (zyc@byshell.org)
 */
public class ServletContextScope extends HashMap<String, Object> {
    /**  */
    private static final long serialVersionUID = -9064716145027131749L;
    public ServletContextScope(ViewContext viewContext) {
        ServletContext servletContext = viewContext.getHttpRequest().getSession(true).getServletContext();
        Enumeration<?> attEnum = servletContext.getAttributeNames();
        while (attEnum.hasMoreElements()) {
            String key = attEnum.nextElement().toString();
            super.put(key, servletContext.getAttribute(key));
        }
    }
    @Override
    public Object get(Object key) {
        return super.get(key);
    }
    @Override
    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }
};