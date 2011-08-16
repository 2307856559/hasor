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
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import org.more.core.classcode.BuilderMode;
import org.more.core.classcode.ClassEngine;
import org.more.core.error.FormatException;
import org.more.remote.RmiBeanCreater;
/**
 * ������{@link RmiBeanCreater}�ӿڵĻ�ʵ�֣���Ҫ�����ǽ�һ���������ͽӿڰ󶨣��������ǵ�һ��RMI�������
 * @version : 2011-8-16
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractRmiBeanCreater implements RmiBeanCreater {
    /*
     * RMI��ʵ�������̳���UnicastRemoteObject�����޷���֤ʵ����Ҳ�������ڸ�������ࡣ
     * ��˴���ԭ��ֻ��������һ���̳���UnicastRemoteObject����ʵ�������ɽӿڵĴ����ࡣ
     * �������������ȥ����Ŀ���������Խӹ��̲��ܱ�֤����������ǿ��ת���ɴ����������͡����ش�˵����
     */
    /**���ݶ����ҪԤ��ʵ�ֵĽӿڴ���һ���ӿڴ�����󣬸ö����ܱ�ǿ��ת��Ϊobj����ʾ�����ͣ�������ת����Ϊfaces�е�����һ�����͡�*/
    protected Remote getRemoteByFaces(Object obj, Class<?>[] faces) throws ClassNotFoundException, RemoteException, IOException {
        ClassEngine ce = new ClassEngine();
        RmiFaceDelegate rfd = new RmiFaceDelegate();
        ce.setSuperClass(RmiObjectPropxy.class);
        ce.setBuilderMode(BuilderMode.Propxy);
        //��һ���Ҫʵ�ֵĽӿڡ�
        if (faces != null)
            for (Class<?> face : faces)
                if (face.isInterface() == false)
                    throw new FormatException(face + "���ӿڲ���һ���ӿ����͡�");
                else
                    ce.addDelegate(face, rfd);
        RmiObjectPropxy propxy = new RmiObjectPropxy();
        propxy.setTarget(obj);
        return (Remote) ce.newInstance(propxy);
    };
    /**����faces����facesת��Ϊ������¶��RemoteԶ�̽ӿڡ�*/
    protected Class<?>[] getRemoteFaces(Class<?>[] faces) throws Throwable {
        return faces;/*�ӿڵ��ӿڵ�ת��*/
    };
};