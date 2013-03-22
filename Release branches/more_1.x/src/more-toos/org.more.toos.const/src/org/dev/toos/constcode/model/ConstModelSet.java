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
package org.dev.toos.constcode.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.dev.toos.internal.util.Message;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
/**
 * ���ڹ���ÿ����Ŀģ���µ��ܿ��ࡣ
 * @version : 2013-2-2
 * @author ������ (zyc@byshell.org)
 */
public class ConstModelSet {
    private static Map<String, ConstModel> modeBeanMap   = new LinkedHashMap<String, ConstModel>();
    private static List<ConstModel>        modeBeanList  = new ArrayList<ConstModel>();
    private static ConstModel              activateModel = null;
    //
    //
    //
    /**��ȡ���ģ�͡�*/
    public static ConstModel getActivateModel() {
        return activateModel;
    }
    /**���Լ���ĳ�������ļ������Ŀ�������ļ���������װ����*/
    public static ConstModel activateModel(String projectName) {
        if (modeBeanMap.containsKey(projectName) == false)
            return null;
        activateModel = modeBeanMap.get(projectName);
        return activateModel;
    }
    /**�ж��Ƿ���������Ŀ��*/
    public static boolean existConstModel(IJavaProject javaProject) {
        String projectName = javaProject.getElementName();
        return modeBeanMap.containsKey(projectName);
    }
    /**����ģ�͡� */
    public static ConstModel newConstModel(IJavaProject javaProject, IProgressMonitor monitor) throws CoreException {
        if (existConstModel(javaProject) == true) {
            ConstModel constModel = getConstModel(javaProject);
            constModel.refresh(monitor);//֪ͨģ�ͽ���ˢ�¡�
            return constModel;
        }
        ConstModel modelBean = new ConstModel(javaProject);
        modelBean.initLoad(monitor);//ִ�г�ʼ��װ�أ��ò������װ�سɹ��Ὣģ����Ϊ��Ч״̬
        modeBeanMap.put(modelBean.getProjectName(), modelBean);
        modeBeanList.add(modelBean);
        return modelBean;
    }
    /**��ȡģ�͡�*/
    public static ConstModel getConstModel(IJavaProject javaProject) {
        String projectName = javaProject.getElementName();
        return modeBeanMap.get(projectName);
    }
    /**�������빤���ռ���������Ŀ��*/
    public static void refresh(IProgressMonitor monitor) {
        activateModel = null;
        /* 1.ɨ��projects����������xml
         *   1.1.�µ���������ִ�����룬���ұ��Ϊ��Ч��
         *   1.2.�Ѿ����ڵ���Ŀ����ʧЧ���Ϊ��Ч��
         * 2.����Ĭ����Ŀ
         */
        //
        HashMap<String, ConstModel> oldModeMap = new HashMap<String, ConstModel>(modeBeanMap);
        Message.updateTask(monitor, "scanning projects...", 3, 1);
        //
        IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
        if (projects != null) {
            for (int i = 0; i < projects.length; i++) {
                IProject pro = projects[i];
                Message.updateTask(monitor, "scanning projects(��" + pro.getName() + "��)", projects.length, i);
                if (pro.isOpen() == false)
                    continue;
                if (pro.exists() == false)
                    continue;
                IJavaProject javaProject = JavaCore.create(pro);
                if (javaProject == null || javaProject.exists() == false)
                    continue;
                //ת��ΪJava��Ŀ�����Ҳ���core-codes.xml�����ļ���
                try {
                    IJavaElement[] javaElements = javaProject.getChildren();
                    if (javaElements == null)
                        continue;
                    oldModeMap.remove(javaProject.getElementName());
                    if (existConstModel(javaProject) == true)
                        getConstModel(javaProject).refresh(monitor);//֪ͨˢ����Ŀ
                    else
                        newConstModel(javaProject, monitor).refresh(monitor);//֪ͨˢ����Ŀ
                } catch (Exception e) {
                    Message.errorInfo("Refresh Job", e);
                }
            }
        }
        //
        for (ConstModel constModel : getModeBeanList())
            if (constModel.getGroups().size() == 0)
                oldModeMap.put(constModel.getProjectName(), constModel);
        for (Entry<String, ConstModel> ent : oldModeMap.entrySet())
            ConstModelSet.removeModel(ent.getValue().getProject());
        //
        Message.updateTask(monitor, "activate current Project...", 3, 2);
        if (getModeBeanList().size() != 0) {
            String projectName = null;
            IProject currentProject = ResourcesPlugin.getWorkspace().getRoot().getProject();
            if (currentProject != null) {
                ConstModel currentModel = modeBeanMap.get(currentProject.getName());
                if (currentModel != null)
                    projectName = currentModel.getProjectName();
            }
            if (projectName == null)
                projectName = getModeBeanList().get(0).getProjectName();
            activateModel(projectName);
        }
        //
        Message.updateTask(monitor, "process callBack function...", 3, 3);
    }
    public static void removeModel(IJavaProject javaProject) {
        modeBeanList.remove(modeBeanMap.remove(javaProject.getElementName()));
    }
    /**��ȡ�Ѿ�ת�ص�ģ���б����ص�ֻ���б�*/
    public static List<ConstModel> getModeBeanList() {
        return Collections.unmodifiableList(modeBeanList);
    }
}