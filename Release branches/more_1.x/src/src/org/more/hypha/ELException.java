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
import org.more.core.error.MoreActionException;
/**
 * hypha��el�쳣��
 * @version : 2011-5-18
 * @author ������ (zyc@byshell.org)
 */
public class ELException extends MoreActionException {
    private static final long serialVersionUID = 4226135960558522394L;
    /** hypha��el�쳣��*/
    public ELException(String string) {
        super(string);
    };
    /** hypha��el�쳣��*/
    public ELException(String string, Throwable error) {
        super(string, error);
    };
}