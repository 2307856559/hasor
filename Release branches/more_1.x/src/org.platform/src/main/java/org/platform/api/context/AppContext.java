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
package org.platform.api.context;
import java.io.File;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.more.core.global.Global;
import org.platform.api.scope.Scope;
import org.platform.api.scope.Scope.ScopeEnum;
import org.platform.api.services.IService;
/**
 * 
 * @version : 2013-3-26
 * @author ������ (zyc@byshell.org)
 */
public abstract class AppContext {
    /**��ȡ��ʼ��������*/
    public abstract InitContext getInitContext();
    /**ͨ�����ƴ���beanʵ����ʹ��guice��*/
    public abstract <T> T getBean(String name);
    /**ͨ�����ʹ�������ʵ����ʹ��guice*/
    public abstract <T> T getBean(Class<T> beanType);
    /**ͨ�����ƴ���beanʵ����ʹ��guice��*/
    public abstract <T extends IService> T getService(String servicesName);
    /**ͨ�����ʹ�������ʵ����ʹ��guice*/
    public abstract <T extends IService> T getService(Class<T> servicesType);
    /**��ȡ�Ѿ�ע���Bean���ơ�*/
    public abstract List<String> getBeanNames();
    /**ȡ��{@link HttpServletRequest}���Ͷ���*/
    public abstract HttpServletRequest getHttpRequest();
    /**ȡ��{@link HttpServletResponse}���Ͷ���*/
    public abstract HttpServletResponse getHttpResponse();
    /**ȡ��{@link HttpSession}���Ͷ���*/
    public HttpSession getHttpSession(boolean create) {
        return this.getHttpRequest().getSession(create);
    }
    /**��ȡ�������������*/
    public abstract Scope getScope(ScopeEnum scopeEnmu);
    /**��ȡӦ�ó������á�*/
    public Global getSettings() {
        return this.getInitContext().getSettings();
    }
    /*----------------------------------------------------------------------*/
    /**����һ��UUID�ַ�����*/
    public static String genUUID() {
        return UUID.randomUUID().toString();
    }
    /**
     * ����·���㷨��
     * @param number ����
     * @param size ÿ��Ŀ¼�¿���ӵ�е���Ŀ¼���ļ���Ŀ��
     */
    public static String genPath(long number, int size) {
        StringBuffer buffer = new StringBuffer();
        long b = size;
        long c = number;
        do {
            long m = number % b;
            buffer.append(m + File.separator);
            c = number / b;
            number = c;
        } while (c > 0);
        return buffer.reverse().toString();
    }
}