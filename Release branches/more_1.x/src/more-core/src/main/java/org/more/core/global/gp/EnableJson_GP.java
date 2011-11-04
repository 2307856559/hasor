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
package org.more.core.global.gp;
import org.more.core.error.SupportException;
import org.more.core.global.Global;
import org.more.core.global.GlobalProperty;
import org.more.util.StringConvertUtil;
/**
 * _global.enableJson
 * @version : 2011-9-30
 * @author ������ (zyc@byshell.org)
 */
public class EnableJson_GP implements GlobalProperty {
    public Object getValue(Global global) {
        String oriString = global.getOriginalString("_global.enableJson");
        return StringConvertUtil.parseBoolean(oriString, true);
    }
    public void setValue(Object value, Global global) {
        throw new SupportException("_global.enableJson�����Բ�֧��д������");
    }
}