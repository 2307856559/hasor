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
package net.hasor.core.module;
import java.util.ArrayList;
import java.util.List;
import net.hasor.core.Dependency;
import net.hasor.core.Hasor;
import net.hasor.core.ModuleInfo;
import org.more.util.StringUtils;
/**
 * ģ�鷴Ӧ�ѣ������Ŀ���Ƕ�ģ���������
 * @version : 2013-7-26
 * @author ������ (zyc@hasor.net)
 */
public class ModuleReactor {
    private List<ModuleInfo> modules = null;
    public ModuleReactor(List<ModuleInfo> modules) {
        this.modules = modules;
    }
    //
    /**���ģ��������Ƿ���ȷ��*/
    public void checkModule(ModuleInfo info) {
        ModulePropxy infoBean = (ModulePropxy) info;
        List<ReactorModuleInfoElement> stackArray = new ArrayList<ReactorModuleInfoElement>();
        try {
            //�ŵ�ջ��
            stackArray.add(new ReactorModuleInfoElement(0, infoBean, "[OK]    "));
            this.checkModuleInDependency(infoBean, 1, infoBean.getDependency(), new ArrayList<ModuleInfo>(), stackArray);
        } catch (RuntimeException e) {
            String treeInfo = getTreeInfo(stackArray, "[Other] ......");
            Hasor.logError("%s module depend on the loop.\n%s", infoBean.getDisplayName(), treeInfo);
            throw e;
        }
    }
    private void checkModuleInDependency(ModulePropxy info, int depth, List<Dependency> depList, List<ModuleInfo> depArray, List<ReactorModuleInfoElement> stackArray) {
        for (Dependency dep : depList) {
            ModulePropxy depInfo = (ModulePropxy) dep.getModuleInfo();
            ReactorModuleInfoElement infoLog = new ReactorModuleInfoElement(depth, depInfo);
            depArray.add(depInfo);
            stackArray.add(infoLog);
            //
            if (depArray.contains(info)) {
                /*��������ѭ��������Ӧ���ǳ������Լ������Լ�������*/
                infoLog.setMark("[Error] ");
                throw new RuntimeException(depInfo.getDisplayName() + " modules depend on the loop.");
            } else {
                infoLog.setMark("[OK]    ");
                this.checkModuleInDependency(info, depth + 1, depInfo.getDependency(), depArray, stackArray);
            }
        }
    }
    //
    /**������ȷ��ģ������˳���ģ��������򣬸÷���ͬʱ��������ѭ����⡣*/
    public List<ModuleInfo> process() {
        /*1.����ѭ����飬ȷ��ģ����ȶ�����*/
        Hasor.logInfo("begin cycle check...mods %s.", this.modules);
        for (ModuleInfo info : this.modules)
            this.checkModule(info);
        /*2.����ModuleInfo�����е�������*/
        Hasor.logInfo("build dependency tree for ModuleInfo.");
        for (ModuleInfo info : this.modules) {
            ModulePropxy depInfo = (ModulePropxy) info;
            for (Dependency dep : depInfo.getDependency())
                this.updateModuleDependency(dep);
        }
        /*3.ģ��������*/
        List<ReactorModuleInfoElement> tree = new ArrayList<ReactorModuleInfoElement>();
        this.getDependenceTree(tree);
        String treeInfo = getTreeInfo(tree, null);
        Hasor.logInfo("dependence Tree\n%s", treeInfo);
        /*4.ȷ������˳��*/
        List<ModuleInfo> finalList = this.getStartModule(tree);
        //
        StringBuilder sb = new StringBuilder("");
        int size = String.valueOf(finalList.size() - 1).length();
        for (int i = 0; i < finalList.size(); i++) {
            ModuleInfo info = finalList.get(i);
            sb.append(String.format("%0" + size + "d", i));
            sb.append('.');
            sb.append("-->");
            sb.append(info.getDisplayName());
            sb.append(" (");
            sb.append(info.getTarget().getClass());
            sb.append(")\n");
        }
        if (sb.length() > 1)
            sb.deleteCharAt(sb.length() - 1);
        Hasor.logInfo("startup sequence.\n%s", sb);
        return finalList;
    }
    //
    /**ȷ��������*/
    private void getDependenceTree(List<ReactorModuleInfoElement> infoList) {
        ArrayList<ModuleInfo> allModule = new ArrayList<ModuleInfo>(this.modules);//��¡һ��
        /*ȥ�����б���������Ŀ*/
        for (ModuleInfo info : this.modules) {
            ModulePropxy depInfo = (ModulePropxy) info;
            for (Dependency dep : depInfo.getDependency())
                allModule.remove(dep.getModuleInfo());
        }
        for (ModuleInfo info : allModule)
            this.getDependenceTree(0, info, infoList);
    }
    private void getDependenceTree(int depth, ModuleInfo info, List<ReactorModuleInfoElement> infoList) {
        infoList.add(new ReactorModuleInfoElement(depth, info));
        ModulePropxy depInfo = (ModulePropxy) info;
        for (Dependency dep : depInfo.getDependency())
            getDependenceTree(depth + 1, dep.getModuleInfo(), infoList);
    }
    private String getTreeInfo(List<ReactorModuleInfoElement> stackArray, String extInfo) {
        StringBuilder sb = new StringBuilder("");
        int size = String.valueOf(stackArray.size() - 1).length();
        for (int i = 0; i < stackArray.size(); i++) {
            ReactorModuleInfoElement element = stackArray.get(i);
            sb.append(element.getMark());
            sb.append(String.format("%0" + size + "d", i));
            sb.append('.');
            sb.append("-->");
            sb.append(StringUtils.fixedString(' ', (element.getDepth() + 1) * 2));
            sb.append(element.getInfo().getDisplayName());
            sb.append(" (");
            sb.append(element.getInfo().getTarget().getClass());
            sb.append(")\n");
        }
        if (extInfo != null)
            sb.append(extInfo);
        else {
            if (sb.length() > 1)
                sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
    //
    /**����������ȷ��ģ������˳��*/
    private List<ModuleInfo> getStartModule(List<ReactorModuleInfoElement> infoList) {
        List<ModuleInfo> finalList = new ArrayList<ModuleInfo>();
        for (ReactorModuleInfoElement e : infoList)
            if (e.getDepth() == 0) {
                ModuleInfo infoItem = e.getInfo();
                this.loadDependence(infoItem, finalList);
                if (finalList.contains(infoItem) == false)
                    finalList.add(infoItem);
            }
        return finalList;
    }
    private void loadDependence(ModuleInfo e, List<ModuleInfo> finalList) {
        ModulePropxy depInfo = (ModulePropxy) e;
        for (Dependency dep : depInfo.getDependency()) {
            ModuleInfo infoItem = dep.getModuleInfo();
            this.loadDependence(infoItem, finalList);
            if (finalList.contains(infoItem) == false)
                finalList.add(infoItem);
        }
    }
    //
    /**����ModuleInfo�����е�������*/
    private void updateModuleDependency(Dependency dep) {
        DependencyBean depBean = (DependencyBean) dep;
        List<Dependency> refDep = ((ModulePropxy) depBean.getModuleInfo()).getDependency();
        depBean.updateDependency(new ArrayList<Dependency>(refDep));
        for (Dependency e : refDep)
            this.updateModuleDependency(e);
    }
}
class ReactorModuleInfoElement {
    private String     mark  = "";
    private int        depth = 0;
    private ModuleInfo info  = null;
    public ReactorModuleInfoElement(int depth, ModuleInfo info) {
        this.depth = depth;
        this.info = info;
    }
    public ReactorModuleInfoElement(int depth, ModuleInfo info, String mark) {
        this.depth = depth;
        this.info = info;
        this.mark = mark;
    }
    public int getDepth() {
        return depth;
    }
    public ModuleInfo getInfo() {
        return info;
    }
    public String getMark() {
        return mark;
    }
    public void setMark(String mark) {
        this.mark = mark;
    }
    public int hashCode() {
        return info.hashCode();
    }
    public boolean equals(Object obj) {
        return info.equals(obj);
    }
}