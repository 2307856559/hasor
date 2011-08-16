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
package org.more.remote.xml;
import org.more.core.xml.XmlElementHook;
import org.more.core.xml.XmlStackDecorator;
import org.more.core.xml.stream.EndElementEvent;
import org.more.core.xml.stream.StartElementEvent;
import org.more.hypha.context.xml.XmlDefineResource;
import org.more.remote.RemoteService;
import org.more.remote.assembler.DefaultPublisher;
import org.more.util.StringConvertUtil;
/**
 * ���ڽ���remote:remotePublisher��ǩ��
 * @version : 2011-8-15
 * @author ������ (zyc@byshell.org)
 */
public class TagSubmit_RemotePublisher extends TagRemote_NS implements XmlElementHook {
    /**����{@link TagSubmit_RemotePublisher}����*/
    public TagSubmit_RemotePublisher(XmlDefineResource configuration, RemoteService service) {
        super(configuration, service);
    }
    /**��ʼ��ǩ�������ԡ�*/
    public void beginElement(XmlStackDecorator context, String xpath, StartElementEvent event) {
        //1.ȡֵ
        String name = event.getAttributeValue("name");
        String pathRoot = event.getAttributeValue("path");
        String bindAddress = event.getAttributeValue("bindAddress");
        String bindPort = event.getAttributeValue("bindPort");
        //2.Ĭ��ֵ
        String _name = (name == null) ? "" : name;
        String _pathRoot = (pathRoot == null) ? "/" : pathRoot;
        String _bindAddress = (bindAddress == null) ? "localhost" : bindAddress;
        int _bindPort = StringConvertUtil.parseInt(bindPort, 1099);
        //3.����RemoteProxy
        DefaultPublisher publisher = new DefaultPublisher(_pathRoot, _bindAddress, _bindPort);
        //4.ע�����
        this.getService().addPublisher(_name, publisher);
    }
    public void endElement(XmlStackDecorator context, String xpath, EndElementEvent event) {}
}