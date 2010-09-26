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
package org.more.beans.define;
/**
 * ע������ö�٣�������Export��Fact��Ioc��EL��Auto��������ע�뷽ʽ��<br/><br/>
 * <b>Export��ʽ��</b>ʹ��Export��ʽ����ʹ������Ա��������ע����̣���Export��ʽ��more.beans������������κ�ע�������
 * more.bean��ί��{@link ExportIoc}�ӿڽ���ע�����󡣾��������Ե�ע�������ʵ��{@link ExportIoc}�ӿڡ�
 * ����ⲿע�봦�����Ϊ����Export������ע������<br/>
 * <b>Fact��ʽ��</b>��Fact��ʽ���״�����װ����ʱmore.beans������һ������ע����������ʹ���������ע�������ע�롣
 * ����ע������ʹ��get/set��ʽ����ע�롣�Ӷ������˷���ע����������Ĺ��̡����ڻ������Ͷ��Ը÷�ʽ���������˻����������Ե�ע��ʱ�䡣<br/>
 * <b>Ioc��ʽ��</b>��ͳ��ע�뷽ʽ��ʹ��java.lang.reflect���е�����з��������ʵ������ע�롣<br/>
 * <b>EL��ʽ��</b>����beans2.0������һ��ע�뷽ʽ�������Ե� ע��ʹ�õ���Ognl�������ʵ�֡�<br/>
 * <b>Auto��ʽ(Ĭ��)��</b>����Ĭ�ϵ�ע�뷽ʽ��ϵͳ�������Ҫ�Զ�ѡ������4��ע�뷽ʽ��
 * @version 2010-9-18
 * @author ������ (zyc@byshell.org)
 */
public enum IocTypeEnum {
    /**����ע��Ĺ��̵�{@link ExportIoc}�ӿ���ע������ɿ�����Ա�ֶ���ɡ�*/
    Export,
    /**����ע�룬����ע�������һ������ע����ͨ�������ִ��ע����̡�*/
    Fact,
    /**��ͳ��ע�뷽ʽ��ʹ��java.lang.reflect���е�����з��������ʵ������ע�롣*/
    Ioc,
    /**ʹ�õ���Ognl���ʽ����ʵ��ע�롣*/
    EL,
    /**�Զ�ѡ��ע����ʽ��*/
    Auto,
}