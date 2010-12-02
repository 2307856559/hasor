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
package org.more.hypha.beans.support;
import org.more.core.xml.XmlParserKit;
import org.more.hypha.beans.TypeManager;
import org.more.hypha.context.XmlDefineResource;
import org.more.hypha.context.XmlNameSpaceRegister;
/**
 * ����ʵ����{@link XmlNameSpaceRegister}�ӿڲ����ṩ�˶������ռ䡰http://project.byshell.org/more/schema/beans���Ľ���֧�֡�
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public class Register_Beans implements XmlNameSpaceRegister {
    /**���û��ָ��namespaceURL������ó�������ָ��Ĭ�ϵ������ռ䡣*/
    public static final String DefaultNameSpaceURL = "http://project.byshell.org/more/schema/beans";
    /**ִ�г�ʼ��ע�ᡣ*/
    public void initRegister(String namespaceURL, XmlDefineResource resource) {
        //1.ע���ǩ������
        XmlParserKit kit = new XmlParserKit();
        kit.regeditHook("/beans", new TagBeans_Beans(resource));
        kit.regeditHook("/beans/meta", new TagBeans_MetaData(resource));
        kit.regeditHook("/beans/defaultPackage", new TagBeans_DefaultPackage(resource));
        kit.regeditHook(new String[] { "/beans/package", "/beans/*/package" }, new TagBeans_Package(resource));
        //
        kit.regeditHook(new String[] { "/beans/classBean", "/beans/*/classBean" }, new TagBeans_ClassBean(resource));
        kit.regeditHook(new String[] { "/beans/generateBean", "/beans/*/generateBean" }, new TagBeans_GenerateBean(resource));
        kit.regeditHook(new String[] { "/beans/refBean", "/beans/*/refBean" }, new TagBeans_RefBean(resource));
        kit.regeditHook(new String[] { "/beans/scriptBean", "/beans/*/scriptBean" }, new TagBeans_ScriptBean(resource));//ͬʱ�߱�text��element
        kit.regeditHook(new String[] { "/beans/templateBean", "/beans/*/templateBean" }, new TagBeans_TemplateBean(resource));
        kit.regeditHook(new String[] { "/beans/varBean", "/beans/*/varBean" }, new TagBeans_VarBean(resource));
        //
        kit.regeditHook("/beans/*Bean/constructor-arg", new TagBeans_Constructor(resource));
        kit.regeditHook("/beans/*Bean/property", new TagBeans_Property(resource));
        kit.regeditHook("/beans/*Bean/meta", new TagBeans_MetaData(resource));
        kit.regeditHook("/beans/*Bean/*/meta", new TagBeans_MetaData(resource));
        kit.regeditHook("/beans/*Bean/method", new TagBeans_Method(resource));
        kit.regeditHook("/beans/*Bean/method/param", new TagBeans_Param(resource));
        //
        kit.regeditHook("/beans/*Bean/*/value", new TagBeans_Value(resource));
        kit.regeditHook("/beans/*Bean/*/date", new TagBeans_Date(resource));
        kit.regeditHook("/beans/*Bean/*/enum", new TagBeans_Enum(resource));
        kit.regeditHook("/beans/*Bean/*/bigText", new TagBeans_BigText(resource));
        kit.regeditHook("/beans/*Bean/*/ref", new TagBeans_Ref(resource));
        kit.regeditHook("/beans/*Bean/*/file", new TagBeans_File(resource));
        kit.regeditHook("/beans/*Bean/*/directory", new TagBeans_Directory(resource));
        kit.regeditHook("/beans/*Bean/*/uri", new TagBeans_URI(resource));
        kit.regeditHook("/beans/*Bean/*/el", new TagBeans_EL(resource));
        kit.regeditHook("/beans/*Bean/*/array", new TagBeans_Array(resource));
        kit.regeditHook("/beans/*Bean/*/set", new TagBeans_Set(resource));
        kit.regeditHook("/beans/*Bean/*/list", new TagBeans_List(resource));
        kit.regeditHook("/beans/*Bean/*/map", new TagBeans_Map(resource));
        kit.regeditHook("/beans/*Bean/*/map/entity", new TagBeans_Entity(resource));
        //
        //2.ע�������ռ�
        if (namespaceURL == null)
            namespaceURL = DefaultNameSpaceURL;
        resource.regeditXmlParserKit(namespaceURL, kit);
        //3.ע���������ֵ��������˳��������ȼ���
        /*��xml����ͼ�����˶����������ֵʱ�����ȼ����������ã�����ͬʱ������value �� refBean���ԡ���ôvalue�����ȼ���refBean�ߡ�*/
        TypeManager typeManager = resource.getTypeManager();
        typeManager.regeditTypeParser(new QPP_Value());
        typeManager.regeditTypeParser(new QPP_EL());
        typeManager.regeditTypeParser(new QPP_Date());
        typeManager.regeditTypeParser(new QPP_Enum());
        typeManager.regeditTypeParser(new QPP_Ref());
        typeManager.regeditTypeParser(new QPP_File());
        typeManager.regeditTypeParser(new QPP_Directory());
        typeManager.regeditTypeParser(new QPP_URILocation());
    }
}