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
package org.more.beans.info;
/**
 * ��ʾ�����һ�������е�Bean����bean����ͨ�����ᱻ����bean���õ�������bean��һ���ص�������ǵĵ�̬ģʽͨ����ʧЧ�ġ�
 * <br/>Date : 2009-11-18
 * @author ������
 */
public class PropBean extends BeanProp {
    //========================================================================================Field
    /**  */
    private static final long serialVersionUID = -194590250590692070L;
    private BeanDefinition    beanDefinition   = null;                //�����е�bean���塣
    //==================================================================================Constructor
    public PropBean(BeanDefinition beanDefinition) {}
    //==========================================================================================Job
    /**��ȡ�����е�bean���塣*/
    public BeanDefinition getBeanDefinition() {
        return beanDefinition;
    }
    /**���������е�bean���塣*/
    public void setBeanDefinition(BeanDefinition beanDefinition) {
        this.beanDefinition = beanDefinition;
    }
}