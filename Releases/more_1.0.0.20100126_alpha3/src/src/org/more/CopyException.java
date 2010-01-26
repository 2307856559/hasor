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
 * ������Դʱ�����쳣��
 * @version 2009-5-21
 * @author ������ (zyc@byshell.org)
 */
public class CopyException extends RuntimeException {
    /**  */
    private static final long serialVersionUID = -1658004519032584076L;
    /**
     * ������Դʱ�����쳣��
     * @param string �쳣��������Ϣ
     */
    public CopyException(String string) {
        super(string);
    }
    /**
     * ������Դʱ�����쳣��
     * @param error �쳣��������Ϣ
     */
    public CopyException(Throwable error) {
        super(error);
    }
    /**
     * ������Դʱ�����쳣��
     * @param string �쳣��������Ϣ��
     * @param error �нӵ���һ���쳣����
     */
    public CopyException(String string, Throwable error) {
        super(string, error);
    }
}