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
package org.more.hypha;
import org.more.RepeateException;
/**
 * �����ظ����壬���ָ��쳣ͨ���Ƕ��Դ��ڵĶ�������Խ����˵ڶ������¶��塣
 * @version 2009-4-29
 * @author ������ (zyc@byshell.org)
 */
public class RepeateBeanException extends RepeateException {
    /**  */
    private static final long serialVersionUID = 2377606123252842745L;
    /**
     * �����ظ����壬���ָ��쳣ͨ���Ƕ��Դ��ڵĶ�������Խ����˵ڶ������¶��塣
     * @param string �쳣��������Ϣ
     */
    public RepeateBeanException(String string) {
        super(string);
    }
    /**
     * �����ظ����壬���ָ��쳣ͨ���Ƕ��Դ��ڵĶ�������Խ����˵ڶ������¶��塣
     * @param error �쳣��������Ϣ
     */
    public RepeateBeanException(Throwable error) {
        super(error);
    }
    /**
     * �����ظ����壬���ָ��쳣ͨ���Ƕ��Դ��ڵĶ�������Խ����˵ڶ������¶��塣
     * @param string �쳣��������Ϣ��
     * @param error �нӵ���һ���쳣����
     */
    public RepeateBeanException(String string, Throwable error) {
        super(string, error);
    }
}