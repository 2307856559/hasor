package org.more.webui.web;
/**
 * ������
 * @version : 2012-5-21
 * @author ������ (zyc@byshell.org)
 */
public enum PostFormEnum {
    /**�����¼����齨*/
    PostForm_TargetParamKey("WebUI_PF_Target"),
    //    /**�������¼�*/
    //    PostForm_EventParamKey,
    /**ִ����Ⱦ������*/
    PostForm_RenderParamKey("WebUI_PF_Render"),
    /**�ش�״̬��״̬����*/
    PostForm_StateDataParamKey("WebUI_PF_State"), ;
    //
    //
    //
    //
    private String value = null;
    PostFormEnum(String value) {
        this.value = value;
    }
    public String value() {
        return this.value;
    }
}
