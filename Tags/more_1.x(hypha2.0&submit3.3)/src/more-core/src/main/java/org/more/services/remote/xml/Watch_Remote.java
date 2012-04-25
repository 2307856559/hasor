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
package org.more.services.remote.xml;
import java.lang.annotation.Annotation;
import org.more.hypha.anno.KeepWatchParser;
import org.more.hypha.context.xml.XmlDefineResource;
import org.more.services.remote.Publisher;
import org.more.services.remote.Remote;
import org.more.services.remote.RemoteService;
import org.more.services.remote.RmiBeanCreater;
import org.more.services.remote.assembler.creater.AnnoRmiBeanCreater;
/**
 * �����Ŀ�������ڽ���{@link Remote}ע�⡣
 * @version 2010-10-14
 * @author ������ (zyc@byshell.org)
 */
class Watch_Remote implements KeepWatchParser {
    private RemoteService service = null;
    public Watch_Remote(RemoteService service) {
        this.service = service;
    }
    public void process(Object target, Annotation annoData, XmlDefineResource resource) {
        Class<?> classType = (Class<?>) target;
        Remote remoteData = (Remote) annoData;
        //1.ȡ�ò���
        String nameStr = remoteData.name();
        Class<?>[] faces = remoteData.faces();
        String publisherStr = remoteData.forPublisher();
        //2.Ĭ��ֵ
        String _name = (nameStr.equals("") == true) ? classType.getSimpleName() : nameStr;
        Class<?>[] _faces = (faces == null || faces.length == 0) ? classType.getInterfaces() : faces;
        String _publisher = (publisherStr == null) ? "" : publisherStr;
        //3.�Ǽ�ע�� 
        Publisher publisher = this.service.getPublisher(_publisher);
        RmiBeanCreater annoBeanPropxy = new AnnoRmiBeanCreater(_faces, classType, this.service);//����ע�����
        publisher.pushRemote(_name, annoBeanPropxy);
    };
};