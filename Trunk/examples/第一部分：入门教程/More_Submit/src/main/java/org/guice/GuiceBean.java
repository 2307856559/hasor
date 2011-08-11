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
package org.guice;
import org.more.submit.Action;
import org.more.submit.ActionStack;
import org.more.submit.acs.guice.GBean;
/**
 * 
 * @version : 2011-8-11
 * @author ������ (zyc@byshell.org)
 */
@GBean
public class GuiceBean {
    @Action
    public Object testGuice(ActionStack stack) {
        System.out.println("����һ������Guice��Bean");
        return null;
    };
    @Action("/hi_1")
    public Object testHi(ActionStack stack) {
        System.out.println("����һ������Guice��Bean,��ַΪ hi_1");
        return null;
    };
}
/*
Action�ĵ�ַ�ǣ�
    guice://org.guice.GuiceBean.testGuice
    guice://hi_1
*/