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
 * ��ʽ���󣬳��ָ��쳣ͨ�����ڲ���ĳЩ����ʱ�����ݸ�ʽ�쳣���߲���֧�֡�
 * @version 2009-10-17
 * @author ������ (zyc@byshell.org)
 */
public class FormatException extends RuntimeException {
    /**  */
    private static final long serialVersionUID = 6079652246835019946L;
    /**
     * ��ʽ���󣬳��ָ��쳣ͨ�����ڲ���ĳЩ����ʱ�����ݸ�ʽ�쳣���߲���֧�֡�
     * @param string �쳣��������Ϣ
     */
    public FormatException(String string) {
        super(string);
    }
    /**
     * ��ʽ���󣬳��ָ��쳣ͨ�����ڲ���ĳЩ����ʱ�����ݸ�ʽ�쳣���߲���֧�֡�
     * @param error �쳣��������Ϣ
     */
    public FormatException(Throwable error) {
        super(error);
    }
    /**
     * ��ʽ���󣬳��ָ��쳣ͨ�����ڲ���ĳЩ����ʱ�����ݸ�ʽ�쳣���߲���֧�֡�
     * @param string �쳣��������Ϣ��
     * @param error �нӵ���һ���쳣����
     */
    public FormatException(String string, Throwable error) {
        super(string, error);
    }
}