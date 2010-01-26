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
package org.more.beans.core.injection;
import java.util.HashMap;
import org.more.beans.core.ResourceBeanFactory;
import org.more.beans.core.propparser.MainPropertyParser;
import org.more.beans.info.BeanDefinition;
import org.more.beans.info.IocTypeEnum;
import org.more.core.task.Task;
/**
 * ����bean�����Զ�ѡ��ע�뷽ʽ����ִ��ע�롣Exportע������������˵�̬ģʽ���Ӧ��{@link ExportInjection}����ᱻ���档
 * InjectionFactoryҲ��һ������({@link Task})����,��������������Ѿ��������{@link ExportInjection}����
 * ע������ִ�з���doRun��ͬ��������
 * @version 2009-11-9
 * @author ������ (zyc@byshell.org)
 */
public class InjectionFactory extends Task implements Injection {
    //========================================================================================Field
    /**  */
    private static final long                serialVersionUID = 7265406116749265174L;
    /** �������Exportע�� */
    private HashMap<String, ExportInjection> exportMap        = new HashMap<String, ExportInjection>();
    /** Fact��ʽע�� */
    private FactInjection                    fact;
    /** Ioc��ʽע�� */
    private IocInjection                     ioc;
    //==================================================================================Constructor
    /**����һ��IocInjection���󣬴���ʱ����ָ�����Խ�������*/
    public InjectionFactory(MainPropertyParser propParser) {
        if (propParser == null)
            throw new NullPointerException("����ָ��propParser��������IocInjectionʹ��������Խ������������ԡ�");
        /** Fact��ʽע�� */
        this.fact = new FactInjection();
        /** Ioc��ʽע�� */
        this.ioc = new IocInjection(propParser);
    }
    //==========================================================================================Job
    /** ����bean�����Զ�ѡ��ע�뷽ʽ����ִ��ע�롣Exportע������������˵�̬ģʽ���Ӧ��{@link ExportInjection}����ᱻ���档 */
    @Override
    public Object ioc(Object object, Object[] params, BeanDefinition definition, ResourceBeanFactory context) throws Exception {
        if (definition.getIocType() == IocTypeEnum.Export) {
            //Export��ʽ�����Exportע���������˵�̬ģʽ���и��õ�����Ч�ʡ�
            String exportName = definition.getExportRefBean();
            ExportInjection exp = null;
            if (this.exportMap.containsKey(exportName) == false) {
                Object exportObj = context.getBean(exportName, params);
                if (exportObj == null || exportObj instanceof ExportInjectionProperty == false)
                    throw new InjectionException("�޷�װ��" + exportName + " Exportע����������ע����������ת��ΪExportInjectionProperty���͡�");
                exp = new ExportInjection((ExportInjectionProperty) exportObj);
                if (context.isSingleton(exportName) == true)
                    this.exportMap.put(exportName, exp);
            } else
                exp = this.exportMap.get(exportName);
            return exp.ioc(object, params, definition, context);
        } else if (definition.getIocType() == IocTypeEnum.Fact)
            //Fact��ʽ�������õ�����Ч�ʱ���֧��BeanDefinition���档
            return this.fact.ioc(object, params, definition, context);
        else if (definition.getIocType() == IocTypeEnum.Ioc)
            //��ͳIoc�������õ�����Ч�ʱ���֧��BeanDefinition���档
            return this.ioc.ioc(object, params, definition, context);
        else
            throw new InjectionException("δ֪ע�뷽ʽ���޷�ִ��ע�룡");
    }
    /**
     * ��������InjectionFactory�����{@link ExportInjection}���󣬱������{@link ExportInjection}������bean���嶼�ǵ�̬����
     * ע��÷�����ͬ����������{@link org.more.beans.core.ResourceBeanFactory}������clearBeanCache����ʱҲ�ἤ��doRun��ִ�С�
     *  */
    @Override
    protected synchronized void doRun() throws Exception {
        this.exportMap.clear();
    }
}