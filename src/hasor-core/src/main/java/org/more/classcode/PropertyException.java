/*
 * Copyright 2008-2009 the original 赵永春(zyc@hasor.net).
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
package org.more.classcode;
/**
 * 属性异常。
 * @version 2009-6-26
 * @author 赵永春 (zyc@hasor.net)
 */
public class PropertyException extends ClassCodeRuntimeException {
    private static final long serialVersionUID = -7774988512856603877L;
    /**属性异常*/
    public PropertyException(final String string) {
        super(string);
    }
    /**属性异常*/
    public PropertyException(final Throwable error) {
        super(error);
    }
    /**属性异常*/
    public PropertyException(final String string, final Throwable error) {
        super(string, error);
    }
}