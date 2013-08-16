/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
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
package org.more.webui.components.page;
import org.more.webui.component.UIComponent;
import org.more.webui.component.support.UICom;
import org.more.webui.component.values.AbstractValueHolder;
import org.more.webui.context.ViewContext;
/**
 * ��ҳ�齨
 * @version : 2012-5-15
 * @author ������ (zyc@byshell.org)
 */
@UICom(tagName = "ui_Page", renderType = PageRender.class)
public class PageCom extends UIComponent {
    /**���Ա�*/
    public static enum Propertys {
        /**�Ƿ���ʾ����ҳ����ť��RW��*/
        showFirst,
        /**�Ƿ���ʾ����һҳ����ť��RW��*/
        showPrev,
        /**�Ƿ���ʾ��ҳ�롿��ť��RW��*/
        showNum,
        /**�Ƿ���ʾ����һҳ����ť��RW��*/
        showNext,
        /**�Ƿ���ʾ��βҳ����ť��RW��*/
        showLast,
        /**��ʼ��ҳ��ţ�RW��*/
        startWith,
        /**ҳ��С��RW��*/
        pageSize,
        /**��ǰҳ��RW��*/
        currentPage,
        /**��¼������RW��*/
        rowCount,
        /**��û������ʱ��ʾģʽ���ɵ��ӣ����ŷָ��B����ҳ��С���ã���G��ҳ������򣩡�F(��ҳ��ť)��P(��һҳ��ť)��N(��һҳ��ť)��L��βҳ��ť����I(ҳ�밴ť)��T(��ʾui_pNoDate��ǩ����)��ע��I��Tֻ����һ����Ч��RW��*/
        noDateMode,
        /**��ҳ�齨�����ӣ�RW��*/
        pageLink,
    };
    @Override
    public String getComponentType() {
        return "ui_Page";
    }
    @Override
    protected void initUIComponent(ViewContext viewContext) {
        super.initUIComponent(viewContext);
        this.setPropertyMetaValue(Propertys.showFirst.name(), false);
        this.setPropertyMetaValue(Propertys.showPrev.name(), true);
        this.setPropertyMetaValue(Propertys.showNum.name(), true);
        this.setPropertyMetaValue(Propertys.showNext.name(), true);
        this.setPropertyMetaValue(Propertys.showLast.name(), false);
        this.setPropertyMetaValue(Propertys.startWith.name(), 0);
        this.setPropertyMetaValue(Propertys.pageSize.name(), 20);
        this.setPropertyMetaValue(Propertys.currentPage.name(), 0);
        this.setPropertyMetaValue(Propertys.rowCount.name(), 0);
        this.setPropertyMetaValue(Propertys.noDateMode.name(), "T");
    }
    /**�Ƿ���ʾ����ҳ����ť*/
    public boolean isShowFirst() {
        return this.getProperty(Propertys.showFirst.name()).valueTo(Boolean.TYPE);
    }
    /**�Ƿ���ʾ����ҳ����ť*/
    public void setShowFirst(boolean showFirst) {
        this.getProperty(Propertys.showFirst.name()).value(showFirst);
    }
    /**�Ƿ���ʾ����һҳ����ť*/
    public boolean isShowPrev() {
        return this.getProperty(Propertys.showPrev.name()).valueTo(Boolean.TYPE);
    }
    /**�Ƿ���ʾ����һҳ����ť*/
    public void setShowPrev(boolean showPrev) {
        this.getProperty(Propertys.showPrev.name()).value(showPrev);
    }
    /**�Ƿ���ʾ��ҳ�롿��ť��RW��*/
    public boolean isShowNum() {
        return this.getProperty(Propertys.showNum.name()).valueTo(Boolean.TYPE);
    }
    /**�Ƿ���ʾ��ҳ�롿��ť��RW��*/
    public void setShowNum(boolean showNum) {
        this.getProperty(Propertys.showNum.name()).value(showNum);
    }
    /**�Ƿ���ʾ����һҳ����ť*/
    public boolean isShowNext() {
        return this.getProperty(Propertys.showNext.name()).valueTo(Boolean.TYPE);
    }
    /**�Ƿ���ʾ����һҳ����ť*/
    public void setShowNext(boolean showNext) {
        this.getProperty(Propertys.showNext.name()).value(showNext);
    }
    /**�Ƿ���ʾ��βҳ����ť*/
    public boolean isShowLast() {
        return this.getProperty(Propertys.showLast.name()).valueTo(Boolean.TYPE);
    }
    /**�Ƿ���ʾ��βҳ����ť*/
    public void setShowLast(boolean showLast) {
        this.getProperty(Propertys.showLast.name()).value(showLast);
    }
    /**��ʼ��ҳ���*/
    public int getStartWith() {
        return this.getProperty(Propertys.startWith.name()).valueTo(Integer.TYPE);
    }
    /**��ʼ��ҳ���*/
    public void setStartWith(Integer startWith) {
        this.getProperty(Propertys.startWith.name()).value(startWith);
    }
    /**ҳ��С*/
    public int getPageSize() {
        return this.getProperty(Propertys.pageSize.name()).valueTo(Integer.TYPE);
    }
    /**ҳ��С*/
    public void setDataSource(Integer pageSize) {
        this.getProperty(Propertys.pageSize.name()).value(pageSize);
    }
    /**��ǰҳ��*/
    public int getCurrentPage() {
        return this.getProperty(Propertys.currentPage.name()).valueTo(Integer.TYPE);
    }
    /**��ǰҳ��*/
    public void setCurrentPage(int currentPage) {
        this.getProperty(Propertys.currentPage.name()).value(currentPage);
    }
    /**��¼����*/
    public int getRowCount() {
        return this.getProperty(Propertys.rowCount.name()).valueTo(Integer.TYPE);
    }
    /**��¼����*/
    public void setRowCount(int rowCount) {
        this.getProperty(Propertys.rowCount.name()).value(rowCount);
    }
    /**��û������ʱ��ʾģʽ���ɵ��ӣ����ŷָ��F(��ҳ��ť)��P(��һҳ��ť)��N(��һҳ��ť)��L��βҳ��ť����I(ҳ�밴ť)��T(��ʾui_pNoDate��ǩ����)��ע��I��Tֻ����һ����Ч*/
    public String getNoDateMode() {
        return this.getProperty(Propertys.noDateMode.name()).valueTo(String.class);
    }
    /**��û������ʱ��ʾģʽ���ɵ��ӣ����ŷָ��F(��ҳ��ť)��P(��һҳ��ť)��N(��һҳ��ť)��L��βҳ��ť����I(ҳ�밴ť)��T(��ʾui_pNoDate��ǩ����)��ע��I��Tֻ����һ����Ч*/
    public void setNoDateMode(String noDateMode) {
        this.getProperty(Propertys.noDateMode.name()).value(noDateMode);
    }
    /**��ҳ�齨������(ʹ��EL����)*/
    public String getPageLink() {
        return this.getProperty(Propertys.pageLink.name()).valueTo(String.class);
    }
    /**��ҳ�齨������(ʹ��EL����)*/
    public void setPageLink(String pageLink) {
        this.getProperty(Propertys.pageLink.name()).value(pageLink);
    }
    /**��ҳ�齨������(ʹ��ģ�����)*/
    public String getPageLinkAsTemplate(ViewContext viewContext) {
        AbstractValueHolder avh = this.getProperty(Propertys.pageLink.name());
        String metaValue = (String) avh.getMetaValue();
        try {
            return viewContext.processTemplateString(metaValue);
        } catch (Exception e) {
            return "javascript:alert('PageCom.getPageLinkAsTemplate()����ִ����������');";
        }
    }
    /*-------------------------------------------------------------------------------*/
    Mode renderMode = null;
    public static enum Mode {
        First, Prev, Item, Next, Last, NoDate
    }
}