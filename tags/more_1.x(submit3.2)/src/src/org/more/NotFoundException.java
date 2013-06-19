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
 * �Ҳ����쳣��ͨ�����ָ����쳣ʱͨ����Ҫ�����Ŀ����Դ�����ڡ������������Ŀ����Դ�Ѿ����壬Ҳ������δ���塣
 * @version 2009-4-29
 * @author ������ (zyc@byshell.org)
 */
public class NotFoundException extends RuntimeException {
    /**  */
    private static final long serialVersionUID = 3664651649094973500L;
    /**
    * �Ҳ����쳣��ͨ�����ָ����쳣ʱͨ����Ҫ�����Ŀ����Դ�����ڡ������������Ŀ����Դ�Ѿ����壬Ҳ������δ���塣
     * @param string �쳣��������Ϣ
     */
    public NotFoundException(String string) {
        super(string);
    }
    /**
    * �Ҳ����쳣��ͨ�����ָ����쳣ʱͨ����Ҫ�����Ŀ����Դ�����ڡ������������Ŀ����Դ�Ѿ����壬Ҳ������δ���塣
     * @param error �쳣��������Ϣ
     */
    public NotFoundException(Throwable error) {
        super(error);
    }
    /**
    * �Ҳ����쳣��ͨ�����ָ����쳣ʱͨ����Ҫ�����Ŀ����Դ�����ڡ������������Ŀ����Դ�Ѿ����壬Ҳ������δ���塣
     * @param string �쳣��������Ϣ��
     * @param error �нӵ���һ���쳣����
     */
    public NotFoundException(String string, Throwable error) {
        super(string, error);
    }
}