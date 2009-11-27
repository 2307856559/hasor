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
package org.more.submit;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.more.InvokeException;
import org.more.core.copybean.CopyBeanUtil;
import org.more.util.attribute.AttBase;
import org.more.util.attribute.IAttribute;
/**
 * ���ฺ�����action���ñ���ԣ�����ִ�е���������õ�Ŀ�귽��ӵ�з���ֵʱͨ��manager��
 * ����֮��doAction�Ὣ����ֵ���ء�manager���õ�action���Ѿ����������������֮��Ľ����
 * Date : 2009-6-25
 * @author ������
 */
@SuppressWarnings("unchecked")
public class ActionManager {
    /** ����װ����������action���� */
    private ActionContext           context            = null;                         //
    private ThreadLocal<IAttribute> paramContext       = new ThreadLocal<IAttribute>(); //��ǰ�߳��еĻ���������
    private IAttribute              publicParamContext = new AttBase();                //ȫ�ֻ�������
    /** ˽�л� */
    ActionManager() {}
    /**
     * ����action���ñ���Բ���ִ��action���ã���������õ�action�з���ֵ�򷵻ش���֮��ķ���ֵ��
     * doAction������ִ�е�Ŀ��action����ʱ��װ�������ļ�ָ����action��������
     * @param execActionExp ����action��ʹ�õ�action���ñ����
     * @param params ����actionĿ������action���ݵĲ�����
     * @throws InvokeException ����ڵ��ù����з������쳣��
     */
    public Object doAction(String execActionExp, Map params) throws InvokeException {
        if (context == null)
            throw new InvokeException("û������ActionContext����ִ�и÷�����");
        //
        String regex = "(.*)\\.(.*)";
        if (Pattern.matches(regex, execActionExp) == false)
            return null;
        //����action���ñ��ʽ�����һ�ø������ֲ���
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(execActionExp);
        m.find();
        String actionName = m.group(1);
        String methodName = m.group(2);
        //���Action�Ƿ���ڡ�
        if (this.context.containsAction(actionName) == false)
            throw new InvokeException("�Ҳ����򲻴�������Ϊ[" + actionName + "]��Action���塣");
        //���ָ��Action����ʹ��filterManager������й��������ء�
        PropxyAction action = this.context.findAction(actionName);
        //�����¼�����
        try {
            //****** �����event�ﶨ��������������� ******
            //������������
            ActionMethodEvent event = new ActionMethodEvent();
            event.setContext(this);
            event.setActionName(actionName);
            event.setMethod(methodName);//Ҫ���õ�Ŀ�귽������
            event.setInvokeString(execActionExp);
            event.setParam(params);
            if (this.paramContext.get() != null) {
                CopyBeanUtil copyUtil = CopyBeanUtil.newInstance();
                copyUtil.copy(this.publicParamContext, event, "ref");//����ȫ�ֻ�������
                copyUtil.copy(this.paramContext.get(), event, "ref");//�����߳ǻ�������
            }
            //����Ŀ�귽��
            Object obj = action.execute(methodName, event);
            return obj;
        } catch (InvokeException e) {
            throw e;
        }
    }
    /**
     * ����action���ñ���Բ���ִ��action���ã���������õ�action�з���ֵ�򷵻ش���֮��ķ���ֵ��
     * doAction������ִ�е�Ŀ��action����ʱ��װ�������ļ�ָ����action��������
     * @param execActionExp ����action��ʹ�õ�action���ñ����
     * @param params ����actionĿ������action���ݵĲ�����
     * @throws InvokeException ����ڵ��ù����з������쳣��
     */
    public Object doAction(String execActionExp, Object... params) throws InvokeException {
        Map map = new HashMap();
        for (int i = 0; i < params.length; i++)
            map.put(String.valueOf(i), params[i]);
        return this.doAction(execActionExp, map);
    }
    //=========================================================================================================
    /**
     * ����һ��������ActionManager�����У��ò����뵱ǰ�̰߳ﶨ������̵߳���action�����õĲ������ܻ���Ϊ�̻߳�����ͬ���ܵ�Ӱ�졣
     * @param name Ҫ���õĲ�������
     * @param value Ҫ���õĲ���ֵ��
     */
    public void addThreadContextParams(String name, Object value) {
        IAttribute att = this.paramContext.get();
        if (att == null) {
            att = new AttBase();
            this.paramContext.set(att);
        }
        if (value != null)
            att.setAttribute(name, value);
    }
    /**
     * ɾ��һ���Ѿ����õ�ActionManager�������������Ҫ��ɾ���Ĳ�����������÷�������������κ�Ч����
     * @param name Ҫɾ���Ĳ�������
     */
    public void removeThreadContextParams(String name) {
        IAttribute att = this.paramContext.get();
        if (att == null)
            return;
        att.removeAttribute(name);
    }
    /**
     * ����ActionManager�����������Ƿ����ĳ�����ԡ����������������򷵻�true���򷵻�false��
     * @param name Ҫ���ԵĻ�����������
     * @return ���������������򷵻�true���򷵻�false��
     */
    public boolean containsThreadContextParams(String name) {
        IAttribute att = this.paramContext.get();
        if (att == null)
            return false;
        return att.contains(name);
    }
    /** ���ActionManager����������ע����뵱ǰ�߳��йصĲ�������Щ��������addThreadContextParams����ע��ġ� */
    public void clearThreadContextParams() {
        this.paramContext.remove();
    }
    //=========================================================================================================
    /**
     * ���manager��action��������øû���֮�����������������ĸ�����������һЩ�����߼�������
     * @return ���manager��action��������øû���֮�����������������ĸ�����������һЩ�����߼�������
     */
    public ActionContext getContext() {
        return context;
    }
    /**
     * ����manager��action������managerͨ���û�����ȡ��action��
     * @param context Ҫ���õ�action������
     */
    public void setContext(ActionContext context) {
        this.context = context;
    }
}