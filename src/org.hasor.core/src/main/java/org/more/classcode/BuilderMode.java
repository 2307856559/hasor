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
package org.more.classcode;
/**
 * ��ö�ٶ�����ClassCode�Ĺ���ģʽ��<br/>BuilderModeö���ж�����{@link BuilderMode#Propxy}��{@link BuilderMode#Super}����ģʽ��
 * ������ģʽ���Դ����Ӧ�����ֽ��������������Propxyģʽ�¿����ڲ����¹�����������¸����µ����ԡ��������ԡ�ί�С���Superģʽ�Ǵ�ͳ��
 * �̳и����д��ʽ��ʵ�֡���Ȼ������Propxyģʽ����Superģʽ���Ƕ�����֧����ͬ���ܵ�aop���ԣ�ֻ��������Ҫ�ܵ�java������������η�����Լ��
 * @version 2010-9-3
 * @author ������ (zyc@hasor.net)
 */
public enum BuilderMode {
    /**
     * ʹ�ü̳з�ʽʵ�����࣬��������ģʽ�±���Ҫ���������ͺ��ж������ɵ������Ǽ̳�ԭ����ʵ�ֵģ����и��ӷ�����д�������С�
     * ԭʼ���е����з���������д������super��ʽ���ø��ࡣ˽�з�����������д���롣ͬʱ���������aop����private������������Aopװ�䡣
     * <br/>�ڸ�ģʽ��ֻ��public��protected��������Aopװ�䡣���в��ܲ���aop�ķ�����aop���Խӿڲ��ᷢ�����������
     */
    Super,
    /**
     * ʹ�ô���ʽʵ�����࣬��������ģʽ���ص��ǿ����ڶ�����ֱ��ʹ��ClassCode�Ĺ���������´���һ���µ����Ͳ���ȥ��������
     * ������ʵ�ַ�ʽ����һ����̬����ʽʵ�֡�������Ҫע����Ǵ����������Ͳ�����ԭʼ���ͣ������ԭʼ���е����й��췽��
     * ȫ��ɾ��ȡ����֮��������һ��ֻ��һ�������Ĺ��췽�����ò������;��ǻ������͡����з������ö�ʹ�����ע������Ͷ�����á�
     * <br/>�ڸ�ģʽ��ֻ��public��������Aopװ�䣬private������protected�ķ��������Ȩ�����ⲻ�ܲ���Aop��aop���Խӿڲ��ᷢ����Щ�����Եķ�����
     */
    Propxy
}