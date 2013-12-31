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
package net.hasor.core.setting;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.more.util.map.DecSequenceMap;
/***
 * ����֧�֡�
 * @version : 2013-9-8
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractBaseSettings extends AbstractSettings {
    private Map<String, Map<String, Object>> namespaceSettingsMap = new HashMap<String, Map<String, Object>>();
    private DecSequenceMap<String, Object>   mergeSettingsMap     = new DecSequenceMap<String, Object>();
    //
    /**�ֱ𱣴�ÿ�������ռ��µ�������ϢMap*/
    protected final Map<String, Map<String, Object>> getNamespaceSettingMap() {
        return namespaceSettingsMap;
    }
    protected final DecSequenceMap<String, Object> getSettingsMap() {
        return mergeSettingsMap;
    }
    /**����Ѿ�װ�ص��������ݡ�*/
    protected void cleanData() {
        this.getNamespaceSettingMap().clear();
        this.getSettingsMap().removeAllMap();
    }
    //
    /**��ȡָ��ĳ���ض������ռ��µ�Settings�ӿڶ���*/
    public String[] getSettingArray() {
        Set<String> nsSet = this.getNamespaceSettingMap().keySet();
        return nsSet.toArray(new String[nsSet.size()]);
    }
    //
    /**��ȡָ��ĳ���ض������ռ��µ�Settings�ӿڶ���*/
    public final AbstractSettings getSetting(String namespace) {
        final AbstractSettings setting = this;
        final Map<String, Object> data = this.getNamespaceSettingMap().get(namespace);
        if (data == null)
            return null;
        return new AbstractSettings() {
            public void refresh() throws IOException {
                throw new UnsupportedOperationException();
            }
            public AbstractSettings getSetting(String namespace) {
                return setting.getSetting(namespace);
            }
            public String[] getSettingArray() {
                return setting.getSettingArray();
            }
            public Map<String, Object> getSettingsMap() {
                return data;
            }
        };
    }
    public void refresh() throws IOException {}
}