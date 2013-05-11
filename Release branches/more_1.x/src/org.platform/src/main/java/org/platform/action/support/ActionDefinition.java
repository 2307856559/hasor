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
package org.platform.action.support;
import java.lang.reflect.Method;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.inject.Provider;
/**
 * ��ʾһ��Action���塣
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
class ActionDefinition implements Provider<ActionInvoke>, ActionInvoke {
    private String   namespace    = null; //���������ռ�
    private String   actionMethod = null; //action����
    private Class<?> targetClass  = null;
    //
    //
    //
    //
    @Override
    public ActionInvoke get() {
        return this;
    }
    /*-------------------------------------------------------*/
    @Override
    public String getActionMethod() {
        return this.actionMethod;
    }
    @Override
    public Object invoke(HttpServletRequest request, HttpServletResponse response, Map<String, String[]> params) {
        
        return params;
    }
}