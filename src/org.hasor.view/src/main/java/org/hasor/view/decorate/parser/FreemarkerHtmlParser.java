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
package org.hasor.view.decorate.parser;
import java.io.IOException;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import org.hasor.freemarker.FreemarkerManager;
import org.hasor.view.decorate.DecorateFilter;
import org.hasor.view.decorate.DecorateFilterChain;
import org.hasor.view.decorate.DecorateServletRequest;
import org.hasor.view.decorate.DecorateServletResponse;
import com.google.inject.Inject;
/**
 * HTMLװ����
 * @version : 2013-6-14
 * @author ������ (zyc@byshell.org)
 */
public class FreemarkerHtmlParser implements DecorateFilter {
    @Inject
    private FreemarkerManager freemarkerManager = null;
    @Override
    public void doDecorate(DecorateServletRequest request, DecorateServletResponse response, DecorateFilterChain chain) throws IOException {
        byte[] oriData = response.getBufferData();
        response.resetBuffer();
        try {
            response.getWriter().write(new String(oriData, "utf-8"));
            //response.getWriter().flush();
            //            freemarkerManager.getTemplate(null).process(null, response.getWriter());
            chain.doDecorate(request, response);
        } catch (Exception e) {
            // TODO: handle exception
            response.getOutputStream().write(oriData);
        }
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
    }
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }
}