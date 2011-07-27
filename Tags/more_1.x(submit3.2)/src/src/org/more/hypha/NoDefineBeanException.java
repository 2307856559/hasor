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
import org.more.core.error.DefineException;
/**
 * �����ڵ�Bean���塣
 * @version 2009-4-29
 * @author ������ (zyc@byshell.org)
 */
public class NoDefineBeanException extends DefineException {
    private static final long serialVersionUID = 3254479748206568531L;
    /** �����ڵ�Bean���塣*/
    public NoDefineBeanException(String string) {
        super(string);
    };
    /** �����ڵ�Bean���塣*/
    public NoDefineBeanException(Throwable error) {
        super(error);
    };
    /** �����ڵ�Bean���塣*/
    public NoDefineBeanException(String string, Throwable error) {
        super(string, error);
    };
}