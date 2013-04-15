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
import org.more.core.global.Global;
import org.platform.Assert;
import com.google.inject.Injector;
/**
 * 
 * @version : 2013-3-26
 * @author ������ (zyc@byshell.org)
 */
public abstract class AppContext {
    private InitContext initContext = null;
    /**��ȡӦ�ó������á�*/
    public Global getSettings() {
        InitContext initContext = this.getInitContext();
        Assert.isNotNull(initContext, "AppContext.getInitContext() return is null.");
        return initContext.getSettings();
    };
    /**��ȡ��ʼ��������*/
    public InitContext getInitContext() {
        if (this.initContext == null)
            this.initContext = this.getGuice().getInstance(InitContext.class);
        return this.initContext;
    }
    /**���Guice������*/
    public abstract Injector getGuice();
    /**ͨ�����ƴ���beanʵ����ʹ��guice��*/
    public <T> T getBean(String name) {
        Class<T> classType = this.getBeanType(name);
        if (classType == null)
            return null;
        return this.getBean(classType);
    };
    /**ͨ������ȡBean�����͡�*/
    public abstract <T> Class<T> getBeanType(String name);
    /**ͨ�����ʹ�������ʵ����ʹ��guice*/
    public <T> T getBean(Class<T> beanType) {
        return this.getGuice().getInstance(beanType);
    };
    //    /**ͨ�����ƴ���beanʵ����ʹ��guice��*/
    //    public abstract <T extends IService> T getService(String servicesName);
    //    /**ͨ�����ʹ�������ʵ����ʹ��guice*/
    //    public abstract <T extends IService> T getService(Class<T> servicesType);
    /**��ȡ�Ѿ�ע���Bean���ơ�*/
    public abstract List<String> getBeanNames();
    /*----------------------------------------------------------------------*/
    /**����һ��UUID�ַ�����32���ַ������ȡ�*/
    public static String genIDBy32() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    /**����һ��UUID�ַ�����36���ַ������ȡ�*/
    public static String genIDBy36() {
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
    };
}