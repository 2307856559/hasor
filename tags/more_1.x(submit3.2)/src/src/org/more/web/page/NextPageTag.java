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
import org.more.util.StringConvertUtil;
/**
 * ��ҳ��ǩ������ҳ��ı�ǩ����ǩ������N���ǩ��
 * @version 2009-6-17
 * @author ������ (zyc@byshell.org)
 */
public class NextPageTag extends BasePageTag {
    /**  */
    private static final long serialVersionUID = 9093745696928091335L;
    private String            first            = null;                //��ǰҳ�����Ǿ��һҳ�ļ������
    private String            last             = null;                //��ǰҳ�����Ǿ����һҳ�ļ������
    protected void doFirstStartPageTag() throws JspException {
        //��������ڲ��ü���������������Ϊ�����ڵķ�ҳ��ᱻִ��
        if (this.page.info.hasNext() == false)
            return;
        this.page.childTags.add(this);//ע���Լ�
        //����firstIndex��lastIndex
        int first = StringConvertUtil.parseInt(this.first, -1);//Ĭ��ֵ-1
        int last = StringConvertUtil.parseInt(this.last, -1);//Ĭ��ֵ-1
        //û������first��last
        if (first == -1 && last == -1)
            return;
        //================================================================
        //������first��last
        int last_true = this.page.info.getSize() - 1 - last;//
        //����firstIndex��lastIndex�Ե�ʱ�����������⡣
        if (first >= last_true)
            return;//���ֳ�������,������
        //
        Object firstItem = this.page.info.getItem(first);//��ȡ��ǵ�ͷ
        Object lastItem = this.page.info.getItem(last_true);//��ȡ��ǵ�β
        if (this.page.after.contains(firstItem) == false && firstItem != null)
            this.page.after.add(firstItem); //ռ��firstItem
        if (this.page.after.contains(lastItem) == false && lastItem != null)
            this.page.after.add(lastItem); //ռ��lastItem
    }
    protected int doStartPageTag() throws JspException {
        Object item = page.info.getCurrentItem();
        //����firstIndex��lastIndex
        int first = StringConvertUtil.parseInt(this.first, -1);//Ĭ��ֵ-1
        int last = StringConvertUtil.parseInt(this.last, -1);//Ĭ��ֵ-1
        //û������first��last
        if (first == -1 && last == -1) {
            if (this.page.after.contains(item) == false && item != null) {
                //û�˴���next����
                this.page.after.add(item);
                return this.doTag();
            } else
                return SKIP_BODY;
        }
        //================================================================
        //������first��last
        int last_true = this.page.info.getSize() - 1 - last;//
        //����firstIndex��lastIndex�Ե�ʱ�����������⡣
        if (first >= last_true)
            return SKIP_BODY;//���ֳ�������,������
        //
        Object firstItem = this.page.info.getItem(first);//��ȡ��ǵ�ͷ
        Object lastItem = this.page.info.getItem(last_true);//��ȡ��ǵ�β
        if (this.page.after.contains(firstItem) == true || this.page.after.contains(lastItem) == true)
            if (item == firstItem || item == lastItem)
                return this.doTag();
        return SKIP_BODY;
    }
    protected int doTag() throws JspException {
        Object item = page.info.getCurrentItem();
        //
        //�����ǰ�������ǵ�һ���first������occupyFirst����Ϊtrue��������
        if (page.info.isFirstIndex(0, item) == true) {
            BasePageTag[] fTag = this.page.getTagByClass(FirstPageTag.class);
            if (fTag.length == 1) {
                FirstPageTag ft = (FirstPageTag) fTag[0];
                boolean occupyFirst = StringConvertUtil.parseBoolean(ft.occupyFirst.toString());
                if (occupyFirst == true)
                    return SKIP_BODY;
            }
        }
        //�����ǰ�����������һ���last������occupyLast����Ϊtrue��������
        if (page.info.isLastIndex(0, item) == true) {
            BasePageTag[] fTag = this.page.getTagByClass(LastPageTag.class);
            if (fTag.length == 1) {
                LastPageTag ft = (LastPageTag) fTag[0];
                boolean occupyLast = StringConvertUtil.parseBoolean(ft.occupyLast.toString());
                if (occupyLast == true)
                    return SKIP_BODY;
            }
        }
        //�����ǰ�������ǵ�ǰ��,����������current��ǩ�����
        if (page.currentItem == item) {
            BasePageTag[] fTag = this.page.getTagByClass(CurrentPageTag.class);
            if (fTag.length != 0)
                return SKIP_BODY;
        }
        return super.doTag();
    }
    public int doEndTag() throws JspException {
        return super.doEndTag();
    }
    //====================================================
    public void setFirst(String first) {
        this.first = first;
    }
    public void setLast(String last) {
        this.last = last;
    }
}
