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
package org.more.remote.assembler;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.more.core.error.FormatException;
import org.more.core.log.Log;
import org.more.core.log.LogFactory;
import org.more.remote.Publisher;
import org.more.remote.RemoteService;
import org.more.remote.RmiBeanCreater;
import org.more.remote.RmiBeanDirectory;
import org.more.remote.assembler.publisher.RmiClientSocketFactory;
import org.more.remote.assembler.publisher.RmiServerSocketFactory;
/**
 * Ĭ�ϵ�{@link Publisher}�ӿ�ʵ���ࡣ
 * @version : 2011-8-15
 * @author ������ (zyc@byshell.org)
 */
public class DefaultPublisher implements Publisher {
    private static Log                  log              = LogFactory.getLog(DefaultPublisher.class);
    private URI                         publisherAddress = null;
    //
    private Map<String, Remote>         remoteMap        = new HashMap<String, Remote>();
    private Map<String, RmiBeanCreater> remoteCreaterMap = new HashMap<String, RmiBeanCreater>();
    private List<RmiBeanDirectory>      directoryList    = new ArrayList<RmiBeanDirectory>();
    //
    private Registry                    registry         = null;
    //
    public DefaultPublisher(String bindAddress, int bindPort, String pathRoot) {
        String str = "rmi://" + bindAddress + ":" + bindPort + "/";
        if (pathRoot.equals("/") == false)
            str += pathRoot;
        try {
            this.publisherAddress = new URI(str);
        } catch (URISyntaxException e) {
            log.error("can`t create RMI Services URL, please check bindAddress,bindPort and pathRoot. URL is ��{%0}��", str);
            throw new FormatException("�޷�����RMI�����ַ�������ַ�ַ�������");
        }
    };
    public URI getPublisherAddress() {
        return this.publisherAddress;
    };
    public void pushRemote(String name, Remote rmiBean) {
        this.remoteMap.put(name, rmiBean);
    };
    public void pushRemote(String name, RmiBeanCreater rmiBeanCreater) {
        this.remoteCreaterMap.put(name, rmiBeanCreater);
    };
    public void pushRemoteList(RmiBeanDirectory rmiDirectory) {
        this.directoryList.add(rmiDirectory);
    };
    /*----------------------------------------------*/
    protected void bindRemote(String name, Remote remoteObject) throws AccessException, RemoteException, AlreadyBoundException {
        if (remoteObject == null)
            return;
        String rmiName = this.publisherAddress.getPath() + name;
        rmiName = rmiName.substring(1);
        log.info("Push RMI Service ��{0}��", rmiName);
        this.registry.bind(rmiName, remoteObject);
    };
    public void start(RemoteService remoteService) throws Throwable {
        //1.����registry
        String bindAddress = this.publisherAddress.getHost();
        int bindPort = this.publisherAddress.getPort();
        RMIServerSocketFactory s = new RmiServerSocketFactory(bindAddress, remoteService);
        RMIClientSocketFactory c = new RmiClientSocketFactory(remoteService);
        this.registry = LocateRegistry.createRegistry(bindPort, c, s);
        //2.��������
        //UnicastRemoteObject.exportObject(obj, port, csf, ssf);//������ͬ�����ӿ���ʵ�� IPV4  IPV6 ˫Э��ջ
        for (String key : this.remoteMap.keySet())
            this.bindRemote(key, this.remoteMap.get(key));
        for (String key : this.remoteCreaterMap.keySet())
            this.bindRemote(key, this.remoteCreaterMap.get(key).create());
        for (RmiBeanDirectory item : this.directoryList) {
            Map<String, RmiBeanCreater> map = item.getCreaterMap();
            if (map != null)
                for (String key : map.keySet())
                    this.bindRemote(key, map.get(key).create());
        }
    };
    public void stop(RemoteService remoteService) throws Throwable {
        for (String name : this.registry.list())
            this.registry.unbind(name);
    };
}