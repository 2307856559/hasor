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
package org.more.core.copybean.rw;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.more.core.copybean.PropertyReader;
import org.more.core.copybean.PropertyWrite;
/**
 * Map���д����ʹ�ø�����Ϊ��д������ʵ�ִ�Map�����п������Ի�����Map�п������ԡ�
 * @version : 2011-12-24
 * @author ������ (zyc@byshell.org)
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MapRW implements PropertyReader<Map>, PropertyWrite<Map> {
    public List<String> getPropertyNames(Map target) {
        return new ArrayList<String>(target.keySet());
    };
    public boolean canWrite(String propertyName, Map target, Object newValue) {
        return true;
    }
    public boolean canReader(String propertyName, Map target) {
        return true;
    }
    public boolean writeProperty(String propertyName, Map target, Object newValue) {
        target.put(propertyName, newValue);
        return true;
    }
    public Object readProperty(String propertyName, Map target) {
        return target.get(propertyName);
    }
    /**֧��Map�Ķ�д������*/
    public Class<?> getTargetClass() {
        return Map.class;
    }
}