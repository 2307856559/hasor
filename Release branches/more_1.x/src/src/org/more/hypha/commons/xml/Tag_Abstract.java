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
package org.more.hypha.commons.xml;
import java.lang.reflect.Method;
import org.more.PropertyException;
import org.more.hypha.context.xml.XmlDefineResource;
import org.more.log.ILog;
import org.more.log.LogFactory;
import org.more.util.StringConvert;
import org.more.util.StringUtil;
/**
 * namespace���з����漰����xml���඼��Ҫ���ɵ��࣬����Ŀ����Ϊ���ṩһ��ͳһ��{@link XmlDefineResource}�����ȡ�ӿں�һЩTag����ʱ�Ĺ���������
 * @version 2010-9-23
 * @author ������ (zyc@byshell.org)
 */
public abstract class Tag_Abstract {
    private static final ILog log           = LogFactory.getLog(Tag_Abstract.class);
    private XmlDefineResource configuration = null;
    /*------------------------------------------------------------------------------*/
    /**����Tag_Abstract����*/
    public Tag_Abstract(XmlDefineResource configuration) {
        this.configuration = configuration;
    }
    /**��ȡ{@link XmlDefineResource}����*/
    protected XmlDefineResource getDefineResource() {
        return this.configuration;
    }
    /*------------------------------------------------------------------------------*/
    /**����ĳ�����Ƶķ������÷���������һ��������*/
    private Method findMethod(String methodName, Class<?> type) {
        if (methodName == null || type == null) {
            log.error("findMethod an error , methodName or type is null, please check it.");
            return null;
        }
        for (Method m : type.getMethods())
            if (m.getName().equals(methodName) == true)
                if (m.getParameterTypes().length == 1) {
                    log.debug("find method {%0}.", m);
                    return m;
                }
        log.debug("{%0} method not exist at {%1}.", methodName, type);
        return null;
    }
    /**ִ������ע�룬����ע��int,short,long,�Ȼ�������֮��÷�����֧��ע��ö�����͡�*/
    protected final void putAttribute(Object define, String attName, Object value) {
        if (define == null || attName == null) {
            log.error("putAttribute an error, define or attName is null, please check it.");
            return;
        }
        //1.���ҷ���
        String methodName = "set" + StringUtil.toUpperCase(attName);
        Class<?> defineType = define.getClass();
        Method writeMethod = this.findMethod(methodName, defineType);
        if (writeMethod == null) {
            log.warning("can`t invoke {%0} , this method not exist on {%1}", methodName, defineType);
            return;
        }
        //2.ִ������ת��
        Class<?> toType = writeMethod.getParameterTypes()[0];
        Object attValueObject = StringConvert.changeType(value, toType);
        //3.ִ������ע��
        try {
            writeMethod.invoke(define, attValueObject);
            log.debug("putAttribute OK! newValue is {%0}.", attValueObject);
        } catch (Exception e) {
            log.error("putAttribute {%0} an error! error = {%1}", attName, e);
            throw new PropertyException(attName + "���Բ��ܱ�д�뵽" + define + "�С�", e);
        }
    };
}