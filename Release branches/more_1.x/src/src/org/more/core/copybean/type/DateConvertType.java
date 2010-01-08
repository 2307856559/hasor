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
package org.more.core.copybean.type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.more.core.copybean.ConvertType;
/**
 * CopyBean����Date����ת���ĸ����ࡣ
 * @version 2009-5-23
 * @author ������ (zyc@byshell.org)
 */
public class DateConvertType extends ConvertType {
    /**  */
    private static final long serialVersionUID         = -5864989968961653320L;
    /** �����������ֵΪ��ʱ������null�� */
    public final static int   DefaultType_Null         = 0;
    /** �����������ֵΪ��ʱ������staticDefault���Ե�ʱ�䡣 */
    public final static int   DefaultType_DefaultValue = 1;
    /** �����������ֵΪ��ʱ������һ����ʱ�䷵�ء� */
    public final static int   DefaultType_NewDate      = 2;
    /** Ĭ��ʱ���ʽ */
    private String            format                   = "yyyy-MM-dd hh:mm:ss";
    /** ʱ���ʽ��Ĭ�����͡� */
    private Date              staticDefault            = null;
    /** ��׼��ת����ʱ��Ϊ��ʱ��Ĭ��ֵ������ʲô���ò�����DateConvertType.DefaultType_�ƶ��� */
    private int               defaultType              = DateConvertType.DefaultType_NewDate;
    /**
     * ��õ�׼��ת����ʱ��Ϊ��ʱ��Ĭ��ֵ���ԡ�
     * @return ���ص�׼��ת����ʱ��Ϊ��ʱĬ��ֵ�����ԡ�
     */
    public int getDefaultType() {
        return defaultType;
    }
    /**
     * ���õ�׼��ת����ʱ��Ϊ��ʱĬ��ֵ�����ԡ�
     * @param defaultType Ĭ��ֵ������
     */
    public void setDefaultType(int defaultType) {
        this.defaultType = defaultType;
    }
    @Override
    public boolean checkType(Object from, Class<?> to) {
        return (to == Date.class) ? true : false;
    }
    @Override
    public Object convert(Object object) {
        if (object == null)
            return getDefaultValue();
        if (object instanceof Date)
            return object;
        else
            try {
                return new SimpleDateFormat(this.format).parse(object.toString());
            } catch (ParseException e) {
                return getDefaultValue();
            }
    }
    private Date getDefaultValue() {
        switch (this.defaultType) {
        case DateConvertType.DefaultType_Null:
            return null;
        case DateConvertType.DefaultType_DefaultValue:
            return this.staticDefault;
        case DateConvertType.DefaultType_NewDate:
            return new Date();
        }
        return null;
    }
    /**
     * ���ʱ�����ڸ�ʽ��
     * @return ����ʱ�����ڸ�ʽ��
     */
    public String getFormat() {
        return format;
    }
    /**
     * ����ʱ�����ڸ�ʽ��
     * @param format Ҫ���õ�ʱ�����ڸ�ʽ��
     */
    public void setFormat(String format) {
        this.format = format;
    }
}