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
package org.console;
import org.more.submit.CasingDirector;
import org.more.submit.SubmitContext;
import org.more.submit.casing.spring.ClientSpringBuilder;
/**
 * 
 * Date : 2009-12-11
 * @author Administrator
 */
public class HelloWord {
    /**
     * @param args
     * @throws Throwable 
     */
    public static void main(String[] args) throws Throwable {
        CasingDirector cd = new CasingDirector(null);//����SubmitContext�����������ฺ������SubmitContext�����
        cd.build(new ClientSpringBuilder());//����build��������SubmitContext����
        SubmitContext submitContext = cd.getResult();//������ɵ�SubmitContext����
        //�ڶ������������ǵ�HelloWord Action���������һ�÷���ֵ��
        Object res = submitContext.doAction("action.hello");
        //���������������ֵ
        System.out.println(res);
    }
}