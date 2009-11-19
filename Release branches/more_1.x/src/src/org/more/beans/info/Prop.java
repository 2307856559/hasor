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
import org.more.util.attribute.AttBase;
/**
 * ����more.beans��������ĺ����࣬Prop���ڱ�ʾһ��ӵ�о������͵�bean������Դ��
 * <br/>Date : 2009-11-18
 * @author ������
 */
public abstract class Prop extends AttBase {
    //========================================================================================Field
    /**  */
    private static final long serialVersionUID = -3350281122432032642L;
    private String            id               = null;                 //Ψһ��Bean ID
    private String            propType         = null;                 //������Ӧ�ÿ���ͨ��BeanFactory.getBeanClassLoader().loadClass()��ȡ
    //==========================================================================================Job
    /**��ȡΨһ��Bean ID��*/
    public String getId() {
        return id;
    }
    /**����Ψһ��Bean ID��*/
    public void setId(String id) {
        this.id = id;
    }
    /**��ȡ���ͣ�������Ӧ�ÿ���ͨ��BeanFactory.getBeanClassLoader().loadClass()��ȡ��*/
    public String getPropType() {
        return propType;
    }
    /**�������ͣ�������Ӧ�ÿ���ͨ��BeanFactory.getBeanClassLoader().loadClass()��ȡ��*/
    public void setPropType(String propType) {
        this.propType = propType;
    }
}