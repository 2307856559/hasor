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
package org.more.util;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.WeakHashMap;
import org.more.asm.AnnotationVisitor;
import org.more.asm.ClassReader;
import org.more.asm.ClassVisitor;
import org.more.asm.Opcodes;
import org.more.util.ResourcesUtils.ScanEvent;
import org.more.util.ResourcesUtils.ScanItem;
/**
 * 
 * @version : 2013-8-13
 * @author ������ (zyc@hasor.net)
 */
public class ScanClassPath {
    private ClassLoader                  classLoader  = null;
    private String[]                     scanPackages = null;
    private Map<Class<?>, Set<Class<?>>> cacheMap     = new WeakHashMap<Class<?>, Set<Class<?>>>();
    //
    private ScanClassPath(String[] scanPackages) {
        this(scanPackages, null);
    };
    private ScanClassPath(String[] scanPackages, ClassLoader classLoader) {
        this.scanPackages = scanPackages;
        this.classLoader = classLoader == null ? Thread.currentThread().getContextClassLoader() : classLoader;
    };
    //
    public static ScanClassPath newInstance(String[] scanPackages) {
        return new ScanClassPath(scanPackages) {};
    }
    public static ScanClassPath newInstance(String scanPackages) {
        return new ScanClassPath(new String[] { scanPackages }) {};
    }
    /**
     * ɨ��jar���з���ƥ��compareType��������������ء�����ִ�н�������棩
     * @param packagePath Ҫɨ��İ�����
     * @param compareType Ҫ���ҵ�������
     * @return ����ɨ������
     */
    public static Set<Class<?>> getClassSet(String packagePath, Class<?> compareType) {
        return getClassSet(new String[] { packagePath }, compareType);
    }
    public static Set<Class<?>> getClassSet(String[] loadPackages, Class<?> featureType) {
        return newInstance(loadPackages).getClassSet(featureType);
    }
    /**
     * ɨ��jar���з���ƥ��compareType��������������ء�����ִ�н�������棩
     * @param packages Ҫɨ��İ�����
     * @param compareType Ҫ���ҵ�������
     * @return ����ɨ������
     */
    public Set<Class<?>> getClassSet(final Class<?> compareType) {
        //0.���Դӻ����л�ȡ
        Set<Class<?>> returnData = this.cacheMap.get(compareType);
        if (returnData != null)
            return Collections.unmodifiableSet(returnData);
        //1.׼������
        final String compareTypeStr = compareType.getName();//Ҫƥ�������
        final Set<String> classStrSet = new HashSet<String>();//����������Class
        //2.ɨ��
        for (String tiem : this.scanPackages) {
            if (StringUtils.isBlank(tiem))
                continue;
            try {
                ResourcesUtils.scan(tiem.replace(".", "/") + "*.class", new ScanItem() {
                    public void found(ScanEvent event, boolean isInJar) throws IOException {
                        String name = event.getName();
                        if (name.endsWith(".class") == false)
                            return;
                        //1.ȡ������
                        name = name.substring(0, name.length() - ".class".length());
                        name = name.replace("/", ".");
                        //2.װ����
                        InputStream inStream = event.getStream();
                        ClassInfo info = loadClassInfo(name, inStream, classLoader);
                        //3.����Ŀ�����Ƿ�ƥ��
                        for (String face : info.superLink)
                            if (face.equals(compareTypeStr))//����
                                classStrSet.add(name);
                        for (String face : info.interFacesLink)
                            if (face.equals(compareTypeStr))//�ӿ�
                                classStrSet.add(name);
                        for (String face : info.annos)
                            if (face.equals(compareTypeStr))//ע��
                                classStrSet.add(name);
                    }
                });
            } catch (Exception e) {}
        }
        //3.����
        returnData = new HashSet<Class<?>>();
        for (String atClass : classStrSet) {
            try {
                Class<?> clazz = Class.forName(atClass, false, this.classLoader);
                returnData.add(clazz);
            } catch (Exception e) {}
        }
        this.cacheMap.put(compareType, returnData);
        return returnData;
    }
    //
    private Map<String, ClassInfo> classInfoMap = new HashMap<String, ClassInfo>();
    /**��������ֽ��룬���������л�ݹ���������ʵ�ֵĽӿ�*/
    private ClassInfo loadClassInfo(String className, InputStream inStream, final ClassLoader loader) throws IOException {
        /*һ��������Ƿ��Ѿ������ع��������ظ�ɨ��ͬһ����*/
        if (this.classInfoMap.containsKey(className) == true)
            return this.classInfoMap.get(className);
        /*����ʹ�� ClassReader ��ȡ��Ļ�����Ϣ*/
        ClassReader classReader = new ClassReader(inStream);
        className = classReader.getClassName().replace('/', '.');
        /*������ȡ��ģ����ơ����ࡢ�ӿڡ�ע�⣩��Ϣ*/
        final ClassInfo info = new ClassInfo();
        classReader.accept(new ClassVisitor(Opcodes.ASM4) {
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                //1.��ȡ������Ϣ
                info.className = name.replace('/', '.');
                if (superName != null)
                    info.superName = superName.replace('/', '.');
                //2.��ȡ�ӿ�
                info.interFaces = interfaces;
                for (int i = 0; i < info.interFaces.length; i++)
                    info.interFaces[i] = info.interFaces[i].replace('/', '.');
                super.visit(version, access, name, signature, superName, interfaces);
            }
            public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                //3.ɨ������Ϣ����ȡ��ǵ�ע��
                /**��һ��Ljava/lang/Object;��ʽ���ַ���ת��Ϊjava/lang/Object��ʽ��*/
                String[] annoArrays = info.annos == null ? new String[0] : info.annos;
                //
                String[] newAnnoArrays = new String[annoArrays.length + 1];
                System.arraycopy(annoArrays, 0, newAnnoArrays, 0, annoArrays.length);
                //
                String annnoType = desc.substring(1, desc.length() - 1);
                newAnnoArrays[newAnnoArrays.length - 1] = annnoType.replace('/', '.');
                //
                info.annos = newAnnoArrays;
                return super.visitAnnotation(desc, visible);
            }
        }, ClassReader.SKIP_CODE);
        //�ġ��ݹ��������
        if (info.superName != null) {
            InputStream superStream = loader.getResourceAsStream(info.superName.replace('.', '/') + ".class");
            if (superStream != null)
                loadClassInfo(info.superName, superStream, loader);//���ظ���
        }
        //�塢�ݹ�����ӿ�
        for (String faces : info.interFaces) {
            InputStream superStream = loader.getResourceAsStream(faces.replace('.', '/') + ".class");
            if (superStream != null)
                loadClassInfo(faces, superStream, loader);//���ظ���
        }
        //����ȡ�ø�����
        List<String> superLink = new ArrayList<String>();
        String superName = info.superName;
        if (superName != null) {
            while (true) {
                ClassInfo superInfo = this.classInfoMap.get(superName);
                if (superInfo == null)
                    break;
                superLink.add(superName);
                superName = superInfo.superName;
            }
        }
        info.superLink = superLink.toArray(new String[superLink.size()]);
        //�ߡ�ȡ�ýӿ���
        Set<String> facesLink = new TreeSet<String>();
        addFaces(info, facesLink);
        info.interFacesLink = facesLink.toArray(new String[facesLink.size()]);
        //
        this.classInfoMap.put(info.className, info);
        return info;
    }
    private void addFaces(ClassInfo info, Set<String> addTo) {
        addTo.addAll(Arrays.asList(info.interFaces));
        for (String atFaces : info.interFaces)
            addFaces(this.classInfoMap.get(atFaces), addTo);
    }
    //
    /**����Ϣ�ṹ*/
    private static class ClassInfo {
        /*����*/
        public String   className      = null;
        /*�̳еĸ���*/
        public String   superName      = null;
        /*�̳еĸ�����*/
        public String[] superLink      = new String[0];
        /*ֱ��ʵ�ֵĽӿ�*/
        public String[] interFaces     = new String[0];
        /*ʵ�ֵ����нӿ�*/
        public String[] interFacesLink = new String[0];
        /*��ǵ�ע��*/
        public String[] annos          = new String[0];
    }
}