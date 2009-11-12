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
package org.more.beans.core.injection;
import org.more.beans.BeanFactory;
import org.more.beans.info.BeanDefinition;
/**
 * ����ע�����ӿڣ���ʹ��factģʽע��bean����ʱ��more.beans������һ��ר�Ÿ���ע�����Ե�����ע������
 * �������ע�����Ǽ̳��˼���ע�����Ե�bean���ͣ�ͬʱʵ���˸ýӿڣ���˱���ȷ����factģʽ��bean�в��ܴ��ڸýӿڵķ������߱�־ʵ�ָýӿڡ�
 * FactIoc��more.beans����fact��ʽע��Ľӿڣ��ýӿڿ�����Ա����Ӵ�����������beanҪ��fact��ʽע��ʱmore.beans���Զ���������<br/>
 * ����bean����ʱʹ��fact��ʽע����Ȼ����ǿ������ת��bean��������ΪFactIoc����Ϊmore.beansʹ�ô���ʽִ�е�ע��ԭʼ���󲻻ᱻ�ƻ���
 * Date : 2009-11-8
 * @author ������
 */
public interface FactIoc {
    /** �÷�����ʵ�ַ��������ɵģ����ɵĴ���Ϊԭʼ��get/set�������á� */
    public void ioc(Object obj, Object[] params, BeanFactory context, BeanDefinition definition) throws Throwable;
}