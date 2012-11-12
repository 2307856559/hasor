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
import org.more.core.log.Log;
import org.more.core.log.LogFactory;
/**
 * ���ж���Ļ��ࡣ
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractDefine<T> {
    private static final Log        log       = LogFactory.getLog(AbstractDefine.class);
    private HashMap<String, Object> attribute = null;                                   //���ԣ�Ϊ���ṩIAttribute�ӿڹ��ܡ�
    /*------------------------------------------------------------------------------*/
    /**��ȡ��������Զ��󣬲�����{@link Map}�ӿ���ʽ���ء�*/
    protected Map<String, Object> getAttribute() {
        if (this.attribute == null) {
            this.attribute = new HashMap<String, Object>();
            log.debug("create attribute OK!");
        }
        return this.attribute;
    }
}