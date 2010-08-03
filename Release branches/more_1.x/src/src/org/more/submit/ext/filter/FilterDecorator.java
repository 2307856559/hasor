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
package org.more.submit.ext.filter;
import java.util.Iterator;
import java.util.LinkedList;
import org.more.NoDefinitionException;
import org.more.submit.ActionContext;
import org.more.submit.ActionContextDecorator;
import org.more.submit.ActionInvoke;
import org.more.submit.ActionStack;
import org.more.util.StringConvert;
/**
 * ʵ��submit��������װ������
 * @version : 2010-7-26
 * @author ������(zyc@byshell.org)
 */
public class FilterDecorator extends ActionContextDecorator {
    private FilterContext filterContext;
    private String[]      publicFilters = null;
    @Override
    public boolean initDecorator(ActionContext actionContext) {
        super.initDecorator(actionContext);
        if (actionContext instanceof FilterContext == false)
            return false;
        filterContext = (FilterContext) actionContext;
        return true;
    }
    @Override
    public ActionInvoke findAction(String actionName, String invoke) throws NoDefinitionException {
        ActionInvoke ai = super.findAction(actionName, invoke);
        LinkedList<String> ns = new LinkedList<String>();
        //1.���ȫ�ֹ�������
        if (this.publicFilters == null) {
            Iterator<String> filterNS = this.filterContext.getFilterNameIterator();
            while (filterNS.hasNext()) {
                String fName = filterNS.next();
                if (fName == null)
                    break;
                Class<?> filterType = this.filterContext.getFilterType(fName);
                Filter fts = filterType.getAnnotation(Filter.class);
                boolean isPublic = false;
                if (fts == null) {
                    String isPublicStr = (String) this.filterContext.getFilterProperty(fName, "isPublicFilter");
                    isPublic = StringConvert.parseBoolean(isPublicStr, false);
                } else
                    isPublic = fts.isPublic();
                //
                if (isPublic == true)
                    ns.add(fName);
            };
            this.publicFilters = new String[ns.size()];
            ns.toArray(this.publicFilters);
        } else
            for (String fn : this.publicFilters)
                ns.add(fn);
        //2.����ע���еĹ�������
        Class<?> actionType = this.getActionType(actionName);
        ActionFilters ats = actionType.getAnnotation(ActionFilters.class);
        if (ats != null)
            for (String n : ats.value())
                ns.add(n);
        //3.׷�������ļ��еĹ�������
        String obj = (String) super.getActionProperty(actionName, "actionFilters");
        if (obj == null || obj.equals("") == true) {} else
            for (String n : obj.split(","))
                ns.add(n);
        if (ns.size() == 0)
            return ai;
        //4.װ�������
        FilterChain chain = null;
        for (String filterName : ns) {
            if (this.filterContext.containsFilter(filterName) == false)
                throw new NoDefinitionException("�޷�װ�������" + filterName + "��ΪfilterContext�в��������Ķ��塣");
            //
            Class<?> filterType = this.filterContext.getFilterType(filterName);
            Filter ft = filterType.getAnnotation(Filter.class);
            if (ft == null) {
                String mark = (String) this.filterContext.getFilterProperty(filterName, "isFilter");
                if (StringConvert.parseBoolean(mark) == false)
                    throw new NoDefinitionException("������" + filterName + "û�б���ǳ�Ϊһ����Ч�Ĺ�����������û������isFilter��������û�б��ע�⡣");
            }
            //
            if (chain == null)
                chain = new FilterChain(ai);
            chain = new FilterChain(chain, this.filterContext.findFilter(filterName));
        }
        return new FilterActionInvoke(chain);
    }
}
/**
 * ���ฺ���ṩ{@link ActionFilter ActionFilter�ӿ�}��ActionInvoke�ӿ���ʽ��
 * @version 2009-12-1
 * @author ������ (zyc@byshell.org)
 */
class FilterActionInvoke implements ActionInvoke {
    //========================================================================================Field
    private FilterChain filterChain = null;
    //==================================================================================Constructor
    public FilterActionInvoke(FilterChain filterChain) {
        this.filterChain = filterChain;
    }
    //==========================================================================================Job
    @Override
    public Object invoke(ActionStack stack) throws Throwable {
        return filterChain.doInvokeFilter(stack);//ִ�й�����
    }
}