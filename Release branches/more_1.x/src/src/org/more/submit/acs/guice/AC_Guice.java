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
package org.more.submit.acs.guice;
import org.more.submit.acs.simple.AC_Simple;
/**
 * ������չ��{@link AC_Simple}�������ж��Ƿ���һ��guice Bean��
 * �������ʹ��guice����������ʹ��{@link AC_Simple}��ʽ������
 * @version : 2011-7-14
 * @author ������ (zyc@byshell.org)
 */
public class AC_Guice extends AC_Simple {
    public Object guiceInjector = null;
    public Object modules       = null;
    //
    //  
    protected Object getBean(Class<?> type) throws Throwable {
        GBean annoBean = type.getAnnotation(GBean.class);
        if (annoBean == null)
            return super.getBean(type);
        System.out.println("juice create");
        return null;
    }
    a
};