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
package org.stacktree;
import java.util.Map;
import org.more.submit.CasingDirector;
import org.more.submit.SubmitContext;
import org.more.submit.casing.more.ClientMoreBuilder;
import org.more.util.StringConvert;
public class Main {
    /**
     * @param args
     * @throws Throwable 
     */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Throwable {
        CasingDirector cd = new CasingDirector(null);//����SubmitContext�����������ฺ������SubmitContext�����
        cd.build(new ClientMoreBuilder("more-config.xml"));//����build��������SubmitContext����ClientMoreBuilder������רע������������������
        SubmitContext submitContext = cd.getResult();//������ɵ�SubmitContext����
        Map var = StringConvert.parseMap("var=value");
        System.out.println("var=" + var.get("var"));
        System.out.println("-------------");
        submitContext.doAction("a_1.invoke", null, var);//a_1�����a_2��ͬʱ����var������
        System.out.println("-------------");
        submitContext.doAction("a_2.invoke", null, var);//a_2�����a_3�����var������
        System.out.println("-------------");
        submitContext.doAction("a_3.invoke", null, var);//��� var������
    }
}