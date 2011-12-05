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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.more.core.error.FormatException;
import org.more.core.error.InitializationException;
import org.more.core.error.SupportException;
import org.more.core.json.JsonUtil;
import org.more.core.ognl.Ognl;
import org.more.core.ognl.OgnlContext;
import org.more.core.ognl.OgnlException;
import org.more.util.ResourcesUtil;
import org.more.util.StringConvertUtil;
import org.more.util.attribute.AttBase;
import org.more.util.attribute.IAttribute;
import org.more.util.attribute.SequenceStack;
import org.more.util.attribute.TransformToAttribute;
import org.more.util.attribute.TransformToMap;
/**
* ȫ�ֳ�����ȡ��
* @version : 2011-9-3
* @author ������ (zyc@byshell.org)
*/
public abstract class Global implements IAttribute<Object> {
    /**�Ƿ�����el���ʽ������*/
    private final static String                          EnableEL     = "_global.enableEL";
    /**�Ƿ�����json����*/
    private final static String                          EnableJson   = "_global.enableJson";
    /**��˳�������ȼ�˳��*/
    public final static String[]                         Configs      = new String[] { "META-INF/resource/core/global_config.properties", "META-INF/global_config.properties", "global_config.properties" };
    /**��ӵ����������ļ��������ﱣ�棬���ݲ�ͬ��ע���������з��飬_global���_cache��Ҳ�����С�*/
    private LinkedHashMap<String, SequenceStack<Object>> scopeMap     = null;
    private GlobalObject                                 globalObject = new GlobalObject(this);
    private AttBase<Object>                              cache        = new AttBase<Object>();
    // 
    //
    /*------------------------------------------------------------------------*/
    private SequenceStack<Object>                        $elData      = null;
    /**���ؿ����ڼ���EL�ļ��϶��󣬸ü��϶����а�����_Global��_Cache��*/
    protected IAttribute<Object> getALLRoot() {
        if (this.$elData == null) {
            this.$elData = new SequenceStack<Object>();
            for (IAttribute<Object> att : this.scopeMap.values())
                if (att != null)
                    $elData.putStack(att);
            this.$elData.putStack(cache);
        }
        return this.$elData;
    };
    public OgnlContext transformToOgnlContext() {
        HashMap<String, Object> all = new HashMap<String, Object>(this.getALLRoot().toMap());
        all.put(GlobalObject._Global, this.globalObject);
        return new OgnlContext(all);
    };
    /*------------------------------------------------------------------------*/
    public Global(IAttribute<Object> configs) {
        this();
        if (configs != null)
            this.addScope("", configs);
    };
    public Global() {
        this.scopeMap = new LinkedHashMap<String, SequenceStack<Object>>();
    };
    /*------------------------------------------------------------------------*/
    /**ʹ��Ognl�����ַ��������ҷ������������*/
    public Object evalName(String ognlString) throws OgnlException {
        Object oriObject = Ognl.getValue(ognlString, this.transformToOgnlContext());
        if (oriObject instanceof String)
            return this.getEval((String) oriObject);
        else
            return oriObject;
    };
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
    /**����ȫ�����ò��������ҷ���toType����ָ�������͡�*/
    public <T> T getToType(Enum<?> enumItem, Class<T> toType) {
        return this.getToType(enumItem, toType, null);
    };
    /**����ȫ�����ò��������ҷ���toType����ָ�������͡�*/
    public <T> T getToType(String name, Class<T> toType) {
        return this.getToType(name, toType, null);
    };
    /**����ȫ�����ò��������ҷ���toType����ָ�������͡�*/
    public <T> T getToType(String name, Class<T> toType, T defaultValue) {
        Object oriObject = this.getOriginalObject(null, name);
        if (oriObject == null)
            return defaultValue;
        //
        Object var = null;
        if (oriObject instanceof String)
            var = this.getEval((String) oriObject);
        else
            var = oriObject;
        return StringConvertUtil.changeType(var, toType, defaultValue);
    };
    /**����ȫ�����ò��������ҷ���toType����ָ�������͡�*/
    public <T> T getToType(Enum<?> enumItem, Class<T> toType, T defaultValue) {
        if (enumItem == null)
            return defaultValue;
        Object oriObject = this.getOriginalObject(enumItem.getClass().getName(), enumItem.name());//��ȡԭʼ����
        //
        Object var = null;
        if (oriObject instanceof String)
            //ԭʼ�������ַ�������Eval����
            var = this.getEval((String) oriObject);
        else if (oriObject instanceof GlobalProperty)
            //ԭʼ������GlobalPropertyֱ��get
            var = ((GlobalProperty) oriObject).getValue(this);
        else
            //�������Ͳ��账�����ݾ���Ҫ��ֵ��
            var = oriObject;
        return StringConvertUtil.changeType(var, toType, defaultValue);
    };
    /** {@link Global#getOriginalObject(String)}���ַ�����ʽ.*/
    public String getOriginalString(String name) {
        Object obj = this.getOriginalObject(name);
        return (obj != null) ? obj.toString() : null;
    };
    /** {@link Global#getOriginalObject(String, String)}���ַ�����ʽ.*/
    public String getOriginalString(String scope, String name) {
        Object obj = this.getOriginalObject(scope, name);
        return (obj != null) ? obj.toString() : null;
    };
    /**��ȡָ�����Ƶ�ԭʼ������Ϣ����ԭʼ��Ϣ�����Ǵ���������λ�ã�<br/>1._Cache��<br/>2.װ�ص������ļ���<br/>3.ͨ��addConfig����������������ݡ�*/
    public Object getOriginalObject(String name) {
        return this.getOriginalObject(null, name);
    };
    /**��ָ�����������л�ȡָ�����Ƶ�ԭʼ������Ϣ��������������Ϊnull��ô������������������Ѱ����ԭʼ��Ϣ�����Ǵ���������λ�ã�<br/>
     * 1._Cache��<br/>2.װ�ص������ļ���<br/>3.ͨ��addConfig����������������ݡ�*/
    public Object getOriginalObject(String scope, String name) {
        if (name == null)
            return null;
        //1.��list�л�ȡConfig
        IAttribute<Object> config = null;
        if (scope == null || this.scopeMap.containsKey(scope) == false)
            config = this.getALLRoot();//���������ö���
        else
            config = this.scopeMap.get(scope);
        //2.Name
        return config.getAttribute(name);
    };
    /*------------------------------------------------------------------------*/
    public boolean contains(String name) {
        return this.getALLRoot().contains(name);
    };
    /**�÷�����ͬ��{@link #getObject(String)}*/
    public Object getAttribute(String name) {
        return this.getObject(name);
    };
    /**��{@link Global}��ת����{@link Map}�ӿ���ʽ��*/
    public Map<String, Object> toMap() {
        return new TransformToMap<Object>(this);
    };
    /**��{@link Global}��ת����{@link Map}�ӿ���ʽ��*/
    public Map<String, Object> toMap(String scope) {
        return this.scopeMap.get(scope).toMap();
    };
    public String[] getAttributeNames() {
        return this.getALLRoot().getAttributeNames();
    };
    public int size() {
        return this.getALLRoot().size();
    };
    /**ֵ�ᱻֱ�����õ����ö����У��÷�������Ӱ�쵽�̶����������ԣ����ǵ����ȼ���Ȼ����ߵģ���*/
    public void setAttribute(String name, Object newValue) {
        this.cache.put(name, newValue);
    };
    /**�����ö�����ɾ�����ԣ��÷�������Ӱ�쵽�̶����������ԣ����ǵ����ȼ���Ȼ����ߵģ���*/
    public void removeAttribute(String name) {
        this.cache.remove(name);
    };
    /**������ö����е����ԣ��÷�������Ӱ�쵽�̶����������ԣ����ǵ����ȼ���Ȼ����ߵģ���*/
    public void clearAttribute() {
        this.cache.clear();
    };
    /*------------------------------------------------------------------------*/
    /**��һ��ԭʼ��������Ϣ��ӵ�Global�У���������Ѿ�ʹ�õ�������׷�ӡ�*/
    public void addScope(Class<? extends Enum<?>> enumType, Map config) {
        this.addScope(enumType, new TransformToAttribute<Object>(config));
    };
    /**��һ��ԭʼ��������Ϣ��ӵ�Global�У���������Ѿ�ʹ�õ�������׷�ӡ�*/
    public void addScope(String name, Map config) {
        this.addScope(name, new TransformToAttribute<Object>(config));
    };
    /**��һ��ԭʼ��������Ϣ��ӵ�Global�У���������Ѿ�ʹ�õ�������׷�ӡ�*/
    public void addScope(Class<? extends Enum<?>> enumType, IAttribute<Object> config) {
        if (enumType == null || config == null)
            throw new NullPointerException("��enumType�� or ��config�� param is null");
        this.addScope(enumType.getName(), config);
    };
    /**��һ��ԭʼ��������Ϣ��ӵ�Global�У���������Ѿ�ʹ�õ�������׷�ӡ�*/
    public void addScope(String name, IAttribute<Object> config) {
        if (name == null || config == null)
            throw new NullPointerException("��name�� or ��config�� param is null");
        if (this.scopeMap.containsKey(name) == false) {
            //������һ����ͬʱ���������ӵ�allData��
            SequenceStack<Object> stack = new SequenceStack<Object>();
            stack.putStack(config);
            this.scopeMap.put(name, stack);
        } else {
            IAttribute<?> stack = this.scopeMap.get(name);
            if (stack instanceof SequenceStack == false)
                throw new SupportException("��" + name + "�� scope does not support more of the same.");
            SequenceStack<Object> $stack = (SequenceStack<Object>) stack;
            $stack.putStack(config);
        }
    };
    /**�������˳���λ�û�ȡһ��������*/
    public IAttribute<Object> getScope(int index) {
        ArrayList<IAttribute<Object>> list = new ArrayList<IAttribute<Object>>();
        for (SequenceStack<Object> attSeq : this.scopeMap.values())
            for (IAttribute<Object> att : attSeq.getList())
                list.add(att);
        return list.get(index);
    };
    /**������ӵ����ƻ�ȡһ��������*/
    public IAttribute<Object> getScope(String name) {
        return this.scopeMap.get(name);
    };
    /**������ȡ��������������һ�����ʹ�ø÷�����*/
    public IAttribute<Object> getScope(String name, int index) {
        IAttribute<Object> temp = this.getScope(name);
        if (temp instanceof SequenceStack == false)
            return temp;
        SequenceStack<Object> stack = (SequenceStack<Object>) temp;
        return stack.getIndex(index);
    };
    /**����һ�������˶��ٸ�config�������ã��������<b>��������</b>�����Ը�ֵӦ�ô��ڵ���<b>1</b>��*/
    public int getConfigGroupCount() {
        return this.scopeMap.size();
    };
    /**����һ���ж��������á����а���<b>��������</b>��*/
    public int getConfigItemCount() {
        return this.getALLRoot().size();
    };
    /*------------------------------------------------------------------------*/
    private <T> T getEval(String elString) {
        elString = elString.trim();
        if (elString == null || elString.equals("") == true)
            return null;
        //1.��ȡ���λ��
        StringBuffer elStr = new StringBuffer(elString);
        char firstChar = elStr.charAt(0);
        char lastChar = elStr.charAt(elStr.length() - 1);
        //2.��ȡβ��
        if (lastChar == ';') {
            elStr.deleteCharAt(elStr.length() - 1);
            lastChar = elStr.charAt(elStr.length() - 1);//ȥ��β���ķֺ�
        }
        //3.�ж�����
        elStr.deleteCharAt(0);
        if (elStr.length() > 1)
            elStr.deleteCharAt(elStr.length() - 1);
        Object res = null;
        if (firstChar == '(' && lastChar == ')')
            //����JSON
            res = this.$evalJSON(elStr.toString());
        else if (firstChar == '{' && lastChar == '}')
            //����EL
            res = this.$evalEL(elStr.toString());
        else if (firstChar == '"' && lastChar == '"')
            //�����ַ���1
            res = this.$evalString(elStr.toString());
        else if (firstChar == '\'' && lastChar == '\'')
            //�����ַ���2
            res = this.$evalString(elStr.toString());
        else
            //����EL���ı� 
            res = this.$evalEL2(elString);
        //4.���ؽ������
        return (T) res;
    };
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
            return Ognl.getValue(elString, this.transformToOgnlContext());
        } catch (OgnlException e) {
            throw new FormatException("expression ��" + elString + "�� error.");
        }
    };
    /**�ڽ��������и������Json�������_global.enableJson��������Ϊfalse�򲻽���json���ݡ�*/
    protected Object $evalJSON(String jsonString) {
        if (this.getBoolean(EnableJson, true) == false)
            return this.$evalString(jsonString);
        else
            return JsonUtil.transformToObject(jsonString);
    };
    /**�ڽ��������и����������EL�����ַ��������_global.enableEL��������Ϊfalse�򲻽���json���ݡ������ַ�������ͨ��${��}�������EL���֡�*/
    protected Object $evalEL2(String elString) {
        //���Ҫ������ַ����в��������ʽ������ʹ���ַ�����ʽ����
        if (elString.matches(".*\\$\\{.*\\}.*") == false)
            return this.$evalString(elString);
        //TODO:Ŀǰ�汾�ݲ�֧�ְ���EL���ʽ���ַ����������Ժ���Կ���ʹ��JavaCC����������ʽ���н�����
        throw new SupportException("Ŀǰ�汾�ݲ�֧�ְ���EL���ʽ���ַ���������");
        //return this.$evalEL(elString);//ִ��el
    };
    /*------------------------------------------------------------------------*/
    /**�𻺴�����*/
    private static final HashMap<String, Class<?>> globalFactoryMap = new HashMap<String, Class<?>>();
    /**����Ĭ�ϵ�{@link Global}����*/
    public static Global newInstance() throws IOException, ClassNotFoundException {
        return newInstance((Object) null);
    };
    /**����Ĭ�ϵ�{@link Global}���󣬲�����{@link GlobalFactory}�ڴ���{@link Global}ʱ����Ĳ�����*/
    public static Global newInstance(Object... params) throws IOException, ClassNotFoundException {
        return newInstanceByFactory("properties", params);
    };
    /**����{@link Global}���󣬲�����{@link GlobalFactory}�ڴ���{@link Global}ʱ����Ĳ�����factoryName��ָ��ע���{@link GlobalFactory}��*/
    public static Global newInstanceByFactory(String factoryName, Object... params) throws IOException, ClassNotFoundException {
        Class<?> globalFactoryType = null;
        if (globalFactoryMap.containsKey(factoryName) == true)
            globalFactoryType = globalFactoryMap.get(factoryName);
        else {
            IAttribute<String> configAtt = ResourcesUtil.getPropertys(Configs);
            String factoryType = configAtt.getAttribute(factoryName);
            if (factoryType == null)
                throw new SupportException("Global factory ��" + factoryName + "�� is not define.");
            globalFactoryType = Thread.currentThread().getContextClassLoader().loadClass(factoryType);
            globalFactoryMap.put(factoryName, globalFactoryType);
        }
        //
        try {
            GlobalFactory globalFactory = (GlobalFactory) globalFactoryType.newInstance();
            Global global = globalFactory.createGlobal(params);
            //XXX:����װ����������
            return global;
        } catch (Throwable e) {
            throw new InitializationException("init error can`t create type " + globalFactoryType);
        }
    };
    /**����һ��{@link Global}����ʵ��������*/
    public static Global newInterInstance(IAttribute<Object> configs) {
        return new Global(configs) {};
    };
    /**����һ��{@link Global}����ʵ��������*/
    public static Global newInterInstance() {
        return new Global(null) {};
    };
};