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
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.more.core.copybean.PropertyReader;
import org.more.core.copybean.PropertyWrite;
/**
 * Set���д����ʹ�ø�����Ϊ��д������ʵ�ִ�Set�����п������Ի�����Set�п������ԡ�
 * set��֧��propertyName���ԡ�
 * @version 2009-5-15
 * @author ������ (zyc@byshell.org)
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SetRW implements PropertyReader<Set>, PropertyWrite<Set> {
    public List<String> getPropertyNames(Set target) {
        return new ArrayList<String>();
    }
    public boolean canWrite(String propertyName, Set target, Object newValue) {
        return true;
    }
    public boolean canReader(String propertyName, Set target) {
        return true;
    }
    public boolean writeProperty(String propertyName, Set target, Object newValue) {
        if (newValue == null)
            return false;
        else if (newValue.getClass().isArray()) {
            //�������
            Object[] objs = (Object[]) newValue;
            for (Object obj : objs)
                target.add(obj);
        } else if (newValue instanceof Collection)
            //���ϲ��� 
            target.addAll((Collection) newValue);
        else
            target.add(newValue);
        return true;
    }
    /**�����䱾��*/
    public Object readProperty(String propertyName, Set target) {
        return target;
    }
    /**֧�ֶ�Set�Ķ�д������*/
    public Class<?> getTargetClass() {
        return Set.class;
    }
}