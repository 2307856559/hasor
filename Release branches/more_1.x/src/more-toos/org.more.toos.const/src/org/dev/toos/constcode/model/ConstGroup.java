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
import java.util.List;
import org.dev.toos.constcode.data.ConstDao;
import org.dev.toos.constcode.data.VarDao;
import org.dev.toos.constcode.metadata.ConstBean;
import org.dev.toos.constcode.metadata.ConstVarBean;
import org.dev.toos.constcode.metadata.create.NEW;
import org.dev.toos.constcode.model.bridge.ConstBeanBridge;
import org.dev.toos.constcode.model.bridge.VarBeanBridge;
/**
 * 
 * @version : 2013-2-2
 * @author ������ (zyc@byshell.org) 
 */
public abstract class ConstGroup {
    public static enum FromType {
        DB, Source, JAR,
    }
    private String                name          = null;
    private boolean               isReadOnly    = false;
    private FromType              fromType      = null;
    private boolean               isChanged     = false;
    //
    /*������*/
    private List<ConstBeanBridge> constBeanList = new ArrayList<ConstBeanBridge>();
    //
    //
    protected ConstGroup(FromType fromType) {
        if (fromType == null)
            throw new NullPointerException();
        this.fromType = fromType;
    }
    /**�����Ƿ�Ϊֻ������*/
    public boolean isReadOnly() {
        return this.isReadOnly;
    }
    protected void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }
    /**��ȡ��Դ����*/
    public FromType getType() {
        return this.fromType;
    }
    /**��ȡ����*/
    public String getName() {
        return name;
    }
    /**��������*/
    public void setName(String name) {
        this.name = name;
    }
    /**ȷ������������Ƿ��Ѿ����޸Ĺ���*/
    public boolean isConstChanged() {
        return isChanged;
    }
    /**���ⲿ����һ��ֵ����ֵ����˵����������������Ѿ����޸Ĺ���*/
    public void setConstChanged(boolean isChanged) {
        this.isChanged = isChanged;
    }
    /**����ֻ����ʽ�ĳ�������*/
    public List<ConstBeanBridge> constList() {
        return Collections.unmodifiableList(constBeanList);
    }
    /**���볣���ĺ���*/
    public List<ConstBeanBridge> loadChildrenConst(ConstBeanBridge constBean) {
        ArrayList<ConstBeanBridge> cbgList = new ArrayList<ConstBeanBridge>();
        ConstBean targetConstBean = constBean.getTarget();
        if (targetConstBean instanceof NEW == false) {
            List<ConstBean> children = this.getConstDao().getConstChildren(targetConstBean);
            if (children != null)
                for (ConstBean cb : children)
                    cbgList.add(new ConstBeanBridge(constBean, cb, this));
        }
        return cbgList;
    }
    /**���볣��ֵ�ĺ���*/
    public List<VarBeanBridge> loadChildrenVar(VarBeanBridge varBean) {
        ArrayList<VarBeanBridge> cbgList = new ArrayList<VarBeanBridge>();
        ConstVarBean targetVarBean = varBean.getTarget();
        if (targetVarBean instanceof NEW == false/* true is new var*/) {
            List<ConstVarBean> varItems = this.getVarDao().getVarChildren(targetVarBean);
            if (varItems != null)
                for (ConstVarBean cb : varItems)
                    cbgList.add(new VarBeanBridge(varBean.getConst(), varBean, cb, this));
        }
        return cbgList;
    }
    /**���볣������ĳ���ֵ*/
    public List<VarBeanBridge> loadVarRoots(ConstBeanBridge constBean) {
        ArrayList<VarBeanBridge> cbgList = new ArrayList<VarBeanBridge>();
        ConstBean targetConstBean = constBean.getTarget();
        if (targetConstBean instanceof NEW == false) {
            List<ConstVarBean> varItems = this.getVarDao().getVarRoots(targetConstBean);
            if (varItems != null)
                for (ConstVarBean cb : varItems)
                    cbgList.add(new VarBeanBridge(constBean, null, cb, this));
        }
        return cbgList;
    }
    //
    //
    /**�����Ƿ�װ�سɹ�*/
    public boolean loadData() {
        for (ConstBean constBean : this.getConstDao().getRootConst()) {
            ConstBeanBridge constBridge = new ConstBeanBridge(null, constBean, this);
            this.constBeanList.add(constBridge);
        }
        return true;
    };
    /**����װ��XML.��������һ���ľͷ������ء���jar����δ���棩*/
    public boolean reloadData() {
        if (this.getType() == FromType.JAR)//Jar������
            return false;
        if (this.isConstChanged() == true)//��δ���棬����
            return false;
        //
        this.constBeanList.clear();
        return this.loadData();
    }
    /**�õ�֪ͨ׼����ʼ���¡�*/
    public void beginSave() throws Throwable {};
    /**�õ�֪ͨ���¹��̽�����*/
    public void finishSave() throws Throwable {};
    /**�־û�����*/
    public void save() throws Throwable {
        /*��ֻ��ģʽ�·�������*/
        if (this.isReadOnly() == true)
            return;
        /*��Bridge�ϵ����ݸ��µ�����ģ����,Ȼ����г־û�����*/
        for (int i = 0; i < this.constBeanList.size(); i++) {
            ConstBeanBridge beanBridge = this.constBeanList.get(i);
            if (beanBridge.isPropertyChanged() == false)
                continue;
            this.doUpdateConst(i, beanBridge);//TODO ���Կ��ܵĴ���
        }
        this.setConstChanged(false);
    }
    /**��ӳ���*/
    public boolean addConst(ConstBeanBridge constBean) {
        this.setConstChanged(true);
        this.constBeanList.add(constBean);
        return true;
    }
    public boolean addConst(int index, ConstBeanBridge newConst) {
        this.setConstChanged(true);
        this.constBeanList.add(index, newConst);
        return true;
    }
    /**ɾ������*/
    public boolean deleteConst(ConstBeanBridge constBean) {
        this.setConstChanged(true);
        if (constBean.isNew() == true)
            this.constBeanList.remove(constBean);
        else
            constBean.delete();
        return true;
    }
    //
    /**�־û���������*/
    private boolean doUpdateConst(int upDataIndex, ConstBeanBridge target) throws Throwable {
        boolean tempRes = true;
        //0.Ӧ��������
        if (target.applyData() == false)
            return false;
        //1.��������
        if (target.isDelete() == true) {
            return this.getConstDao().deleteConst(target.getTarget());
        }
        ConstBean newRes = null;
        if (target.isNew() == true)
            newRes = this.getConstDao().addConst(target.getTarget(), upDataIndex);
        else
            newRes = this.getConstDao().updateConst(target.getTarget(), upDataIndex);
        if (newRes == null)
            return false;
        target.updateState(newRes);
        //2.����ֵ����
        List<VarBeanBridge> varList = target.getVarRoots();
        if (varList != null)
            for (int i = 0; i < varList.size(); i++) {
                VarBeanBridge varBean = varList.get(i);
                if (varBean.isPropertyChanged() == false)
                    continue;
                tempRes = this.doUpdateVar(i, varList.get(i));
                if (tempRes == false)
                    return false;
            }
        //3.���Ӵ���
        List<ConstBeanBridge> constChildren = target.getChildren();
        for (int i = 0; i < constChildren.size(); i++) {
            tempRes = this.doUpdateConst(i, constChildren.get(i));
            if (tempRes == false)
                return false;
        }
        return true;
    };
    /**�־û���������ֵ*/
    private boolean doUpdateVar(int upDataIndex, VarBeanBridge target) throws Throwable {
        boolean tempRes = true;
        //0.Ӧ��������
        if (target.applyData() == false)
            return false;
        //1.��������
        if (target.isDelete() == true)
            return this.getVarDao().deleteVar(target.getTarget());
        ConstVarBean newRes = null;
        if (target.isNew() == true)
            newRes = this.getVarDao().addVar(target.getTarget(), upDataIndex);
        else
            newRes = this.getVarDao().updateVar(target.getTarget(), upDataIndex);
        if (newRes == null)
            return false;
        target.updateState(newRes);
        //2.���Ӵ���
        List<VarBeanBridge> varList = target.getChildren();
        if (varList != null)
            for (int i = 0; i < varList.size(); i++) {
                VarBeanBridge varBean = varList.get(i);
                if (varBean.isPropertyChanged() == false)
                    continue;
                tempRes = this.doUpdateVar(i, varList.get(i));
                if (tempRes == false)
                    return false;
            }
        return true;
    };
    //
    protected abstract void initGroup();
    protected abstract ConstDao getConstDao();
    protected abstract VarDao getVarDao();
}