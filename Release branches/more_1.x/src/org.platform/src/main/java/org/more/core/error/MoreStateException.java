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
package org.more.core.error;
/**
 * more״̬���쳣���������쳣��ָ��ĳ��ʱ��Ƭ���µ��쳣���������쳣����������ʺͶ���˫�����ԣ����Ǹ������쳣���ܵ�ʱ��Ƭ�����ơ�
 * ���磺��д״̬���ظ��������ȱ�١�ʱ��Ƭ���Եġ�
 * @version 2009-7-8
 * @author ������ (zyc@byshell.org)
 */
public class MoreStateException extends MoreRuntimeException {
    private static final long serialVersionUID = 5032345759263916241L;
    /**״̬���쳣*/
    public MoreStateException(String string) {
        super(string);
    }
    /**״̬���쳣*/
    public MoreStateException(Throwable error) {
        super(error);
    }
    /**״̬���쳣*/
    public MoreStateException(String string, Throwable error) {
        super(string, error);
    }
}