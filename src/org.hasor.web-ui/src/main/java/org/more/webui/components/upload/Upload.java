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
package org.more.webui.components.upload;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.more.webui.component.UIComponent;
import org.more.webui.component.support.NoState;
import org.more.webui.component.support.UICom;
import org.more.webui.component.values.MethodExpression;
import org.more.webui.components.UIInput;
import org.more.webui.context.ViewContext;
import org.more.webui.event.Event;
import org.more.webui.event.EventListener;
/**
 * <b>����</b>���ļ��ϴ��齨��
 * <br><b>�齨����</b>��ui_Upload
 * <br><b>��ǩ</b>��@ui_Upload
 * <br><b>������¼�</b>��OnUpLoad
 * <br><b>��Ⱦ��</b>��{@link UploadRender}
 * @version : 2012-5-15
 * @author ������ (zyc@byshell.org)
 */
@UICom(tagName = "ui_Upload", renderType = UploadRender.class)
public class Upload extends UIInput {
    /**ͨ�����Ա�*/
    public enum Propertys {
        /**�����ϴ���action��ַ����������˸�����bizActionEL���Ի�ʧЧ����R��*/
        uploadAction,
        /**�ļ��ϴ�����ʱĿ¼��Ĭ��Ŀ¼��_uploadFileTempDir��-����*/
        uploadTempDir,
        /**�����ϴ����ڴ滺���С��Ĭ��10MB��-����*/
        uploadSizeThreshold,
        /**Ĭ��������ļ������⣨RW��*/
        allowFiles,
        /**Ĭ��������ļ�����˵����RW��*/
        allowFilesDesc,
        /**Ĭ������Ĵ�С������1MB��RW��*/
        allowSize,
        /**�ϴ���ť��RW��*/
        buttonWidth,
        /**�ϴ���ť�ߣ�RW��*/
        buttonHeight,
        /**��ť�ϵ�ͼƬ��RW��*/
        buttonImage,
        /**�Ƿ�������ļ��ϴ���Ĭ�ϲ�����RW��*/
        allowMulti,
        /**����˴����������������uploadAction������Ի�ʧЧ����-��*/
        bizActionEL,
        /**�Ƿ���ʾ��������RW��*/
        showProgress,
    }
    @Override
    public String getComponentType() {
        return "ui_Upload";
    }
    @Override
    protected void initUIComponent(ViewContext viewContext) {
        super.initUIComponent(viewContext);
        this.setPropertyMetaValue(Propertys.uploadAction.name(), null);
        this.setPropertyMetaValue(Propertys.uploadTempDir.name(), "/_uploadFileTempDir");
        this.setPropertyMetaValue(Propertys.uploadSizeThreshold.name(), 10 * 1024 * 1024);
        this.setPropertyMetaValue(Propertys.allowFiles.name(), "*");
        this.setPropertyMetaValue(Propertys.allowFilesDesc.name(), "ALL Files");
        this.setPropertyMetaValue(Propertys.allowSize.name(), "10MB");
        this.setPropertyMetaValue(Propertys.buttonWidth.name(), 65);
        this.setPropertyMetaValue(Propertys.buttonHeight.name(), 29);
        this.setPropertyMetaValue(Propertys.buttonImage.name(), null);
        this.setPropertyMetaValue(Propertys.allowMulti.name(), false);
        this.setPropertyMetaValue(Propertys.bizActionEL.name(), null);
        this.setPropertyMetaValue(Propertys.showProgress.name(), true);
        this.addEventListener(SWFUpload_Event_OnUpLoad.OnUpLoad, new SWFUpload_Event_OnUpLoad());
    }
    public String getUploadAction() {
        return this.getProperty(Propertys.uploadAction.name()).valueTo(String.class);
    }
    @NoState
    public void setUploadAction(String uploadAction) {
        this.getProperty(Propertys.uploadAction.name()).value(uploadAction);
    }
    @NoState
    public String getUploadTempDir() {
        return this.getProperty(Propertys.uploadTempDir.name()).valueTo(String.class);
    }
    @NoState
    public void setUploadTempDir(String uploadTempDir) {
        this.getProperty(Propertys.uploadTempDir.name()).value(uploadTempDir);
    }
    @NoState
    public int getUploadSizeThreshold() {
        return this.getProperty(Propertys.uploadSizeThreshold.name()).valueTo(Integer.TYPE);
    }
    @NoState
    public void setUploadSizeThreshold(int uploadSizeThreshold) {
        this.getProperty(Propertys.uploadSizeThreshold.name()).value(uploadSizeThreshold);
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
    public String getButtonImage() {
        return this.getProperty(Propertys.buttonImage.name()).valueTo(String.class);
    }
    public void setButtonImage(String buttonImage) {
        this.getProperty(Propertys.buttonImage.name()).value(buttonImage);
    }
    public boolean getAllowMulti() {
        return this.getProperty(Propertys.allowMulti.name()).valueTo(Boolean.TYPE);
    }
    public void setAllowMulti(boolean allowMulti) {
        this.getProperty(Propertys.allowMulti.name()).value(allowMulti);
    }
    public boolean getShowProgress() {
        return this.getProperty(Propertys.showProgress.name()).valueTo(Boolean.TYPE);
    }
    public void setShowProgress(boolean showProgress) {
        this.getProperty(Propertys.showProgress.name()).value(showProgress);
    }
    @NoState
    public void setBizActionEL(String bizActionEL) {
        this.getProperty(Propertys.bizActionEL.name()).value(bizActionEL);
    }
    @NoState
    public String getBizActionEL() {
        return this.getProperty(Propertys.bizActionEL.name()).valueTo(String.class);
    }
    private MethodExpression onBizActionExp = null;
    public MethodExpression getOnBizActionExpression() {
        if (this.onBizActionExp == null) {
            String onBizActionExpString = this.getBizActionEL();
            if (onBizActionExpString == null || onBizActionExpString.equals("")) {} else
                this.onBizActionExp = new MethodExpression(onBizActionExpString);
        }
        return this.onBizActionExp;
    }
}
/**������OnUpLoad�¼���EL����*/
class SWFUpload_Event_OnUpLoad implements EventListener {
    public static Event OnUpLoad = Event.getEvent("OnUpLoad");
    public void onEvent(Event event, UIComponent component, ViewContext viewContext) throws Throwable {
        Upload swfUpload = (Upload) component;
        HttpServletRequest httpRequest = viewContext.getHttpRequest();
        ServletContext servletContext = httpRequest.getSession(true).getServletContext();
        if (ServletFileUpload.isMultipartContent(httpRequest) == false)
            return;// ������������Ƿ����multipart�����ݡ�
        try {
            //1.׼���ϴ�����
            DiskFileItemFactory factory = new DiskFileItemFactory();// Ϊ�����󴴽�һ��DiskFileItemFactory����ͨ��������������ִ�н��������еı���Ŀ��������һ��List�С�
            ServletFileUpload upload = new ServletFileUpload(factory);
            String charset = httpRequest.getCharacterEncoding();
            if (charset != null)
                upload.setHeaderEncoding(charset);
            factory.setSizeThreshold(swfUpload.getUploadSizeThreshold());
            File uploadTempDir = new File(servletContext.getRealPath(swfUpload.getUploadTempDir()));
            if (uploadTempDir.exists() == false)
                uploadTempDir.mkdirs();
            factory.setRepository(uploadTempDir);
            //2.��������ļ�
            List<FileItem> itemList = upload.parseRequest(httpRequest);
            List<FileItem> finalList = new ArrayList<FileItem>();
            Map<String, String> finalParam = new HashMap<String, String>();
            for (FileItem item : itemList)
                if (item.isFormField() == false)
                    finalList.add(item);
                else
                    finalParam.put(new String(item.getFieldName().getBytes("iso-8859-1")), new String(item.getString().getBytes("iso-8859-1")));
            //3.֪ͨҵ��ϵͳ
            Object returnData = null;
            MethodExpression onBizActionExp = swfUpload.getOnBizActionExpression();
            if (onBizActionExp != null) {
                HashMap<String, Object> upObject = new HashMap<String, Object>();
                upObject.put("files", finalList);
                upObject.put("params", finalParam);
                HashMap<String, Object> upParam = new HashMap<String, Object>();
                upParam.put("up", upObject);
                returnData = onBizActionExp.execute(component, viewContext, upParam);
            }
            //4.��������
            for (FileItem item : itemList)
                try {
                    item.delete();
                } catch (Exception e) {}
            //5.��Ӧ������
            viewContext.sendObject(returnData);
        } catch (Exception e) {
            viewContext.sendError(e);
        }
    }
};