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
package org.more.submit.acs.spring;
import java.lang.reflect.Method;
import org.more.hypha.anno.define.Bean;
import org.more.submit.acs.hypha.HBean;
import org.more.submit.acs.simple.AC_Simple;
import org.more.submit.impl.AbstractAC;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
/**
 * ������չ��{@link AC_Simple}�࣬���������{@link Bean}�Ӹ�����Զ�����bean����
 * ��hyphaʹ�õ���xml���õ�beanʱ�򣬿���ʹ��{@link HBean}ע����ָ��bean����
 * ��������ע�ⶼû��ָ��ʱ�򷵻�null��
 * @version : 2011-7-14
 * @author ������ (zyc@byshell.org)
 */
public class AC_Spring extends AbstractAC {
    public ApplicationContext application = null;
    public String             config      = null;
    //
    protected Object getBean(Method actionPath, String queryInfo) throws Throwable {
        Class<?> type = actionPath.getDeclaringClass();
        Service annoBean = type.getAnnotation(Service.class);
        String beanID = null;
        if (annoBean != null)
            beanID = annoBean.value();
        else {
            SBean annoBean2 = type.getAnnotation(SBean.class);
            if (annoBean2 != null)
                beanID = annoBean2.refID();
        }
        if (this.application == null)
            this.application = this.createContext(this.config);
        if (beanID != null)
            return application.getBean(beanID);
        else
            return null;
    };
    protected ApplicationContext createContext(String config) {
        return new ClassPathXmlApplicationContext(config);
    };
};