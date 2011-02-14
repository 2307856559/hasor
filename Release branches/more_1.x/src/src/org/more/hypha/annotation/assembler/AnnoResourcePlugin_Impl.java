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
package org.more.hypha.annotation.assembler;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.more.core.asm.ClassReader;
import org.more.core.asm.ClassWriter;
import org.more.hypha.DefineResource;
import org.more.hypha.annotation.AnnoResourcePlugin;
import org.more.hypha.annotation.KeepWatchParser;
import org.more.hypha.aop.AopResourceExpand;
/**
 * ע�����ӿ�{@link AnnoResourcePlugin_Impl}��ʵ���ࡣ
 * @version 2010-10-14
 * @author ������ (zyc@byshell.org)
 */
public class AnnoResourcePlugin_Impl implements AnnoResourcePlugin {
    private DefineResource                         config      = null;
    private ArrayList<String>                      parserTypes = new ArrayList<String>();
    private HashMap<String, List<KeepWatchParser>> parserMap   = new HashMap<String, List<KeepWatchParser>>();
    //
    public AnnoResourcePlugin_Impl(DefineResource config) {
        this.config = config;
    }
    public DefineResource getTarget() {
        return this.config;
    }
    public AopResourceExpand getAopDefineResourcePlugin() {
        return (AopResourceExpand) this.config.getPlugin(AopResourceExpand.AopDefineResourcePluginName);
    }
    /**
     * ע��һ��ע����������÷�����֪ͨע�����ϵͳ��������ĳ���ض���ע��ʱ����ཻ�����ע�⴦�������д���
     * @param anno ע���ע�����͡�
     * @param watch ��������
     */
    public void registerAnnoKeepWatch(Class<? extends Annotation> anno, KeepWatchParser watch) {
        String annoType = anno.getName();
        List<KeepWatchParser> parserList = null;
        if (this.parserTypes.contains(annoType) == false) {
            parserList = new ArrayList<KeepWatchParser>();
            this.parserMap.put(annoType, parserList);
            this.parserTypes.add(annoType);
        } else
            parserList = this.parserMap.get(annoType);
        parserList.add(watch);
    }
    public boolean containsKeepWatchParser(String annoType) {
        return this.parserTypes.contains(annoType);
    }
    /**����Anno���Ͷ���ȷ����ȡע�����������ϵĽ�������*/
    public Collection<KeepWatchParser> getAnnoKeepWatch(String annoType) {
        return this.parserMap.get(annoType);
    }
    /**֪ͨaop��������������࣬className������ʾ����Ԥ������������������ͨ��{@link DefineResource}�е�ClassLoaderװ�صġ�*/
    public synchronized void parserClass(String className) throws ClassNotFoundException, IOException {
        String classPath = className.replace(".", "/") + ".class";
        InputStream is = ClassLoader.getSystemResourceAsStream(classPath);
        this.parserClass(is);
    }
    /**֪ͨaop��������������࣬classInputStream������ʾ����Ԥ����������������*/
    public synchronized void parserClass(InputStream classInputStream) throws ClassNotFoundException, IOException {
        //2.֪ͨ����ɨ������࣬ȷ���Ƿ��б�Ҫ������ʹ��ASM����ɨ�������ٶȡ�
        ClassReader reader = new ClassReader(classInputStream);
        EV_Class ev = new EV_Class(this, new ClassWriter(ClassWriter.COMPUTE_MAXS));
        reader.accept(ev, ClassReader.SKIP_DEBUG);
        //3.֪ͨ����ִ�н�����������а����߱�����������ע�⡣
        if (ev.isMark() == true)
            this.parserClass(ev.getClassName(), ev.getAnnos());
    }
    /**��ʽ�����ࡣ*/
    private void parserClass(String className, List<String> annoTypes) throws ClassNotFoundException {
        //1.����ע��˳������
        Collections.sort(annoTypes, new Comparator<String>() {
            public int compare(String o1, String o2) {
                int a = AnnoResourcePlugin_Impl.this.parserTypes.indexOf(o1);
                int b = AnnoResourcePlugin_Impl.this.parserTypes.indexOf(o2);
                if (a > b)
                    return 1;
                else if (a == b)
                    return 0;
                return -1;
            }
        });
        //2.����
        for (String anno : annoTypes) {
            for (KeepWatchParser p : this.parserMap.get(anno))
                p.process(ClassLoader.getSystemClassLoader().loadClass(className), this.config, this);
        }
    }
}