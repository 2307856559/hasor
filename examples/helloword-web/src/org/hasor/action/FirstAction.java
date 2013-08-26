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
package org.hasor.action;
import org.hasor.mvc.controller.Controller;
import org.hasor.mvc.controller.HeaderParam;
import org.hasor.mvc.controller.Path;
import org.hasor.mvc.controller.PathParam;
import org.hasor.mvc.controller.QueryParam;
/**
 * 
 * @version : 2013-8-23
 * @author ������(zyc@hasor.net)
 */
@Controller("/abc/123")
//�����ռ�
public class FirstAction {
    /*print��action����*/
    public void print() {
        System.out.println("Hello Action!");
    }
    @Path("/user/{uid}/")
    public void userInfo(@PathParam("uid") String uid,//@Path�������Ĳ�����
            @HeaderParam("User-Agent") String userAgent,//Heade����ͷ
            @QueryParam("age") int age,//�����ַ��?��֮��Ĳ�����
            @QueryParam("ns") String[] ns) {//ͬ����������
        //
        System.out.println(String.format("user %s age=%s by:%s", uid, age, userAgent));
    }
}