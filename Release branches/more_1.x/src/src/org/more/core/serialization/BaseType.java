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
package org.more.core.serialization;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.more.CastException;
import org.more.RepeateException;
/**
 * more�������л����ͳ����࣬������Ҫ�����Ƕ��������͵Ĺ���������Ҳ����װ�������͵���̬�ֶ��С�
 * ʹ��getOriginalName()�������Ի�õ����͵�ԭ�������ƣ�ԭ��������ƽ̨�޹ء�ʹ��toString��
 * toObject��������ʵ�ֶ�����໥ת�����ܡ���more���л��齨�����͵����ȼ������°��ŵġ�<br/>
 * NullType > BooleanType > NumberType > StringType > ArrayType > UserType > TableType
 * Date : 2009-7-7
 * @author ������
 */
public abstract class BaseType {
    //===================================================================================================================ȫ�־�̬���ԣ�����������
    /** moreϵͳ������ԭ������ǰ׺ */
    private static String                          Prefix_Type = "More";
    /** �ü��ϱ������more���л��齨��֧�ֵĻ��������б�more���л���ֻ�У�1���֣�2�ַ�����3������4��ֵ��5���飬6��7�Զ������͡����ֻ������͡� */
    private static LinkedHashMap<String, BaseType> types       = new LinkedHashMap<String, BaseType>();
    /** �ô�����Ŀ����װ��ϵͳ�Ѿ�������������͡����ҳ�ʼ��types��̬�ֶ� */
    static {
        BaseType type = null;
        //
        type = new NullType();
        BaseType.types.put(type.getOriginalName(), type);
        //
        type = new BooleanType();
        BaseType.types.put(type.getOriginalName(), type);
        //
        type = new NumberType();
        BaseType.types.put(type.getOriginalName(), type);
        //
        type = new StringType();
        BaseType.types.put(type.getOriginalName(), type);
        //
        type = new ArrayType();
        BaseType.types.put(type.getOriginalName(), type);
        //
        type = new TableType();
        BaseType.types.put(type.getOriginalName(), type);
        //
        InputStream in = BaseType.class.getResourceAsStream("/org/more/core/serialization/type/config.properties");
        BufferedReader sr = new BufferedReader(new InputStreamReader(in));
        while (true) {
            try {
                String readString = sr.readLine();
                if (readString == null)
                    break;
                Class<?> userType = Class.forName(readString);
                BaseType.regeditType((UserType) userType.newInstance());
            } catch (Exception e) {}
        }
    }
    /**
     * �Ƴ�ָ��ԭʼ���������Զ�������ע�ᡣ�����ͼ�Ƴ������Ͳ����ڻ�����ͼɾ�����Զ�����������󽫱����ԡ�
     * @param originalName Ҫɾ��������ԭʼ��������
     */
    public synchronized static void removeRegeditType(String originalName) {
        BaseType type = BaseType.types.get(originalName);
        if (type == null || type instanceof UserType == false) {} else
            BaseType.types.remove(originalName);
    }
    /**
     * ע��һ���Զ��������Լ�������Ҫ�����л��Ķ���һ���Զ�������ֻ��ע��һ�Σ����ע���λ�����RepeateException�쳣��
     * ���͵��ظ��ж����������͵�ԭʼ���������еġ�
     * @param type Ҫע������͡�
     * @throws RepeateException �����һ�����ͽ������ظ�ע������������쳣��
     */
    public synchronized static void regeditType(UserType type) throws RepeateException {
        if (BaseType.types.containsKey(type.getOriginalName()) == false) {
            BaseType.types.put(type.getOriginalName(), type);
            BaseType last = BaseType.types.get("[More Table]");
            BaseType.types.remove(last.getOriginalName());
            BaseType.types.put(last.getOriginalName(), last);
        } else
            throw new RepeateException("�����ظ�ע������:" + type.getOriginalName());
    }
    /**
     * ��ȡ�Ѿ�ע����Զ��������б�
     * @return �����Ѿ�ע����Զ��������б�
     */
    public synchronized static UserType[] userTypeList() {
        ArrayList<UserType> list = new ArrayList<UserType>();
        for (BaseType type : BaseType.types.values())
            if (type instanceof UserType)
                list.add((UserType) type);
        //
        UserType[] r = new UserType[list.size()];
        list.toArray(r);
        return r;
    }
    /**
     * ����һ�����Դ�������ַ���ת����more���л����Ͷ����Դ������ݡ����ؿ��Դ���������͵����л����Ͷ���
     * ����Ҳ����򷵻�null��
     * @param object Ҫ���������͵����л�����
     * @return ���ز���һ�����Դ�������ַ���ת����more���л����Ͷ����Դ������ݡ����ؿ��Դ���������͵����л����Ͷ���
     */
    public static BaseType findType(Object object) {
        for (BaseType type : BaseType.types.values()) {
            if (type.testObject(object) == true)
                return type;
        }
        return null;
    }
    /**
     * ����һ�����Դ����ַ����������more���л����Ͷ����Դ������ݡ����ؿ��Դ���������͵ķ����л����Ͷ���
     * ����Ҳ����򷵻�null��
     * @param string Ҫ���������͵����л�����
     * @return ���ز���һ�����Դ����ַ����������more���л����Ͷ����Դ������ݡ����ؿ��Դ���������͵ķ����л����Ͷ���
     */
    public static BaseType findType(String string) {
        for (BaseType type : BaseType.types.values()) {
            if (type.testString(string) == true)
                return type;
        }
        return null;
    }
    /**
     * ����ָ����ԭʼ����������������Ͷ����ҷ��ء�����Ҳ���ָ�������򷵻�null��
     * @param originalName Ҫ�������͵�ԭʼ��������
     * @return ���ظ���ָ����ԭʼ����������������Ͷ����ҷ��ء�����Ҳ���ָ�������򷵻�null��
     */
    public static BaseType findTypeByOriginalName(String originalName) {
        return BaseType.types.get(originalName);
    }
    //===================================================================================================================�������е����Է���
    /**
     * ������͵�ԭʼ��������ԭʼ�������ĸ�ʽ�ǡ�[Prefix Type]����ԭʼ��������������ƽ̨�޹ص�һ��������������������more���л��齨���е����͡�
     * more���л��齨Ϊ�˴ﵽ�����ڿ�������ƽ̨��ͳһ���Բ����Լ��Ķ����������͡���Щ��������ֻ�����֣���������bean������more���л��齨������
     * ���������ʹ��ڵġ�more���л��齨��֧�ֵ�������1���֣�2�ַ�����3������4��ֵ��5���飬6��
     * @return �������͵�ԭʼ��������
     */
    public String getOriginalName() {
        return "[" + BaseType.Prefix_Type + " " + this.getShortOriginalName() + "]";
    }
    /**
     * ����Ŀ���ַ����Ƿ����ʹ�õ�ǰ������ת�������֧�ַ���true�����򷵻�false��
     * @param string Ҫ�����Ե�Ŀ�����
     * @return ����Ŀ���ַ����Ƿ����ʹ�õ�ǰ������ת�������֧�ַ���true�����򷵻�false��
     */
    public abstract boolean testString(String string);
    /**
     * ����Ŀ������Ƿ����ʹ�õ�ǰ������ת�������֧�ַ���true�����򷵻�false��
     * @param object Ҫ�����Ե�Ŀ�����
     * @return ����Ŀ������Ƿ����ʹ�õ�ǰ������ת�������֧�ַ���true�����򷵻�false��
     */
    public abstract boolean testObject(Object object);
    /**
     * ִ�����л��������ҷ������л�����֮����ַ�����
     * @param object Ҫ���л��Ķ���
     * @return �������л�������ַ���������ʽ��
     * @throws CastException ��ִ������ת��ʱ�����쳣��
     */
    public abstract String toString(Object object) throws CastException;
    /**
     * �����л�֮����ַ������з����л����Ի�ԭ�����͡�
     * @param string Ҫ�����л����ַ�����
     * @return �����л�֮����ַ������з����л����Ի�ԭ�����͡�
     * @throws CastException ��ִ������ת��ʱ�����쳣��
     */
    public abstract Object toObject(String string) throws CastException;
    /**
     * �÷����Ǳ������ķ������÷����������͵ľ������������÷����ķ���ֵ����getOriginalName���á�
     * getOriginalName�������Զ��������ԭʼ�����÷����ķ���ֵ�����¸�ʽString,Integer,Short...
     * @return �������͵ľ�����������
     */
    protected abstract String getShortOriginalName();
}