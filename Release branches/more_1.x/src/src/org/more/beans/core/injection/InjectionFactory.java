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
import org.more.beans.BeanFactory;
import org.more.beans.info.BeanDefinition;
import org.more.beans.info.IocTypeEnum;
import org.more.task.Task;
/**
 * ����bean�����Զ�ѡ��ע�뷽ʽ����ִ��ע�롣Exportע������������˵�̬ģʽ���Ӧ��ExportInjection����ᱻ���档
 * InjectionFactoryҲ��һ���������,��������������Ѿ��������ExportInjection����ע������ִ�з���doRun��ͬ��������
 * Date : 2009-11-9
 * @author ������
 */
public class InjectionFactory extends Task implements Injection {
    /**  */
    private static final long                serialVersionUID = 7265406116749265174L;
    /** �������Exportע�� */
    private HashMap<String, ExportInjection> exportMap        = new HashMap<String, ExportInjection>();
    /** Fact��ʽע�� */
    private FactInjection                    fact             = new FactInjection();
    /** Ioc��ʽע�� */
    private IocInjection                     ioc              = new IocInjection();
    //====================================================================================================
    /** ����bean�����Զ�ѡ��ע�뷽ʽ����ִ��ע�롣Exportע������������˵�̬ģʽ���Ӧ��ExportInjection����ᱻ���档 */
    @Override
    public void ioc(Object object, Object[] params, BeanDefinition definition, BeanFactory context) throws Throwable {
        if (definition.getIocType() == IocTypeEnum.Export) {
            //Export��ʽ�����Exportע���������˵�̬ģʽ���и��õ�����Ч�ʡ�
            String exportName = definition.getExportIocRefBean();
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
            exp.ioc(object, params, definition, context);
        } else if (definition.getIocType() == IocTypeEnum.Fact)
            //Fact��ʽ�������õ�����Ч�ʱ���֧��BeanDefinition���档
            this.fact.ioc(object, params, definition, context);
        else if (definition.getIocType() == IocTypeEnum.Ioc)
            //��ͳIoc�������õ�����Ч�ʱ���֧��BeanDefinition���档
            this.ioc.ioc(object, params, definition, context);
        else
            throw new InjectionException("δ֪ע�뷽ʽ���޷�ִ��ע�룡");
    }
    /** ��������InjectionFactory�����ExportInjection���󣬱������ExportInjection������bean���嶼�ǵ�̬����ע��÷�����ͬ�������� */
    @Override
    protected synchronized void doRun() throws Exception {
        this.exportMap.clear();
    }
}
