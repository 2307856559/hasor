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
package org.more.core.copybean;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.more.core.io.AutoCloseInputStream;
import org.more.core.log.ILog;
import org.more.core.log.LogFactory;
/**
 * Bean���������࣬���������ʵ���˿��Խ�Bean���Կ���������bean�л��߿�����map�С�
 * �����߿���ͨ����չBeanType���Խ��ܸ����bean���͡�ϵͳ���Ѿ�֧����
 * Map,Object,IAttribute,ServletRequest.getParameterMap()��
 * ��ʾ��Ĭ�ϵĿ�����ʽ�����(value)�������Ҫǳ��������Ҫ����changeDefaultCopy����
 * �ı��俽�����͡���ѡ�Ŀ�������������һ�������(value)��һ����ǳ����(ref)��
 * ʵ��������ᴦ��java8���������ͼ���string�Լ�date��һ��10�����͡���10�����ͻ����������ǳ������ʽִ�С�
 * @version 2009-5-20
 * @author ������ (zyc@byshell.org)
 */
public final class CopyBeanUtil implements Serializable, Cloneable {
    /**  */
    private static final long             serialVersionUID = 2607862244142020076L;
    /** �����־ */
    private transient static ILog         log              = LogFactory.getLog("org_more_core_copybean");
    /** Bean��������ԭ�� */
    private transient static CopyBeanUtil utilPrototype    = null;
    //=======================================================================================
    /** Bean����������֧�ֵ�Bean���͡� */
    private LinkedList<BeanType>          typeList         = new LinkedList<BeanType>();
    /** Bean����������֧�ֵ�Bean���͡� */
    private Map<String, Copy>             copyList         = new Hashtable<String, Copy>();
    /** Bean�ڿ�����������֧�ֵ�����ת���� */
    private LinkedList<ConvertType>       convertType      = new LinkedList<ConvertType>();
    /** Ĭ�ϵ�Bean�������� */
    private Copy                          defaultCopy      = null;
    /** ˽�л� */
    private CopyBeanUtil() {}
    /** �ô�������ڳ�ʼ����Ĭ��Bean���͡� */
    static {
        CopyBeanUtil.log.debug("BeginInit CopyBeanUtil file = config.properties");
        try {
            CopyBeanUtil utilPrototype = new CopyBeanUtil();
            //��ô��֧��Bean���͵������ļ�
            //config.properties�����ļ���������˳��������������б������
            InputStream is_1 = CopyBeanUtil.class.getResourceAsStream("/org/more/core/copybean/config.properties");
            Properties pro_1 = new Properties();
            pro_1.load(new AutoCloseInputStream(is_1));//װ�������ļ�
            //
            for (Object n : pro_1.keySet()) {
                String key = n.toString();
                String value = pro_1.get(n).toString();
                CopyBeanUtil.log.debug("see config.properties key=" + key + " value=" + value);
                //
                if (Pattern.matches(" *defaultCopy *", key) == true) {
                    //��ʼ��defaultCopy
                    Class<?> cls = Class.forName(value);
                    utilPrototype.defaultCopy = (Copy) cls.newInstance();
                    utilPrototype.defaultCopy.setConvertType(utilPrototype.convertType);//ע������ת��֧�ּ���
                } else if (Pattern.matches(" *rw\\..*", key) == true) {
                    //��ʼ��typeList
                    Class<?> cls = Class.forName(value);
                    BeanType obj = (BeanType) cls.newInstance();
                    utilPrototype.typeList.addLast(obj);
                } else if (Pattern.matches(" *copy\\..*", key) == true) {
                    Matcher ma = Pattern.compile(" *copy\\.(.*)").matcher(key);
                    ma.find();
                    //��ʼ��copyList
                    Class<?> cls = Class.forName(value);
                    Copy obj = (Copy) cls.newInstance();
                    obj.setConvertType(utilPrototype.convertType);//ע������ת��֧�ּ���
                    utilPrototype.copyList.put(ma.group(1), obj);
                } else if (Pattern.matches(" *type\\..*", key) == true) {
                    //��֧�ֵ����� ��ʼ��copyList
                    Class<?> cls = Class.forName(value);
                    ConvertType obj = (ConvertType) cls.newInstance();
                    utilPrototype.convertType.addLast(obj);
                }
            }
            //����Ĭ�Ͽ�������
            CopyBeanUtil.utilPrototype = utilPrototype;
            CopyBeanUtil.log.debug("EndInit CopyBeanUtil file = config.properties");
        } catch (Exception e) {
            CopyBeanUtil.log.debug("InitError config.properties message=" + e.getMessage());
        }
    }
    /**
     * ע��һ���µ�Bean������֧�ֵ����ͣ���ע���Bean����Ҫ��ϵͳ���õ����ȼ��ߡ�ͨ���÷���ע������ͽ����Ժ�����CopyBeanUtilʵ���еõ�֧�֡�
     * @param type �µ�Bean����
     */
    public static void regeditStaticType(BeanType type) {
        CopyBeanUtil.utilPrototype.typeList.addFirst(type);
    }
    /**
     * ���ĳ������ת�����󡣻�������õ�ConvertType������CopyBeanUtil��ԭ���е����ݡ��޸ĸö��󽫻�Ӱ���Ժ������CopyBeanUtilʵ����
     * @param typeClass ���Ԥ���ת����ʽ��
     * @return ����ĳ������ת������
     */
    public static ConvertType getStaticConvertType(Class<? extends ConvertType> typeClass) {
        for (ConvertType type : CopyBeanUtil.utilPrototype.convertType)
            if (typeClass.isInstance(type) == true)
                return type;
        return null;
    }
    /**
     * ͨ����¡��ʽ����һ���µ�CopyBeanUtil�������Bean�����������¡ʧ���򷵻�null��
     * @return ���ؿ�¡��ʽ����һ���µ�CopyBeanUtil�������Bean������
     */
    public static CopyBeanUtil newInstance() {
        try {
            return (CopyBeanUtil) CopyBeanUtil.utilPrototype.clone();
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * ִ��Bean��������object�����е����Կ�����to�С����سɹ������������ԣ��������-1��ʾ��������֧�֣��������0��ʾû�����Ա�������
     * @param object Ҫ������ԭ����
     * @param to Ҫ������Ŀ�Ķ���
     * @param mode ����ģʽ���ò����൱�ڵ���CopyBeanUtil�����changeDefaultCopy������
     * @return ���سɹ������������ԣ��������-1��ʾ��������֧�֣��������0��ʾû�����Ա�������
     */
    public int copy(Object object, Object to, String... mode) {
        if (object == null || to == null)
            return -1;
        //
        if (mode != null && mode.length >= 1)
            this.changeDefaultCopy(mode[0]);
        //
        //��ԴBean���Լ�������
        Map<String, PropertyReaderWrite> o_1 = null;
        BeanType o_2_type = null;
        //�������ͱ������Ķ����л�ȡ������Ҫ���������Լ���
        CopyBeanUtil.log.debug("copybean obj=" + object + " to=" + to + " begin find BeanType");
        for (BeanType type : this.typeList) {
            //�����ԴBean�Ƿ�֧��
            if (type.checkObject(object) == true)
                if (o_1 == null)
                    o_1 = type.getPropertys(object);//from
            //��鿽��Ŀ��Bean�Ƿ�֧��
            if (type.checkObject(to) == true)
                if (o_2_type == null)
                    o_2_type = type; //to
            if (o_1 != null && o_2_type != null)
                break;
        }
        CopyBeanUtil.log.debug("copybean end find BeanType objType=" + o_1);
        //������Լ������п��򷵻�-1
        if (o_1 == null)
            return -1;
        //
        CopyBeanUtil.log.debug("copybean begin copy ...");
        int i = 0;
        for (String key : o_1.keySet()) {
            PropertyReaderWrite prw = o_2_type.getPropertyRW(to, key);
            Copy copy_1 = null;
            try {
                copy_1 = (Copy) this.defaultCopy.clone();
            } catch (CloneNotSupportedException e) {
                continue;
            }
            copy_1.setCopyBeanUtil(this);
            copy_1.setRw(o_1.get(key));
            //
            if (prw.canWrite() == true && copy_1.canReader() == true) {
                copy_1.copyTo(prw);
                i++;
                CopyBeanUtil.log.debug("copybean copy name=" + key);
            } else
                CopyBeanUtil.log.debug("copybean copy Ignore name=" + key);
        }
        CopyBeanUtil.log.debug("copybean end copy copy count =" + i);
        return i;
    }
    /**
     * ע��һ���µ�Bean������֧�ֵ����ͣ���ע���Bean����Ҫ��ϵͳ���õ����ȼ��ߡ�ͨ���÷���ע�������ֻ�е�ǰʵ���еõ�֧�֡�
     * @param type �µ�Bean����
     */
    public void regeditType(BeanType type) {
        this.typeList.addFirst(type);
    }
    /**
     * ���ĳ������ת������
     * @param typeClass ���Ԥ���ת����ʽ��
     * @return ����ĳ������ת������
     */
    public ConvertType getConvertType(Class<? extends ConvertType> typeClass) {
        for (ConvertType type : this.convertType)
            if (typeClass.isInstance(type) == true)
                return type;
        return null;
    }
    protected Object clone() throws CloneNotSupportedException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            oos.flush();
            //
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
            CopyBeanUtil util = (CopyBeanUtil) ois.readObject();
            return util;
        } catch (Exception e) {
            throw new CloneNotSupportedException("�ڿ�¡ʱ�����쳣 msg=" + e.getMessage());
        }
    }
    /**
     * ע��һ���µ�Bean�������ÿ��������������ν��п���������ϵͳĬ��ʹ�õ���ǳ��������������Ҫ�û�����ʵ�֡�
     * �÷�������ʹ�û����뿽�����̣����õ���չ����ע���µĿ�������֮����Ҫ�ı䵱ǰ��������ע�����copy�ſ�����Ч��
     * �޸ĸö��󽫻�Ӱ���Ժ������CopyBeanUtilʵ����
     * @param copy �µĿ�������
     */
    public static void regeditStaticCopy(Copy copy) {
        CopyBeanUtil.utilPrototype.copyList.put(copy.getName(), copy);
    }
    /**
     * ��ȡCopyBeanUtil�ľ�̬��������ͨ���÷�����������Ĭ�Ͽ��������һЩ���ԡ�����������Զ��忽������������Ǻ��а����ġ�
     * ע���޸ĸö��󽫻�Ӱ���Ժ������CopyBeanUtilʵ����
     * @return ����CopyBeanUtil�ľ�̬Ĭ�Ͽ�������
     */
    public static Copy getStaticDefaultCopy() {
        return CopyBeanUtil.utilPrototype.defaultCopy;
    }
    /**
     * �ı䵱ǰĬ�Ͽ�������Ϊָ�����ƵĿ������󡣲����ǿ����������������ƿ���ͨ��copy.getName()��ʽ��á�
     * ͨ���÷����ı��Ĭ�Ͽ�������ֻ�е�ǰʵ���еõ�֧�֡�ע���޸ĸö��󽫻�Ӱ���Ժ������CopyBeanUtilʵ����
     * ���Ҫ�ı��Ŀ�꿽�����󲻴�������Ըÿ�����
     * @param copyName �ı��Ŀ�꿽���������������ƿ���ͨ��copy.getName()��ʽ��á�
     */
    public static void changeStaticDefaultCopy(String copyName) {
        CopyBeanUtil.utilPrototype.changeDefaultCopy(copyName);
    }
    /**
     * ע��һ���µ�Bean�������ÿ��������������ν��п���������ϵͳĬ��ʹ�õ���ǳ��������������Ҫ�û�����ʵ�֡�
     * �÷�������ʹ�û����뿽�����̣����õ���չ����ע���µĿ�������֮����Ҫ�ı䵱ǰ��������ע�����copy�ſ�����Ч��
     * ͨ���÷���ע��Ŀ�������ֻ�е�ǰʵ���еõ�֧�֡�
     * @param copy �µĿ�������
     */
    public void regeditCopy(Copy copy) {
        if (this.copyList.containsKey(copy.getName()) == false)
            this.copyList.put(copy.getName(), copy);
    }
    /**
     * ��ȡ��ǰ��Ĭ�Ͽ�������ͨ���÷�����������Ĭ�Ͽ��������һЩ���ԡ�����������Զ��忽������������Ǻ��а����ġ�
     * @return ���ص�ǰ��Ĭ�Ͽ�������
     */
    public Copy getDefaultCopy() {
        return defaultCopy;
    }
    /**
     * �ı䵱ǰĬ�Ͽ�������Ϊָ�����ƵĿ������󡣲����ǿ����������������ƿ���ͨ��copy.getName()��ʽ��á�
     * ͨ���÷����ı��Ĭ�Ͽ�������ֻ�е�ǰʵ���еõ�֧�֡����Ҫ�ı��Ŀ�꿽�����󲻴�������Ըÿ�����
     * @param copyName �ı��Ŀ�꿽���������������ƿ���ͨ��copy.getName()��ʽ��á�
     */
    public void changeDefaultCopy(String copyName) {
        if (this.copyList.containsKey(copyName) == true)
            this.defaultCopy = this.copyList.get(copyName);
    }
}