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
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
public abstract class AbstractGlobal extends AbstractMap<String, Object> {
    /*------------------------------------------------------------------------*/
    private Map<String, Object> targetContainer  = new HashMap<String, Object>();
    private Map<String, Object> $targetContainer = null;
    public AbstractGlobal() {
        this(null);
    };
    public AbstractGlobal(Map<String, Object> configs) {
        if (configs == null)
            this.targetContainer = new HashMap<String, Object>();
        else
            this.targetContainer = configs;
    };
    /**���������д�÷������滻targetContainer����������*/
    protected Map<String, Object> getAttContainer() {
        if (this.$targetContainer != null)
            return this.$targetContainer;
        //
        if (this.isCaseSensitive() == true)
            this.$targetContainer = this.targetContainer;
        else {
            this.$targetContainer = new HashMap<String, Object>();
            Set<String> ns = this.targetContainer.keySet();
            for (String n : ns)
                this.$targetContainer.put(n.toLowerCase(), this.targetContainer.get(n));
        }
        //
        return $targetContainer;
    }
    @Override
    public Set<Entry<String, Object>> entrySet() {
        return getAttContainer().entrySet();
    }
    /*------------------------------------------------------------------------*/
    private OgnlContext ognlContext = null;
    private OgnlContext transformToOgnlContext() {
        if (this.ognlContext == null)
            this.ognlContext = new OgnlContext(this.getAttContainer());
        return this.ognlContext;
    };
    private boolean caseSensitive = true;
    /**�Ƿ����ĸ��Сд���У�����true��ʾ���С�*/
    public boolean isCaseSensitive() {
        return this.caseSensitive;
    };
    /**���ã���Сд���С�*/
    public void enableCaseSensitive() {
        this.caseSensitive = true;
        this.$targetContainer = null;
    }
    /**���ã���Сд�����С�*/
    public void disableCaseSensitive() {
        this.caseSensitive = false;
        this.$targetContainer = null;
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
            return this.getEval((String) oriObject, Object.class);
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
        Object oriObject = this.getAttContainer().get((this.isCaseSensitive() == false) ? name.toLowerCase() : name);
        if (oriObject == null)
            return defaultValue;
        //
        T var = null;
        if (oriObject instanceof String)
            //ԭʼ�������ַ�������Eval����
            var = this.getEval((String) oriObject, toType);
        else if (oriObject instanceof GlobalProperty)
            //ԭʼ������GlobalPropertyֱ��get
            var = ((GlobalProperty) oriObject).getValue(this, toType);
        else
            //�������Ͳ��账�����ݾ���Ҫ��ֵ��
            var = (T) oriObject;
        return var;
    };
    /**����ȫ�����ò��������ҷ���toType����ָ�������͡�*/
    public <T> T getToType(Enum<?> enumItem, Class<T> toType, T defaultValue) {
        return getToType(enumItem.name(), toType, defaultValue);
    };
    /*------------------------------------------------------------------------*/
    private <T> T getEval(String elString, Class<T> toType) {
        return StringConvertUtil.changeType(elString, toType);
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