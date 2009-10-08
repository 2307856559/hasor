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
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;
/**
 * ��ҳ��ǩ��Ļ��࣬�����ж����˱�ǩ�״�ִ�еķ�������ִ�з�����
 * Date : 2009-6-17
 * @author ������
 */
abstract class BasePageTag extends BodyTagSupport {
    protected PageTag page = null; // ����ǩ
    //1
    //�÷��������ڵ�һ��ѭ��ʱ����doFirstStartPageTag�����Ժ������ѭ������doStartPageTag������
    @Override
    public int doStartTag() throws JspException {
        //��鸸��ǩ
        Tag tag = this.getParent();
        if (tag instanceof PageTag == false)
            throw new JspException("last��ǩ������page��ǩ�¡�");
        page = (PageTag) tag; //ǿ��ת���ɸ���ǩ�ı�ǩ����
        //====����Ƿ�Ϊ��һ��ִ��====��
        if (page.firstRun == true) {
            this.doFirstStartPageTag();//ִ���״�ִ�д�������
            return SKIP_BODY;//����ִ��
        }
        //�����ǰ��ĿΪ����ִ��doStartPageTag����Ϊ��ǰ��ĿΪ�տ�����ζ���Ѿ���ȫѭ����ϡ�
        if (this.page.info.getCurrentItem() == null)
            return SKIP_BODY;
        //
        return this.doStartPageTag();
    }
    //3
    @Override
    public int doEndTag() throws JspException {
        this.page = null;
        return super.doEndTag();
    }
    /** �÷�������ִ�б�ǩ���������ñ�Ҫ������ */
    protected int doTag() throws JspException {
        //prefix  page_item  page_current  page_items
        String page_item = page.prefix + "_item";
        String page_current = page.prefix + "_current";
        String page_items = page.prefix + "_items";
        String page_index = page.prefix + "_index";
        //
        if (page.info.getCurrentItem() != null)
            this.pageContext.setAttribute(page_item, page.info.getCurrentItem());
        if (page.currentItem != null)
            this.pageContext.setAttribute(page_current, page.currentItem);
        this.pageContext.setAttribute(page_items, page.info.getList());
        this.pageContext.setAttribute(page_index, page.info.getList().indexOf(page.info.getCurrentItem()));
        return EVAL_BODY_INCLUDE;
    }
    /** �����״�ִ�еĴ����� */
    protected abstract void doFirstStartPageTag() throws JspException;
    /** ����ִ�еĴ����� */
    protected abstract int doStartPageTag() throws JspException;
}
