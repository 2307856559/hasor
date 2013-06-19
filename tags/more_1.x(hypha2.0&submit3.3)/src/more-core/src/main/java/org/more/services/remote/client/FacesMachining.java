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
package org.more.services.remote.client;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.more.core.asm.ClassAdapter;
import org.more.core.asm.ClassReader;
import org.more.core.asm.ClassVisitor;
import org.more.core.asm.ClassWriter;
import org.more.core.asm.MethodVisitor;
import org.more.core.asm.Opcodes;
import org.more.core.classcode.EngineToos;
import org.more.core.error.FormatException;
/**
 * ���ฺ������һ���ӿڵ�RMI��ʽ�ӿڡ�
 * @version : 2011-8-19
 * @author ������ (zyc@byshell.org)
 */
public class FacesMachining extends ClassLoader implements ShortName {
    private Map<String, byte[]> resBytes = new HashMap<String, byte[]>();
    public FacesMachining() {
        super();
    }
    public FacesMachining(ClassLoader parentLoader) {
        super(parentLoader);
    }
    public Class<?> createFaces(Class<?> classType) throws IOException {
        if (classType.isInterface() == false)
            throw new FormatException("���͸�ʽ����������һ����Ч�Ľӿ����͡�");
        String className = classType.getName();
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(className.replace('.', '/') + ".class");
        ClassReader reader = new ClassReader(is);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        reader.accept(new FacesClassAdapter(classType, writer), ClassReader.SKIP_DEBUG);
        byte[] arrayByte = writer.toByteArray();
        this.resBytes.put(className.replace(".", "/") + ShortName + ".class", arrayByte);
        //װ������
        return this.defineClass(className + ShortName, arrayByte, 0, arrayByte.length);
    }
    public InputStream getResourceAsStream(String name) {
        if (this.resBytes.containsKey(name) == true)
            return new ByteArrayInputStream(this.resBytes.get(name));
        return super.getResourceAsStream(name);
    }
}
class FacesClassAdapter extends ClassAdapter implements Opcodes, ShortName {
    private Map<String, Method> methodMap = new HashMap<String, Method>();
    //
    public FacesClassAdapter(Class<?> classType, ClassVisitor cv) {
        super(cv);
        for (Method m : classType.getMethods()) {
            String mInfo = EngineToos.methodToAsmMethod(m);
            this.methodMap.put(mInfo, m);
        }
    }
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        //�µĽӿڼ̳�ԭ�нӿ�ͬʱ��ʵ��Remote�ӿڡ�
        super.visit(version, access, name + ShortName, signature, superName, new String[] { name, "java/rmi/Remote" });
    }
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        //1.��¼method
        String fullName = name + desc;
        if (this.methodMap.containsKey(fullName) == true)
            this.methodMap.remove(fullName);
        //2.���ϱ�Ҫ��RemoteException�����쳣
        ArrayList<String> al = new ArrayList<String>();
        if (exceptions != null)
            Collections.addAll(al, exceptions);
        if (al.contains("java/rmi/RemoteException") == false)
            al.add("java/rmi/RemoteException");
        String[] strs = new String[al.size()];
        al.toArray(strs);
        return super.visitMethod(access, name, desc, signature, strs);
    }
    public void visitEnd() {
        for (String key : this.methodMap.keySet()) {
            int mark = ACC_PUBLIC + ACC_ABSTRACT;
            Method m = this.methodMap.get(key);
            String desc = "(" + EngineToos.toAsmType(m.getParameterTypes()) + ")" + EngineToos.toAsmType(m.getReturnType());
            //�쳣
            Class<?>[] eTypes = m.getExceptionTypes();
            ArrayList<String> al = new ArrayList<String>();
            for (Class<?> eType : eTypes)
                al.add(EngineToos.toAsmType(eType));
            if (al.contains("java/rmi/RemoteException") == false)
                al.add("java/rmi/RemoteException");
            String[] strs = new String[al.size()];
            al.toArray(strs);
            MethodVisitor mv = super.visitMethod(mark, m.getName(), desc, null, strs);
            mv.visitCode();
            mv.visitEnd();
        }
        super.visitEnd();
    }
}