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
package org.hasor.test.core.bean;
import net.hasor.core.ApiBinder;
import net.hasor.core.ApiBinder.ModuleSettings;
import net.hasor.core.context.AnnoModule;
import org.hasor.test.core.bean.beans.CustomerBean;
/**
 * ���뷽ʽע��Bean
 * @version : 2013-8-11
 * @author ������ (zyc@hasor.net)
 */
@AnnoModule
public class CustomerBeanMod extends AbstractHasorModule {
    public void configuration(ModuleSettings info) {
        // TODO Auto-generated method stub
    }
    public void init(ApiBinder apiBinder) {
        /*���뷽ʽע��Bean*/
        apiBinder.newBean("Customer").bindType(CustomerBean.class);
        /*���뷽ʽע��Bean����̬*/
        //apiBinder.newBean("Customer").bindType(CustomerBean.class).asEagerSingleton();
    }
}