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
package org.dev.toos.constcode.model;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.dev.toos.constcode.model.ConstGroup.FromType;
import org.dev.toos.constcode.model.group.FileConstCodeGroup;
import org.dev.toos.constcode.model.group.JARSourceConstCodeGroup;
import org.dev.toos.internal.util.Message;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJarEntryResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
/**
 * 
 * @version : 2013-2-2
 * @author ������ (zyc@byshell.org)
 */
public class ConstModel {
    private static final String     ResourceName    = "/META-INF/core-codes.xml";
    //
    private IJavaProject            javaProject     = null;
    private boolean                 isLoadedLibrary = false;
    private Map<String, ConstGroup> constGroupMap   = new Hashtable<String, ConstGroup>();
    private List<ConstGroup>        constGroupList  = new ArrayList<ConstGroup>();
    private ConstGroup              currentGroup    = null;
    //
    //
    //
    /**����ConstCodeModel����*/
    public ConstModel(IJavaProject javaProject) {
        this.javaProject = javaProject;
    }
    /**��ȡ��Ŀ��*/
    public String getProjectName() {
        return this.javaProject.getElementName();
    }
    /**��ȡ��Ŀ*/
    public IJavaProject getProject() {
        return this.javaProject;
    }
    /**��ȡ��ģ�����ڱ�ʾ���ַ���*/
    public String getTitle() {
        return "(" + this.getProjectName() + ")";
    }
    /**��ʼ��װ��*/
    public void initLoad(IProgressMonitor monitor) throws CoreException {
        IJavaElement[] javaElements = this.javaProject.getChildren();
        if (javaElements != null) {
            //A.ɨ����Դ
            Map<String, Object> findRes = new Hashtable<String, Object>();
            for (int j = 0; j < javaElements.length; j++) {
                IJavaElement element = javaElements[j];
                if (element instanceof JarPackageFragmentRoot == false)
                    iterateItem(element, ResourceName, findRes);
                Message.updateTask(monitor, "scanning project ' " + javaProject.getElementName() + " '...", javaElements.length, j);
            }
            //B.������Դ
            for (Entry<String, Object> ent : findRes.entrySet()) {
                String key = ent.getKey();
                Object var = ent.getValue();
                if (var instanceof IJarEntryResource)
                    this.loadConst4Jar(key, (IJarEntryResource) var);
                else if (var instanceof IResource)
                    this.loadConst4Res(key, (IResource) var);
            }
        }
    }
    /**��������з�Source�ķ���*/
    public void cleanLibraryGroup() {
        this.isLoadedLibrary = false;
        ArrayList<ConstGroup> libGroup = new ArrayList<ConstGroup>();
        for (ConstGroup group : constGroupList)
            if (group.getType() != FromType.Source)
                libGroup.add(group);
        for (ConstGroup group : libGroup)
            constGroupList.remove(constGroupMap.remove(group.getName()));
    }
    /**ִ��һ����ȫ������װ��*/
    public void initLoadLibrary(IProgressMonitor monitor) throws CoreException {
        //
        IJavaElement[] javaElements = javaProject.getChildren();
        if (javaElements != null) {
            //A.ɨ����Դ
            Map<String, Object> findRes = new Hashtable<String, Object>();
            for (int j = 0; j < javaElements.length; j++) {
                IJavaElement element = javaElements[j];
                if (element instanceof JarPackageFragmentRoot == true)
                    iterateItem(element, ResourceName, findRes);
                Message.updateTask(monitor, "scanning project ' " + javaProject.getElementName() + " '...", javaElements.length, j);
            }
            //B.������Դ
            for (Entry<String, Object> ent : findRes.entrySet()) {
                String key = ent.getKey();
                Object var = ent.getValue();
                if (var instanceof IJarEntryResource)
                    this.loadConst4Jar(key, (IJarEntryResource) var);
                else if (var instanceof IResource)
                    this.loadConst4Res(key, (IResource) var);
            }
        }
        this.isLoadedLibrary = true;
    }
    /**֪ͨģ�ͽ���ˢ�£����Ƿ�ִ��ˢ������ģ����Ը������*/
    public void refresh(IProgressMonitor monitor) throws CoreException {
        //A.��ȫ���
        IJavaElement[] javaElements = javaProject.getChildren();
        if (javaElements == null) {
            this.constGroupMap = new Hashtable<String, ConstGroup>();
            this.constGroupList = new ArrayList<ConstGroup>();
        }
        //B.ɨ����Դ
        Map<String, Object> findRes = new Hashtable<String, Object>();
        for (int j = 0; j < javaElements.length; j++) {
            IJavaElement element = javaElements[j];
            if (this.isLoadedLibrary == true)
                //��������
                iterateItem(element, ResourceName, findRes);
            else {
                //����Src
                if (element instanceof JarPackageFragmentRoot == false)
                    iterateItem(element, ResourceName, findRes);
            }
            Message.updateTask(monitor, "scanning project ' " + javaProject.getElementName() + " '...", javaElements.length, j);
        }
        /*----------------------------------------------*/
        List<String> scanRes = null;
        List<String> nowRes = null;
        //C.����scanRes������µĳ�Ա
        scanRes = new ArrayList<String>(findRes.keySet());
        nowRes = new ArrayList<String>(this.constGroupMap.keySet());
        scanRes.removeAll(nowRes);
        for (String itemStr : scanRes) {
            Object resObj = findRes.get(itemStr);
            if (resObj instanceof IJarEntryResource)
                this.loadConst4Jar(itemStr, (IJarEntryResource) resObj);
            else
                this.loadConst4Res(itemStr, (IResource) resObj);
        }
        //D.����nowRes������Ѿ�ʧЧ�ĳ�Ա
        scanRes = new ArrayList<String>(findRes.keySet());
        nowRes = new ArrayList<String>(this.constGroupMap.keySet());
        nowRes.removeAll(scanRes);
        for (String itemStr : nowRes) {
            ConstGroup constGroup = this.constGroupMap.get(itemStr);
            this.constGroupMap.remove(itemStr);
            this.constGroupList.remove(constGroup);
        }
        //E.
        for (ConstGroup groupItem : this.constGroupList)
            groupItem.reloadData();
    }
    public void save(IProgressMonitor monitor) {
        for (ConstGroup groupItem : this.constGroupList) {
            try {
                groupItem.beginSave();
                groupItem.save();
                groupItem.finishSave();
            } catch (Throwable e) {
                Message.errorInfo("save Group error ��" + groupItem.getName() + "��.", e);
            }
        }
    }
    /**���ó���������Ϣ��*/
    public void setGroup(String groupName) {
        if (groupName == null || groupName.equals("")) {
            this.currentGroup = null;
            return;
        }
        this.currentGroup = this.constGroupMap.get(groupName);
    }
    /**��ȡָ���ķ���*/
    public ConstGroup group(String groupName) {
        if (groupName == null)
            return null;
        return this.constGroupMap.get(groupName);
    }
    //
    // get/set���Բ���
    //
    /**��ȡ�Ѿ�װ�ص�XML�ļ�������Ϣ��*/
    public List<ConstGroup> getGroups() {
        return this.constGroupList;
    }
    /**�Ƿ��Ѿ�װ�ع���*/
    public boolean isLoadedLibrary() {
        return isLoadedLibrary;
    }
    /**��ȡ��ǰ����*/
    public ConstGroup getCurrentGroup() {
        return currentGroup;
    }
    //
    // ɨ�貿��......
    //
    /**��Դ���������*/
    private void iterateItem(Object atElement, String resourceName, Map<String, Object> findRes) throws CoreException {
        if (atElement instanceof IFolder) {
            this.iterateIFolder((IFolder) atElement, resourceName, findRes);
        } else if (atElement instanceof IFile) {
            this.iterateIFile((IFile) atElement, resourceName, findRes);
        } else if (atElement instanceof IJavaElement) {
            this.iterateIJavaElement((IJavaElement) atElement, resourceName, findRes);
        } else if (atElement instanceof IJarEntryResource) {
            this.iterateIJarEntryResource((IJarEntryResource) atElement, resourceName, findRes);
        }
    }
    /**�ڰ��������в���*/
    private void iterateIJavaElement(IJavaElement atElement, String resourceName, Map<String, Object> findRes) throws CoreException {
        if (atElement.exists() == false)
            return;
        switch (atElement.getElementType()) {
        /*package root*/
        case IJavaElement.PACKAGE_FRAGMENT_ROOT: {
            IPackageFragmentRoot e = (IPackageFragmentRoot) atElement;
            Object[] resourcesItem = e.getNonJavaResources();
            if (resourcesItem != null)
                for (Object element : resourcesItem)
                    iterateItem(element, resourceName, findRes);
            //            IJavaElement[] childs = e.getChildren();
            //            if (childs != null)
            //                for (IJavaElement elementItem : childs)
            //                    iterateItem(elementItem, resourceName);
            break;
        }
        /*package*/
        case IJavaElement.PACKAGE_FRAGMENT: {
            IPackageFragment e = (IPackageFragment) atElement;
            Object[] resourcesItem = e.getNonJavaResources();
            if (resourcesItem != null)
                for (Object element : resourcesItem)
                    iterateItem(element, resourceName, findRes);
            //            IJavaElement[] childs = e.getChildren();
            //            if (childs != null)
            //                for (IJavaElement elementItem : childs)
            //                    iterateItem(elementItem, resourceName);
            break;
        }
        }
    }
    /**��Jar�ļ��в���*/
    private void iterateIJarEntryResource(IJarEntryResource atElement, final String resourceName, Map<String, Object> findRes) throws CoreException {
        if (atElement.getFullPath().toString().endsWith(resourceName) == true) {
            //System.out.println(atElement.getFullPath().toString());
            JarPackageFragmentRoot root = (JarPackageFragmentRoot) atElement.getPackageFragmentRoot();
            String name = root.getJar().getName();
            findRes.put(name, atElement);
        }
        IJarEntryResource[] resourcesItem = atElement.getChildren();
        if (resourcesItem != null)
            for (IJarEntryResource element : resourcesItem)
                iterateItem(element, resourceName, findRes);
    }
    /**���ļ����в���*/
    private void iterateIFolder(IFolder atElement, final String resourceName, final Map<String, Object> findRes) throws CoreException {
        if (atElement.exists() == false)
            return;//loadResource
        atElement.accept(new IResourceVisitor() {
            @Override
            public boolean visit(IResource resource) throws CoreException {
                if (resource.exists() == false)
                    return true;
                if (resource.getLocation().toString().endsWith(resourceName) == true) {
                    //System.out.println(resource.getType() + "\t" + resource.getLocation().toString());
                    findRes.put(resource.getLocation().toString(), resource);
                }
                return true;
            }
        });
    }
    /**���ļ��в���*/
    private void iterateIFile(IFile atElement, String resourceName, Map<String, Object> findRes) throws CoreException {
        if (atElement.exists() == false)
            return;
        // System.out.println(atElement.getType() + "\t" + atElement.getName());
    }
    //
    // ɨ����������......
    //
    /**װ��һ������jar�� */
    private void loadConst4Jar(String name, IJarEntryResource atElement) {
        //1.ִ��װ��
        ConstGroup groupModel = this.constGroupMap.get(name);
        if (groupModel == null) {
            InputStream inStream = null;
            String resourceName = atElement.getName();
            try {
                //                if (atElement.)
                JarPackageFragmentRoot root = (JarPackageFragmentRoot) atElement.getPackageFragmentRoot();
                ZipFile jar = root.getJar();
                ZipEntry zipEntry = jar.getEntry(atElement.getFullPath().toString().substring(1));
                if (zipEntry == null)
                    return;
                inStream = jar.getInputStream(zipEntry);
                if (inStream == null)
                    return;
            } catch (Exception e) {
                Message.errorInfo("load IJarEntryResource ��" + resourceName + "��", e);
                return;
            }
            //
            groupModel = new JARSourceConstCodeGroup(name, inStream);
            groupModel.initGroup();
            this.constGroupMap.put(name, groupModel);
            this.constGroupList.add(groupModel);
            groupModel.loadData();
        } else
            groupModel.reloadData();
    }
    /**װ��һ�����������ļ���ֻҪ�ɹ�װ��һ�ξͽ�ģ����Ϊ��Ч�� */
    private void loadConst4Res(String name, IResource resource) {
        //1.ִ��װ��
        ConstGroup groupModel = this.constGroupMap.get(name);
        if (groupModel == null) {
            groupModel = new FileConstCodeGroup(name, (IFile) resource);
            groupModel.initGroup();
            this.constGroupMap.put(name, groupModel);
            this.constGroupList.add(groupModel);
            groupModel.loadData();
        } else
            groupModel.reloadData();
    }
}