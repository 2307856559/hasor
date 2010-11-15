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
package org.more.hypha.annotation;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.Collection;
import org.more.hypha.DefineResource;
import org.more.hypha.DefineResourcePlugin;
/**
 * ��ǿ��{@link DefineResource}�ӿڣ����ṩ�˸�Ϊ�ḻ��anno��ط�����
 * @version 2010-10-8
 * @author ������ (zyc@byshell.org)
 */
public interface AnnotationDefineResourcePlugin extends DefineResourcePlugin {
    /**Ҫע��Ĳ����*/
    public static final String AnnoDefineResourcePluginName = "$more_anno_ResourcePlugin";
    /**
     * ע��ע��һ��ע����������÷�����֪ͨע�����ϵͳ��������ĳ���ض���ע��ʱ����ཻ�����ע�⴦�������д���
     * @param anno ע���ע�����͡�
     * @param watch ��������
     */
    public void registerAnnoKeepWatch(Class<? extends Annotation> anno, KeepWatchParser watch);
    /**����Ƿ�ע���˲�������ʾ��ע������������ע���˷���true���򷵻�false*/
    public boolean containsKeepWatchParser(String annoType);
    /**����Anno���Ͷ���ȷ����ȡע�����������ϵĽ�������*/
    public Collection<KeepWatchParser> getAnnoKeepWatch(String annoType);
    /**֪ͨaop��������������࣬className������ʾ����Ԥ������������������ͨ��{@link DefineResource}�е�ClassLoaderװ�صġ�*/
    public void parserClass(String className) throws ClassNotFoundException, IOException;
    /**֪ͨaop��������������࣬classInputStream������ʾ����Ԥ����������������*/
    public void parserClass(InputStream classInputStream) throws ClassNotFoundException, IOException;
}