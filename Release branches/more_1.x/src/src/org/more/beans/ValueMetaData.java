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
package org.more.beans;
import org.more.util.attribute.AttBase;
import org.more.util.attribute.IAttribute;
/**
 * �������ڱ�ʾ{@link AbstractPropertyDefine}�����ж��������ֵ������Ϣ��
 * {@link PropertyMetaTypeEnum}ö�ٿ��Ը���ϸ������{@link ValueMetaData}
 * �����������������ݡ�
 * @version 2010-9-17
 * @author ������ (zyc@byshell.org)
 */
public abstract class ValueMetaData implements IAttribute, DefineConfig {
    private IAttribute attribute    = new AttBase(); //����
    private IAttribute defineconfig = new AttBase(); //��չ��������
    /**��ö�ٶ�����{@link ValueMetaData}����Ԫ��Ϣ�������������ͷ��룬�������а����˻������ͷ�����ּ������ͷ����Լ�һЩ�������ͷ��롣*/
    public enum PropertyMetaTypeEnum {
        /**
         * һ���������������ͣ������ַ������ڵ�����java�������Ͷ�������SimpleType����ʾ��
         * ����Ϊ�����͵��У�null,boolean,byte,short,int,long,float,double,char,string��
         */
        SimpleType,
        /**��ʾһ��ö������*/
        Enum,
        /**��ʾ��һ��bean������*/
        RelationBean,
        /**��ʾһ���������ͼ��ϡ�*/
        ArrayCollection,
        /**��ʾһ��List���ͼ��ϡ�*/
        ListCollection,
        /**��ʾһ��Set���ͼ��ϡ�*/
        SetCollection,
        /**��ʾһ��Map���ͼ��ϡ�*/
        MapCollection,
        /**��ʾһ���ļ�����*/
        File,
        /**��ʾһ������������Դ��*/
        URI,
        /**��ʾһ�����ı�����*/
        BigText,
        /**��ʾһ�����ڶ���*/
        Date,
    };
    /**����������Ե��������ͣ������������������Ե�����������*/
    public abstract PropertyMetaTypeEnum getPropertyType();
    /**������չDefine����������*/
    public IAttribute getDefineConfig() {
        return this.defineconfig;
    };
    //-------------------------------------------------------------
    public boolean contains(String name) {
        return this.attribute.contains(name);
    };
    public void setAttribute(String name, Object value) {
        this.attribute.setAttribute(name, value);
    };
    public Object getAttribute(String name) {
        return this.attribute.getAttribute(name);
    };
    public void removeAttribute(String name) {
        this.attribute.removeAttribute(name);
    };
    public String[] getAttributeNames() {
        return this.attribute.getAttributeNames();
    };
    public void clearAttribute() {
        this.attribute.clearAttribute();
    };
}