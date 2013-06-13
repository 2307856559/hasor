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
package org.platform.dbmapping;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * ��һ������ӳ�䵽���ݿ����ϡ�
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Field {
    /**ӳ��ı�������*/
    public String column() default "";
    /**���ֶ��Ƿ������ֵ��*/
    public boolean isNull() default true;
    /**������ó��ȡ�*/
    public int length() default 1000;
    /**Ĭ��ֵ��*/
    public String defaultValue() default "";
    /**�ֶ��Ƿ������¡�*/
    public boolean update() default true;
    /**�ֶ��Ƿ������롣*/
    public boolean insert() default true;
    /**�ֶ��Ƿ�Ϊ�����ء�*/
    public boolean lazy() default false;
    /**���ݿ�ʹ�õ��������͡�*/
    public DBType dbType() default DBType.Nvarchar;
    //    public Type type() default Type.String;//  "string(default)|float|int|double|long|boolean|datetime|uuid|btye|json|bytes"
    //    /**֧�ֵ�Java�������͡�*/
    //    public enum Type {
    //        /**�ַ���*/
    //        String,
    //        /**������*/
    //        Float,
    //        /**����*/
    //        Integer,
    //        /**˫��������*/
    //        Double,
    //        /**������*/
    //        Long,
    //        /**��������*/
    //        Boolean,
    //        /**ʱ������*/
    //        Date,
    //        /**UUID*/
    //        UUID,
    //        /**�ֽ����͡�*/
    //        Btye,
    //        /**�ֽ�����*/
    //        Btyes
    //    }
}