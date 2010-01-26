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
import org.more.util.StringConvert;
/**
 * ������һҳ�ı�ǩ
 * @version 2009-6-22
 * @author ������ (zyc@byshell.org)
 */
public class DownPageTag extends BasePageTag {
    /**  */
    private static final long serialVersionUID = 55468171157393674L;
    //����λ�� ����Ϊǰ��ĵڼ������Ϊ���棬0��������ߡ�
    private String            first            = null;              //��ǰҳ�����Ǿ��һҳ�ļ������
    private String            last             = null;              //��ǰҳ�����Ǿ����һҳ�ļ������
    @Override
    protected void doFirstStartPageTag() throws JspException {
        this.page.childTags.add(this);//�ڴ󻷾���ע���Լ���
    }
    @Override
    protected int doStartPageTag() throws JspException {
        Object item_c = this.page.info.getCurrentItem();
        //
        //����firstIndex��lastIndex
        int first = StringConvert.parseInt(this.first, -1);//Ĭ��ֵ-1
        int last = StringConvert.parseInt(this.last, -1);//Ĭ��ֵ-1
        //
        //Ҫ��������ʱ��
        if (first <= -1 && last <= -1)
            if (this.page.info.isLastIndex(0, item_c) == true)
                return this.thisDoTag();
        //Ҫ����ǰ��ʱ��
        if (first >= 0)
            if (this.page.info.isFirstIndex(first, item_c) == true)
                return this.thisDoTag();
        //Ҫ����β��
        if (last >= 0)
            if (this.page.info.isLastIndex(last, item_c) == true)
                return this.thisDoTag();
        return SKIP_BODY;
    }
    private int thisDoTag() {
        Object item = this.page.currentItem;
        int index_item = this.page.info.getList().indexOf(item) + 1;
        if (index_item < this.page.info.getList().size())
            item = this.page.info.getList().get(index_item);
        //��������
        //prefix  page_item  page_current  page_items
        String page_item = page.prefix + "_item";
        String page_current = page.prefix + "_current";
        String page_items = page.prefix + "_items";
        String page_index = page.prefix + "_index";
        //
        if (item != null)
            this.pageContext.setAttribute(page_item, item);
        if (page.currentItem != null)
            this.pageContext.setAttribute(page_current, page.currentItem);
        this.pageContext.setAttribute(page_items, page.info.getList());
        this.pageContext.setAttribute(page_index, page.info.getList().indexOf(item));
        return EVAL_BODY_INCLUDE;
    }
    public void setFirst(String first) {
        this.first = first;
    }
    public void setLast(String last) {
        this.last = last;
    }
}
