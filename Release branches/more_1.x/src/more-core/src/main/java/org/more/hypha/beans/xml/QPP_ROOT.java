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
import java.util.ArrayList;
import org.more.core.log.Log;
import org.more.core.log.LogFactory;
import org.more.hypha.AbstractPropertyDefine;
import org.more.hypha.ValueMetaData;
import org.more.hypha.beans.define.PropertyType;
import org.more.hypha.beans.define.Simple_ValueMetaData;
import org.more.util.attribute.IAttribute;
/**
 * ���Կ��ٽ����������ַ���ֵ����Ϊָ�������͡�
 * @version 2010-10-10
 * @author ������ (zyc@byshell.org)
 */
public class QPP_ROOT implements QPP {
    private static Log     log        = LogFactory.getLog(QPP_ROOT.class);
    private ArrayList<QPP> parserList = new ArrayList<QPP>();             //���Կ��ٽ���������
    /**ע��һ����������ֵ��������*/
    public synchronized void regeditTypeParser(QPP parser) {
        if (parser == null) {
            log.warning("regedit TypeParser error , parser is null.");
            return;
        }
        if (this.parserList.contains(parser) == false) {
            log.debug("{%0} parser regedit OK!", parser);
            this.parserList.add(parser);
        } else
            log.error("{%0} parser is exist.", parser);
    };
    /**ȡ��һ����������ֵ��������ע�ᡣ*/
    public synchronized void unRegeditTypeParser(QPP parser) {
        if (parser == null) {
            log.warning("unRegedit TypeParser error , parser is null.");
            return;
        }
        if (this.parserList.contains(parser) == true) {
            log.debug("{%0} parser unRegedit OK!", parser);
            this.parserList.remove(parser);
        } else
            log.error("unRegedit error {%0} is not exist.", parser);
    };
    /**������ֵ����Ϊĳһ�ض����͵�ֵ����value������ֵת����ָ����Ԫ��Ϣ������*/
    public synchronized ValueMetaData parser(IAttribute<String> att, AbstractPropertyDefine property) {
        ValueMetaData valueMETADATA = null;
        for (QPP tp : parserList) {
            valueMETADATA = tp.parser(att, property);
            if (valueMETADATA != null)
                return valueMETADATA;
        }
        log.debug("parser use null.");
        /**ʹ��Ĭ��ֵnull*/
        Simple_ValueMetaData simple = new Simple_ValueMetaData();
        simple.setValueMetaType(PropertyType.Null);
        simple.setValue(null);
        return simple;
    };
}