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
package org.more.log;
/**
 * ��־�쳣
 * Date : 2009-5-13
 * @author ������
 */
public class LogException extends RuntimeException {
    private static final long serialVersionUID = 8222826886737695884L;
    // ===============================================================
    /** ��־�쳣 */
    public LogException() {
        super("��־�쳣");
    }
    /** ��־�쳣��������Ϣ�ɲ������� */
    public LogException(String msg) {
        super(msg);
    }
    /** ��־�쳣��������Ϣ�ǳн���һ���쳣���� */
    public LogException(Exception e) {
        super(e);
    }
}
