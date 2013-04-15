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
package org.platform.binder;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.platform.context.AppContext;
import org.platform.context.ViewContext;
import com.google.inject.ImplementedBy;
/**
 * ʹ��ApiBinder������Servlet,Filter��ɵĹܵ���
 * @version : 2013-4-11
 * @author ������ (zyc@byshell.org)
 */
@ImplementedBy(DefaultFilterPipeline.class)
public interface FilterPipeline {
    /**��ʼ���ܵ�*/
    public void initPipeline(AppContext appContext) throws ServletException;
    /**ִ���������*/
    public void dispatch(ViewContext viewContext, HttpServletRequest request, HttpServletResponse response, FilterChain defaultFilterChain) throws IOException, ServletException;
    /**���ٹܵ�*/
    public void destroyPipeline(AppContext appContext);
}