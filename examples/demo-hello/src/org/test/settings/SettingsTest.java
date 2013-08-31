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
package org.test.settings;
import java.io.IOException;
import org.hasor.context.Settings;
import org.hasor.context.XmlProperty;
import org.hasor.context.core.DefaultAppContext;
import org.more.util.StringUtils;
public class SettingsTest {
    public static void main(String[] args) throws IOException {
        DefaultAppContext context = new DefaultAppContext();
        Settings setting = context.getSettings();
        //
        System.out.println(setting.getString("myProject.name"));//��Ŀ����
        System.out.println(setting.getString("myProject"));//��Ŀ��Ϣ
        System.out.println(setting.getInteger("userInfo.age"));//����
        /*��Ȼ���ڵ㲻����Key/Valueת�����ǿ��Ի�ȡ����*/
        XmlProperty xmlNode = setting.getXmlProperty("config");
        for (XmlProperty node : xmlNode.getChildren()) {
            if ("userInfo".equals(node.getName())) {
                if ("001".equals(node.getAttributeMap().get("id"))) {
                    System.out.println(node);
                }
            }
        }
    }
}