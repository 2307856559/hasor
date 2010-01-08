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
package org.more.beans.resource.xml.core;
import java.lang.reflect.Array;
import java.util.ArrayList;
import javax.xml.stream.XMLStreamReader;
import org.more.NoDefinitionException;
import org.more.beans.info.BeanDefinition;
import org.more.beans.info.BeanInterface;
import org.more.beans.info.BeanProperty;
import org.more.beans.info.CreateTypeEnum;
import org.more.beans.info.IocTypeEnum;
import org.more.beans.resource.xml.ContextStack;
import org.more.beans.resource.xml.TagProcess;
import org.more.util.StringConvert;
/**
 * ���ฺ����bean��ǩ<br/>
 * id="" name="test_1" type="int" singleton="true" iocType="Export" export-refBean="abc" lazyInit="true" aopFilters-refBean="xxx,xxx,xxx"
 * @version 2009-11-21
 * @author ������ (zyc@byshell.org)
 */
@SuppressWarnings("unchecked")
public class Tag_Bean extends TagProcess {
    @Override
    public void doStartEvent(String xPath, XMLStreamReader xmlReader, ContextStack context) {
        //
        BeanDefinition bean = new BeanDefinition();
        bean.setCreateType(CreateTypeEnum.New);
        //һ����������
        int attCount = xmlReader.getAttributeCount();
        for (int i = 0; i < attCount; i++) {
            String key = xmlReader.getAttributeLocalName(i);
            String var = xmlReader.getAttributeValue(i);
            if (key.equals("id") == true)
                bean.setId(var);
            else if (key.equals("name") == true)
                bean.setName(var);
            else if (key.equals("type") == true)
                bean.setPropType(var);
            else if (key.equals("singleton") == true)
                bean.setSingleton(StringConvert.parseBoolean(var, true));//Ĭ�ϵ�̬����
            else if (key.equals("iocType") == true)
                /* ---------- */
                if (var.equals("Ioc") == true)
                    bean.setIocType(IocTypeEnum.Ioc);
                else if (var.equals("Fact") == true)
                    bean.setIocType(IocTypeEnum.Fact);
                else if (var.equals("Export") == true)
                    bean.setIocType(IocTypeEnum.Export);
                else
                    bean.setIocType(IocTypeEnum.Ioc);
            /* ---------- */
            else if (key.equals("export-refBean") == true)
                bean.setExportRefBean(var);
            else if (key.equals("lazyInit") == true)
                bean.setLazyInit(StringConvert.parseBoolean(var, true));
            else if (key.equals("aopFilters-refBean") == true)
                bean.setAopFiltersRefBean(var.split(","));
            else
                throw new NoDefinitionException("bean��ǩ����δ��������[" + key + "]");
        }
        context.context = bean;
    }
    @Override
    public void doEndEvent(String xPath, XMLStreamReader xmlReader, ContextStack context) {
        BeanDefinition bean = (BeanDefinition) context.context;
        /* �������ã�tag_Property*/
        if (context.containsKey("tag_Property") == true) {
            ArrayList al = (ArrayList) context.get("tag_Property");
            BeanProperty[] propertys = (BeanProperty[]) this.toArray(al, BeanProperty.class);
            bean.setPropertys(propertys);
        }
        /* ��������������tag_MethodParam*/
        if (context.containsKey("tag_MethodParam") == true) {
            ArrayList al = (ArrayList) context.get("tag_MethodParam");
            BeanProperty[] propertys = (BeanProperty[]) this.toArray(al, BeanProperty.class);
            bean.setFactoryMethodParams(propertys);
        }
        /* ���췽��������tag_ConstructorArg*/
        if (context.containsKey("tag_ConstructorArg") == true) {
            ArrayList al = (ArrayList) context.get("tag_ConstructorArg");
            BeanProperty[] propertys = (BeanProperty[]) this.toArray(al, BeanProperty.class);
            bean.setConstructorParams(propertys);
        }
        /* ���ӽӿ�ʵ�֣�tag_AddImpl*/
        if (context.containsKey("tag_AddImpl") == true) {
            ArrayList al = (ArrayList) context.get("tag_AddImpl");
            BeanInterface[] propertys = (BeanInterface[]) this.toArray(al, BeanInterface.class);
            bean.setImplImplInterface(propertys);
        }
        //        /*------------------*/
        //        ContextStack parent = context.getParent();
        //        //��������bean������
        //        ArrayList allBeanNS = (ArrayList) parent.get("allBeanNS");
        //        allBeanNS.add(bean.getName());
        //        //��������lazy����Ϊfalse�����������˵�̬Ϊtrue��bean���ơ�
        //        ArrayList initBeanNS = (ArrayList) parent.get("initBeanNS");
        //        if (bean.isLazyInit() == false && bean.isSingleton() == true)
        //            initBeanNS.add(bean.getName());
        //        //�����̬�����л��еط��ͱ���һ�ݡ�
        //        Integer staticCatch = (Integer) parent.get("staticCatch");
        //        Integer staticCatchCurrent = (Integer) parent.get("staticCatchCurrent");
        //        if (staticCatchCurrent < staticCatch) {
        //            ArrayList al = (ArrayList) parent.context;
        //            al.add(context.context);
        //            staticCatchCurrent++;
        //            parent.setAttribute("staticCatchCurrent", staticCatchCurrent);
        //        }
    }
    private Object[] toArray(ArrayList al, Class<?> toType) {
        Object array = Array.newInstance(toType, al.size());
        for (int i = al.size() - 1; i >= 0; i--) {
            Object obj = al.get(i);
            Array.set(array, i, toType.cast(obj));
        }
        return (Object[]) array;
    }
}