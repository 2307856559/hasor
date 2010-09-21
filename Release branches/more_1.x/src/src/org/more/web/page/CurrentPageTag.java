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
 * ��ҳ��ǩ������ǰҳ�ı�ǩ����ǩ������C���ǩ��
 * @version 2009-6-17
 * @author ������ (zyc@byshell.org)
 */
public class CurrentPageTag extends BasePageTag {
    /**  */
    private static final long serialVersionUID = -2131953397444450076L;
    protected void doFirstStartPageTag() throws JspException {
        this.page.childTags.add(this);//�ڴ󻷾���ע���Լ���
    }
    protected int doStartPageTag() throws JspException {
        Object item = this.page.info.getCurrentItem();
        //�����ǰѭ������ǵ�ǰ����ô����
        if (this.page.currentItem != item)
            return SKIP_BODY;
        //
        //�����ǰ���ǵ�һ���first������occupyFirst����Ϊtrue��������
        if (page.info.isFirstIndex(0, item) == true) {
            BasePageTag[] fTag = this.page.getTagByClass(FirstPageTag.class);
            if (fTag.length == 1) {
                FirstPageTag ft = (FirstPageTag) fTag[0];
                boolean occupyFirst = StringConvert.parseBoolean(ft.occupyFirst.toString());
                if (occupyFirst == true)
                    return SKIP_BODY;
            }
        }
        //�����ǰ�������һ���last������occupyLast����Ϊtrue��������
        if (page.info.isLastIndex(0, item) == true) {
            BasePageTag[] fTag = this.page.getTagByClass(LastPageTag.class);
            if (fTag.length == 1) {
                LastPageTag ft = (LastPageTag) fTag[0];
                boolean occupyLast = StringConvert.parseBoolean(ft.occupyLast.toString());
                if (occupyLast == true)
                    return SKIP_BODY;
            }
        }
        return this.doTag();
    }
}