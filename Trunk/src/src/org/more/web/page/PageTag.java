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
package org.more.web.page;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
/**
 * ��ҳ��ǩ�������
 * Date : 2009-6-17
 * @author ������
 */
public class PageTag extends BodyTagSupport {
    /**  */
    private static final long serialVersionUID  = 2242078414461148271L;
    PageInfo                  info              = null;                         //Ҫִ�еķ�ҳ������
    //page_item��ҳ�����ݴ����ҳ���������еı�������ʹ��EL����Կ��Է��ʴ����ݡ�
    //page_current��ǰҳ��ҳ�����ݴ����ҳ���������еı�������ʹ��EL����Կ��Է��ʴ����ݡ�
    //page_items 
    String                    prefix            = "page";                       //����ǰ׺, 
    private String            expressionCurrent = null;                         //�жϵ�ǰҳ�ı����
    //-----------------
    Object                    currentItem       = null;                         //��ǰҳ�������
    boolean                   firstRun          = true;                         //�Ƿ��ǵ�һ��ѭ������һ��ѭ��ִ��Ŀ����Ϊ�˷���Item����ʾ˳��
    List<BasePageTag>         childTags         = new ArrayList<BasePageTag>(0); //���Զα����˵�ǰ��ǩ�а������ӱ�ǩ�������������ӱ�ǩ�Լ��򸸱�ǩע��ġ�
    List<Object>              after             = new ArrayList<Object>(0);     //����Ѿ��������
    //====================================================
    /**
     * ��ȡ�ض����͵��ӱ�ǩ���ϡ�
     * @param type Ҫ��ȡ�ı�ǩ���͡�
     * @return ���ػ�ȡ���ӱ�ǩ����
     */
    BasePageTag[] getTagByClass(Class<?> type) {
        List<BasePageTag> al = new ArrayList<BasePageTag>(0);
        for (BasePageTag bpt : childTags)
            //����һ�������ƥ��ɹ��б�
            if (bpt.getClass() == type)
                al.add(bpt);
        //
        BasePageTag[] tags = new BasePageTag[al.size()];
        al.toArray(tags);
        return tags;
    }
    /** ����obj�Ƿ�Ϊ��ҳ���еĵ�ǰ��ҳ�� */
    private boolean testCurrentItem(Object obj) {
        //�ж��Ƿ��в��Ϸ�����Ի��߱����Ϊ��
        if (expressionCurrent == null || Pattern.matches(".*=.*", this.expressionCurrent) == false)
            return false;
        //ͨ�����䷽ʽ��ȡ����
        try {
            String[] ns = expressionCurrent.split("=");
            ns[0] = ns[0].trim();
            ns[1] = ns[1].trim();
            //
            if (ns[0] == null || ns[0].equals(""))
                //��ns[0]Ϊ��
                return obj.equals(ns[1]);
            else {
                //��ns[0]��Ϊ��
                PropertyDescriptor pe = new PropertyDescriptor(ns[0], obj.getClass());
                Object returnObject = pe.getReadMethod().invoke(obj);
                if (returnObject == null)
                    return false;
                if (returnObject.toString().equals(ns[1]) == true)
                    return true;
                else
                    return false;
            }
        } catch (Exception e) {}
        return false;
    }
    /** ��������ǩpageʱִ�и÷������÷�������ʼ��info����ĵ��������ڵ��������ҳ� */
    @Override
    public int doStartTag() throws JspException {
        this.info.release();
        //���Ե������е�ǰ���Ƿ�Ϊ��ǰ��ҳ��
        while (this.info.hasNext() == true) {
            if (this.testCurrentItem(this.info.next()) == true)
                this.currentItem = this.info.getCurrentItem();
            if (this.currentItem != null)
                break;
        }
        this.info.release();
        this.firstRun = true;
        if (this.info.getCurrentItem() != null || this.info.hasNext() == true) {
            //ִ�б�ǩ������ִ��init����
            return EVAL_BODY_INCLUDE;
        } else
            //��ִ�б�ǩ��ִ��
            return SKIP_BODY;
    }
    //2
    @Override
    public int doAfterBody() throws JspException {
        if (this.info == null)
            throw new JspException("page��ǩ��������info���ԣ�������������PageInfo���͡�");
        if (this.prefix == null || this.prefix.equals(""))
            throw new JspException("page��ǩ��������prefix���ԣ�������������String���͡�");
        if (this.info.hasNext() == true) {
            //�����ǵ�һ��ִ��ʱ���е���
            if (this.firstRun == false)
                this.info.next();
            //���ִ�����һ��
            this.firstRun = false;
            // ѭ��ִ�б�ǩ
            return EVAL_BODY_AGAIN;
        } else
            //ִ�б��˱�ǩ֮���ڽ���ѭ��ִ��
            return SKIP_BODY;
    }
    //3������ʱ����
    @Override
    public int doEndTag() throws JspException {
        this.info = null;
        this.childTags.clear();
        this.after.clear();
        this.currentItem = null;
        return EVAL_PAGE;//ִ��ʢ�µ�ҳ��
    }
    //====================================================
    public void setInfo(PageInfo info) {
        this.info = info;
        this.info.initData();//ִ�г�ʼ������
    }
    public void setExpressionCurrent(String expressionCurrent) {
        this.expressionCurrent = expressionCurrent;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
