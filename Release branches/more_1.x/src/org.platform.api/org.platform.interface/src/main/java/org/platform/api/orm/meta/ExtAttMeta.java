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
package org.platform.api.orm.meta;
import java.util.ArrayList;
import java.util.List;
/**
 * ��չ���Զ���
 * @version : 2013-1-27
 * @author ������ (zyc@byshell.org)
 */
public class ExtAttMeta extends AttMeta {
    /**��չ������*/
    private String        extTable     = "";
    /**��չ��������С�*/
    private String        extPKColumn  = "";
    /**��չ�������С�*/
    private String        extFKColumn  = "";
    /**��չ����ģʽ��column��������Ϊ��չ���ԣ�row����¼��Ϊ��չ���ԣ�*/
    private ExtModeEnum   extMode      = ExtModeEnum.Row;
    /**extMode��rowģʽ�´����������������*/
    private String        extKeyColumn = "";
    /**extMode��rowģʽ�´������ֵ��������*/
    private String        extVarColumn = "";
    /**����Ԫ��*/
    private List<AttMeta> attList      = new ArrayList<AttMeta>();
    //
    //
    //
    public String getExtTable() {
        return extTable;
    }
    public void setExtTable(String extTable) {
        this.extTable = extTable;
    }
    public String getExtPKColumn() {
        return extPKColumn;
    }
    public void setExtPKColumn(String extPKColumn) {
        this.extPKColumn = extPKColumn;
    }
    public String getExtFKColumn() {
        return extFKColumn;
    }
    public void setExtFKColumn(String extFKColumn) {
        this.extFKColumn = extFKColumn;
    }
    public ExtModeEnum getExtMode() {
        return extMode;
    }
    public void setExtMode(ExtModeEnum extMode) {
        this.extMode = extMode;
    }
    public String getExtKeyColumn() {
        return extKeyColumn;
    }
    public void setExtKeyColumn(String extKeyColumn) {
        this.extKeyColumn = extKeyColumn;
    }
    public String getExtVarColumn() {
        return extVarColumn;
    }
    public void setExtVarColumn(String extVarColumn) {
        this.extVarColumn = extVarColumn;
    }
    public List<AttMeta> getAttList() {
        return attList;
    }
    public void setAttList(List<AttMeta> attList) {
        this.attList = attList;
    }
}