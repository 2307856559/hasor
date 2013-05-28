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
package org.platform.action.support;
import static org.platform.action.ActionConfig.ActionServlet_Enable;
import static org.platform.action.ActionConfig.ActionServlet_Intercept;
import static org.platform.action.ActionConfig.ActionServlet_Mode;
import org.platform.context.SettingListener;
import org.platform.context.Settings;
/**
 * 
 * @version : 2013-5-11
 * @author ������ (zyc@byshell.org)
 */
public class ActionSettings implements SettingListener {
    /**Action���ܵĹ���ģʽ��*/
    public static enum ActionWorkMode {
        /**��������rest�����*/
        RestOnly,
        /**ͨ������servletת��*.do������*/
        ServletOnly,
        /**ͬʱ������RestOnly��ServletOnly����ģʽ�¡�����ServletOnlyģʽ���ȡ�*/
        Both
    }
    private boolean        enable    = true; //�Ƿ�����Action����.
    private String         intercept = null; //action������.
    private ActionWorkMode mode      = null; //����ģʽ
    @Override
    public void loadConfig(Settings newConfig) {
        this.enable = newConfig.getBoolean(ActionServlet_Enable, true);
        this.intercept = newConfig.getString(ActionServlet_Intercept, "*.do");
        this.mode = newConfig.getEnum(ActionServlet_Mode, ActionWorkMode.class, ActionWorkMode.ServletOnly);
    }
    public boolean isEnable() {
        return enable;
    }
    public String getIntercept() {
        return intercept;
    }
    public ActionWorkMode getMode() {
        return mode;
    }
}