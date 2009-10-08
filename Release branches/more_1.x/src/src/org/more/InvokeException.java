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
package org.more;
/**
 * ������ʱ������ִ�в���ʱ�����쳣��
 * Date : 2009-6-26
 * @author ������
 */
public class InvokeException extends RuntimeException {
    /**  */
    private static final long serialVersionUID = -7774988512856603877L;
    /**
     * ������ʱ������ִ�в���ʱ�����쳣��
     * @param string �쳣��������Ϣ
     */
    public InvokeException(String string) {
        super(string);
    }
    /**
     * ������ʱ������ִ�в���ʱ�����쳣��
     * @param error �쳣��������Ϣ
     */
    public InvokeException(Throwable error) {
        super(error);
    }
}
