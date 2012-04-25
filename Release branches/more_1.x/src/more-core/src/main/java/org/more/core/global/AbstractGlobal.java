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
import java.util.Map;
import org.more.core.error.FormatException;
import org.more.core.error.SupportException;
import org.more.core.iatt.Attribute;
import org.more.core.iatt.IAttribute;
import org.more.core.iatt.TransformToAttribute;
import org.more.core.json.JsonUtil;
import org.more.core.ognl.Node;
import org.more.core.ognl.Ognl;
import org.more.core.ognl.OgnlContext;
import org.more.core.ognl.OgnlException;
import org.more.util.StringConvertUtil;
/**
 * Globalϵͳ�ĺ���ʵ��
 * @version : 2011-12-31
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractGlobal implements IAttribute<Object> {
    /*------------------------------------------------------------------------*/
    private IAttribute<Object> targetContainer = new Attribute<Object>();
    public AbstractGlobal() {};
    public AbstractGlobal(Map<String, Object> configs) {
        this(new TransformToAttribute<Object>(configs));
    };
    public AbstractGlobal(IAttribute<Object> configs) {
        this.targetContainer = configs;
    };
    /**���������д�÷������滻targetContainer����������*/
    protected IAttribute<Object> getAttContainer() {
        return targetContainer;
    }
    /*------------------------------------------------------------------------*/
    @Override
    public boolean contains(String name) {
        return this.getAttContainer().contains(name);
    }
    @Override
    public void setAttribute(String name, Object value) {
        this.getAttContainer().setAttribute(name, value);
    }
    @Override
    public Object getAttribute(String name) {
        return this.getAttContainer().getAttribute(name);
    }
    @Override
    public void removeAttribute(String name) {
        this.getAttContainer().removeAttribute(name);
    }
    @Override
    public String[] getAttributeNames() {
        return this.getAttContainer().getAttributeNames();
    }
    @Override
    public void clearAttribute() {
        this.getAttContainer().clearAttribute();
    }
    @Override
    public Map<String, Object> toMap() {
        return this.getAttContainer().toMap();
    }
    @Override
    public void putMap(Map<String, Object> params) {
        this.getAttContainer().putMap(params);
    }
    @Override
    public int size() {
        return this.getAttContainer().size();
    }
    /*------------------------------------------------------------------------*/
    private OgnlContext ognlContext = null;
    private OgnlContext transformToOgnlContext() {
        if (this.ognlContext == null) {
            this.ognlContext = new OgnlContext(this.toMap());
        }
        return this.ognlContext;
    };
    /**�Ƿ�����el���ʽ������*/
    public boolean isEnableEL() {
        return this.getToType("_global.enableEL", Boolean.class, false);
    }
    /**�Ƿ�����json����*/
    public boolean isEnableJson() {
        return this.getToType("_global.enableJson", Boolean.class, false);
    }
    private Map<String, Node> cacheNode = new java.util.Hashtable<String, Node>();
    /**ʹ��Ognl�����ַ��������ҷ������������*/
    public Object evalExpression(String ognlString) throws OgnlException {
        Node expressionNode = this.cacheNode.get(ognlString);
        if (expressionNode == null) {
            expressionNode = (Node) Ognl.parseExpression(ognlString);
            this.cacheNode.put(ognlString, expressionNode);
        }
        Object oriObject = expressionNode.getValue(this.transformToOgnlContext(), this.transformToOgnlContext());
        if (oriObject instanceof String)
            return this.getEval((String) oriObject);
        else
            return oriObject;
    };
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
        Object oriObject = this.getAttribute(name);
        if (oriObject == null)
            return defaultValue;
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
    /**����ȫ�����ò��������ҷ���toType����ָ�������͡�*/
    public <T> T getToType(Enum<?> enumItem, Class<T> toType, T defaultValue) {
        return getToType(enumItem.name(), toType, defaultValue);
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
            res = this.$evalEL2(elString);//TODO
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
        if (this.isEnableEL() == false)
            return this.$evalString(elString);//������EL����
        //2.����elString
        try {
            return this.evalExpression(elString);
        } catch (OgnlException e) {
            throw new FormatException("expression ��" + elString + "�� error.");
        }
    };
    /**�ڽ��������и������Json�������_global.enableJson��������Ϊfalse�򲻽���json���ݡ�*/
    protected Object $evalJSON(String jsonString) {
        if (this.isEnableJson() == true)
            return JsonUtil.transformToObject(jsonString);//�����м���
        else
            return this.$evalString(jsonString);
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
    /**����һ��{@link Global}����ʵ��������*/
    public static Global newInterInstance(Map<String, Object> configs) {
        return new Global(configs) {};
    };
    /**����һ��{@link Global}����ʵ��������*/
    public static Global newInterInstance() {
        return new Global() {};
    };
};