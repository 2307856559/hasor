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
package org.more.core.global;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.more.core.error.FormatException;
import org.more.core.error.SupportException;
import org.more.core.io.AutoCloseInputStream;
import org.more.core.ognl.Ognl;
import org.more.core.ognl.OgnlException;
import org.more.util.ResourcesUtil;
import org.more.util.StringConvertUtil;
import org.more.util.attribute.AttBase;
import org.more.util.attribute.IAttribute;
import org.more.util.attribute.SequenceStack;
import org.more.util.attribute.TransformToMap;
/**
* 
* @version : 2011-9-3
* @author ������ (zyc@byshell.org)
*/
public abstract class Global implements IAttribute<Object> {
    private Map<String, IAttribute<String>> listConfigAtt = null;
    private SequenceStack<String>           allConfigAtt  = null;
    private Object                          context       = null;
    /*------------------------------------------------------------------------*/
    public Global() {
        this.listConfigAtt = new HashMap<String, IAttribute<String>>();
        this.allConfigAtt = new SequenceStack<String>();
    };
    /*------------------------------------------------------------------------*/
    /**����ȫ�����ò��������ҷ�����{@link Object}��ʽ����*/
    public Object getObject(Enum<?> name) {
        return this.getToType(name, Object.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Object}��ʽ����*/
    public Object getObject(String name) {
        return this.getToType(name, Object.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Character}��ʽ����*/
    public Character getChar(Enum<?> name) {
        return this.getToType(name, Character.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Character}��ʽ����*/
    public Character getChar(String name) {
        return this.getToType(name, Character.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link String}��ʽ����*/
    public String getString(Enum<?> name) {
        return this.getToType(name, String.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link String}��ʽ����*/
    public String getString(String name) {
        return this.getToType(name, String.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Boolean}��ʽ����*/
    public Boolean getBoolean(Enum<?> name) {
        return this.getToType(name, Boolean.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Boolean}��ʽ����*/
    public Boolean getBoolean(String name) {
        return this.getToType(name, Boolean.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Short}��ʽ����*/
    public Short getShort(Enum<?> name) {
        return this.getToType(name, Short.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Short}��ʽ����*/
    public Short getShort(String name) {
        return this.getToType(name, Short.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Integer}��ʽ����*/
    public Integer getInteger(Enum<?> name) {
        return this.getToType(name, Integer.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Integer}��ʽ����*/
    public Integer getInteger(String name) {
        return this.getToType(name, Integer.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Long}��ʽ����*/
    public Long getLong(Enum<?> name) {
        return this.getToType(name, Long.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Long}��ʽ����*/
    public Long getLong(String name) {
        return this.getToType(name, Long.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Float}��ʽ����*/
    public Float getFloat(Enum<?> name) {
        return this.getToType(name, Float.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Float}��ʽ����*/
    public Float getFloat(String name) {
        return this.getToType(name, Float.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Double}��ʽ����*/
    public Double getDouble(Enum<?> name) {
        return this.getToType(name, Double.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Double}��ʽ����*/
    public Double getDouble(String name) {
        return this.getToType(name, Double.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ����*/
    public Date getDate(Enum<?> name) {
        return this.getToType(name, Date.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ����*/
    public Date getDate(String name) {
        return this.getToType(name, Date.class);
    };
    public boolean contains(String name) {
        return this.allConfigAtt.contains(name);
    }
    public Object getAttribute(String name) {
        return this.getObject(name);
    }
    public Map<String, Object> toMap() {
        return new TransformToMap<Object>(this);
    }
    public String[] getAttributeNames() {
        return this.allConfigAtt.getAttributeNames();
    }
    public void setAttribute(String name, Object value) {
        throw new SupportException("Global����֧�ָ÷�����");
    }
    public void removeAttribute(String name) {
        throw new SupportException("Global����֧�ָ÷�����");
    }
    public void clearAttribute() {
        throw new SupportException("Global����֧�ָ÷�����");
    }
    /*------------------------------------------------------------------------*/
    /**��ͼ�����Ը�ö�����������������ļ�����ڸ�ö���ϵ������ļ���������Է��ʵ������ļ��������������{@link #getOriginalString(String)}��ͬ��*/
    public String getOriginalString(Enum<?> name) {
        if (name == null)
            return null;
        //1.ȡ������
        String simpleName = name.getClass().getSimpleName();
        //2.��list�л�ȡConfig
        IAttribute<String> att = null;
        if (this.listConfigAtt.containsKey(simpleName) == false)
            att = this.allConfigAtt;
        else
            att = this.listConfigAtt.get(simpleName);
        return att.getAttribute(name.name());
    };
    /**��������˳�������м����ȫ�������ļ���Ѱ��ָ�����Ƶ�����ֵ�����ҽ�ԭʼ��Ϣ���ء�*/
    public String getOriginalString(String name) {
        if (name == null)
            return null;
        return this.allConfigAtt.getAttribute(name);
    };
    /**��һ��ԭʼ��������Ϣ��ӵ��б��У���������Ѿ�ʹ�õ������򸲸ǡ�*/
    protected void addConfig(String name, IAttribute<String> config) {
        this.listConfigAtt.put(name, config);
    };
    /**���name��ʾ��������Ϣ�Ƿ��Ѿ�ע�ᡣ*/
    protected boolean addConfig(String name) {
        return this.listConfigAtt.containsKey(name);
    };
    /**����ȫ�����ò��������ҷ���toType����ָ�������͡�*/
    public <T> T getToType(Enum<?> enumItem, Class<T> toType) {
        String oriString = this.getOriginalString(enumItem);
        if (oriString == null)
            return null;
        //
        Object var = this.evalEL(oriString);
        return StringConvertUtil.changeType(var, toType);
    };
    /**����ȫ�����ò��������ҷ���toType����ָ�������͡�*/
    public <T> T getToType(String name, Class<T> toType) {
        String oriString = this.getOriginalString(name);
        if (oriString == null)
            return null;
        //
        Object var = this.evalEL(oriString);
        return StringConvertUtil.changeType(var, toType);
    };
    /*------------------------------------------------------------------------*/
    /**��һ��ö�ٵ�һ�������ļ��ϣ�������ö��������{@link PropFile}ע����ʹ�ø�ע���ǵ������ļ�����װ�أ������װ����ö����ͬ���������ļ���*/
    public void addEnum(Class<? extends Enum<?>> enumType) throws Throwable {
        PropFile pFile = enumType.getAnnotation(PropFile.class);
        if (pFile != null)
            if (pFile.file().equals("") == false)
                this.addResource(enumType, new File(pFile.file()));
            else if (pFile.uri().equals("") == false)
                this.addResource(enumType, new URI(pFile.uri()));
            else if (pFile.value().equals("") == false)
                this.addResource(enumType, pFile.value());
            else
                this.addResource(enumType, enumType.getSimpleName());
        this.addResource(enumType, enumType.getSimpleName());
    };
    /**���һ�������ļ������Ұ󶨵�ָ����ö���ϡ�*/
    public void addResource(Class<? extends Enum<?>> enumType, String resource) throws IOException {
        InputStream stream = ResourcesUtil.getResourceAsStream(resource);
        IAttribute<String> att = this.loadConfig(stream);
        this.addAttribute(enumType, att);
    };
    /**���һ�������ļ������Ұ󶨵�ָ����ö���ϡ�*/
    public void addResource(Class<? extends Enum<?>> enumType, URI resource) throws MalformedURLException, IOException {
        IAttribute<String> att = this.loadConfig(new AutoCloseInputStream(resource.toURL().openStream()));
        this.addAttribute(enumType, att);
    };
    /**���һ�������ļ������Ұ󶨵�ָ����ö���ϡ�*/
    public void addResource(Class<? extends Enum<?>> enumType, File resource) throws IOException {
        IAttribute<String> att = this.loadConfig(new AutoCloseInputStream(new FileInputStream(resource)));
        this.addAttribute(enumType, att);
    };
    /*------------------------------------------------------------------------*/
    /**���������󣬲��ҽ������Ľ������Ϊ{@link IAttribute}�ӿ���ʽ��*/
    protected abstract IAttribute<String> loadConfig(InputStream stream) throws IOException;
    /**���������Ķ���*/
    protected Object getContext() {
        return this.context;
    };
    /**���������Ķ���*/
    protected void setContext(Object context) {
        this.context = context;
    };
    /**���������Ķ���*/
    protected Map<String, Object> getRoot() {
        AttBase<Object> root = new AttBase<Object>();
        root.put("context", this.context);
        return root;
    };
    /**��ӵ�������*/
    private void addAttribute(Class<? extends Enum<?>> enumType, IAttribute<String> att) {
        String name = enumType.getSimpleName();
        this.listConfigAtt.put(name, att);
        this.allConfigAtt.putStack(att);
    }
    //
    //
    private <T> T evalEL(String elString) {
        //1.����elString
        elString = elString;
        //2.����elString
        try {
            return (T) Ognl.getValue(elString, this.getRoot());
        } catch (OgnlException e) {
            throw new FormatException(elString + "����ΪEL��������");
        }
    };
    /*------------------------------------------------------------------------*/
    public static Global createForXml(String xmlPath) throws IOException {
        return createForXml(xmlPath, null);
    };
    public static Global createForFile(String propPath) throws IOException {
        return createForFile(propPath, null);
    };
    public static Global createForXml(String xmlPath, Object context) throws IOException {
        XmlGlobal xml = new XmlGlobal();
        xml.setContext(context);
        xml.addResource(GlobalEnum.class, xmlPath);
        return xml;
    };
    public static Global createForFile(String propPath, Object context) throws IOException {
        FileGlobal file = new FileGlobal();
        file.setContext(context);
        file.addResource(GlobalEnum.class, propPath);
        return file;
    };
};
enum GlobalEnum {}