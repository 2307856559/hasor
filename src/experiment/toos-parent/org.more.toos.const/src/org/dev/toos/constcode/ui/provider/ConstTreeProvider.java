package org.dev.toos.constcode.ui.provider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.dev.toos.constcode.model.ConstGroup;
import org.dev.toos.constcode.model.ConstGroup.FromType;
import org.dev.toos.constcode.model.ConstModel;
import org.dev.toos.constcode.model.ConstModelSet;
import org.dev.toos.constcode.model.bridge.ConstBeanBridge;
import org.dev.toos.internal.util.ColorUtils;
import org.dev.toos.ui.internal.ui.eclipse.wb.swt.ResourceManager;
import org.dev.toos.ui.internal.ui.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerRow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
/**
 * 
 * @version : 2013-2-4
 * @author ������ (zyc@byshell.org)
 */
public class ConstTreeProvider extends CellLabelProvider implements ITreeContentProvider {
    /**ʵ��CellLabelProvider���ͷ�����������Ԫ����������ʾ������*/
    @Override
    public void update(ViewerCell cell) {
        ConstBeanBridge element = (ConstBeanBridge) cell.getElement();
        ViewerRow row = cell.getViewerRow();
        row.setText(0, element.getCode());//code
        row.setText(1, element.getLatType().name());//latType
        row.setText(2, element.getSource().getName());//source
        //-------------------------------------------------------------------
        //        //1.ֻ����ɫ
        //        if (element.readOnly() == true) {
        //            cell.setBackground(ColorUtils.getColor4ReadOnly());
        //            return;
        //        }
        //2.�½�
        if (element.isNew() == true) {
            cell.setBackground(ColorUtils.getColor4New());
            return;
        }
        //3.��Դ��ɫ
        FromType formType = element.getSource().getType();
        if (formType == FromType.DB) {
            cell.setBackground(ColorUtils.getColor4DB());
            row.setImage(2, ResourceManager.getPluginImage("org.eclipse.datatools.connectivity.ui", "/icons/jdbc_16.gif"));//type
        } else if (formType == FromType.JAR) {
            cell.setBackground(ColorUtils.getColor4Jar());
            row.setImage(2, ResourceManager.getPluginImage("org.eclipse.jdt.ui", "/icons/full/obj16/library_obj.gif"));//type
        } else if (formType == FromType.Source) {
            cell.setBackground(ColorUtils.getColor4Source());
            row.setImage(2, ResourceManager.getPluginImage("org.eclipse.jdt.ui", "/icons/full/obj16/packagefolder_obj.gif"));//type
        }
        //4.�޸Ĺ���
        if (element.isPropertyChanged() == true)
            cell.setBackground(ColorUtils.getColor4Changed());
        //5.����������޸�
        if (element.isCodeChanged() == true)
            row.setBackground(0, ColorUtils.getColor4Changed2());//code
        if (element.isLatTypeChanged() == true)
            row.setBackground(1, ColorUtils.getColor4Changed2());//latType
        //6.ɾ������Ŀ
        FontData fontData = cell.getFont().getFontData()[0];
        if (element.isDelete() == true) {
            cell.setFont(SWTResourceManager.getFont(fontData.getName(), fontData.getHeight(), fontData.getStyle() | SWT.BOLD | SWT.ITALIC));
            cell.setBackground(ColorUtils.getColor4Delete());
        } else
            cell.setFont(SWTResourceManager.getFont(fontData.getName(), fontData.getHeight(), 0));
    }
    //
    //
    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        /*A-׼����������*/
        ConstModel modelObject = ConstModelSet.getActivateModel();
        if (modelObject == null)
            return;
        ConstGroup currentGroup = modelObject.group((String) newInput);
        if (currentGroup == null)
            currentGroup = ConstModelSet.getActivateModel().getGroups().get(0);//���з���
        if (currentGroup.constList().size() > 0)
            viewer.setSelection(new TreeSelection(new TreePath(new Object[] { currentGroup.constList().get(0) })));
        ((TreeViewer) viewer).refresh();
    }
    /**�������ͻ�ȡ���ڵ�*/
    @Override
    public ConstBeanBridge[] getElements(Object inputElement) {
        /*A-׼����������*/
        ConstGroup currentGroup = ConstModelSet.getActivateModel().group((String) inputElement);
        List<ConstGroup> groupList = null;
        if (currentGroup == null)
            groupList = ConstModelSet.getActivateModel().getGroups();//���з���
        else
            groupList = Arrays.asList(currentGroup);//ѡ������
        /*B-����ת��*/
        ArrayList<ConstBeanBridge> ceList = new ArrayList<ConstBeanBridge>();
        for (ConstGroup ccg : groupList) {
            List<ConstBeanBridge> constBeanList = ccg.constList();
            if (constBeanList != null)
                for (ConstBeanBridge cb : constBeanList)
                    ceList.add(cb);
        }
        /*C-*/
        return ceList.toArray(new ConstBeanBridge[ceList.size()]);
    }
    /**��ȡ�ӽڵ�����*/
    @Override
    public ConstBeanBridge[] getChildren(Object parentElement) {
        ConstBeanBridge constElementBridge = (ConstBeanBridge) parentElement;
        List<ConstBeanBridge> childrenList = constElementBridge.getChildren();
        return childrenList.toArray(new ConstBeanBridge[childrenList.size()]);
    }
    @Override
    public boolean hasChildren(Object element) {
        return true;
    }
    @Override
    public ConstBeanBridge getParent(Object element) {
        ConstBeanBridge constElementBridge = (ConstBeanBridge) element;
        return constElementBridge.getParent();
    }
}