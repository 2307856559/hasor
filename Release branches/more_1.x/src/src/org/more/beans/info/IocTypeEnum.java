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
package org.more.beans.info;
import org.more.beans.core.injection.ExportInjectionProperty;
/**
 * ע������ö�١�������Export��Fact��Ioc��������ע�뷽ʽ��
 * <br/><br/>Export��ʽ��<br/>ʹ��Export��ʽ����ʹ������Ա��������ע����̣���Export��ʽ��more.beans������������κ�ע�������
 * more.bean��ί��{@link ExportInjectionProperty}�ӿڽ���ע�����󡣾��������Ե�ע�������ʵ��{@link ExportInjectionProperty}�ӿڡ�
 * ����ⲿע�봦�����Ϊ����Export������ע������
 * <br/><br/>Fact��ʽ��<br/>��Fact��ʽ���״�����װ����ʱmore.beans������һ������ע���࣬����ʹ���������ע�������ע�롣
 * �������ע����Ĵ�������ȫ��more.classcode�������ɣ����ɵ������ʹ����ԭʼ�ķ�ʽ��bean����get/set��
 * Fact��ʽ�Ƚ�Ioc��ʽʡ���˷���ע��Ĺ��̣�Fact����ֱ�ӵ��÷�����������ע�룬�Ӷ����������ٶȡ���������
 * fact��ʽ�������ٶ���ԭʼget/set�����ٶ��൱�ӽ���100��ν��л�����������ע���ٶ�ֻ���15�������
 * ��1000���ע�������get/set������312�����fact������843���룬ioc��ʽ����Ҫ����18.3�롣
 * �����֤����Fact��ʽ�»��кܺõ�����ע������Ч�ʣ�����FactҲ���ÿ��Ҫ��Fact��bean����һ��ע������
 * ��Ҳ����˵��fact��ʽ�»��ioc��ʽ���������ڴ����ġ����ɵ�ע������������{@link BeanDefinition}�������С�
 * ֻ��{@link BeanDefinition}���󱻻��������������Ч�ʣ�����fact��Ч�ʿ���ԶԶ����ioc��
 * <br/><br/>Ioc��ʽ��<br/>��ͳ��ע�뷽ʽ��ʹ��java.lang.reflect���е�����з��������ʵ������ע�롣Ioc��ʽ�Ƚ�Fact��ʽ����Ч��Ҫ���Ķࡣ<br/>
 * Date : 2009-11-9
 * @author ������
 */
public enum IocTypeEnum {
    /** Export��ʽע�롣 */
    Export,
    /** Ioc��ʽע�롣 */
    Ioc,
    /** Fact��ʽע�롣 */
    Fact
}