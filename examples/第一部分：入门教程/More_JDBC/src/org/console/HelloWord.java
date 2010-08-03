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
import org.more.submit.SubmitBuild;
import org.more.submit.SubmitContext;
import org.more.submit.casing.more.MoreBuilder;
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
        //��һ�������SubmitContext����
        SubmitBuild cd = new SubmitBuild();//����SubmitContext�����������ฺ������SubmitContext�����
        cd.build(new MoreBuilder("more-config.xml"));//����build��������SubmitContext����ClientMoreBuilder������רע������������������
        SubmitContext submitContext = cd.getResult();//������ɵ�SubmitContext����
        //�ڶ������������ǵ� Action���������һ�÷���ֵ��
        submitContext.doAction("testJDBCAction.all");
        submitContext.doAction("testJobAction.insert");
    }
}