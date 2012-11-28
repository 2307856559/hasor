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
package org.more.hypha.utils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.StringUtils;
import org.more.hypha.ApplicationContext;
import org.more.hypha.define.BeanDefine;
import org.more.hypha.define.MethodDefine;
import org.more.hypha.define.ParamDefine;
import org.more.hypha.define.PropertyDefine;
/**
 * 
 *
 * @version : 2012-5-15
 * @author ������ (zyc@byshell.org)
 */
public abstract class DefineUtils {
    /**����bean��Ψһ��ţ����û��ָ��id������idֵ����fullName����ֵ��*/
    public static String getFullName(BeanDefine define) {
        if (define == null)
            throw new NullPointerException("param BeanDefine is null.");
        //
        String _id = StringUtils.trimToNull(define.getId());
        String _name = StringUtils.trimToNull(define.getName());
        String _scope = StringUtils.trimToNull(define.getScope());
        //
        if (_id != null)
            return _id;
        return (_scope != null) ? _scope + "." + _name : _name;
    }
    /**��ȡ�����Ķ��壬�÷���ֻ���ڵ�ǰ�����в��ҡ�*/
    public static MethodDefine getDeclaredMethod(BeanDefine define, String name) {
        if (define == null)
            throw new NullPointerException("param BeanDefine is null.");
        //
        return define.getMethods().get(name);
    };
    /**��ȡ�����Ķ��壬�����ǰ������û���������Զ���ʹ�õ�ģ���в��ҡ���������ֱ��ģ�巵��Ϊ�ա�*/
    public static MethodDefine getMethod(ApplicationContext context, BeanDefine define, String name) {
        if (define == null)
            throw new NullPointerException("param BeanDefine is null.");
        //
        MethodDefine md = getDeclaredMethod(define, name);
        if (md != null)
            return md;
        String _useTemplate = StringUtils.trimToNull(define.getUseTemplate());
        if (_useTemplate != null) {
            BeanDefine templateBeanDefine = context.getBeanDefinition(_useTemplate);
            if (templateBeanDefine != null)
                return getMethod(context, templateBeanDefine, name);
        }
        return null;
    };
    /**��ȡ��ǰ�����������ķ����б����صĽ��������ʹ�õ�ģ���еķ���������*/
    public static Map<String, MethodDefine> getDeclaredMethods(BeanDefine define) {
        if (define == null)
            throw new NullPointerException("param BeanDefine is null.");
        //
        return define.getMethods();
    };
    /**��ȡ��ǰ�����п��õķ����������ϣ������˼̳�ģ���еķ�����*/
    public static Map<String, MethodDefine> getMethods(ApplicationContext context, BeanDefine define) {
        if (define == null)
            throw new NullPointerException("param BeanDefine is null.");
        //
        HashMap<String, MethodDefine> ms = new HashMap<String, MethodDefine>();
        ms.putAll(define.getMethods());
        //
        String _useTemplate = StringUtils.trimToNull(define.getUseTemplate());
        if (_useTemplate != null) {
            BeanDefine templateBeanDefine = context.getBeanDefinition(_useTemplate);
            if (templateBeanDefine != null) {
                Map<String, MethodDefine> templateMethods = getMethods(context, templateBeanDefine);
                for (Entry<String, MethodDefine> ent : templateMethods.entrySet())
                    if (ms.containsKey(ent.getKey()) == false)
                        ms.put(ent.getKey(), ent.getValue());
            }
        }
        return ms;
    };
    /**��ȡ���Զ��壬�÷���ֻ���ڵ�ǰ�����в��ҡ�*/
    public static PropertyDefine getDeclaredProperty(BeanDefine define, String name) {
        if (define == null)
            throw new NullPointerException("param BeanDefine is null.");
        //
        return define.getPropertys().get(name);
    };
    /**��ȡ���Զ��壬�����ǰ������û���������Զ���ʹ�õ�ģ���в��ҡ���������ֱ��ģ�巵��Ϊ�ա�*/
    public static PropertyDefine getProperty(ApplicationContext context, BeanDefine define, String name) {
        if (define == null)
            throw new NullPointerException("param BeanDefine is null.");
        //
        PropertyDefine pd = getDeclaredProperty(define, name);
        if (pd != null)
            return pd;
        String _useTemplate = StringUtils.trimToNull(define.getUseTemplate());
        if (_useTemplate != null) {
            BeanDefine templateBeanDefine = context.getBeanDefinition(_useTemplate);
            if (templateBeanDefine != null)
                return getProperty(context, templateBeanDefine, name);
        }
        return null;
    };
    /**��ȡ��ǰ�����������������б����صĽ��������ʹ�õ�ģ���е�����������*/
    public static Map<String, PropertyDefine> getDeclaredPropertys(BeanDefine define) {
        if (define == null)
            throw new NullPointerException("param BeanDefine is null.");
        //
        return define.getPropertys();
    };
    /**��ȡ��ǰ�����п��õ������������ϣ������˼̳�ģ���е����ԡ�*/
    public static Map<String, PropertyDefine> getPropertys(ApplicationContext context, BeanDefine define) {
        if (define == null)
            throw new NullPointerException("param BeanDefine is null.");
        //
        HashMap<String, PropertyDefine> ms = new HashMap<String, PropertyDefine>();
        ms.putAll(define.getPropertys());
        //
        String _useTemplate = StringUtils.trimToNull(define.getUseTemplate());
        if (_useTemplate != null) {
            BeanDefine templateBeanDefine = context.getBeanDefinition(_useTemplate);
            if (templateBeanDefine != null) {
                Map<String, PropertyDefine> templateMethods = getPropertys(context, templateBeanDefine);
                for (Entry<String, PropertyDefine> ent : templateMethods.entrySet())
                    if (ms.containsKey(ent.getKey()) == false)
                        ms.put(ent.getKey(), ent.getValue());
            }
        }
        return ms;
    };
    /**���һ����������������ӵ������������Զ���������*/
    public static void addInitParam(BeanDefine define, int index, ParamDefine paramDefine) {
        if (define == null)
            throw new NullPointerException("param BeanDefine is null.");
        //
        List<ParamDefine> paramList = define.getInitParams();
        paramList.add(index, paramDefine);
        for (int i = 0; i < paramList.size(); i++)
            paramList.get(i).setIndex(i);
    };
    /**���һ����������������ӵ������������Զ���������*/
    public static void addInitParam(BeanDefine define, ParamDefine paramDefine) {
        if (define == null)
            throw new NullPointerException("param BeanDefine is null.");
        //
        List<ParamDefine> paramList = define.getInitParams();
        paramList.add(paramDefine);
        paramDefine.setIndex(paramList.indexOf(paramDefine));
        define.setInitParams(paramList);
    };
    /**���һ�����ԡ�*/
    public static void addProperty(BeanDefine define, PropertyDefine propertyDefine) {
        if (define == null)
            throw new NullPointerException("param BeanDefine is null.");
        //
        define.getPropertys().put(propertyDefine.getName(), propertyDefine);
    };
    /**���һ������������*/
    public static void addMethod(BeanDefine define, MethodDefine methodDefine) {
        if (define == null)
            throw new NullPointerException("param BeanDefine is null.");
        //
        define.getMethods().put(methodDefine.getName(), methodDefine);
    };
    /**���ط���������������*/
    public static String getMethodDesc(MethodDefine methodDefine) {
        //Type0 method(Type1,Type2)
        StringBuffer descBuffer = new StringBuffer();
        descBuffer.append(methodDefine.getReturnType());
        descBuffer.append(" ");
        descBuffer.append(methodDefine.getName());
        descBuffer.append("(");
        List<ParamDefine> params = methodDefine.getParams();
        for (int i = 0; i < params.size(); i++)
            descBuffer.append(params.get(i).getClassType() + ",");
        if (descBuffer.charAt(descBuffer.length()) == ',')
            descBuffer = descBuffer.deleteCharAt(descBuffer.length());
        descBuffer.append(")");
        return descBuffer.toString();
    };
    /**���һ����������������ӵķ����������Զ���������*/
    public static void addMethodParam(MethodDefine methodDefine, int index, ParamDefine paramDefine) {
        if (methodDefine == null || paramDefine == null)
            throw new NullPointerException("param methodDefine or paramDefine is null.");
        //
        List<ParamDefine> paramList = methodDefine.getParams();
        paramList.add(index, paramDefine);
        for (int i = 0; i < paramList.size(); i++)
            paramList.get(i).setIndex(i);
    };
    /**���һ����������������ӵķ����������Զ���������*/
    public static void addMethodParam(MethodDefine methodDefine, ParamDefine paramDefine) {
        if (methodDefine == null || paramDefine == null)
            throw new NullPointerException("param methodDefine or paramDefine is null.");
        //
        List<ParamDefine> paramList = methodDefine.getParams();
        paramList.add(paramDefine);
        paramDefine.setIndex(paramList.indexOf(paramDefine));
        methodDefine.setParams(paramList);
    };
}