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
package org.more.core.json;
import org.more.FormatException;
/**
 * ����json���ݴ���ͨ���ǵ�����json���ݻ��߽��������л�Ϊjson���������ĸ����쳣��
 * @version 2010-1-7
 * @author ������ (zyc@byshell.org)
 */
public class JsonException extends FormatException {
    /**  */
    private static final long serialVersionUID = 6079652246835019946L;
    /**
     * ����json���ݴ���ͨ���ǵ�����json���ݻ��߽��������л�Ϊjson���������ĸ����쳣��
     * @param string �쳣��������Ϣ
     */
    public JsonException(String string) {
        super(string);
    }
    /**
     * ����json���ݴ���ͨ���ǵ�����json���ݻ��߽��������л�Ϊjson���������ĸ����쳣��
     * @param error �쳣��������Ϣ
     */
    public JsonException(Throwable error) {
        super(error);
    }
    /**
     * ����json���ݴ���ͨ���ǵ�����json���ݻ��߽��������л�Ϊjson���������ĸ����쳣��
     * @param string �쳣��������Ϣ��
     * @param error �нӵ���һ���쳣����
     */
    public JsonException(String string, Throwable error) {
        super(string, error);
    }
}