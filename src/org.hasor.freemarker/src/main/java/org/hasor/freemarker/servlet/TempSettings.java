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
package org.hasor.freemarker.servlet;
import java.util.ArrayList;
import org.hasor.context.Settings;
import org.more.util.StringUtils;
/**
 * ������Ϣ
 * @version : 2013-4-23
 * @author ������ (zyc@byshell.org)
 */
public class TempSettings /*implements HasorSettingListener*/{
    public static enum OnErrorMode {
        /**�׳��쳣*/
        ThrowError,
        /**��ӡ������̨����־*/
        PrintOnConsole,
        /**���ԣ���������һ��������Ϣ*/
        Warning,
        /**��ӡ��ҳ��*/
        PrintOnPage
    };
    private boolean     enable      = false;
    private String      contentType = null;
    private String[]    suffix      = null;
    private OnErrorMode onError     = null;
    //
    public boolean isEnable() {
        return enable;
    }
    public String[] getSuffix() {
        return suffix;
    }
    public OnErrorMode getOnError() {
        return onError;
    }
    public String getContentType() {
        return contentType;
    }
    protected void setContentType(String contentType) {
        this.contentType = contentType;
    }
    protected void setEnable(boolean enable) {
        this.enable = enable;
    }
    protected void setSuffix(String[] suffix) {
        this.suffix = suffix;
    }
    protected void setOnError(OnErrorMode onError) {
        this.onError = onError;
    }
    public void onLoadConfig(Settings newConfig) {
        this.enable = newConfig.getBoolean("freemarker.servlet.enable");
        String[] suffixArray = newConfig.getStringArray("freemarker.servlet.suffixSet");
        this.contentType = newConfig.getString("freemarker.servlet.contentType", "text/html");
        //
        ArrayList<String> suffixList = new ArrayList<String>();
        for (String sufItem : suffixArray) {
            if (StringUtils.isBlank(sufItem))
                continue;
            String[] suffix = sufItem.split(",");
            for (String s : suffix) {
                if (StringUtils.isBlank(s))
                    continue;
                suffixList.add(s);
            }
        }
        this.suffix = suffixList.toArray(new String[suffixList.size()]);
        this.onError = newConfig.getEnum("freemarker.servlet.onError", OnErrorMode.class, OnErrorMode.Warning);
    }
}