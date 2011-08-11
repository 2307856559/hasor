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
package org.spring;
import org.more.submit.Action;
import org.more.submit.ActionStack;
import org.springframework.stereotype.Service;
/**
 * ��ΪAction������ǹ��еķǳ���ģ�ͬʱҲ�����ǽӿڡ�
 * Date : 2009-12-11
 */
@Service
public class AnnoSpringBean {
    @Action
    public Object testSpring(ActionStack stack) {
        System.out.println("����һ������Springע�����õ�Bean");
        return null;
    };
    //����ʹ�á�/hi������Ϊ��Spring��������ռ����Ѿ������ˡ�/hi�����Ǹ�bean���������ļ�����ġ�
    @Action("/hi_5")
    public Object testHi(ActionStack stack) {
        System.out.println("����һ������Springע�����õ�Bean,��ַΪ hi_5");
        return null;
    };
}
/*
Action�ĵ�ַ�ǣ�
    spring://org.spring.AnnoSpringBean.testSpring
    spring://hi_5
*/