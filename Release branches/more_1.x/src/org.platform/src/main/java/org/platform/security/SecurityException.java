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
package org.platform.security;
import javax.servlet.ServletException;
/**
 * Ȩ��ϵͳ�쳣��
 * @version : 2013-4-17
 * @author ������ (zyc@byshell.org)
 */
public class SecurityException extends ServletException {
    private static final long serialVersionUID = 2366386850634621969L;
    //
    public SecurityException() {
        super();
    }
    public SecurityException(String msg) {
        super(msg);
    }
    public SecurityException(String msg, Throwable e) {
        super(msg, e);
    }
}