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
 * ת���쳣������������ת��������ת�������κ�ת������ʱ�����������׳����쳣��
 * Date : 2009-7-8
 * @author ������
 */
public class StateException extends Exception {
    /**  */
    private static final long serialVersionUID = 5032345759263916241L;
    /**
     * ת���쳣������������ת��������ת�������κ�ת������ʱ�����������׳����쳣��
     * @param string �쳣��������Ϣ
     */
    public StateException(String string) {
        super(string);
    }
    /**
     * ת���쳣������������ת��������ת�������κ�ת������ʱ�����������׳����쳣��
     * @param error �쳣��������Ϣ
     */
    public StateException(Throwable error) {
        super(error);
    }
    /**
     * ת���쳣������������ת��������ת�������κ�ת������ʱ�����������׳����쳣��
     * @param string �쳣��������Ϣ��
     * @param error �нӵ���һ���쳣����
     */
    public StateException(String string, Throwable error) {
        super(string, error);
    }
}
