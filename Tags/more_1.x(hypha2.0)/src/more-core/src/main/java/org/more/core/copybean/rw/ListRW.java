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
import org.more.core.copybean.PropertyReader;
import org.more.core.copybean.PropertyWrite;
import org.more.util.BeanUtil;
import org.more.util.StringConvertUtil;
/**
 * List���д����ʹ�ø�����Ϊ��д������ʵ�ִ�List�����п������Ի�����List�п������ԡ�<br/>
 * @see {@link #writeProperty(String, List, Object)}<br/>
 * @see {@link #readProperty(String, List)}<br/>
 * @version : 2011-12-24
 * @author ������ (zyc@byshell.org)
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ListRW implements PropertyReader<List>, PropertyWrite<List> {
    public List<String> getPropertyNames(List target) {
        ArrayList<String> names = new ArrayList<String>();
        for (int i = 0; i < names.size(); i++)
            names.add(String.valueOf(i));
        return names;
    }
    public boolean canWrite(String propertyName, List target, Object newValue) {
        return true;
    }
    public boolean canReader(String propertyName, List target) {
        return true;
    }
    /**
     * ����Listִ��д�����ʱ�����ָ����propertyName���ԣ����֣���ô����ӵ�ʱ���д�뵽ָ����λ���ϣ�
     * �������д���λ�ó�����Χ������{@link IndexOutOfBoundsException}�쳣��
     * Integer.MAX_VALUEֵ�ᵼ��׷�Ӳ�����Integer.MIN_VALUEֵ�ᵼ��������ǰ���������
     * ���û��ָ��propertyName���ԣ����֣�����д������ὫnewValueֵ��ӵ�list�С���ʱ���newValue��һ���������ͻ���������ô����ִ��������ӡ�
     */
    public boolean writeProperty(String propertyName, List target, Object newValue) throws IndexOutOfBoundsException {
        if (newValue == null)
            return false;
        if (BeanUtil.isNone(propertyName) == true) {
            if (newValue.getClass().isArray()) {
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
        /**���ﲻʹ�ø�����ʾ����ΪpropertyName��ת����int֮�������һ��������*/
        int setIndex = StringConvertUtil.parseInt(propertyName, Integer.MAX_VALUE);
        if (setIndex < 0)
            throw new IndexOutOfBoundsException();//�����������õķ�Χ��С��
        if (setIndex != Integer.MAX_VALUE && setIndex >= target.size())
            throw new IndexOutOfBoundsException();//�����������õķ�Χ������
        //�������
        if (setIndex == Integer.MAX_VALUE)
            target.add(newValue);//׷��
        if (setIndex == Integer.MIN_VALUE)
            //����û�д���Integer.MIN_VALUE����Ϊ��ֵ�п�����һ������
            target.add(0, newValue);//����
        else
            target.add(setIndex, newValue);//���
        return true;
    }
    /**
     * ����Listִ�ж�ȡ����ʱ�����ָ����propertyName���ԣ����֣���ô�᷵��ָ��λ���ϵ�ֵ��
     * ���������ȡ��λ�ó�����Χ������{@link IndexOutOfBoundsException}�쳣��propertyNameΪ�ջط���list����
     * Integer.MAX_VALUEֵ���ȡ������Integer.MIN_VALUEֵ���ȡβ����
     */
    public Object readProperty(String propertyName, List target) {
        if (BeanUtil.isNone(propertyName) == true)
            return target;
        //
        /**���ﲻʹ�ø�����ʾ����ΪpropertyName��ת����int֮�������һ��������*/
        int getIndex = StringConvertUtil.parseInt(propertyName, Integer.MAX_VALUE);
        if (getIndex < 0)
            throw new IndexOutOfBoundsException();//�����������õķ�Χ��С��
        if (getIndex != Integer.MAX_VALUE && getIndex >= target.size())
            throw new IndexOutOfBoundsException();//�����������õķ�Χ������
        //��ȡ����
        if (target.size() == 0)
            return null;
        if (getIndex == Integer.MAX_VALUE)
            return target.get(target.size() - 1);//β��
        else if (getIndex == Integer.MIN_VALUE)
            return target.get(0);//����
        else
            return target.get(getIndex);//����
    }
    /**֧�ֶ�List�Ķ�д������*/
    public Class<?> getTargetClass() {
        return List.class;
    }
}