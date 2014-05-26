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
package net.hasor.core.binder;
import java.util.Map;
/**
 * ע�ᵽ Hasor �� Bean ��Ԫ��Ϣ��
 * @version : 2013-5-6
 * @author ������ (zyc@hasor.net)
 */
class BeanInfoData implements BeanInfo {
    private String[]            names     = null;
    private String              referID   = null;
    private Class<?>            beanClass = null;
    private Map<String, Object> property  = null;
    //
    public BeanInfoData(String[] names, String referID, Class<?> beanClass, Map<String, Object> property) {
        this.names = names;
        this.referID = referID;
        this.beanClass = beanClass;
        this.property = property;
    }
    /**��ȡbean������*/
    public String[] getNames() {
        return this.names;
    }
    public String getReferID() {
        return this.referID;
    }
    /**��ȡbean������*/
    public Class<?> getType() {
        return this.beanClass;
    }
    public Map<String, Object> propertyMap() {
        return this.property;
    }
}