/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
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
package org.more.classcode;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
/**
 * {@link ClassEngine}�������װ�����������ְ���Ǹ���װ�������������ɵ�Class�ֽ��롣<br/>
 * ����֮�⵱������Ҫװ��ĳЩ����������ʱҲ��Ҫͨ������װ������װ�ء�
 * @version 2010-9-5
 * @author ������ (zyc@hasor.net)
 */
public class RootClassLoader extends ClassLoader {
    private Map<String, ClassEngine> classMap2 = null; // key is org.more.Test
    private Map<String, ClassEngine> classMap  = null; // key is org/more.Test.class
    /**����������װ������parentClassLoader�����Ǹ�װ������ʹ�õĸ���װ������*/
    public RootClassLoader(ClassLoader parentClassLoader) {
        super(parentClassLoader);
        this.classMap = new HashMap<String, ClassEngine>();
        this.classMap2 = new HashMap<String, ClassEngine>();
    }
    /**����װ�����ࡣ*/
    protected final Class<?> findClass(String name) throws ClassNotFoundException {
        if (this.classMap.containsKey(name) == true) {
            byte[] bs = this.classMap.get(name).toBytes();
            return this.defineClass(name, bs, 0, bs.length);
        }
        return super.findClass(name);
    }
    /**��ȡĳһ���Ѿ����͵��ֽ��룬���ֽ��������ͨ��{@link ClassEngine}�������ɵġ�*/
    public byte[] toBytes(Class<?> type) {
        ClassLoader cl = type.getClassLoader();
        if (cl instanceof RootClassLoader)
            return ((RootClassLoader) cl).getRegeditEngine(type.getName()).toBytes();
        return null;
    }
    /**ע��һ��{@link ClassEngine}���浽��װ�����С�*/
    public void regeditEngine(ClassEngine classEngine) {
        String cn = classEngine.getClassName();
        if (this.classMap.containsKey(cn) == false) {
            this.classMap.put(cn, classEngine);
            cn = cn.replace(".", "/") + ".class";
            this.classMap2.put(cn, classEngine);
        }
    }
    /**���һ��{@link ClassEngine}�����ע�ᣬ�Ӵ�ע��֮�����װ�����������ٴλ�ȡ�����ֽ��롣*/
    public void unRegeditEngine(ClassEngine classEngine) {
        String cn = classEngine.getClassName();
        if (this.classMap.containsKey(cn) == true) {
            this.classMap.remove(cn);
            cn = cn.replace(".", "/") + ".class";
            this.classMap2.put(cn, classEngine);
        }
    }
    /**��ȡһ����ע���{@link ClassEngine}���档*/
    public ClassEngine getRegeditEngine(String className) {
        if (this.classMap.containsKey(className) == true)
            return this.classMap.get(className);
        return null;
    }
    public InputStream getResourceAsStream(String name) {
        if (this.classMap.containsKey(name) == true) {
            ClassEngine ce = this.classMap.get(name);
            return new ByteArrayInputStream(ce.toBytes());
        }
        if (this.classMap2.containsKey(name) == true) {
            ClassEngine ce = this.classMap2.get(name);
            return new ByteArrayInputStream(ce.toBytes());
        }
        return super.getResourceAsStream(name);
    }
}