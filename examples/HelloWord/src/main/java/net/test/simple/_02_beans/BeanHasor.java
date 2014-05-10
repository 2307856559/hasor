/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
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
package net.test.simple._02_beans;
import java.io.IOException;
import java.net.URISyntaxException;
import net.hasor.core.ApiBinder;
import net.hasor.core.AppContext;
import net.hasor.core.Module;
import net.hasor.core.context.StandardAppContext;
import net.test.simple._02_beans.pojo.PojoBean;
import org.junit.Test;
/**
 * ��ʾ����ʾ������� Hasor ��ܡ�
 * @version : 2013-8-11
 * @author ������ (zyc@hasor.net)
 */
public class BeanHasor {
    @Test
    public void testBeanHasor() throws IOException, URISyntaxException, InterruptedException {
        System.out.println("--->>testBeanHasor<<--");
        //1.����һ����׼�� Hasor ������
        AppContext appContext = new StandardAppContext();
        appContext.addModule(new Module() {
            public void init(ApiBinder apiBinder) throws Throwable {
                apiBinder.defineBean("myBean1").bindType(PojoBean.class);
                PojoBean pojo = new PojoBean();
                pojo.setName("���˧");
                apiBinder.defineBean("myBean2").bindType(PojoBean.class).toInstance(pojo);
            }
            public void start(AppContext appContext) throws Throwable {
                // TODO Auto-generated method stub
            }
        });
        appContext.start();//���� Hasor �������������̻��ʼ������ģ��Ͳ����
        //
        PojoBean myBean1 = appContext.getBean("myBean1");
        System.out.println(myBean1.getName());
        PojoBean myBean2 = appContext.getBean("myBean2");
        System.out.println(myBean2.getName());
    }
}