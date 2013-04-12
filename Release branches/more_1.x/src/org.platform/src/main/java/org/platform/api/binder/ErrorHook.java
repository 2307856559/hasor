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
package org.platform.api.binder;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.platform.api.context.AppContext;
import org.platform.api.context.Config;
import org.platform.api.context.ViewContext;
/**
 * Servlet�쳣�������
 * @version : 2013-4-12
 * @author ������ (zyc@byshell.org)
 */
public interface ErrorHook {
    /**��ʼ��Servlet�쳣���ӡ�*/
    public void init(AppContext appContext, Config initConfig);
    /**
     * ���쳣ִ�д�����������޷������쳣����ִ�г�������Լ������쳣�����׳����׳����쳣�����½����쳣���������д���<br/>
     * ��������û�������ѭ��.,
     */
    public void doError(ViewContext viewContext, ServletRequest request, ServletResponse response, Throwable error) throws Throwable;
    /**�����쳣���ӡ�*/
    public void destroy(AppContext appContext);
}