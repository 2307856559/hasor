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
package org.platform.view.freemarker.support;
import static org.platform.view.freemarker.ConfigurationFactory.FreemarkerConfig_Enable;
import static org.platform.view.freemarker.ConfigurationFactory.FreemarkerConfig_OnError;
import static org.platform.view.freemarker.ConfigurationFactory.FreemarkerConfig_Suffix;
import org.platform.context.SettingListener;
import org.platform.context.Settings;
import com.google.inject.Singleton;
/**
 * ������Ϣ
 * @version : 2013-4-23
 * @author ������ (zyc@byshell.org)
 */
@Singleton
class FreemarkerSettings implements SettingListener {
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
    private boolean     enable  = false;
    private String[]    suffix  = null;
    private OnErrorMode onError = null;
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
    protected void setEnable(boolean enable) {
        this.enable = enable;
    }
    protected void setSuffix(String[] suffix) {
        this.suffix = suffix;
    }
    protected void setOnError(OnErrorMode onError) {
        this.onError = onError;
    }
    @Override
    public void loadConfig(Settings newConfig) {
        this.enable = newConfig.getBoolean(FreemarkerConfig_Enable);
        String suffix = newConfig.getString(FreemarkerConfig_Suffix);
        this.suffix = suffix.split(",");
        for (int i = 0; i < this.suffix.length; i++)
            this.suffix[i] = this.suffix[i].trim();
        this.onError = newConfig.getEnum(FreemarkerConfig_OnError, OnErrorMode.class, OnErrorMode.Warning);
    }
}