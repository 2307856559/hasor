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
package org.test.more.hypha.xml.class_bean;
import java.util.Date;
/**
 * 
 * @version 2011-6-22
 * @author ������ (zyc@byshell.org)
 */
public class TestBean_0 {
    /*����*/
    public TestBean_0() {
        System.out.println("create bean by C.");
    }
    /*����*/
    public Object factoryMethod(Date d1) {
        System.out.println("create bean by F. \t" + d1);
        return new Object();
    };
    //
    //�������ڷ���
    public void initMethod() {
        System.out.println("initMethod");
    };
    public void destroyMethod() {
        System.out.println("destroyMethod");
    }
    //
    public void ref_init() {
        System.out.println("ref_init");
    };
    public void ref_destroy() {
        System.out.println("ref_destroy");
    }
}