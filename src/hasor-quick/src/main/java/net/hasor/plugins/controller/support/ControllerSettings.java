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
package net.hasor.plugins.controller.support;
import java.util.ArrayList;
import java.util.List;
import net.hasor.core.Settings;
import org.more.util.StringUtils;
/**
 * 
 * @version : 2013-5-11
 * @author ������ (zyc@hasor.net)
 */
class ControllerSettings {
    /**action���ý���.*/
    public static final String ActionServlet_Enable       = "hasor-web.controller.enable";
    /**action������.*/
    public static final String ActionServlet_Intercept    = "hasor-web.controller.intercept";
    /**�������Եķ��������ŷָ���鷽��������ע�⣺���������õĺ��Ի�Ӧ�õ�����action��.*/
    public static final String ActionServlet_GlobalIgnore = "hasor-web.controller.globalIgnore";
    //
    private boolean            enable;
    private String             intercept;                                                       //action������.
    private List<String>       ignoreMethod;                                                    //���Եķ���
    //
    public ControllerSettings(Settings settings) {
        this.enable = settings.getBoolean(ActionServlet_Enable, true);
        this.intercept = settings.getString(ActionServlet_Intercept, "*.do");
        this.ignoreMethod = new ArrayList<String>();
        String[] ignoreStrArray = settings.getStringArray(ActionServlet_GlobalIgnore);
        if (ignoreStrArray != null) {
            for (String ignoreStr : ignoreStrArray) {
                if (StringUtils.isBlank(ignoreStr))
                    continue;
                String[] ignoreArray = ignoreStr.split(",");
                for (String str : ignoreArray) {
                    if (StringUtils.isBlank(str))
                        continue;
                    this.ignoreMethod.add(str.trim());
                }
            }
        }
    }
    public boolean isEnable() {
        return enable;
    }
    public String getIntercept() {
        return intercept;
    }
    public List<String> getIgnoreMethod() {
        return ignoreMethod;
    }
}