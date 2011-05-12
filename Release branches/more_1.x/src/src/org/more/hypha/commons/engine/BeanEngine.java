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
package org.more.hypha.commons.engine;
import java.util.HashMap;
import java.util.Map;
import org.more.RepeateException;
import org.more.hypha.AbstractBeanDefine;
import org.more.hypha.ApplicationContext;
import org.more.hypha.ValueMetaData;
import org.more.util.attribute.IAttribute;
/**
 * �ýӿ��ǻ�����bean��ȡ�ӿڣ��ýӿڵ�ְ���Ǹ���bean���岢�ҽ����bean���崴��������
 * @version : 2011-4-22
 * @author ������ (zyc@byshell.org)
 */
public abstract class BeanEngine {
    private Map<String, AbstractBeanBuilder<AbstractBeanDefine>> beanBuilderMap = new HashMap<String, AbstractBeanBuilder<AbstractBeanDefine>>();
    private RootValueMetaDataParser                              rootParser     = null;
    //----------------------------------------------------------------------------------------------------------
    /**ע��{@link ValueMetaDataParser}�����ע��Ľ����������ظ��������{@link RepeateException}�쳣��*/
    public void regeditValueMetaDataParser(String metaDataType, ValueMetaDataParser<ValueMetaData> parser) {
        this.rootParser.addParser(metaDataType, parser);
    };
    /**���ע��{@link ValueMetaDataParser}�����Ҫ�Ƴ��Ľ��������������Ҳ�����׳��쳣��*/
    public void unRegeditValueMetaDataParser(String metaDataType) {
        this.rootParser.removeParser(metaDataType);
    };
    /**
     * ע��һ��bean�������ͣ�ʹ֮���Ա��������������ظ�ע��ͬһ��bean���ͽ�������{@link RepeateException}�����쳣��
     * @param beanType ע���bean�������͡�
     * @param builder Ҫע���bean������������
     */
    public void regeditBeanBuilder(String beanType, AbstractBeanBuilder<AbstractBeanDefine> builder) throws RepeateException {
        if (this.beanBuilderMap.containsKey(beanType) == false)
            this.beanBuilderMap.put(beanType, builder);
        else
            throw new RepeateException("�����ظ�ע��[" + beanType + "]���͵�BeanBuilder��");
    };
    /**���ָ������bean�Ľ���֧�֣�����Ҫ�Ӵ�ע���bean�����Ƿ���ڸ÷������ᱻ��ȷִ�С�*/
    public void unRegeditBeanBuilder(String beanType) {
        if (this.beanBuilderMap.containsKey(beanType) == true)
            this.beanBuilderMap.remove(beanType);
    };
    /**��ȡָ�����Ƶ�Bean��������*/
    protected AbstractBeanBuilder<AbstractBeanDefine> getBeanBuilder(String name) {
        return this.beanBuilderMap.get(name);
    };
    /**��ȡ{@link ValueMetaData}Ԫ��Ϣ��������*/
    protected ValueMetaDataParser<ValueMetaData> getValueMetaDataParser() {
        return this.rootParser;
    };
    //----------------------------------------------------------------------------------------------------------
    /**��ʼ�������� */
    public abstract void init(ApplicationContext context, IAttribute flash) throws Throwable;
    /**���ٷ�����*/
    public void destroy() throws Throwable {}
    /**
     * ��ȡĳ��Bean��ʵ�����󣬸�ʵ��������ʱ����������þ����䴴����ԭ��ģʽ���ǵ�̬ģʽ�� 
     * ������bean�����Ե�����ע��������ע��Ҳ���ڴ���ʱ���С�
     * @param define Ҫ��������Bean���塣
     * @param objects �ڻ�ȡbeanʵ��ʱ���ܻᴫ�ݵĲ�����Ϣ��
     */
    public abstract <T> T builderBean(AbstractBeanDefine define, Object[] params) throws Throwable;
    /**
     * ����Bean���ƻ�ȡ��bean���ͣ��÷�����������bean���������õ�bean���͡�
     * ��ôgetBeanType�������������ɵ��������Ͷ���
     * @param define Ҫ��������Bean���塣
     * @param objects �ڻ�ȡbeanʵ��ʱ���ܻᴫ�ݵĲ�����Ϣ��
     */
    public abstract Class<?> builderType(AbstractBeanDefine define, Object[] params) throws Throwable;
};