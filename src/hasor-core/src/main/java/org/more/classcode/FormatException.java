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
 * ��ʽ���󣬳��ָ��쳣ͨ�����ڲ���ĳЩ����ʱ�����ݸ�ʽ�쳣���߲���֧�֡�
 * @version 2009-10-17
 * @author ������ (zyc@hasor.net)
 */
public class FormatException extends ClassCodeRuntimeException {
    private static final long serialVersionUID = 7219173260772984152L;
    /**��ʽ�쳣*/
    public FormatException(String string) {
        super(string);
    }
    /**��ʽ�쳣*/
    public FormatException(Throwable error) {
        super(error);
    }
    /**��ʽ�쳣*/
    public FormatException(String string, Throwable error) {
        super(string, error);
    }
}