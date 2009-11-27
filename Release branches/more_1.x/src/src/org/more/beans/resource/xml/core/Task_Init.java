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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLStreamReader;
import org.more.beans.info.BeanDefinition;
import org.more.beans.resource.xml.ContextStack;
import org.more.beans.resource.xml.TagProcess;
import org.more.beans.resource.xml.TaskProcess;
import org.more.util.attribute.AttBase;
/**
 * ִ�г�ʼ��ɨ��XML����������װ�ؾ�̬�����Լ��Ķ�beans�������ԡ�<br/>
 * �����Ƿ����ĳ��bean������<br/>
 * ��������init<br/>
 * �����������<br/>
 * ����ֵ��<br/>
 * (int)staticCacheĬ��ֵ10����ʾ��̬�����С��<br/>
 * (HashMap<String, BeanDefinition>)beanMap����ʾ��̬���档<br/>
 * (int)dynamicCacheĬ��ֵ50����ʾ��̬�����С��
 * <br/>Date : 2009-11-24
 * @author ������
 */
public class Task_Init implements TaskProcess {
    private AttBase                         result             = new AttBase();
    private int                             currentStaticCatch = 0;
    private Integer                         maxStaticCatch     = 10;
    private HashMap<String, BeanDefinition> beanMap            = new HashMap<String, BeanDefinition>();
    private ArrayList<String>               initBean           = new ArrayList<String>(0);
    private ArrayList<String>               allNames           = new ArrayList<String>(0);
    @Override
    public void setConfig(Object[] params) {}
    @Override
    public Object getResult() {
        result.setAttribute("beanList", beanMap);
        result.setAttribute("initBean", initBean);
        result.setAttribute("allNames", allNames);
        return result;
    }
    @Override
    public void onEvent(ContextStack elementStack, String onXPath, int eventType, XMLStreamReader reader, Map<String, TagProcess> tagProcessMap) {
        String tagName = elementStack.getTagName();
        TagProcess process = tagProcessMap.get(tagName);
        /*------------*/
        switch (eventType) {
        case START_ELEMENT:
            process.doStartEvent(onXPath, reader, elementStack);
            if (tagName.equals("beans") == true) {
                maxStaticCatch = (Integer) elementStack.get("staticCache");
                maxStaticCatch = (maxStaticCatch == null) ? 50 : maxStaticCatch;
                result.setAttribute("staticCache", maxStaticCatch);
                result.setAttribute("dynamicCache", elementStack.get("dynamicCache"));
            }
            break;
        case END_ELEMENT:
            process.doEndEvent(onXPath, reader, elementStack);
            if (tagName.equals("bean") == true) {
                BeanDefinition bean = (BeanDefinition) elementStack.context;
                if (maxStaticCatch > currentStaticCatch)
                    if (currentStaticCatch < maxStaticCatch) {
                        beanMap.put(bean.getName(), bean);//��̬����
                        currentStaticCatch++;
                    }
                if (bean.isLazyInit() == false && bean.isSingleton() == true)
                    initBean.add(bean.getName());//initBeans
                allNames.add(bean.getName());
            }
            break;
        case CDATA:
            process.doCharEvent(onXPath, reader, elementStack);
            break;
        case CHARACTERS:
            process.doCharEvent(onXPath, reader, elementStack);
            break;
        }
    }
}