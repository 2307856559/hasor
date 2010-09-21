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
 * ��ҳ��ǩ�������һҳ�ı�ǩ����ǩ������F���ǩ��
 * @version 2009-6-17
 * @author ������ (zyc@byshell.org)
 */
public class FirstPageTag extends BasePageTag {
    /**  */
    private static final long serialVersionUID = -9201682684698198985L;
    /** ��ǩ���ԣ�ָ��First��ǩ�Ƿ�ռ��ҳ�벿�ֵĵ�һ�������ռ����ҳ��ӵڶ�����ʼ������ҳ��ӵ�һ����ʼ��true��ʾռ�죬false��ʾ��ռ�졣 */
    Object                    occupyFirst      = "false";
    protected void doFirstStartPageTag() throws JspException {
        Object item = page.info.getItem(0); //��õ�һ��
        //����Ƿ�ռ��ҳ�벿�ֵĵ�һ��
        boolean occupyFirstB = StringConvert.parseBoolean(this.occupyFirst.toString());
        //first���������ռ��һ�����һ��������ӵ�after�����б�־�����Ѿ�������
        if (occupyFirstB == true && item != null)
            this.page.after.add(item);
        this.page.childTags.add(this);//�ڴ󻷾���ע���Լ���
    }
    protected int doStartPageTag() throws JspException {
        Object item = page.info.getItem(0); //��õ�һ��
        //������ǵ�һ����ִ��
        if (item != this.page.info.getCurrentItem())
            return SKIP_BODY;
        else
            return this.doTag();
    }
    //============================================================
    public void setOccupyFirst(Object occupyFirst) {
        this.occupyFirst = occupyFirst;
    }
}
