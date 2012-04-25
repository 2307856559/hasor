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
package org.more.core.json.parser;
import org.more.core.json.JsonException;
import org.more.core.json.JsonParser;
import org.more.core.json.JsonUtil;
import org.more.util.StringConvertUtil;
/**
 * ������Number���͵�json��ʽ��ת��
 * @version 2010-1-7
 * @author ������ (zyc@byshell.org)
 */
public class JsonNumber extends JsonParser {
    public JsonNumber(JsonUtil currentContext) {
        super(currentContext);
    };
    public Object toObject(String str) {
        return StringConvertUtil.parseNumber(str, 0);
    }
    public String toString(Object bean) {
        if (bean instanceof Number == true)
            return bean.toString();
        else
            throw new JsonException("JsonNumber���ܽ�һ�����������Ͷ���ת��ΪJSON��Ӧ��ʽ��");
    }
}