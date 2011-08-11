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
package org.more.submit.web.result;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import org.more.submit.ResultProcess;
import org.more.submit.impl.DefaultActionStack;
import org.more.submit.web.WebActionStack;
/**
 * ���������Ӧ
 * @version : 2011-7-25
 * @author ������ (zyc@byshell.org)
 */
public class WebErrorResultProcess implements ResultProcess<WebErrorResult> {
    public Object invoke(DefaultActionStack onStack, WebErrorResult res) throws ServletException, IOException {
        WebActionStack webStack = (WebActionStack) onStack;
        HttpServletResponse response = webStack.getHttpResponse();
        if (response.isCommitted() == false)
            response.sendError(res.getNumber(), res.getObject());
        return res;
    }
    public void addParam(String key, String value) {}
}