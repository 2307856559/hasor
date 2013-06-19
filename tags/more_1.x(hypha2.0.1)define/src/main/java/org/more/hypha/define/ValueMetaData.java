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
package org.more.hypha.define;
import java.util.HashMap;
import java.util.Map;
/**
 * ��ʾһ������ֵ�ĳ�����
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public abstract class ValueMetaData extends AbstractDefine {
    /*����ֵ������֮�����õ��������ԣ���Щ��չ�������Ե�Ŀ���ǵ���������ʱ���ʹ�á�������Я����Ϣ��*/
    private Map<String, String> extParams = new HashMap<String, String>();
    /*------------------------------------------------------------------*/
    /**��ȡֵ����*/
    public abstract String getType();
    /**��ȡ���õ��������ԣ���Щ��չ�������Ե�Ŀ���ǵ���������ʱ���ʹ�á�������Я����Ϣ��*/
    public Map<String, String> getExtParams() {
        return extParams;
    }
    /**�������õ��������ԣ���Щ��չ�������Ե�Ŀ���ǵ���������ʱ���ʹ�á�������Я����Ϣ��*/
    public void setExtParams(Map<String, String> extParams) {
        this.extParams = extParams;
    }
}