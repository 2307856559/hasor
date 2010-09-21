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
package org.more.log;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.more.util.StringConvert;
/**
 * �齨���棬������
 * @version 2009-5-13
 * @author ������ (zyc@byshell.org)
 */
class ComObjectFactory {
    private Map<String, Object> comCache   = new Hashtable<String, Object>(0);
    private ComFactory          comFactory = null;
    private final String[]      keys       = new String[1];                   // ������Ա����ֻ��߹ؼ��֡�
    // ===============================================================
    /**
     * ComObjectFactory������
     * @param comFactory ��־ϵͳ�����ࡣ
     */
    public ComObjectFactory(ComFactory comFactory) {
        keys[0] = "class";
        this.comFactory = comFactory;
    }
    // ===============================================================
    /**
     * ����־�齨�����л��һ����־�齨��
     * @param name �齨����
     * @return ������־�齨��
     * @throws LogException ���������־�쳣��
     */
    public Object getComObject(String name, Object... params) throws LogException {
        // ����齨��
        // ���齨ע������
        // �����齨
        // -------------------------------------
        // class ��λ��
        // name ����
        // singleton ��̬
        // formater ��ʽ������ -- ILog ��������
        // allwrite ���м���ͨ����� -- ILog ��������
        // debugwrite ���Լ������ -- ILog ��������
        // infowrite ��Ϣ������� -- ILog ��������
        // errorwrite ���󼶱���� -- ILog ��������
        // warningwrite ���漶����� -- ILog ��������
        // -------------------------------------
        name = name.trim();
        ComBean cb = this.comFactory.getComBean(name);
        if (cb == null)
            throw new LogException("�Ҳ����齨[" + name + "]����");
        //
        if (this.comCache.containsKey(name))
            return this.comCache.get(name);
        //
        if (cb.getCommand("class") == null)
            throw new LogException("�齨[" + name + "]û�ж�����������class");
        Object obj = null;
        try {
            Class<?> cls = Class.forName(cb.getCommand("class").getValue());
            obj = cls.newInstance();
            //
            if (cb.getName() == null)
                throw new LogException("�齨û�ж�����������");
            //
            List<CommandAttribut> calist = cb.getCommand();
            for (CommandAttribut ca : calist) {
                boolean mack = false;// ���
                for (String key : this.keys)
                    if (ca.getName().equals(key))
                        mack = true;
                if (mack)
                    continue;
                //
                if (ca.getName().equals("formater") && ILog.class.isAssignableFrom(cls)) {// ��ʽ������
                    // -- ILog ��������
                    try {
                        Method method = cls.getMethod("setFormater", ILogFormater.class);
                        method.invoke(obj, (ILogFormater) this.getComObject(ca.getValue(), params));
                    } catch (Exception e) {
                        throw new LogException("����formater�����������ʱ�����쳣��" + e.getMessage());
                    }
                } else if (ca.getName().equals("allwrite") && ILog.class.isAssignableFrom(cls))// ���м���ͨ�����
                    // -- ILog ��������
                    this.putWrite(obj, cls, ca.getValue(), ILog.LogLevel_ALL, params);
                else if (ca.getName().equals("debugwrite") && ILog.class.isAssignableFrom(cls))// ���Լ������
                    // -- ILog ��������
                    this.putWrite(obj, cls, ca.getValue(), ILog.LogLevel_Debug, params);
                else if (ca.getName().equals("infowrite") && ILog.class.isAssignableFrom(cls))// ��Ϣ�������
                    // -- ILog ��������
                    this.putWrite(obj, cls, ca.getValue(), ILog.LogLevel_Info, params);
                else if (ca.getName().equals("errorwrite") && ILog.class.isAssignableFrom(cls))// ���󼶱����
                    // -- ILog ��������
                    this.putWrite(obj, cls, ca.getValue(), ILog.LogLevel_Error, params);
                else if (ca.getName().equals("warningwrite") && ILog.class.isAssignableFrom(cls))// ���漶�����
                    // -- ILog ��������
                    this.putWrite(obj, cls, ca.getValue(), ILog.LogLevel_Warning, params);
                else {
                    PropertyDescriptor pd = new PropertyDescriptor(ca.getName(), cls);
                    Object o = null;
                    if (pd.getPropertyType().isAssignableFrom(String.class)) {
                        String pStr_1 = "\\x20*\\{\\$\\d+\\}(\\\\.+)*\\\\?";// 1.{$n}
                        // param
                        String value = ca.getValue();
                        if (Pattern.matches(pStr_1, value) == true) {
                            // {$n}
                            Matcher ma_tem = Pattern.compile(pStr_1).matcher(value);
                            ma_tem.find();
                            int paramIndex = StringConvert.parseInt(ma_tem.group(1), 0);
                            Object tem_o = ((paramIndex < params.length) ? params[paramIndex] : null);
                            o = tem_o.toString() + ca.getValue().split("\\{\\$" + paramIndex + "\\}")[1];
                        }
                    } else
                        o = ca.getValue();
                    pd.getWriteMethod().invoke(obj, o);
                }
            }
        } catch (ClassNotFoundException e) {
            throw new LogException("�޷��ҵ���[" + cb.getCommand("class").getValue() + "]");
        } catch (InstantiationException e) {
            throw new LogException("������[" + cb.getCommand("class").getValue() + "]����ʧ�ܣ�����ӿڻ���һ�������ࡣ");
        } catch (IllegalAccessException e) {
            throw new LogException("����ʧ��[" + e.getMessage() + "]����ʧ�ܣ��޷�����Ŀ�귽����");
        } catch (IntrospectionException e) {
            throw new LogException("�������Զ�д��PropertyDescriptorʧ�ܣ�" + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new LogException("��д����ʱ�򲻺Ϸ�����ȷ�Ĳ�����" + e.getMessage());
        } catch (InvocationTargetException e) {
            throw new LogException("�ɹ�����Ŀ�꣬��Ŀ���׳��쳣��" + e.getMessage());
        }
        //
        if (cb.getCommand("singleton") == null || StringConvert.parseBoolean(cb.getCommand("singleton").getValue()) == true)
            this.comCache.put(name, obj);
        //
        return obj;
    }
    /**
     * ����־������������ò�ͬ������־�������
     * @param obj Ҫ������Ե���־�齨����
     * @param clas ��־�齨���������ӦClass�����
     * @param value ��־���������level������ʹ�õ�����齨�����б�
     * @param level Ҫ����ļ���
     * @param params ���������
     * @throws LogException ���������־�쳣��
     */
    private void putWrite(Object obj, Class<?> clas, String value, String level, Object... params) throws LogException {
        try {
            Method method = clas.getMethod("addWrite", ILogWrite.class, String.class);
            String[] names = value.split(",");
            for (String str : names)
                method.invoke(obj, this.getComObject(str, params), level);
        } catch (Exception e) {
            throw new LogException("����allwrite��debugwrite��infowrite��errorwrite��warningwrite�����������ʱ�����쳣��" + e.getMessage());
        }
    }
    /** ��ջ��� */
    public void clear() {
        this.comCache.clear();
    }
}