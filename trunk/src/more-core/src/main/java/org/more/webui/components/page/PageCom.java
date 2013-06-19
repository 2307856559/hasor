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
package org.more.webui.components.page;
import org.more.webui.context.ViewContext;
import org.more.webui.support.UICom;
import org.more.webui.support.UIComponent;
import org.more.webui.support.values.AbstractValueHolder;
/**
 * ��ҳ�齨
 * @version : 2012-5-15
 * @author ������ (zyc@byshell.org)
 */
@UICom(tagName = "ui_Page")
public class PageCom extends UIComponent {
    /**���Ա�*/
    public static enum Propertys {
        /**�Ƿ���ʾ����ҳ����ť*/
        showFirst,
        /**�Ƿ���ʾ����һҳ����ť*/
        showPrev,
        /**�Ƿ���ʾ����һҳ����ť*/
        showNext,
        /**�Ƿ���ʾ��βҳ����ť*/
        showLast,
        /**��ʼ��ҳ���*/
        startWith,
        /**ҳ��С*/
        pageSize,
        /**��ǰҳ*/
        currentPage,
        /**��¼����*/
        rowCount,
        /**��û������ʱ��ʾģʽ���ɵ��ӣ����ŷָ��F(��ҳ��ť)��P(��һҳ��ť)��N(��һҳ��ť)��L��βҳ��ť����I(ҳ�밴ť)��T(��ʾui_pNoDate��ǩ����)��ע��I��Tֻ����һ����Ч*/
        noDateMode,
        /**��ҳ�齨������*/
        pageLink,
    };
    @Override
    protected void initUIComponent(ViewContext viewContext) {
        super.initUIComponent(viewContext);
        this.setProperty(Propertys.showFirst.name(), false);
        this.setProperty(Propertys.showPrev.name(), true);
        this.setProperty(Propertys.showNext.name(), true);
        this.setProperty(Propertys.showLast.name(), false);
        this.setProperty(Propertys.startWith.name(), 0);
        this.setProperty(Propertys.pageSize.name(), 20);
        this.setProperty(Propertys.currentPage.name(), 0);
        this.setProperty(Propertys.rowCount.name(), 0);
        this.setProperty(Propertys.noDateMode.name(), "T");
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
    Mode runMode = null;
    enum Mode {
        First, Prev, Item, Next, Last, NoDate
    }
}