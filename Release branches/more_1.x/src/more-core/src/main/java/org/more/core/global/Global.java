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
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.more.core.error.FormatException;
import org.more.core.error.InitializationException;
import org.more.core.error.SupportException;
import org.more.core.global.impl.FileGlobal;
import org.more.core.global.impl.XmlGlobal;
import org.more.core.io.AutoCloseInputStream;
import org.more.core.json.JsonUtil;
import org.more.core.ognl.Ognl;
import org.more.core.ognl.OgnlException;
import org.more.util.ResourcesUtil;
import org.more.util.StringConvertUtil;
import org.more.util.attribute.AttBase;
import org.more.util.attribute.IAttribute;
import org.more.util.attribute.SequenceStack;
import org.more.util.attribute.TransformToMap;
/**
* ȫ�ֳ�����ȡ��
* @version : 2011-9-3
* @author ������ (zyc@byshell.org)
*/
public abstract class Global implements IAttribute<Object> {
    /**���ö�����*/
    private final static String                _global      = "_global";
    private final static String                EnableEL     = "_global.enableEL";
    private final static String                EnableJson   = "_global.enableJson";
    public final static String[]               Configs      = new String[] { "global_config.properties", "META-INF/global_config.properties", "META-INF/resource/core/global_config.properties" };
    /**��ӵ����������ļ��������ﱣ�棬���ݲ�ͬ��ע���������з���*/
    private Map<String, SequenceStack<String>> poolMap      = null;
    private SequenceStack<String>              allData      = null;
    private SequenceStack<Object>              rootMap      = null;
    //
    private Object                             context      = null;
    private GlobalObject                       globalObject = null;
    //
    /*------------------------------------------------------------------------*/
    public Global(IAttribute<String> configs) {
        this();
        this.addConfig("", configs);
    }
    public Global() {
        this.poolMap = new LinkedHashMap<String, SequenceStack<String>>();
        this.allData = new SequenceStack<String>();
        //����el root����
        this.rootMap = new SequenceStack<Object>();
        this.rootMap.putStack(new AttBase<Object>());
        this.rootMap.putStack(this);
        this.globalObject = new GlobalObject(this);
        this.rootMap.setAttribute(_global, new TransformToMap<Object>(this.globalObject));//���ö���
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
    /**����ȫ�����ò��������ҷ�����{@link Object}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Object getObject(Enum<?> name, Object defaultValue) {
        return this.getToType(name, Object.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Object}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Object getObject(String name, Object defaultValue) {
        return this.getToType(name, Object.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Character}��ʽ����*/
    public Character getChar(Enum<?> name) {
        return this.getToType(name, Character.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Character}��ʽ����*/
    public Character getChar(String name) {
        return this.getToType(name, Character.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Character}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Character getChar(Enum<?> name, Character defaultValue) {
        return this.getToType(name, Character.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Character}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Character getChar(String name, Character defaultValue) {
        return this.getToType(name, Character.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link String}��ʽ����*/
    public String getString(Enum<?> name) {
        return this.getToType(name, String.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link String}��ʽ����*/
    public String getString(String name) {
        return this.getToType(name, String.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link String}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public String getString(Enum<?> name, String defaultValue) {
        return this.getToType(name, String.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link String}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public String getString(String name, String defaultValue) {
        return this.getToType(name, String.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Boolean}��ʽ����*/
    public Boolean getBoolean(Enum<?> name) {
        return this.getToType(name, Boolean.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Boolean}��ʽ����*/
    public Boolean getBoolean(String name) {
        return this.getToType(name, Boolean.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Boolean}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Boolean getBoolean(Enum<?> name, Boolean defaultValue) {
        return this.getToType(name, Boolean.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Boolean}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Boolean getBoolean(String name, Boolean defaultValue) {
        return this.getToType(name, Boolean.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Short}��ʽ����*/
    public Short getShort(Enum<?> name) {
        return this.getToType(name, Short.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Short}��ʽ����*/
    public Short getShort(String name) {
        return this.getToType(name, Short.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Short}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Short getShort(Enum<?> name, Short defaultValue) {
        return this.getToType(name, Short.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Short}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Short getShort(String name, Short defaultValue) {
        return this.getToType(name, Short.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Integer}��ʽ����*/
    public Integer getInteger(Enum<?> name) {
        return this.getToType(name, Integer.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Integer}��ʽ����*/
    public Integer getInteger(String name) {
        return this.getToType(name, Integer.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Integer}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Integer getInteger(Enum<?> name, Integer defaultValue) {
        return this.getToType(name, Integer.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Integer}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Integer getInteger(String name, Integer defaultValue) {
        return this.getToType(name, Integer.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Long}��ʽ����*/
    public Long getLong(Enum<?> name) {
        return this.getToType(name, Long.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Long}��ʽ����*/
    public Long getLong(String name) {
        return this.getToType(name, Long.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Long}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Long getLong(Enum<?> name, Long defaultValue) {
        return this.getToType(name, Long.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Long}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Long getLong(String name, Long defaultValue) {
        return this.getToType(name, Long.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Float}��ʽ����*/
    public Float getFloat(Enum<?> name) {
        return this.getToType(name, Float.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Float}��ʽ����*/
    public Float getFloat(String name) {
        return this.getToType(name, Float.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Float}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Float getFloat(Enum<?> name, Float defaultValue) {
        return this.getToType(name, Float.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Float}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Float getFloat(String name, Float defaultValue) {
        return this.getToType(name, Float.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Double}��ʽ����*/
    public Double getDouble(Enum<?> name) {
        return this.getToType(name, Double.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Double}��ʽ����*/
    public Double getDouble(String name) {
        return this.getToType(name, Double.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Double}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Double getDouble(Enum<?> name, Double defaultValue) {
        return this.getToType(name, Double.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Double}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Double getDouble(String name, Double defaultValue) {
        return this.getToType(name, Double.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ����*/
    public Date getDate(Enum<?> name) {
        return this.getToType(name, Date.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ����*/
    public Date getDate(String name) {
        return this.getToType(name, Date.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Date getDate(Enum<?> name, Date defaultValue) {
        return this.getToType(name, Date.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Date getDate(String name, Date defaultValue) {
        return this.getToType(name, Date.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Date getDate(Enum<?> name, long defaultValue) {
        return this.getToType(name, Date.class, new Date(defaultValue));
    };
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Date getDate(String name, long defaultValue) {
        return this.getToType(name, Date.class, new Date(defaultValue));
    };
    /*------------------------------------------------------------------------*/
    /**��ͼ�����Ը�ö�����������������ļ�����ڸ�ö���ϵ������ļ���������Է��ʵ������ļ��������������{@link #getOriginalString(String)}��ͬ��*/
    public String getOriginalString(String scope, String name) {
        if (name == null)
            return null;
        //2.��list�л�ȡConfig
        IAttribute<String> config = null;
        if (scope == null || this.poolMap.containsKey(scope) == false)
            config = this.allData;
        else
            config = this.poolMap.get(scope);
        return config.getAttribute(name);
    };
    /**��������˳�������м����ȫ�������ļ���Ѱ��ָ�����Ƶ�����ֵ�����ҽ�ԭʼ��Ϣ���ء�*/
    public String getOriginalString(String name) {
        return this.getOriginalString(null, name);
    };
    /**����ȫ�����ò��������ҷ���toType����ָ�������͡�*/
    public <T> T getToType(String name, Class<T> toType, T defaultValue) {
        String oriString = this.getOriginalString(null, name);
        if (oriString == null)
            return defaultValue;
        //
        Object var = this.getEval(oriString);
        return StringConvertUtil.changeType(var, toType, defaultValue);
    }
    /**����ȫ�����ò��������ҷ���toType����ָ�������͡�*/
    public <T> T getToType(Enum<?> enumItem, Class<T> toType, T defaultValue) {
        if (enumItem == null)
            return defaultValue;
        String oriString = this.getOriginalString(enumItem.getClass().getSimpleName(), enumItem.name());
        if (oriString == null)
            return defaultValue;
        //
        Object var = this.getEval(oriString);
        return StringConvertUtil.changeType(var, toType, defaultValue);
    }
    /**����ȫ�����ò��������ҷ���toType����ָ�������͡�*/
    public <T> T getToType(Enum<?> enumItem, Class<T> toType) {
        return this.getToType(enumItem, toType, null);
    };
    /**����ȫ�����ò��������ҷ���toType����ָ�������͡�*/
    public <T> T getToType(String name, Class<T> toType) {
        return this.getToType(name, toType, null);
    };
    /*------------------------------------------------------------------------*/
    public boolean contains(String name) {
        return this.allData.contains(name);
    }
    /**�÷�����ͬ��{@link #getObject(String)}*/
    public Object getAttribute(String name) {
        return this.getObject(name);
    }
    /**��{@link Global}��ת����{@link Map}�ӿ���ʽ��*/
    public Map<String, Object> toMap() {
        return new TransformToMap<Object>(this);
    }
    public String[] getAttributeNames() {
        return this.allData.getAttributeNames();
    }
    /**Global����֧�ָ÷�����*/
    public void setAttribute(String name, Object value) {
        throw new SupportException("Global����֧�ָ÷�����");
    }
    /**Global����֧�ָ÷�����*/
    public void removeAttribute(String name) {
        throw new SupportException("Global����֧�ָ÷�����");
    }
    /**Global����֧�ָ÷�����*/
    public void clearAttribute() {
        throw new SupportException("Global����֧�ָ÷�����");
    }
    /*------------------------------------------------------------------------*/
    /**��һ��ԭʼ��������Ϣ��ӵ�Global�У���������Ѿ�ʹ�õ�������׷�ӡ�*/
    public void addConfig(Class<? extends Enum<?>> enumType, IAttribute<String> config) {
        if (enumType == null || config == null)
            throw new NullPointerException("��enumType�� or ��config�� param is null");
        this.addConfig(enumType.getSimpleName(), config);
    }
    /**��һ��ԭʼ��������Ϣ��ӵ�Global�У���������Ѿ�ʹ�õ�������׷�ӡ�*/
    public void addConfig(String name, IAttribute<String> config) {
        if (name == null || config == null)
            throw new NullPointerException("��name�� or ��config�� param is null");
        SequenceStack<String> stack = null;
        if (this.poolMap.containsKey(name) == false) {
            //������һ����ͬʱ���������ӵ�allData��
            stack = new SequenceStack<String>();
            this.poolMap.put(name, stack);
            this.allData.putStack(stack);
        } else
            stack = this.poolMap.get(name);
        stack.putStack(config);
    };
    /**���name��ʾ��������Ϣ�Ƿ��Ѿ�ע�ᡣ*/
    protected boolean containsConfig(String name) {
        return this.poolMap.containsKey(name);
    };
    /**���������󣬲��ҽ������Ľ������Ϊ{@link IAttribute}�ӿ���ʽ��*/
    protected IAttribute<String> loadConfig(InputStream stream) throws IOException {
        return new AttBase<String>();
    };
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
        return this.rootMap.toMap();
    };
    /**����һ�������˶��ٸ�config�������á�*/
    protected int getConfigGroupCount() {
        return this.poolMap.size();
    }
    /**����һ�������˶��ٸ�config���á�*/
    protected int getConfigAllCount() {
        return this.allData.size();
    }
    /**���һ����������*/
    public void addGlobalProperty(String name, GlobalProperty property) {
        if (name == null || name.equals("") == true || property == null)
            throw new NullPointerException("");
        this.globalObject.addGlobalProperty(name, property);
    }
    /*------------------------------------------------------------------------*/
    /*������EL���ַ���ת����ΪEL�ַ�����*/
    private String toEL(String string) {
        ELStateVisitor elv = new ELStateVisitor();
        ELParser elParser = new ELParser(elv);
        elParser.parser(string);
        return elv.getEL();
    };
    private <T> T getEval(String elString) {
        elString = elString.trim();
        if (elString == null || elString.equals("") == true)
            return null;
        //1.��ȡ���λ��
        StringBuffer elStr = new StringBuffer(elString.trim());
        char firstChar = elStr.charAt(0);
        char lastChar = elStr.charAt(elStr.length() - 1);
        //2.��ȡβ��
        if (lastChar == ';') {
            elStr.deleteCharAt(elStr.length());
            lastChar = elStr.charAt(elStr.length() - 1);//ȥ��β���ķֺ�
        }
        //3.�ж�����
        elStr.deleteCharAt(0);
        elStr.deleteCharAt(elStr.length() - 1);
        Object res = null;
        if (firstChar == '(' && lastChar == ')')
            res = this.$evalJSON(elStr.toString());
        else if (firstChar == '{' && lastChar == '}')
            res = this.$evalEL(elStr.toString());
        else {
            //   .*\$\{.*\}.*
            if (elString.matches(".*\\$\\{.*\\}.*") == false)
                res = this.$evalString(elString);//��ͨ�ַ���
            else
                res = this.$evalEL(toEL(elString));//����EL���ַ���
        }
        return (T) res;
    }
    /**�ڽ��������и�������ַ���*/
    protected String $evalString(String string) {
        return string;
    };
    /**�ڽ��������и������EL�������_global.enableEL��������Ϊfalse�򲻽���json���ݡ�*/
    protected Object $evalEL(String elString) {
        //1.����elString
        if (this.getBoolean(EnableEL, true) == false)
            return this.$evalString(elString);
        //2.����elString
        try {
            return Ognl.getValue(elString, this.getRoot());
        } catch (OgnlException e) {
            throw new FormatException(elString + "����ΪEL��������");
        }
    };
    /**�ڽ��������и������Json�������_global.enableJson��������Ϊfalse�򲻽���json���ݡ�*/
    protected Object $evalJSON(String jsonString) {
        if (this.getBoolean(EnableJson, true) == false)
            return this.$evalString(jsonString);
        else
            return JsonUtil.transformToObject(jsonString);
    };
    /*------------------------------------------------------------------------*/
    public static Global newInstance(String factoryName, Object... params) throws IOException {
        IAttribute<String> configAtt = ResourcesUtil.getPropertys(Configs);
        String factoryType = configAtt.getAttribute(factoryName);
        if (factoryType == null)
            throw new SupportException("Global factory ��" + factoryName + "�� is not define.");
        try {
            GlobalFactory globalFactory = (GlobalFactory) Thread.currentThread().getContextClassLoader().loadClass(factoryType).newInstance();
            return globalFactory.createGlobal(params);
        } catch (Exception e) {
            throw new InitializationException("init error can`t create GlobalFactory  " + factoryName + "=" + factoryType);
        }
    };
    public static Global newInstance(IAttribute<String> configs) {
        return new Global(configs) {};
    }
};
//class ELWrite {
//    private static final Map<Character, String> charset     = new HashMap<Character, String>(); ;
//    static {
//        charset.put('"', "\\\"");
//        charset.put('\'', "\\\'");
//        charset.put('\\', "\\\\");
//        charset.put('/', "\\/");
//        charset.put('\b', "\\\b");
//        charset.put('\f', "\\\f");
//        charset.put('\n', "\\\n");
//        charset.put('\r', "\\\r");
//        charset.put('\t', "\\\t");
//    };
//    private ArrayList<String>                   ss          = new ArrayList<String>();
//    private ArrayList<Boolean>                  bs          = new ArrayList<Boolean>();
//    private StringBuffer                        cur         = new StringBuffer();
//    //
//    private boolean                             sense       = false;                           //�Ƿ���ת��
//    private boolean                             el          = false;                           //�Ƿ���EL
//    private boolean                             readyEL     = false;                           //EL��⣬���
//    private int                                 depth       = 0;                               //���������
//    private char                                startString = 0;
//    //
//    public void write(char c) {
//        /*����ת��*/
//        if (sense == true) {
//            sense = false;
//            cur.append(c);
//            return;
//        }
//        /*����\����ת��*/
//        if (c == '\\') {
//            sense = true;
//            return;
//        }
//        if (startString == 0) {
//            /*����$Ԥ��EL*/
//            if (el == false && c == '$') {
//                readyEL = true;
//                return;
//            }
//            /*����Ԥ��el�µ���һ���ַ�*/
//            if (readyEL == true)
//                if (c == '{' && el == false) {
//                    newLine();
//                    el = true;//��ʼel
//                    readyEL = false;
//                    depth++;
//                    return;
//                } else
//                    throw new FormatException("�����EL���ʽ��ʼ��");
//            /*EL����*/
//            if (el == true && c == '}' && depth == 1) {
//                newLine();
//                el = false;
//                depth = 0;
//                return;
//            }
//        }
//        /*���⴦�� {}*/
//        if (el == true)
//            if (c == '\"' || c == '\'')
//                if (startString == 0)
//                    startString = c;
//                else if (startString == c)
//                    startString = 0;
//        this.cur.append(c);
//    }
//    private void newLine() {
//        if (cur.length() > 0) {
//            String elStr = cur.toString().trim();
//            if (elStr.equals("") == false) {
//                ss.add(elStr);
//                if (el == true)
//                    bs.add(true);
//                else
//                    bs.add(false);
//            }
//        }
//        cur = new StringBuffer();
//    }
//    public List<String> getS() {
//        newLine();
//        return ss;
//    }
//    public List<Boolean> getB() {
//        newLine();
//        return bs;
//    }
//    public String getEL() {
//        List<String> ss = this.getS();
//        List<Boolean> bs = this.getB();
//        //
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < ss.size(); i++) {
//            String s = ss.get(i);
//            if (bs.get(i) == false) {
//                sb.append(" + ");
//                sb.append('\"');
//                try {
//                    Reader sr = new StringReader((String) s);
//                    while (true) {
//                        int c_read = sr.read();
//                        char c = (char) c_read;
//                        if (c_read == -1)
//                            break;
//                        if (charset.containsKey(c) == true)
//                            sb.append(charset.get(c));
//                        else
//                            sb.append(c);
//                    }
//                } catch (Exception e) {}
//                sb.append('\"');
//                //
//            } else
//                sb.append(s);
//        }
//        return sb.toString();
//    }
//}