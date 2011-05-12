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
package org.more.hypha.beans.xml;
import org.more.hypha.AbstractPropertyDefine;
import org.more.hypha.ValueMetaData;
import org.more.hypha.beans.define.File_ValueMetaData;
import org.more.util.attribute.IAttribute;
/**
 * �ļ���������ֵ��������
 * @version 2010-9-23
 * @author ������ (zyc@byshell.org)
 */
public class QPP_File implements QPP {
    /**��ͼ������Ϊ{@link File_ValueMetaData}�������ʧ�ܷ���null��*/
    public ValueMetaData parser(IAttribute attribute, AbstractPropertyDefine property) {
        //1.����Ƿ���Խ���
        String value = (String) attribute.getAttribute("file");
        if (value == null)
            return null;
        //2.���н���
        File_ValueMetaData newMETA = new File_ValueMetaData();
        newMETA.setFileObject(value);
        newMETA.setDir(false);
        return newMETA;
    }
}