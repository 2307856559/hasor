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
package org.more.classcode;
/**
 * �����쳣��
 * @version 2009-7-7
 * @author ������ (zyc@hasor.net)
 */
public class TypeException extends ClassCodeRuntimeException {
    private static final long serialVersionUID = -6286611015368846627L;
    /**�����쳣*/
    public TypeException(String string) {
        super(string);
    }
    /**�����쳣*/
    public TypeException(Throwable error) {
        super(error);
    }
    /**�����쳣*/
    public TypeException(String string, Throwable error) {
        super(string, error);
    }
}