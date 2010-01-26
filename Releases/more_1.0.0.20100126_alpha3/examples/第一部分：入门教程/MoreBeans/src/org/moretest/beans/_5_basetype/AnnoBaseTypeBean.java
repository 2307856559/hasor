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
package org.moretest.beans._5_basetype;
import org.more.beans.resource.annotation.Bean;
import org.more.beans.resource.annotation.Property;
/**
 * ע�ⷽʽ���ø�Bean������ע����������������ݡ�
 * @version 2010-1-20
 * @author ������ (zyc@byshell.org)
 */
@Bean
public class AnnoBaseTypeBean {
    @Property(value = "true")
    private boolean booleanData;
    @Property(value = "123")
    private byte    byteData;
    @Property(value = "789")
    private short   shortData;
    @Property(value = "-1245")
    private int     intData;
    @Property(value = "89898989")
    private long    longData;
    @Property(value = "235.65")
    private float   floatData;
    @Property(value = "-3.141592653")
    private double  doubleData;
    @Property(value = "�����ַ�������")
    private String  stringData;
    public boolean isBooleanData() {
        return booleanData;
    }
    public void setBooleanData(boolean booleanData) {
        this.booleanData = booleanData;
    }
    public byte getByteData() {
        return byteData;
    }
    public void setByteData(byte byteData) {
        this.byteData = byteData;
    }
    public short getShortData() {
        return shortData;
    }
    public void setShortData(short shortData) {
        this.shortData = shortData;
    }
    public int getIntData() {
        return intData;
    }
    public void setIntData(int intData) {
        this.intData = intData;
    }
    public long getLongData() {
        return longData;
    }
    public void setLongData(long longData) {
        this.longData = longData;
    }
    public float getFloatData() {
        return floatData;
    }
    public void setFloatData(float floatData) {
        this.floatData = floatData;
    }
    public double getDoubleData() {
        return doubleData;
    }
    public void setDoubleData(double doubleData) {
        this.doubleData = doubleData;
    }
    public String getStringData() {
        return stringData;
    }
    public void setStringData(String stringData) {
        this.stringData = stringData;
    }
};