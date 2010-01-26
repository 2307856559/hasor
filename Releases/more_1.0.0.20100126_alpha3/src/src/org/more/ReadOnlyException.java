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
 * ��ͼ����ֻ����Դ�쳣������ͼ��ĳ��Դ��ֻ���ڼ�����������ֵʱ������
 * @version 2009-4-29
 * @author ������ (zyc@byshell.org)
 */
public class ReadOnlyException extends RuntimeException {
    /**  */
    private static final long serialVersionUID = 6090587253990681159L;
    /**
     * ��ͼ����ֻ����Դ�쳣������ͼ��ĳ��Դ��ֻ���ڼ�����������ֵʱ������
     * @param string �쳣��������Ϣ
     */
    public ReadOnlyException(String string) {
        super(string);
    }
    /**
     * ��ͼ����ֻ����Դ�쳣������ͼ��ĳ��Դ��ֻ���ڼ�����������ֵʱ������
     * @param error �쳣��������Ϣ
     */
    public ReadOnlyException(Throwable error) {
        super(error);
    }
    /**
     * ��ͼ����ֻ����Դ�쳣������ͼ��ĳ��Դ��ֻ���ڼ�����������ֵʱ������
     * @param string �쳣��������Ϣ��
     * @param error �нӵ���һ���쳣����
     */
    public ReadOnlyException(String string, Throwable error) {
        super(string, error);
    }
}