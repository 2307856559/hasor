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
package org.more.classcode.objects;
import org.more.classcode.ClassEngine;
import org.more.classcode.ClassNameStrategy;
/**
 * ������{@link ClassNameStrategy}��Ĭ��ʵ�֣��������ǡ�_DynamicObject$������������org.more.core.classcode������
 * @version 2010-9-3
 * @author ������ (zyc@byshell.org)
 */
public class DefaultClassNameStrategy implements ClassNameStrategy {
    private static long         generateID  = 0;
    private static final String ClassPrefix = "_Dynamic$";                     //�������������׺��
    private static final String ClassName   = "org.more.core.classcode.Object"; //Ĭ�������������
    public void initStrategy(ClassEngine classEngine) {}
    public synchronized String generateName(Class<?> superClass) {
        String cn = null;
        if (superClass == null)
            cn = ClassName;
        else
            cn = superClass.getName();
        generateID++;
        if (cn.startsWith("java.lang") == true)
            cn = "org.more";
        return cn + ClassPrefix + generateID;
    }
}