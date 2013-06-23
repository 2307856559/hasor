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
package org.more.hypha.context.xml;
import java.io.IOException;
import org.more.core.error.LoadException;
import org.more.core.xml.XmlParserKit;
import org.more.core.xml.register.XmlRegisterHook;
/**
 * Ϊ��{@link XmlDefineResource}���ṩ��һ��ע�����ӿڣ����Ҫע���µ�xml����֧������Ҫʵ������ӿڲ���
 * ����һ���޲εĹ��췽����ͬʱ�ڡ�/META-INF/resource/beans/regedit.xml��λ�ñ�д�����ļ���
 * �����ļ���ʽ�ο�more����ĵ���
 * @version 2010-9-24
 * @author ������ (zyc@byshell.org)
 */
public interface XmlNameSpaceRegister extends XmlRegisterHook {
    /**
     * ִ��ע��
     * @param resource {@link XmlParserKit}����
     */
    public void initRegister(XmlParserKit parserKit, XmlDefineResource resource) throws LoadException, IOException;
}