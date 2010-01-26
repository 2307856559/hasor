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
package org.more.util.attribute;
import java.util.Properties;
/**
 * ��������ת���ӿڣ�������Խӿ�ʵ����ʵ���˸ýӿھ;߱����������������ͽ���ת���Ĺ��ܡ�
 * @version 2009-5-1
 * @author ������ (zyc@byshell.org)
 */
public interface IAttTransform {
    /**
     * ��ȡ���Խӿڵ�Properties��ʽ����IAttribute�ӿ�ת����Properties����ʱ���IAttribute
     * �ӿڶ����д���ֵΪ�յ���������ת�������к��ԡ�
     * @return ���ص�ǰ���Խӿڵ�Properties��ʽ
     */
    public Properties toProperties();
    /**
     * ��Properties��ʽ����������ת����IAttribute�ӿ���ʽ��ע�⣺����Properties�������ṩ����
     * Object���Ͷ�IAttribute�ӿ��ṩ�������ͻ����������ͣ������ʹ�ø÷���ʱ��Ҫע������ת�����⡣
     * ���򽫻���������ת���쳣���������ת���쳣�ĳ��������ǽ����ͻ���������ΪObject��
     * @param prop Properties��ʽ����������
     */
    public void fromProperties(Properties prop);
}