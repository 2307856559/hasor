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
package org.more.core.classcode;
import java.util.LinkedList;
/**
 * {@link ClassEngine}�������װ�����������ְ���Ǹ���װ�������������ɵ�Class�ֽ��롣<br/>
 * ����֮�⵱������Ҫװ��ĳЩ����������ʱҲ��Ҫͨ������װ������װ�ء�
 * @version 2010-9-5
 * @author ������ (zyc@byshell.org)
 */
public class RootClassLoader extends ClassLoader {
    private LinkedList<ClassEngine> classEngineList = null;
    /**����������װ������parentClassLoader�����Ǹ�װ������ʹ�õĸ���װ������*/
    public RootClassLoader(ClassLoader parentClassLoader) {
        super(parentClassLoader);
        this.classEngineList = new LinkedList<ClassEngine>();
    }
    /**����װ�����ࡣ*/
    @Override
    protected final Class<?> findClass(String name) throws ClassNotFoundException {
        for (ClassEngine engine : this.classEngineList)
            if (engine.getClassName().equals(name) == true) {
                byte[] bs = engine.toBytes();
                return this.defineClass(name, bs, 0, bs.length);
            }
        return super.findClass(name);
    }
    /**��ȡĳһ���Ѿ����͵��ֽ��룬���ֽ��������ͨ��{@link ClassEngine}�������ɵġ�*/
    public byte[] toBytes(Class<?> type) {
        for (ClassEngine engine : this.classEngineList) {
            Class<?> findType = engine.toClass();
            if (findType == type)
                return engine.toBytes();
        }
        return null;
    }
    /**ע��һ��{@link ClassEngine}���浽��װ�����С�*/
    public void regeditEngine(ClassEngine classEngine) {
        if (this.classEngineList.contains(classEngine) == false)
            this.classEngineList.add(classEngine);
    }
    /**���һ��{@link ClassEngine}�����ע�ᣬ�Ӵ�ע��֮�����װ�����������ٴλ�ȡ�����ֽ��롣*/
    public void unRegeditEngine(ClassEngine classEngine) {
        if (this.classEngineList.contains(classEngine) == true)
            this.classEngineList.remove(classEngine);
    }
}