package org.dev.toos.constcode.model.bridge;
import java.util.Hashtable;
import java.util.Map;
import org.dev.toos.constcode.metadata.UpdateState;
import org.dev.toos.constcode.metadata.create.NEW;
import org.dev.toos.constcode.model.ConstGroup;
import org.more.util.BeanUtil;
/**
 * 
 * @version : 2013-2-19
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractBridge<T> implements UpdateState<T> {
    private ConstGroup          targetSource   = null;                           //�����������Դ
    private T                   targetBean     = null;                           //����Ŀ��
    private boolean             activateModify = false;                          //�Ƿ񼤻����޸�ģʽ
    private boolean             newMark        = false;                          //�Ƿ�Ϊ�½���Ŀ
    private boolean             deleteMark     = false;                          //�Ƿ�ɾ���˸ö���
    private Map<String, Object> tempProperty   = new Hashtable<String, Object>();
    //
    //
    public AbstractBridge(T target, ConstGroup targetSource) {
        this.targetSource = targetSource;
        this.targetBean = target;
        if (this.targetBean instanceof NEW)
            this.newMark = true;
    }
    /**�Ƿ�Ϊֻ��*/
    public boolean readOnly() {
        return this.targetSource.isReadOnly();
    }
    /**��ȡ�����Ŀ��*/
    public T getTarget() {
        return this.targetBean;
    }
    /**��ȡ�����Ŀ��*/
    public void setTarget(T target) {
        this.targetBean = target;
    }
    /**��ȡ��Դ*/
    public ConstGroup getSource() {
        return this.targetSource;
    }
    /**�ж϶����Ƿ�Ϊ�½��ġ�*/
    public boolean isNew() {
        return newMark;
    }
    /**�Ƿ��ڱ༭ģʽ��*/
    public boolean isActivateModify() {
        return this.activateModify;
    }
    /**����༭ģʽ*/
    public void doEdit() {
        this.activateModify = true;
    }
    /**ȡ���༭ģʽ*/
    public void cancelEdit() {
        this.activateModify = false;
    }
    /**�жϸ������Ƿ�ɾ��*/
    public boolean isDelete() {
        return this.deleteMark;
    }
    /**ɾ������*/
    public void delete() {
        this.deleteMark = true;
    }
    /**�ظ������޸�ǰ��״̬*/
    public void recover() {
        clearMark();
    }
    /**��Bridge�ϵ����ݸ��µ�����ģ���ϡ�*/
    public abstract boolean applyData();
    /**����޸ı��*/
    public void clearMark() {
        this.deleteMark = false;
        this.tempProperty.clear();
    }
    /**��ȡ����ֵ*/
    public Object getProperty(String propertyName) {
        if (this.tempProperty.containsKey(propertyName) == true)
            return this.tempProperty.get(propertyName);
        else if (this.targetBean != null)
            return BeanUtil.readPropertyOrField(this.targetBean, propertyName);
        return null;
    }
    /**��������ֵ��*/
    public boolean setProperty(String propertyName, Object newValue) {
        if (this.readOnly() == true)
            return false;
        if (newValue == null)
            return false;
        Object proValue = this.getProperty(propertyName);
        if (proValue == newValue)
            return false;
        if (proValue != null && proValue.equals(newValue) == true)
            return false;
        //
        this.tempProperty.put(propertyName, newValue);
        this.getSource().setConstChanged(true);
        return true;
    }
    /**�ж������Ƿ��޸Ĺ���*/
    public boolean isPropertyChanged(String propertyName) {
        return this.tempProperty.containsKey(propertyName);
    }
    /**�����Ƿ�ı��*/
    public boolean isPropertyChanged() {
        if (this.deleteMark == true)
            return true;
        if (this.newMark == true)
            return true;
        return !this.tempProperty.isEmpty();
    }
}
