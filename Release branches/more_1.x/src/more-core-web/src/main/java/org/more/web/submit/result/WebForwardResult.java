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
package org.more.web.submit.result;
import org.more.services.submit.impl.DefaultResultImpl;
/**
 * ת��
 * @version : 2011-7-25
 * @author ������ (zyc@byshell.org)
 */
public class WebForwardResult extends DefaultResultImpl<Object> {
    private ForwardEnum enumType = null;
    private String      toURL    = null;
    public enum ForwardEnum {
        /**�����ת��*/
        Forward,
        /**�ͻ����ض���*/
        Redirect,
        /**ת������ҳ��*/
        F_Home,
        /**�ض�����ҳ��*/
        R_Home,
        /**֪ͨ�ͻ�������ˢ�µ�ǰ����*/
        Refresh,
    }
    /**Ĭ���ض�����ҳ��*/
    public WebForwardResult() {
        this(ForwardEnum.R_Home, null, null);
    }
    public WebForwardResult(ForwardEnum enumType) {
        this(enumType, null, null);
    }
    public WebForwardResult(ForwardEnum enumType, String toURL) {
        this(enumType, toURL, null);
    }
    public WebForwardResult(ForwardEnum enumType, String toURL, Object returnValue) {
        super("webForward", returnValue);
        this.enumType = enumType;
        this.toURL = toURL;
    }
    public ForwardEnum getEnumType() {
        return this.enumType;
    }
    public String getToURL() {
        return this.toURL;
    }
}