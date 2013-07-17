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
package org.hasor.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.hasor.context.HasorModule;
/**
 * ��־����ע�ᵽϵͳ��ʼ�����̣������ڱ��ע��ʱ����ʵ��{@link HasorModule}�ӿڡ�
 * @version : 2013-3-20
 * @author ������ (zyc@byshell.org)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface Module {
    /**Ĭ�����ƣ���������ϵͳ����̨���ڹ�����ʾ��;��*/
    public String displayName() default "";
    /**�Ը����������Ϣ��*/
    public String description() default "";
    /**����˳��Ĭ��ֵ0����ֵԽ���ʾ����˳��Խ�Ӻ���ʾ����ֵ��ͬ�ڡ�0����*/
    public int startIndex() default 0;
    /*�����壺���м����������Integet����Сֵ��*/
    /**����0��1~99����ʾ��ʼ�㣺1*/
    public static int Lv_0    = Integer.MIN_VALUE + 1;  //
    /**����0��1~99����ʾ�����㣺99*/
    public static int Lv_0Max = Integer.MIN_VALUE + 99; //
    /**����1��101~199����ʾ��ʼ�㣺101*/
    public static int Lv_1    = Integer.MIN_VALUE + 101; //
    /**����1��101~199����ʾ�����㣺199*/
    public static int Lv_1Max = Integer.MIN_VALUE + 199; //
    /**����2��201~299����ʾ��ʼ�㣺201*/
    public static int Lv_2    = Integer.MIN_VALUE + 201; //
    /**����2��201~299����ʾ�����㣺299*/
    public static int Lv_2Max = Integer.MIN_VALUE + 299; //
    /**����3��301~399����ʾ��ʼ�㣺301*/
    public static int Lv_3    = Integer.MIN_VALUE + 301; //
    /**����3��301~399����ʾ�����㣺399*/
    public static int Lv_3Max = Integer.MIN_VALUE + 399; //
    /**����4��401~499����ʾ��ʼ�㣺401*/
    public static int Lv_4    = Integer.MIN_VALUE + 401; //
    /**����4��401~499����ʾ�����㣺499*/
    public static int Lv_4Max = Integer.MIN_VALUE + 499; //
    /**����5��501~599����ʾ��ʼ�㣺501*/
    public static int Lv_5    = Integer.MIN_VALUE + 501; //
    /**����5��501~599����ʾ�����㣺599*/
    public static int Lv_5Max = Integer.MIN_VALUE + 599; //
    /**����6��601~699����ʾ��ʼ�㣺601*/
    public static int Lv_6    = Integer.MIN_VALUE + 601; //
    /**����6��601~699����ʾ�����㣺699*/
    public static int Lv_6Max = Integer.MIN_VALUE + 699; //
    /**����7��701~799����ʾ��ʼ�㣺701*/
    public static int Lv_7    = Integer.MIN_VALUE + 701; //
    /**����7��701~799����ʾ�����㣺799*/
    public static int Lv_7Max = Integer.MIN_VALUE + 799; //
    /**����8��801~899����ʾ��ʼ�㣺801*/
    public static int Lv_8    = Integer.MIN_VALUE + 801; //
    /**����8��801~899����ʾ�����㣺899*/
    public static int Lv_8Max = Integer.MIN_VALUE + 899; //
    /**����9��901~999����ʾ��ʼ�㣺901*/
    public static int Lv_9    = Integer.MIN_VALUE + 901; //
    /**����9��901~999����ʾ�����㣺999*/
    public static int Lv_9Max = Integer.MIN_VALUE + 999; //
}