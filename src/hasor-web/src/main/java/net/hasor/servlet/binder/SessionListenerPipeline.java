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
package net.hasor.servlet.binder;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionListener;
import net.hasor.context.AppContext;
/**
 * 
 * @version : 2013-4-17
 * @author ������ (zyc@hasor.net)
 */
public interface SessionListenerPipeline extends HttpSessionListener, ServletContextListener {
    /**��ʼ��Servlet�쳣���ӡ�*/
    public void init(AppContext appContext);
}