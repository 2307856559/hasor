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
package org.more.beans.core.propparser;
import org.more.DoesSupportException;
import org.more.beans.core.ResourceBeanFactory;
import org.more.beans.info.BeanDefinition;
import org.more.beans.info.BeanProp;
import org.more.beans.info.BeanProperty;
import org.more.beans.info.Prop;
import org.more.beans.info.PropArray;
import org.more.beans.info.PropList;
import org.more.beans.info.PropMap;
import org.more.beans.info.PropRefValue;
import org.more.beans.info.PropSet;
import org.more.beans.info.PropVarValue;
/**
 * �������������Խ���������ڣ���Ҳʵ����{@link PropertyParser}�ӿڵ���ʹ��MainPropertyParser����ʱʹ�����ļ�parser���������ط�����
 * ���ط���������һЩ����Ҫ�Ĳ����ṩ����Щ�����Ѿ�����װ��MainPropertyParser�����С�
 * @version 2009-11-18
 * @author ������ (zyc@byshell.org)
 */
public class MainPropertyParser implements PropertyParser {
    //========================================================================================Field
    private ValueTypeParser     valueTypeParser = new ValueTypeParser(); //
    private RefTypeParser       refTypeParser   = new RefTypeParser();  //
    private ListParser          listParser      = new ListParser();     //
    private MapParser           mapParser       = new MapParser();      //
    private SetParser           setParser       = new SetParser();      //
    private ArrayParser         arrayParser     = new ArrayParser();    //
    private ResourceBeanFactory factory         = null;                 //
    //==================================================================================Constructor
    /**����MainPropertyParser���󣬱���ָ��ResourceBeanFactory����*/
    public MainPropertyParser(ResourceBeanFactory factory) {
        //        if (factory == null)
        //            throw new NullPointerException("����ָ��factory������");
        this.factory = factory;
    }
    //==========================================================================================Job
    /**
     * �÷�����{@link PropertyParser}�ӿڷ����ļ����ط������������Բ��ҷ��ض�����Ƭ�εĽ��������
     * @param context ��ǰҪ�������������ĸ������ϵ����ԣ���������������ǹ��췽������������Ŀ�����û�д��������ڹ��췽��������context��null������֮�⹤������Ҳһ����
     * @param contextParams ����beanʱ�򸽴��Ļ������������������ResourceBeanFactory.getBean()ʱ���ݵĲ�����
     * @param prop Ҫ����������Ƭ�Ρ�
     * @param propContext ����������Ƭ���������Ǹ�{@link BeanDefinition}�ж�������Զ����������Ƭ���ж����νṹ���ǽ�����ЩƬ��֮�����Ҫע�������ȴ�ǲ���ģ�������ԵĶ�����Ǹò�����
     * @param definition ����propContext�������Ǹ�{@link BeanDefinition}����
     * @return ���ؽ����Ľ����
     * @throws Exception �������̷����쳣��
     * @throws DoesSupportException ���������prop�������Ͳ����Ѿ���֧�ֵ����ͽ����������쳣��
     */
    public Object parser(Object context, Object[] contextParams, BeanProp prop, BeanProperty propContext, BeanDefinition definition) throws Exception {
        return this.parser(context, contextParams, prop, propContext, definition, this.factory, null);
    }
    /**
     * �÷�����{@link PropertyParser}�ӿڷ����ļ����ط������������Բ��ҷ��ض�����Ƭ�εĽ����������������������͡�
     * @param context ��ǰҪ�������������ĸ������ϵ����ԣ���������������ǹ��췽������������Ŀ�����û�д��������ڹ��췽��������context��null������֮�⹤������Ҳһ����
     * @param contextParams ����beanʱ�򸽴��Ļ������������������ResourceBeanFactory.getBean()ʱ���ݵĲ�����
     * @param prop Ҫ����������Ƭ�Ρ�
     * @param propContext ����������Ƭ���������Ǹ�{@link BeanDefinition}�ж�������Զ����������Ƭ���ж����νṹ���ǽ�����ЩƬ��֮�����Ҫע�������ȴ�ǲ���ģ�������ԵĶ�����Ǹò�����
     * @param definition ����propContext�������Ǹ�{@link BeanDefinition}����
     * @return ���ؽ����Ľ����
     * @throws Exception �������̷����쳣��
     * @throws DoesSupportException ���������prop�������Ͳ����Ѿ���֧�ֵ����ͽ����������쳣��
     */
    public Class<?> parserType(Object context, Object[] contextParams, BeanProp prop, BeanProperty propContext, BeanDefinition definition) throws Exception {
        return this.parserType(context, contextParams, prop, propContext, definition, this.factory, null);
    }
    @Override
    public Object parser(Object context, Object[] contextParams, BeanProp prop, BeanProperty propContext, BeanDefinition definition, ResourceBeanFactory factory, PropertyParser contextParser) throws Exception {
        if (prop == null)
            return null;
        else if (prop instanceof PropVarValue)
            return valueTypeParser.parser(context, contextParams, prop, propContext, definition, factory, this);
        else if (prop instanceof PropRefValue)
            return refTypeParser.parser(context, contextParams, prop, propContext, definition, factory, this);
        else if (prop instanceof PropList)
            return listParser.parser(context, contextParams, prop, propContext, definition, factory, this);
        else if (prop instanceof PropMap)
            return mapParser.parser(context, contextParams, prop, propContext, definition, factory, this);
        else if (prop instanceof PropSet)
            return setParser.parser(context, contextParams, prop, propContext, definition, factory, this);
        else if (prop instanceof PropArray)
            return arrayParser.parser(context, contextParams, prop, propContext, definition, factory, this);
        else
            throw new DoesSupportException("Ŀǰ�ݲ�֧��[" + prop.getClass() + "]�������ͽ�������֧�֡�");
    }
    @Override
    public Class<?> parserType(Object context, Object[] contextParams, BeanProp prop, BeanProperty propContext, BeanDefinition definition, ResourceBeanFactory factory, PropertyParser contextParser) throws Exception {
        String propType = propContext.getPropType();
        if (propType != null)
            return Prop.getType(propType, factory.getBeanClassLoader());
        //
        if (prop == null)
            return factory.getBeanClassLoader().loadClass(propContext.getPropType());
        else if (prop instanceof PropVarValue)
            return valueTypeParser.parserType(context, contextParams, prop, propContext, definition, factory, this);
        else if (prop instanceof PropRefValue)
            return refTypeParser.parserType(context, contextParams, prop, propContext, definition, factory, this);
        else if (prop instanceof PropList)
            return listParser.parserType(context, contextParams, prop, propContext, definition, factory, this);
        else if (prop instanceof PropMap)
            return mapParser.parserType(context, contextParams, prop, propContext, definition, factory, this);
        else if (prop instanceof PropSet)
            return setParser.parserType(context, contextParams, prop, propContext, definition, factory, this);
        else if (prop instanceof PropArray)
            return arrayParser.parserType(context, contextParams, prop, propContext, definition, factory, this);
        else
            throw new DoesSupportException("Ŀǰ�ݲ�֧���Զ����������ͽ�������֧�֡�");
    }
}