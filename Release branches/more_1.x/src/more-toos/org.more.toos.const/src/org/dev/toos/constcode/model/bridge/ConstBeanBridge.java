package org.dev.toos.constcode.model.bridge;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.dev.toos.constcode.metadata.ConstBean;
import org.dev.toos.constcode.metadata.ConstVarBean;
import org.dev.toos.constcode.metadata.LatType;
import org.dev.toos.constcode.metadata.create.NewConstBean;
import org.dev.toos.constcode.model.ConstGroup;
import org.more.util.StringConvertUtil;
/**
 * ����view�б���ConstBean����
 * @version : 2013-2-16
 * @author ������ (zyc@byshell.org)
 */
public class ConstBeanBridge extends AbstractBridge<ConstBean> {
    private ConstBeanBridge       parentBridge = null;
    private List<ConstBeanBridge> childrenList = null;
    private List<VarBeanBridge>   varList      = null;
    //
    //
    public ConstBeanBridge(ConstBeanBridge parent, ConstGroup targetSource) {
        this(parent, new NewConstBean((parent == null) ? null : parent.getTarget()), targetSource);
    }
    public ConstBeanBridge(ConstBeanBridge parent, ConstBean targetBean, ConstGroup targetSource) {
        super(targetBean, targetSource);
        this.parentBridge = parent;
    }
    /*--------------------------------------------------------------------*/
    /**��ȡ��*/
    public ConstBeanBridge getParent() {
        return this.parentBridge;
    }
    /**��ȡ�ӽڵ�*/
    public List<ConstBeanBridge> getChildren() {
        if (this.childrenList == null) {
            this.childrenList = this.getSource().loadChildrenConst(this);
            if (this.childrenList == null)
                this.childrenList = new ArrayList<ConstBeanBridge>();
        }
        return Collections.unmodifiableList(this.childrenList);
    }
    /**��ȡ����ֵ���ڵ�*/
    public List<VarBeanBridge> getVarRoots() {
        if (this.varList == null) {
            this.varList = this.getSource().loadVarRoots(this);
            if (this.varList == null)
                this.varList = new ArrayList<VarBeanBridge>();
        }
        return Collections.unmodifiableList(this.varList);
    }
    //    /**�����Ԫ��*/
    //    public void addConst(String constCode, String constName, GroupType type) {
    //        //TODO xxx
    //    }
    //    /**�����Ԫ��*/
    //    public void addVar() {
    //        //TODO xxx
    //    }
    /**���ֵԪ��*/
    public boolean addVar(int index, VarBeanBridge newVar) {
        getVarRoots();//��ʼ������
        if (this.varList.contains(newVar) == true)
            return false;
        this.varList.add(index, newVar);
        this.getSource().setConstChanged(true);//֪ͨ�޸Ĺ�����
        return true;
    }
    public void deleteVar(VarBeanBridge bridge) {
        if (this.varList.contains(bridge) == false)
            return;
        if (bridge.isNew() == true)
            this.varList.remove(bridge);
        else
            bridge.delete();
        this.getSource().setConstChanged(true);//֪ͨ�޸Ĺ�����
    }
    /*--------------------------------------------------------------------*/
    /**�����Ƿ�ı������д���������˶��Ӽ������ͳ���ֵ�ļ��*/
    public boolean isPropertyChanged() {
        boolean res = super.isPropertyChanged();
        for (ConstBeanBridge childrenConst : this.getChildren())
            if (childrenConst.isPropertyChanged() == true) {
                res = true;
                break;
            }
        for (VarBeanBridge childrenVar : this.getVarRoots())
            if (childrenVar.isPropertyChanged() == true) {
                res = true;
                break;
            }
        return res;
    }
    @Override
    public void recover() {
        //1.this recover
        super.recover();
        if (this.isNew() == true)
            this.getSource().deleteConst(this);
        //2.children recover without delete
        ArrayList<ConstBeanBridge> deleteConstList = new ArrayList<ConstBeanBridge>();
        if (this.childrenList != null)
            for (ConstBeanBridge constBridge : this.childrenList) {
                if (constBridge.isNew() == true)
                    deleteConstList.add(constBridge);
                else
                    constBridge.recover();
            }
        //3.children recover on delete
        for (ConstBeanBridge constBridge : deleteConstList)
            constBridge.recover();
        //4.varList recover without delete
        ArrayList<VarBeanBridge> deleteVarList = new ArrayList<VarBeanBridge>();
        if (this.varList != null)
            for (VarBeanBridge varBridge : this.varList) {
                if (varBridge.isNew() == true)
                    deleteVarList.add(varBridge);
                else
                    varBridge.recover();
            }
        //5.varList recover on delete
        for (VarBeanBridge varBridge : deleteVarList)
            varBridge.recover();
    }
    public boolean applyData() {
        if (this.readOnly() == true)
            return false;
        //
        ConstBean target = this.getTarget();
        target.setConstCode(this.getCode());
        target.setConstVar(this.getVar());
        target.setConstExtData(this.getExtData());
        //������Ԫ�ص�applyData
        if (this.childrenList != null)
            for (ConstBeanBridge constBean : this.childrenList)
                if (constBean.applyData() == false)
                    return false;
        if (this.varList != null)
            for (VarBeanBridge varBean : this.varList)
                if (varBean.applyData() == false)
                    return false;
        return true;
    }
    public void updateState(ConstBean target) {
        //A.Const part
        if (this.getTarget() != target) {
            //target����
            this.setTarget(target);
            //���º��ӵĸ���
            if (this.childrenList != null)
                for (ConstBeanBridge constBean : this.childrenList) {
                    ConstBean _target = constBean.getTarget();
                    _target.setParent(target);
                    constBean.updateState(_target);
                }
        }
        //B.Var part
        if (this.varList != null)
            for (VarBeanBridge varBean : this.varList) {
                ConstVarBean targetBean = varBean.getTarget();
                targetBean.setConst(target);
                varBean.updateState(targetBean);
            }
    }
    /*--------------------------------------------------------------------*/
    /**ConstCode*/
    public String getCode() {
        return (String) this.getProperty("ConstCode");
    }
    public boolean setCode(String code) {
        return this.setProperty("ConstCode", code);
    }
    public boolean isCodeChanged() {
        return this.isPropertyChanged("ConstCode");
    }
    /**ConstVar*/
    public String getVar() {
        return (String) this.getProperty("ConstVar");
    }
    public boolean setVar(String var) {
        return this.setProperty("ConstVar", var);
    }
    public boolean isVarChanged() {
        return this.isPropertyChanged("ConstVar");
    }
    /**ConstLatType*/
    public LatType getLatType() {
        return StringConvertUtil.changeType(this.getProperty("ConstLatType"), LatType.class, LatType.No);
    }
    public boolean setLatType(LatType latType) {
        return this.setProperty("ConstLatType", latType);
    }
    public boolean isLatTypeChanged() {
        return this.isPropertyChanged("ConstLatType");
    }
    /**ConstExtData*/
    public String getExtData() {
        return (String) this.getProperty("ConstExtData");
    }
    public boolean setExtData(String extData) {
        return this.setProperty("ConstExtData", extData);
    }
    public boolean isExtDataChanged() {
        return this.isPropertyChanged("ConstExtData");
    }
}