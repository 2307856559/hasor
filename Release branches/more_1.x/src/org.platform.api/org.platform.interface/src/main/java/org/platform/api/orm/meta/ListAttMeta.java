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
package org.platform.api.orm.meta;
/**
 * һ�Զࣺӳ�伯�ϣ����û������column������ʹ��PK��Ϊ���������������У�����att��ǩ���������Զ��壩
 * @version : 2013-1-27
 * @author ������ (zyc@byshell.org)
 */
public class ListAttMeta extends AttMeta {
    /**���ʵ�����ơ�*/
    private String        forEntity   = "";
    /**���ʵ��������С�*/
    private String        forProperty = "";
    /**�����У���ֵ��ʾ������*/
    private String        orderBy     = "";
    /**����ģʽ*/
    private OrderModeEnum orderMode   = OrderModeEnum.asc;
    /**���ʵ��Ľ�һ������*/
    private String        filter      = "";
    //
    //
    //
    public String getForEntity() {
        return forEntity;
    }
    public void setForEntity(String forEntity) {
        this.forEntity = forEntity;
    }
    public String getForProperty() {
        return forProperty;
    }
    public void setForProperty(String forProperty) {
        this.forProperty = forProperty;
    }
    public String getOrderBy() {
        return orderBy;
    }
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    public OrderModeEnum getOrderMode() {
        return orderMode;
    }
    public void setOrderMode(OrderModeEnum orderMode) {
        this.orderMode = orderMode;
    }
    public String getFilter() {
        return filter;
    }
    public void setFilter(String filter) {
        this.filter = filter;
    }
}