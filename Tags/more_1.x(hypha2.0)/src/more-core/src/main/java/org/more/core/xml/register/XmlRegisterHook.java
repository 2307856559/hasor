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
package org.more.core.xml.register;
import org.more.core.xml.XmlParserKit;
/**
 * 
 * @version : 2011-12-5
 * @author ������ (zyc@byshell.org)
 */
public interface XmlRegisterHook {
    /**����{@link XmlParserKit}xpathע������������Ҫ�����������ռ䡣*/
    public XmlParserKit createXmlParserKit(final String namespace, final XmlRegister useRegister);
};