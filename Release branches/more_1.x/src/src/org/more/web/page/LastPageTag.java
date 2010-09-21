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
 * ��ҳ��ǩ���������һҳ�ı�ǩ����ǩ������L���ǩ��
 * @version 2009-6-17
 * @author ������ (zyc@byshell.org)
 */
public class LastPageTag extends BasePageTag {
    /**  */
    private static final long serialVersionUID = -6705535575688797813L;
    /** ��ǩ���ԣ�ָ��Last��ǩ�Ƿ�ռ��ҳ�벿�ֵ����һ����true��ʾռ�죬false��ʾ��ռ�졣 */
    Object                    occupyLast       = "false";
    protected void doFirstStartPageTag() throws JspException {
        Object item = page.info.getItem(page.info.getSize() - 1); //������һ��
        //����Ƿ�ռ��ҳ�벿�ֵ����һ��
        boolean occupyLastB = StringConvert.parseBoolean(this.occupyLast.toString());
        //first���������ռ���һ�������һ��������ӵ�after�����б�־�����Ѿ�������
        if (occupyLastB == true && item != null)
            this.page.after.add(item);
        this.page.childTags.add(this);//�ڴ󻷾���ע���Լ���
    }
    protected int doStartPageTag() throws JspException {
        Object item = page.info.getItem(page.info.getSize() - 1); //������һ��
        //����������һ����ִ��
        if (item != this.page.info.getCurrentItem())
            return SKIP_BODY;
        else
            return this.doTag();
    }
    //============================================================
    public void setOccupyLast(Object occupyLast) {
        this.occupyLast = occupyLast;
    }
}
