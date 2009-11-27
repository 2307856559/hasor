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
 * δ�����쳣��ͨ�����ָ����쳣������ʹ����δ��������Ի��߲��ԣ�ͬʱ���쳣Ҳ��ʾ��Щ��ͼ���ʲ����ڵ���Դ�쳣��
 * Date : 2009-4-29
 * @author ������
 */
public class NoDefinitionException extends RuntimeException {
    /**  */
    private static final long serialVersionUID = 3664651649094973500L;
    /**
     * δ�����쳣��ͨ�����ָ����쳣������ʹ����δ��������Ի��߲��ԣ�ͬʱ���쳣Ҳ��ʾ��Щ��ͼ���ʲ����ڵ���Դ�쳣��
     * @param string �쳣��������Ϣ
     */
    public NoDefinitionException(String string) {
        super(string);
    }
    /**
     * δ�����쳣��ͨ�����ָ����쳣������ʹ����δ��������Ի��߲��ԣ�ͬʱ���쳣Ҳ��ʾ��Щ��ͼ���ʲ����ڵ���Դ�쳣��
     * @param error �쳣��������Ϣ
     */
    public NoDefinitionException(Throwable error) {
        super(error);
    }
    /**
     * δ�����쳣��ͨ�����ָ����쳣������ʹ����δ��������Ի��߲��ԣ�ͬʱ���쳣Ҳ��ʾ��Щ��ͼ���ʲ����ڵ���Դ�쳣��
     * @param string �쳣��������Ϣ��
     * @param error �нӵ���һ���쳣����
     */
    public NoDefinitionException(String string, Throwable error) {
        super(string, error);
    }
}
