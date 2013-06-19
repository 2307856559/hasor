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
 * more�������쳣���������쳣��ָ��̬�Ĳ��ɱ�����������µġ�ͨ���쳣�������������ʵģ���Խʱ��ġ�
 * ���磺��ʽ��֧���ԡ������ԡ��ռ�̶ȡ�
 * @version 2009-10-17
 * @author ������ (zyc@byshell.org)
 */
public class MoreDefineException extends MoreException {
    private static final long serialVersionUID = 5497348918164005888L;
    /** more�������쳣��*/
    public MoreDefineException(String string) {
        super(string);
    }
    /** more�������쳣��*/
    public MoreDefineException(Throwable error) {
        super(error);
    }
    /** more�������쳣��*/
    public MoreDefineException(String string, Throwable error) {
        super(string, error);
    }
}