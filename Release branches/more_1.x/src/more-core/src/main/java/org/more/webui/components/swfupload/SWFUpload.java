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
package org.more.webui.components.swfupload;
import org.more.webui.context.ViewContext;
import org.more.webui.support.UICom;
import org.more.webui.support.UIComponent;
/**
 * �ļ��ϴ��齨
 * @version : 2012-5-15
 * @author ������ (zyc@byshell.org)
 */
@UICom(tagName = "ui_SWFUpload")
public class SWFUpload extends UIComponent {
    /**ͨ�����Ա�*/
    public enum Propertys {
        /**�ϴ�����Ŀ�ĵ�*/
        uploadDir,
        /**Ĭ��������ļ�������*/
        allowFiles,
        /**Ĭ��������ļ�����˵��*/
        allowFilesDesc,
        /**Ĭ������Ĵ�С������1MB*/
        allowSize,
        /**�ϴ���ť��*/
        buttonWidth,
        /**�ϴ���ť��*/
        buttonHeight,
        /**�ϴ���ϢԪ��ID*/
        infoTargetID,
        /**ѡ���ļ���ťԪ��ID*/
        selectButtonID,
        /**flash���ϴ���ť��ʾ��html��λ�ã������Ƶ�Ԫ�ػᱻ�滻��objectԪ��*/
        uploadButtonID,
        /**ȡ���ϴ���ťԪ��ID*/
        cancelButtonID,
    }
    @Override
    public String getComponentType() {
        return "ui_SWFUpload";
    }
    @Override
    protected void initUIComponent(ViewContext viewContext) {
        super.initUIComponent(viewContext);
        this.setProperty(Propertys.uploadDir.name(), null);
        this.setProperty(Propertys.allowFiles.name(), "*");
        this.setProperty(Propertys.allowFilesDesc.name(), "ALL Files");
        this.setProperty(Propertys.allowSize.name(), "10MB");
        this.setProperty(Propertys.buttonWidth.name(), 62);
        this.setProperty(Propertys.buttonHeight.name(), 21);
        this.setProperty(Propertys.infoTargetID.name(), null);
        //
        this.setProperty(Propertys.selectButtonID.name(), viewContext.newClientID());
        this.setProperty(Propertys.uploadButtonID.name(), viewContext.newClientID());
        this.setProperty(Propertys.cancelButtonID.name(), viewContext.newClientID());
    }
    public String getUploadDir() {
        return this.getProperty(Propertys.uploadDir.name()).valueTo(String.class);
    }
    public void setUploadDir(String uploadDir) {
        this.getProperty(Propertys.uploadDir.name()).value(uploadDir);
    }
    public String getAllowFilesDesc() {
        return this.getProperty(Propertys.allowFilesDesc.name()).valueTo(String.class);
    }
    public void setAllowFilesDesc(String allowFilesDesc) {
        this.getProperty(Propertys.allowFilesDesc.name()).value(allowFilesDesc);
    }
    public String getAllowFiles() {
        return this.getProperty(Propertys.allowFiles.name()).valueTo(String.class);
    }
    public void setAllowFiles(String allowFiles) {
        this.getProperty(Propertys.allowFiles.name()).value(allowFiles);
    }
    public String getAllowSize() {
        return this.getProperty(Propertys.allowSize.name()).valueTo(String.class);
    }
    public void setAllowSize(String allowSize) {
        this.getProperty(Propertys.allowSize.name()).value(allowSize);
    }
    public int getButtonWidth() {
        return this.getProperty(Propertys.buttonWidth.name()).valueTo(Integer.TYPE);
    }
    public void setButtonWidth(int buttonWidth) {
        this.getProperty(Propertys.buttonWidth.name()).value(buttonWidth);
    }
    public int getButtonHeight() {
        return this.getProperty(Propertys.buttonHeight.name()).valueTo(Integer.TYPE);
    }
    public void setButtonHeight(int buttonHeight) {
        this.getProperty(Propertys.buttonHeight.name()).value(buttonHeight);
    }
    public String getInfoTargetID() {
        return this.getProperty(Propertys.infoTargetID.name()).valueTo(String.class);
    }
    public void setInfoTargetID(String infoTargetID) {
        this.getProperty(Propertys.infoTargetID.name()).value(infoTargetID);
    }
    public String getSelectButtonID() {
        return this.getProperty(Propertys.selectButtonID.name()).valueTo(String.class);
    }
    public void setSelectButtonID(String selectButtonID) {
        this.getProperty(Propertys.selectButtonID.name()).value(selectButtonID);
    }
    public String getUploadButtonID() {
        return this.getProperty(Propertys.uploadButtonID.name()).valueTo(String.class);
    }
    public void setUploadButtonID(String uploadButtonID) {
        this.getProperty(Propertys.uploadButtonID.name()).value(uploadButtonID);
    }
    public String getCancelButtonID() {
        return this.getProperty(Propertys.cancelButtonID.name()).valueTo(String.class);
    }
    public void setCancelButtonID(String cancelButtonID) {
        this.getProperty(Propertys.cancelButtonID.name()).value(cancelButtonID);
    }
}