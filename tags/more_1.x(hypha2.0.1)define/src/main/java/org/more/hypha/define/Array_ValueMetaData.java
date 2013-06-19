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
/**
 * ��ʾһ�����鼯�����͵�ֵԪ��Ϣ������
 * @version 2010-9-17
 * @author ������ (zyc@byshell.org)
 */
public class Array_ValueMetaData extends Collection_ValueMetaData<ValueMetaData> {
    /*���ϳ�ʼ����С*/
    private int initSize = 0;
    /*------------------------------------------------------------------*/
    /**����{@link PropertyType#Array}*/
    @Override
    public String getType() {
        return PropertyType.Array.value();
    }
    /**��ȡ���ϳ�ʼ����С*/
    public int getInitSize() {
        return initSize;
    }
    /**���ü��ϳ�ʼ����С*/
    public void setInitSize(int initSize) {
        this.initSize = initSize;
    }
}