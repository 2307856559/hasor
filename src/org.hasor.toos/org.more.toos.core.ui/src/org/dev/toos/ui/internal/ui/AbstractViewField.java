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
package org.dev.toos.ui.internal.ui;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
/**
 * 
 * @version : 2013-3-20
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractViewField<T> {
    /*�����ֶΣ�ֻ�������˵��ֶβ����յ����������¼�֪ͨ��*/
    private boolean            enable            = true;
    /*�ֶο���������ʾ�ı�ǩ��*/
    private Label              fieldLabel        = null;
    /*�ֶα�ǩ������ʾ���ı����ݡ�*/
    private String             fieldLabelText    = "";
    /*�ֶμ�������*/
    private IViewFieldListener viewFieldListener = null;
    //
    //
    //
    /**���{@link IViewFieldListener}������*/
    public void setViewFieldListener(IViewFieldListener listener) {
        this.viewFieldListener = listener;
    }
    /**ɾ��{@link IViewFieldListener}������*/
    public IViewFieldListener getViewFieldListener() {
        return this.viewFieldListener;
    }
    /**�����ֶ�ֵ�仯�¼���*/
    protected void fireOnFieldChanged() {
        if (this.viewFieldListener != null)
            this.viewFieldListener.viewFieldChanged(this);
    }
    /**�ֶεõ����㣬��������չ�÷���ʱ�������ָ���Ŀؼ��õ����㡣*/
    public boolean setFocus() {
        return false;
    }
    /**��ȡViewField�������Label����*/
    protected Label getLabelControl(Composite parent) {
        if (this.fieldLabel == null) {
            Assert.isNotNull(parent, "uncreated control requested with composite null");
            this.fieldLabel = new Label(parent, SWT.LEFT | SWT.WRAP);
            this.fieldLabel.setFont(parent.getFont());
            this.fieldLabel.setEnabled(this.enable);
            if (this.fieldLabelText != null && !"".equals(this.fieldLabelText)) {
                this.fieldLabel.setText(this.fieldLabelText);
            } else {
                // XXX: to avoid a 16 pixel wide empty label - revisit
                this.fieldLabel.setText("."); //$NON-NLS-1$
                this.fieldLabel.setVisible(false);
            }
        }
        return this.fieldLabel;
    }
    /**��ȡViewField�����enable���ԡ�*/
    public final boolean isEnable() {
        return this.enable;
    }
    /**����ViewField�����enable���ԡ�*/
    public final void setEnable(boolean enabled) {
        if (enabled != this.enable) {
            this.enable = enabled;
            updateEnableState();
        }
    }
    /** �յ�֪ͨViewField��enable״̬���Ա��޸ġ� */
    protected void updateEnableState() {
        if (this.fieldLabel != null)
            this.fieldLabel.setEnabled(this.enable);
    }
    /*-----------------------------------------------------------------*/
    /**��ȡ�ֶ�ֵ*/
    public abstract T getFieldValue();
    /**��ȡ���齨�ᴴ�����ٸ�Controls����*/
    public int getNumberOfControls() {
        return 1;
    }
    /**���齨��䵽Ŀ�������С�*/
    public Control[] doFillIntoGrid(Composite parentComposite, int numColumnsCount) {
        assertEnoughColumns(numColumnsCount);
        Label label = getLabelControl(parentComposite);//��ȡ��ǩ����
        label.setLayoutData(gridDataForLabel(numColumnsCount));
        return new Control[] { label };
    }
    /*-----------------------------------------------------------------*/
    /**���ڼ��Ŀ�������������Ŀ�Ƿ�������Ҫ����������������������׳��쳣��*/
    protected final void assertEnoughColumns(int nColumns) {
        Assert.isTrue(nColumns >= getNumberOfControls(), "given number of columns is too small"); //$NON-NLS-1$
    }
    protected static final boolean isOkToUse(Control control) {
        return (control != null) && (Display.getCurrent() != null) && !control.isDisposed();
    }
    /**����Ҫ���span��С����GridData���ֹ�������*/
    protected static GridData gridDataForLabel(int span) {
        GridData gd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
        gd.horizontalSpan = span;
        return gd;
    }
}