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
package org.more.hypha.xml.support.beans;
import java.util.ArrayList;
import org.more.hypha.AbstractPropertyDefine;
import org.more.hypha.ValueMetaData;
import org.more.hypha.define.beans.PropertyType;
import org.more.hypha.define.beans.Simple_ValueMetaData;
import org.more.hypha.xml.support.BeansTypeParser;
import org.more.util.attribute.IAttribute;
/**
 * ���Կ��ٽ����������ַ���ֵ����Ϊָ�������͡�
 * @version 2010-10-10
 * @author ������ (zyc@byshell.org)
 */
public class QPP_ROOT implements BeansTypeParser {
    private ArrayList<BeansTypeParser> parserList = new ArrayList<BeansTypeParser>(); //���Կ��ٽ���������
    /**ע��һ����������ֵ��������*/
    public synchronized void regeditTypeParser(BeansTypeParser parser) {
        if (parser == null)
            throw new NullPointerException("��������Ϊ�ա�");
        if (this.parserList.contains(parser) == false)
            this.parserList.add(parser);
    }
    /**ȡ��һ����������ֵ��������ע�ᡣ*/
    public synchronized void unRegeditTypeParser(BeansTypeParser parser) {
        if (parser == null)
            throw new NullPointerException("��������Ϊ�ա�");
        if (this.parserList.contains(parser) == true)
            this.parserList.remove(parser);
    }
    /**������ֵ����Ϊĳһ�ض����͵�ֵ����value������ֵת����ָ����Ԫ��Ϣ������*/
    public synchronized ValueMetaData parser(IAttribute att, AbstractPropertyDefine property) {
        ValueMetaData valueMETADATA = null;
        for (BeansTypeParser tp : parserList) {
            valueMETADATA = tp.parser(att, property);
            if (valueMETADATA != null)
                return valueMETADATA;
        }
        /**ʹ��Ĭ��ֵnull*/
        Simple_ValueMetaData simple = new Simple_ValueMetaData();
        simple.setValueMetaType(PropertyType.Null);
        simple.setValue(null);
        return simple;
    }
}