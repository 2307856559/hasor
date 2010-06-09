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
package org.more.submit.ext.actionjs;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.more.CastException;
import org.more.core.copybean.CopyBeanUtil;
import org.more.core.json.JsonUtil;
import org.more.submit.ActionContext;
import org.more.submit.ActionStack;
import org.more.submit.support.web.ActionTag;
import org.more.submit.support.web.WebActionStack;
/**
 * Submit���actionjs���ò��ʹjavascript����action����action�ķ���ֵʹ��javascript������Ϊ���ܡ�
 * @version 2010-1-7
 * @author ������ (zyc@byshell.org)
 */
public class JavaScriptSubmitManager {
    private boolean min = true;
    public void setMin(boolean min) {
        this.min = min;
    }
    /**�ͻ���JS����ִ��ĳ��actionʱ�������action������*/
    @SuppressWarnings("unchecked")
    public Object execute(WebActionStack event) throws Throwable {
        String callName = event.getParam("callName").toString();//���ñ����
        Map params = (Map) new JsonUtil().toMap(event.getParamString("args"));//��ȡ�����б�
        Object result = event.getContext().doActionOnStack(callName, event, params);//Action��ʽ����
        //======================================================================================
        HttpServletResponse response = event.getResponse();
        try {
            response.getWriter().print(new JsonUtil().toString(result));
            response.getWriter().flush();
        } catch (Exception e) {}
        return result;
    }
    /**��ȡ�ͻ���JS��action������*/
    public Object config(WebActionStack event) throws IOException, CastException {
        //��ȡ�������
        Writer write = null;
        if (event.contains("tag") == true) {
            ActionTag tag = (ActionTag) event.getParam("tag");
            write = tag.getOut();
        } else {
            HttpServletResponse response = event.getResponse();
            write = response.getWriter();
        }
        //������Ľű�
        StringBuffer str = new StringBuffer();
        InputStream core = CopyBeanUtil.class.getResourceAsStream("/org/more/submit/ext/actionjs/JavaScriptSubmitManager.js");
        BufferedReader reader = new BufferedReader(new InputStreamReader(core, "utf-8"));
        while (true) {
            String str_read = reader.readLine();
            if (str_read == null)
                break;
            else
                str.append(str_read + "\n");
        }
        //����������� org.more.web.submit.ROOT.Action
        HttpServletRequest request = event.getRequest();
        Object protocol = event.getServletContext().getAttribute("org.more.web.submit.ROOT.Action");
        str.append("more.retain.serverCallURL=");
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getLocalPort() + request.getContextPath();
        str.append("'" + url + "/");
        str.append(protocol + "!" + event.getActionName() + ".execute';");
        str.append("more.server={};");
        //�������minΪtrue��ʾ�����С���ű�����С���ű��в�����action�Ķ��塣
        String minParam = event.getParamString("min");
        if (minParam == null) {
            if (this.min == false)
                this.putAllJS(event, str);
        } else if (minParam.equals("false"))
            this.putAllJS(event, str);
        write.write(str + "\n");
        write.flush();
        return str;
    }
    private void putAllJS(WebActionStack event, StringBuffer str) {
        ActionContext context = event.getContext().getActionContext();
        Iterator<String> ns = context.getActionNameIterator();
        while (ns.hasNext()) {
            String n = ns.next();
            if (n == null)
                break;
            boolean haveActionMethod = false;/* Bug 111 ��Ŀ�����action�������κ�action����ʱ�����js�ű��ڴ������һ������ʱ�Ὣ��һ�������Ŵ�����Ӷ�����javascript�﷨�쳣*/
            str.append("more.server." + n + "={");
            Class<?> type = context.getActionType(n);
            Method[] ms = type.getMethods();
            for (Method m : ms) {
                //1.Ŀ�귽�������б������types�ֶ��д�ŵĸ�����һ���ĺ��ԡ�
                Class<?>[] paramTypes = m.getParameterTypes();
                if (paramTypes.length != 1)
                    continue;
                //2.����в������Ͳ�һ����Ҳ����
                if (ActionStack.class.isAssignableFrom(paramTypes[0]) == false)
                    continue;
                //�������
                str.append(m.getName() + ":function(param){");
                str.append("return more.retain.callServerFunction(\"" + n + "." + m.getName() + "\",param);");
                str.append("},");
                haveActionMethod = true;//BUG 111
            }
            if (haveActionMethod == true)//BUG 111
                str.deleteCharAt(str.length() - 1);
            str.append("};");
        }
    }
}