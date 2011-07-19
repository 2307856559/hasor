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
package org.more.core.log;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * �齨����
 * @version 2009-5-13
 * @author ������ (zyc@byshell.org)
 */
class ComFactory {
    private static final String[] x_path  = new String[3];
    // ===============================================================
    static {
        x_path[0] = "(logformater)\\.(\\w+)\\.(\\w+)"; //
        x_path[1] = "(log)\\.(\\w+)\\.(\\w+)"; //
        x_path[2] = "(logwrite)\\.(\\w+)\\.(\\w+)"; //
    }
    // ===============================================================
    private Map<String, ComBean>  comBean = new Hashtable<String, ComBean>(0);
    // ===============================================================
    private ComFactory() {}
    // ===============================================================
    /**
     * ����ComFactory����
     * @return ���ش�����ComFactory����
     */
    public static ComFactory getComFactory() {
        return new ComFactory();
    }
    /** ����齨 */
    public void clear() {
        this.comBean.clear();
    }
    /**
     * ����Ѿ�������齨���Ƽ���
     * @return �����Ѿ�������齨���Ƽ��ϡ�
     */
    public String[] getComNames() {
        String[] names = new String[this.comBean.size()];
        this.comBean.keySet().toArray(names);
        return names;
    }
    /**
     * ���һ�����Ե�ĳ���齨�С�������ӵ��ĸ�����������ֵȷ����������齨�������򴴽����齨��
     * @param ca Ҫ��ӵ�����
     */
    public void addAttribut(CommandAttribut ca) {
        for (String str : x_path) {
            String[] res = getInfo(str, ca);
            ComBean cb = null;
            if (res == null)
                continue;
            else if (res[0].equals("logformater"))
                cb = loadComBean(res[1]);
            else if (res[0].equals("log"))
                cb = loadComBean(res[1]);
            else if (res[0].equals("logwrite"))
                cb = loadComBean(res[1]);
            //
            ca.setName(res[2]);
            cb.addCommand(ca);
            if (this.comBean.containsKey(cb.getName()) == false)
                this.comBean.put(cb.getName(), cb);
            return;
        }
        // System.out.println("Not Attribut");
    }
    private ComBean loadComBean(String name) {
        ComBean cb = null;
        if (this.comBean.containsKey(name))
            cb = this.comBean.get(name);
        else {
            cb = new ComBean();
            cb.setName(name);
        }
        //
        return cb;
    }
    /**
     * ����齨���塣
     * @param name Ҫ��õ����Զ������ơ�
     * @return �����齨���塣
     */
    public ComBean getComBean(String name) {
        if (this.comBean.containsKey(name))
            return this.comBean.get(name);
        else
            return null;
    }
    /**
     * �����������Ի��ָ��������Ϣ��
     * @param xpath �������ԡ�
     * @param ca Ҫ��ȡ����Ϣ�塣
     * @return �����������Ի��ָ��������Ϣ��
     */
    public String[] getInfo(String xpath, CommandAttribut ca) {
        String[] str = null;
        //
        if (Pattern.matches(xpath, ca.getName()) == true) {
            str = new String[3];
            Matcher ma_tem = Pattern.compile(xpath).matcher(ca.getName());
            ma_tem.find();
            str[0] = ma_tem.group(1);
            str[1] = ma_tem.group(2);
            str[2] = ma_tem.group(3);
        }
        //
        return str;
    }
    /**
     * ���Դ����齨���塣
     * @param name �齨����
     */
    public void createComBean(String name) {
        if (this.comBean.containsKey(name))
            throw new LogRepeatException("�ظ�����");
        ComBean cb = new ComBean();
        cb.setName(name);
        this.comBean.put(name, cb);
    }
}