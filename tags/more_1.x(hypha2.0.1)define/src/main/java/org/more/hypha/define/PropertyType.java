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
package org.more.hypha.define;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.Date;
/**
 * ��ö���ж�����{@link Simple_ValueMetaData}����Ա�ʾ�Ļ������͡�
 * @version 2010-11-11
 * @author ������ (zyc@byshell.org)
 */
public enum PropertyType {
    /**null���ݡ�*/
    Null("vt:null"),
    /**�������͡�*/
    Boolean("vt:boolean"),
    /**�ֽ����͡�*/
    Byte("vt:byte"),
    /**���������͡�*/
    Short("vt:short"),
    /**�������͡�*/
    Int("vt:int"),
    /**���������͡�*/
    Long("vt:long"),
    /**�����ȸ��������͡�*/
    Float("vt:float"),
    /**˫���ȸ��������͡�*/
    Double("vt:double"),
    /**�ַ����͡�*/
    Char("vt:char"),
    /**�ַ������͡�*/
    String("vt:string"),
    //
    /**�������͡�*/
    Array("vt:array"),
    /**�������͡�*/
    List("vt:list"),
    /**Set���͡�*/
    Set("vt:set"),
    /**Map���͡�*/
    Map("vt:map"),
    /**Map��һ��ʵ�����͡�*/
    MapEntity("vt:entity"),
    //
    /**Map���͡�*/
    Ref("vt:ref"),
    //
    /**Json�������͡�*/
    Json("vt:json"),
    /**{@link URL}���͡�*/
    URL("vt:url"),
    /**{@link URI}���͡�*/
    URI("vt:uri"),
    /**{@link File}���͡�*/
    File("vt:file"),
    /**{@link Date}���͡�*/
    Date("vt:date"), ;
    /*------------------------------------------------------------------*/
    private String value = null;
    PropertyType(String value) {
        this.value = value;
    }
    public String value() {
        return this.value;
    }
}