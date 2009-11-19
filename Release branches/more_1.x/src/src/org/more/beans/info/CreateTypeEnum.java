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
/**
 * CreateTypeEnumö�ٶ����ˡ�Factory��New�����ִ�����ʽ��
 * Factory��ʽ��<br/>
 * ʹ�ù�����ʽ����һ��Bean�������ַ�ʽ��Ҫָ���������Լ�����������ز�����Factory��ʽ��aop�������ص��ķ�����classcode���ߵ�
 * {@link org.more.core.classcode.ClassEngine.BuilderMode#Propxy Propxt}��ʽ��ͬ��<br/>�÷�ʽ��Ҫbeans����createType����ΪFactory��
 * �����ṩ���󴴽�����ʱ�������Ĺ��������Լ��������������bean������aop���߸��ӽӿ�ʵ���򹤳�bean���صĶ����������ϵͳ����һ�������������࣬
 * �����Ծ�̬����ʽ�ڴ�������ʵ��aop�Լ����ӽӿ�ʵ�֡���ʱaop�������ص��ķ�����classcode���ߵ�
 * {@link org.more.core.classcode.ClassEngine.BuilderMode#Propxy Propxt}��ʽ��ͬ��˽�кͱ������������ܵ�aopӰ�죬�����new��ʽ������ܵ�Ӱ�죩��<br/><br/>
 * New��ʽ��<br/>
 * new��ʽ�ǳ����ִ�й��췽���������������beanû�����ù��췽����ϵͳ�����{@link Class}��newInstance()��������������������˹��췽������ôϵͳ���Զ�
 * Ѱ����ع��췽������ִ���乹�췽����ע�⣺Ĭ�ϲ����εĹ��췽�����Բ����ã������״��ҵ������͹��췽��֮����Щ��Ϣ�ᱻ������{@link BeanDefinition}�����С�<br/>
 * �й�AOP���߸��ӽӿ�ʵ�֡����New��ʽ��������������AOP���߽ӿ�ʵ�������ܻ����½��������������10��~100�����ͬClass������ϵĲ��Խ��������������������н��ܡ�
 * ��AOP���߸��ӽӿ��������µ��������classcode���ߵ�{@link org.more.core.classcode.ClassEngine.BuilderMode#Super Super}��ʽ��ͬ
 * ��˽�кͱ������������ܵ�aopӰ�죬�����new��ʽ������ܵ�Ӱ�죩��
 * <br/>Date : 2009-11-12
 * @author ������
 */
public enum CreateTypeEnum {
    /** ʹ�ù�����ʽ����bean�� */
    Factory,
    /** ͨ�����ù��췽���������� */
    New
}
