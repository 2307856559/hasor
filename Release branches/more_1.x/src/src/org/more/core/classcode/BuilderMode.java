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
package org.more.core.classcode;
/** ClassEngine����������ģʽ�� */
public enum BuilderMode {
    /**
     * �̳з�ʽʵ�����࣬��������ģʽ�±���Ҫ���������ͺ��ж������ɵ������Ǽ̳�ԭ����ʵ�ֵģ�
     * ���и��ӷ�����д�������С�ԭʼ���е����з���������д������super��ʽ���ø��ࡣ˽�з�����������д���롣
     * ˽�з�����������AOP���ܡ��ڼ̳�ģʽ�±��������빫����������AOP���ܡ�
     */
    Super,
    /**
     * ����ʽʵ�����࣬��������ģʽ�¿��������еĶ����ϸ��ӽӿ�ʵ�ֶ�����Ҫ���´�������ͬʱ���ɵ��¶���
     * ���ƻ�ԭ�ж�������ʵ�ַ�ʽ����һ����̬����ʽʵ�֡�ע���������ɷ�ʽ��ȡ������ԭʼ���еĹ��췽����
     * ȡ����֮��������һ��һ�������Ĺ��췽�����ò������;��ǻ������͡����з������ö�ʹ�����ע������Ͷ�����á�
     * ͬʱ�������ɷ�ʽ��˽�з�����������д���롣<br/>
     * �ڴ���ģʽ��ֻ�й�����������AOP���ܣ�˽�з������ܱ����ķ��������Ȩ�����ⲻ�ܲ���AOP��
     */
    Propxy
}
