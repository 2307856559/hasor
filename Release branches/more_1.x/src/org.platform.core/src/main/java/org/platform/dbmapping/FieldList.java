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
 * ��ʾ���һ��ӳ���ϵ��
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface FieldList {
    /**ӳ��ı�������*/
    public String column() default "";
    /**���ֶ��Ƿ������ֵ��*/
    public boolean isNull() default true;
    /**������ó��ȡ�*/
    public int length() default 1000;
    /**Ĭ��ֵ��*/
    public String defaultValue() default "";
    /**�ֶ��Ƿ������¡�*/
    public boolean updateMode() default true;
    /**�ֶ��Ƿ������롣*/
    public boolean insertMode() default true;
    /**�ֶ��Ƿ�Ϊ�����ء�*/
    public boolean lazy() default false;
    /**���ݿ�ʹ�õ��������͡�*/
    public DBType dbType() default DBType.Nvarchar;
    //
    //
    /**����Ŀ��ʵ���һ�������ֶΣ����ֶο��Ժ�����ֶ�������������*/
    public String forProperty();
    /**Ŀ��ʵ�弯�ϵ�˳�����յ���������*/
    public String sortBy() default "";
    /**Ŀ��ʵ�弯�ϵ�˳��ʽ��*/
    public String sortMode() default "asc";
    /**���ӹ���������*/
    public String filter() default "";//"this.userName like 'abc%' and this.attGroup.abc='A'";
}