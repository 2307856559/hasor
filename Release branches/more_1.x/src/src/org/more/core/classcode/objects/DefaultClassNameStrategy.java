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
package org.more.core.classcode.objects;
import org.more.core.classcode.ClassEngine;
import org.more.core.classcode.ClassNameStrategy;
/**
 * ������{@link ClassNameStrategy}��Ĭ��ʵ�֣��������ǡ�_DynamicObject$������������org.more.core.classcode������
 * @version 2010-9-3
 * @author ������ (zyc@byshell.org)
 */
public class DefaultClassNameStrategy implements ClassNameStrategy {
    private static long         generateID  = 0;
    private static final String ClassPrefix = "_DynamicObject$"; //�������������׺��
    public void initStrategy(ClassEngine classEngine) {}
    public void reset() {}
    public String generatePackageName() {
        return "org.more.core.classcode";
    }
    public synchronized String generateSimpleName() {
        generateID++;
        return ClassPrefix + generateID;
    }
}