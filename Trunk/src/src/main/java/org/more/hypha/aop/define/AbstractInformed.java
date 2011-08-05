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
package org.more.hypha.aop.define;
import org.more.hypha.commons.define.AbstractDefine;
/**
 * �����informed�������Ǻ�����ʽ��informed���Ƕ�ֻ�ܰ�һ������㡣������������������ʽ����Ҳ���Ե�����ڡ�
 * @version 2010-9-27
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractInformed extends AbstractDefine<AbstractInformed> {
    private AbstractPointcutDefine refPointcut  = null; //���ӵ��е�����
    private PointcutType           pointcutType = null; //aop�е�����
    private String                 refBean      = null; //���ӵ�aopBean
    /**��ȡ������refBean��*/
    public String getRefBean() {
        return this.refBean;
    }
    /**���ù�����RefBean��*/
    public void setRefBean(String refBean) {
        this.refBean = refBean;
    }
    /**��ȡ���ӵ��е�*/
    public AbstractPointcutDefine getRefPointcut() {
        return this.refPointcut;
    };
    /**�������ӵ��е�*/
    public void setRefPointcut(AbstractPointcutDefine refPointcut) {
        this.refPointcut = refPointcut;
    };
    /**��ȡaop�е�����*/
    public PointcutType getPointcutType() {
        return this.pointcutType;
    };
    /**����aop�е�����*/
    public void setPointcutType(PointcutType pointcutType) {
        this.pointcutType = pointcutType;
    };
};