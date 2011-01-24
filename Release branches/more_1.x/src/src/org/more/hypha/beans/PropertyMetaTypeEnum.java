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
package org.more.hypha.beans;
/**
 * ��ö�ٶ�����{@link ValueMetaData}����Ԫ��Ϣ�������������ͷ��룬
 * �������а����˻������ͷ�����ּ������ͷ����Լ�һЩ�������ͷ��롣
 * @version 2011-1-24
 * @author ������ (zyc@byshell.org)
 */
public interface PropertyMetaTypeEnum {
    /**
     * һ���������������ͣ������ַ������ڵ�����java�������Ͷ�������SimpleType����ʾ��
     * ����Ϊ�����͵��У�null,boolean,byte,short,int,long,float,double,char,string��
     */
    public static String SimpleType      = "SimpleType";
    /**��ʾһ��ö������*/
    public static String Enum            = "Enum";
    /**��ʾ��һ��bean������*/
    public static String RelationBean    = "RelationBean";
    /**��ʾһ���������ͼ��ϡ�*/
    public static String ArrayCollection = "ArrayCollection";
    /**��ʾһ��List���ͼ��ϡ�*/
    public static String ListCollection  = "ListCollection";
    /**��ʾһ��Set���ͼ��ϡ�*/
    public static String SetCollection   = "SetCollection";
    /**��ʾһ��Map���ͼ��ϡ�*/
    public static String MapCollection   = "MapCollection";
    /**��ʾһ���ļ�����*/
    public static String File            = "File";
    /**��ʾһ������������Դ��*/
    public static String URI             = "URI";
    /**��ʾһ�����ı�����*/
    public static String BigText         = "BigText";
    /**��ʾһ�����ڶ���*/
    public static String Date            = "Date";
    /**��ʾһ��elִ�С�*/
    public static String EL              = "EL";
};