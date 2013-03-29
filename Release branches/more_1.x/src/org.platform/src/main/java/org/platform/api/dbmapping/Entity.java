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
package org.platform.api.dbmapping;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * ����һ��ʵ�����
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Entity {
    /**ʵ�����ƣ�Ĭ��ʹ�ü��������Ϊ��ֵ��*/
    public String name() default "";
    /**ʵ��ӳ��ı�����Ĭ��ʹ�ü��������Ϊ��ֵ��*/
    public String table() default "";
    /** ģʽ��������ֵ�������á�
     * @see org.platform.api.dbmapping.Entity.Mode*/
    public Mode mode() default Mode.Mapping;
    /**��ֵ�Ǿ������ʵ�����Ĭ������������,Ĭ��ֵ��false�� */
    public boolean lazy() default false;
    /**ʵ���Ƿ��������±��档*/
    public boolean update() default true;
    /**ʵ���Ƿ������������档*/
    public boolean insert() default true;
    /**ʵ���Ƿ�����ִ��ɾ��������*/
    public boolean delete() default true;
    /**������չ����Ϣ��*/
    public ExtTable extTableInfo() default @ExtTable(extFKColumn = "", extPKColumn = "", extTable = "");
    /**
     * ÿ������ϵͳ����ʵ������ݿ�ӳ���ִ��ʲô�������ö�ٵ�ֵ���塣
     * @version : 2013-3-27
     * @author ������ (zyc@byshell.org)
     */
    public static enum Mode {
        /**�ع����ݿ��������ģʽ�»ᵼ������ȫ����ʧ��*/
        CreateDrop,
        /**�Զ��������ݿ⣬����ģʽ�»�����޶ȱ�֤���ݲ���ʧ������¸������ݿ�ṹ��*/
        Update,
        /**��ӳ�䣬����ģʽ����ִ���κ����ݿ��DDL�����ݿ�ṹ����仯����䡣*/
        Mapping
    }
}