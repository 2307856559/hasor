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
package org.platform.api.dbmapping.meta;
/**
 * ��������
 * @version : 2013-1-27
 * @author ������ (zyc@byshell.org)
 */
public class AttMeta {
    /**������*/
    private String   name         = "";
    /**����ӳ�䵽���ݿ������*/
    private String   column       = "";
    /**�����Ƿ������ֵ*/
    private boolean  notNull      = true;
    /**���ݳ��ȣ���ĳЩ�ض������¸�ֵ��������Ч��*/
    private int      length       = 1000;
    /**Ĭ��ֵ*/
    private String   defaultValue = "";
    /**�����Ƿ������Ϊ��������ʱ��һԱ��*/
    private boolean  update       = true;
    /**�����Ƿ������Ϊ������¼��һԱ��*/
    private boolean  insert       = true;
    /**�������Ƿ���Ա��ӳ�װ�ء�*/
    private boolean  lazy         = false;
    /**���������ݿ��е��������͡�*/
    private String   dbType       = "varchar";
    /**����ӳ�������*/
    private TypeEnum type         = TypeEnum.TString;
    //
    //
    //
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getColumn() {
        return column;
    }
    public void setColumn(String column) {
        this.column = column;
    }
    public boolean isNotNull() {
        return notNull;
    }
    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }
    public int getLength() {
        return length;
    }
    public void setLength(int length) {
        this.length = length;
    }
    public String getDefaultValue() {
        return defaultValue;
    }
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
    public boolean isUpdate() {
        return update;
    }
    public void setUpdate(boolean update) {
        this.update = update;
    }
    public boolean isInsert() {
        return insert;
    }
    public void setInsert(boolean insert) {
        this.insert = insert;
    }
    public boolean isLazy() {
        return lazy;
    }
    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }
    public String getDbType() {
        return dbType;
    }
    public void setDbType(String dbType) {
        this.dbType = dbType;
    }
    public TypeEnum getType() {
        return type;
    }
    public void setType(TypeEnum type) {
        this.type = type;
    }
}