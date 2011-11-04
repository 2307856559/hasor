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
package org.more.core.json;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.more.core.copybean.CopyBeanUtil;
import org.more.core.error.InitializationException;
import org.more.core.error.SupportException;
import org.more.util.ResourcesUtil;
import org.more.util.StringConvertUtil;
import org.more.util.attribute.IAttribute;
import org.more.util.attribute.SequenceStack;
/**
 * ����stringBorder�����ھ����ַ������л�ʱʹ�õ����š�'������˫���š�"��(Ĭ��ֵ)��
 * JsonUtil�����л��ַ�������ʱ֧����String��Character��CharSequence��Reader��Щ���͡�
 * ��Ŀǰ�汾ΪֹJsonUtil��֧�ֿ�ѧ��������ʾ�����֡�
 * @version 2010-1-7
 * @author ������ (zyc@byshell.org)
 */
public abstract class JsonUtil {
    /**Ĭ���ַ�����34:", 39:'*/
    public static char                           StringBorder = '"';
    /*˳�������ȼ�˳��*/
    public static final String[]                 configs      = new String[] { "META-INF/resource/core/json_config.properties", "META-INF/json_config.properties", "json_config.properties" };
    //
    private char                                 stringBorder = JsonUtil.StringBorder;
    private LinkedHashMap<JsonCheck, JsonParser> jsonTypes    = new LinkedHashMap<JsonCheck, JsonParser>();
    /**����JsonUtil�����ַ������л�ʹ��˫���Ż����� */
    protected JsonUtil() throws Exception {
        //1.����index
        ArrayList<String> names = new ArrayList<String>();
        SequenceStack<String> seqStack = new SequenceStack<String>();
        //
        for (String cfg : configs) {
            IAttribute<String> attList = ResourcesUtil.getPropertys(cfg);
            seqStack.putStack(attList);
            String index = attList.getAttribute("index");
            if (index == null)
                continue;
            String[] $index = index.split(",");
            for (String obj : $index)
                if (names.contains(obj) == false)
                    names.add(obj);
        }
        //2.װ��index
        for (String name : names) {
            if (name == null || name.equals("") == true)
                continue;
            String _check = seqStack.getAttribute(name + "_Check");
            String _parser = seqStack.getAttribute(name + "_Parser");
            if (_check == null || _check.equals("") == true || _parser == null || _parser.equals("") == true)
                continue;
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class<?> $_check = loader.loadClass(_check);
            Class<?> $_parser = loader.loadClass(_parser);
            JsonCheck $$_check = (JsonCheck) $_check.newInstance();
            JsonParser $$_parser = (JsonParser) $_parser.getConstructor(JsonUtil.class).newInstance(this);
            this.addType($$_check, $$_parser);
        }
        String useDoubleBorder = seqStack.getAttribute("useDoubleBorder");
        if (useDoubleBorder == null || useDoubleBorder.equals("") == true)
            this.stringBorder = JsonUtil.StringBorder;
        else {
            if (StringConvertUtil.parseBoolean(useDoubleBorder, true) == true)
                this.stringBorder = '"';
            else
                this.stringBorder = '\'';
        }
    };
    /**���һ�����͵Ľ�������ӵ����ͽ����Ǳ�׷�ӽ�ȥ�ġ�TODO ��Ϊ���뷽ʽ*/
    private void addType(JsonCheck check, JsonParser parser) {
        this.jsonTypes.put(check, parser);
    }
    /**��ȡ�����������л�Ϊjson����ʱ�ַ�������������ʽ���Ե����Ż���˫���š�(Ĭ����˫����)  */
    public char getStringBorder() {
        return this.stringBorder;
    };
    /**���õ����������л�Ϊjson����ʱ�ַ�������������ʽ���Ե����Ż���˫���ţ�ֻ�е������˵����š�'������˫���š�"��ʱ�����Ч��������������ַ����������á�*/
    public void setStringBorder(char stringBorder) {
        if (stringBorder == 34 || stringBorder == 39)
            this.stringBorder = stringBorder;
    };
    /**��json����ת��Ϊmap��ʽ��*/
    public Map<?, ?> toMap(String str) {
        String readStr = str.trim();
        if (readStr == null || readStr.equals(""))
            return null;
        /*----------------------------*/
        Object obj = this.toObject(readStr);
        if (obj instanceof Map == false) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("", obj);
            obj = map;
        }
        return (Map<?, ?>) obj;
    }
    /**����������ת��Ϊmap��ʽ���÷������Ƚ�����ת��ΪjsonȻ����ת����map*/
    public Map<?, ?> toMap(Object value) {
        String readStr = this.toString(value);
        return this.toMap(readStr);
    }
    /**��json����ת��Ϊָ�������ͽṹ��*/
    public Object toObject(String str, Class<?> superType) throws InstantiationException, IllegalAccessException {
        Object obj = superType.newInstance();
        Object data = this.toObject(str);
        return CopyBeanUtil.newInstance().copy(data, obj);
    }
    /**��json����ת��Ϊobject��ʽ�����json������һ�������򷵻�������������һ�������򷵻�һ��map*/
    public Object toObject(String str) {
        String readStr = str.trim();
        for (JsonCheck jsonCheck : this.jsonTypes.keySet())
            if (jsonCheck.checkToObject(readStr) == true)
                return this.jsonTypes.get(jsonCheck).toObject(readStr);
        throw new SupportException("'" + readStr + "' is not supported to object.");
    }
    /**������ת��Ϊjson��ʽ���ݣ�ע����������г��ֵݹ��������������ջ����쳣��*/
    public String toString(Object object) {
        for (JsonCheck jsonCheck : this.jsonTypes.keySet())
            if (jsonCheck.checkToString(object) == true)
                return this.jsonTypes.get(jsonCheck).toString(object);
        throw new SupportException(object.getClass() + " is not supported to json format");
    }
    /*---------------------------------------------------------------------------------*/
    private static JsonUtil defaultUtil = null;
    /**������ζ�����һ���µ�JsonUtilʵ����*/
    public static JsonUtil newInstance() {
        try {
            return new JsonUtil() {};
        } catch (Exception e) {
            throw new InitializationException(e);
        }
    };
    /**��ȡһ��JsonUtilʵ�����÷���������һ�ε��ø÷���������ʵ������*/
    public static JsonUtil getJsonUtil() {
        if (defaultUtil == null)
            defaultUtil = newInstance();
        return defaultUtil;
    };
    /**������ת��Ϊjson��ʽ���ݣ�ע����������г��ֵݹ��������������ջ����쳣��*/
    public static String transformToJson(Object dataBean) {
        return getJsonUtil().toString(dataBean);
    };
    /**��json����ת��Ϊobject��ʽ�����json������һ�������򷵻�������������һ�������򷵻�һ��map*/
    public static Object transformToObject(String jsonData) {
        return getJsonUtil().toObject(jsonData);
    };
    /**��json����ת��Ϊָ�������ͽṹ��*/
    public static Object transformToObject(String jsonData, Class<?> toType) throws InstantiationException, IllegalAccessException {
        return getJsonUtil().toObject(jsonData, toType);
    };
    /**��json����ת��Ϊmap��ʽ��*/
    public static Map<?, ?> transformToMap(String jsonData) {
        return getJsonUtil().toMap(jsonData);
    }
    /**����������ת��Ϊmap��ʽ���÷������Ƚ�����ת��ΪjsonȻ����ת����map*/
    public static Map<?, ?> transformToMap(Object dataBean) {
        return getJsonUtil().toMap(dataBean);
    }
}