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
package org.more.services.submit.acs.hypha;
import java.lang.reflect.Method;
import org.more.hypha.ApplicationContext;
import org.more.hypha.anno.assembler.AnnoMetaDataUtil;
import org.more.hypha.anno.define.Bean;
import org.more.services.submit.acs.simple.AC_Simple;
import org.more.services.submit.impl.AbstractAC;
/**
 * ������չ��{@link AC_Simple}�࣬���������{@link Bean}�Ӹ�����Զ�����bean����
 * ��hyphaʹ�õ���xml���õ�beanʱ�򣬿���ʹ��{@link HBean}ע����ָ��bean����
 * ��������ע�ⶼû��ָ��ʱ�򣬷���null��
 * @version : 2011-7-14
 * @author ������ (zyc@byshell.org)
 */
public class AC_Hypha extends AbstractAC {
    public ApplicationContext applicationContext = null;
    //
    protected Object getBean(Method actionPath, String queryInfo) throws Throwable {
        Class<?> type = actionPath.getDeclaringClass();
        Bean annoBean = type.getAnnotation(Bean.class);
        String beanID = null;
        if (annoBean == null) {
            HBean annoBean2 = type.getAnnotation(HBean.class);
            if (annoBean2 != null)
                beanID = annoBean2.beanID();
        } else
            beanID = AnnoMetaDataUtil.getBeanID(annoBean, type);
        if (beanID != null)
            return applicationContext.getBean(beanID);
        else
            return null;
    };
};