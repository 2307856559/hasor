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
import org.more.core.error.FoundException;
import org.more.core.xml.XmlElementHook;
import org.more.core.xml.XmlStackDecorator;
import org.more.core.xml.stream.EndElementEvent;
import org.more.core.xml.stream.StartElementEvent;
import org.more.hypha.context.xml.XmlDefineResource;
import org.more.remote.Publisher;
import org.more.remote.RemoteService;
import org.more.remote.RmiBeanCreater;
import org.more.remote.assembler.creater.XmlRmiBeanCreater;
/**
 * ���ڽ���remote:remote��ǩ��
 * @version : 2011-8-15
 * @author ������ (zyc@byshell.org)
 */
public class TagSubmit_Remote extends TagRemote_NS implements XmlElementHook {
    /**����{@link TagSubmit_Remote}���� 
    * @param remotePropxy */
    public TagSubmit_Remote(XmlDefineResource configuration, RemoteService service) {
        super(configuration, service);
    }
    /**��ʼ��ǩ����expression���ԡ�*/
    public void beginElement(XmlStackDecorator context, String xpath, StartElementEvent event) {
        //1.ȡֵ
        String name = event.getAttributeValue("name");
        String faces = event.getAttributeValue("faces");
        String refBean = event.getAttributeValue("refBean");
        String forPublisher = event.getAttributeValue("forPublisher");
        //2.Ĭ��ֵ
        String _name = (name == null) ? "" : name;
        String _faces = (faces.equals("") == true) ? "java.rmi.Remote" : faces;
        String _refBean = (refBean.equals("") == true) ? null : refBean;
        String _forPublisher = (forPublisher == null) ? "" : forPublisher;
        //3.����Remote
        Publisher publisher = this.getService().getPublisher(_forPublisher);
        if (publisher == null)
            throw new FoundException("�޷��ҵ���" + _forPublisher + "�������ߡ�");
        RmiBeanCreater rmiBean = new XmlRmiBeanCreater(_faces, _refBean, this.getService());//���������ļ�����
        publisher.pushRemote(_name, rmiBean);
    }
    public void endElement(XmlStackDecorator context, String xpath, EndElementEvent event) {}
}