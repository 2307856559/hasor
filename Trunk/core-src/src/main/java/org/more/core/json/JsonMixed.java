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
/**
 *json�����������ݣ����������������ͺͶ������͡�
 * @version 2010-1-7
 * @author ������ (zyc@byshell.org)
 */
public abstract class JsonMixed extends JsonType {
    protected JsonMixed(JsonUtil currentContext) {
        super(currentContext);
    };
    /**��ȡ��һ��������Ŀ*/
    protected String readJSONString(String str) {
        StringBuffer returnS = new StringBuffer();
        int depth = 0;
        //��ȡ�����һ������ֵ
        for (int i = 0; i < str.length(); i++) {
            char s_temp = str.charAt(i);
            if (s_temp == ',' && depth == 0)
                return returnS.toString();
            else if (s_temp == '[' || s_temp == '{')
                depth++;
            else if (s_temp == ']' || s_temp == '}')
                depth--;
            returnS.append(s_temp);
        }
        return returnS.toString();
    }
}