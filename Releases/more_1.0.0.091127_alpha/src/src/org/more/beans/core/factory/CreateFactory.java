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
package org.more.beans.core.factory;
import org.more.DoesSupportException;
import org.more.beans.BeanFactory;
import org.more.beans.core.propparser.MainPropertyParser;
import org.more.beans.info.BeanDefinition;
import org.more.beans.info.CreateTypeEnum;
/**
 * more.beans��һ���ɶ���������ϵͳ�����ṩ�ˡ�Factory��New�����ִ�����ʽ��<br/>
 * ��ϵͳ��more.beans����bean�Ĵ���������Factory������ʽ����{@link FactoryCreateEngine}��ʵ�ֵġ�New��ʽ������{@link ConstructorCreateEngine}��ʵ�ֵġ�
 * ��more.beans��Ԥ������Factory��New���ֶ��󴴽���ʽ��<br/>
 * CreateFactory�Ĺ����Ǹ���bean�������Զ�ѡ��Factory��ʽ��������ʹ��New��ʽ������<br/>
 * Date : 2009-11-14
 * @author ������
 */
public class CreateFactory extends CreateEngine {
    //========================================================================================Field
    private ConstructorCreateEngine constructor; //New��ʽ
    private FactoryCreateEngine     factory;    //Factory��ʽ
    //==================================================================================Constructor
    /**����һ��CreateFactory���󣬴���ʱ����ָ�����Խ�������*/
    public CreateFactory(MainPropertyParser propParser) {
        if (propParser == null)
            throw new NullPointerException("����ָ��propParser��������CreateFactoryʹ��������Խ������������ԡ�");
        this.constructor = new ConstructorCreateEngine(propParser); //New��ʽ
        this.factory = new FactoryCreateEngine(propParser); //Factory��ʽ
    }
    //==========================================================================================Job
    /** �Զ�ѡ�񴴽�����������bean���� */
    @Override
    public Object newInstance(BeanDefinition definition, Object[] createParams, BeanFactory context) throws Exception {
        if (definition.getCreateType() == CreateTypeEnum.New)
            return this.constructor.newInstance(definition, createParams, context);
        else if (definition.getCreateType() == CreateTypeEnum.Factory)
            return this.factory.newInstance(definition, createParams, context);
        else
            throw new DoesSupportException("CreateFactory�෢�ֲ�֧�ֵĴ���ģʽ��");
    }
}
