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
package org.more.submit;
/**
 * ��չ�����������submit���κ������չ����Ҫ�̳�������������������µ������ʹ�á�
 * ��������������ͨ��ʵ��getActionFactory����������{@link ActionContext ActionContext�ӿ�}����
 * <br/>Date : 2009-12-2
 * @author ������
 */
public abstract class CasingBuild {
    //========================================================================================Field
    protected Config      config             = null; //������Ϣ
    private ActionContext cacheActionContext = null;
    private boolean       cacheContext       = true; //�Ƿ񻺴�
    //==========================================================================================Job
    /**
     * ��ʼ�����������Ҵ��ݳ�ʼ��������
     * @param config ��ʼ����������
     */
    public void init(Config config) throws Exception {
        this.config = config;
    }
    /**
     * ��ȡ���ö��󡣸÷���ֻ���ڼ̳�CasingBuild��֮����ܱ����á�
     * @return �������ö��󡣸÷���ֻ���ڼ̳�CasingBuild��֮����ܱ����á�
     */
    protected Config getConfig() {
        return config;
    }
    /**
     * ����submit�����չ��һ�������齨Action��������sbumitͨ��ActionContext���һ�ȡaction����
     * @return ���ش���submit�����չ��һ�������齨Action��������
     */
    public ActionContext getActionFactory() {
        if (cacheContext == false)
            return this.createActionContext();
        if (this.cacheActionContext == null)
            this.cacheActionContext = this.createActionContext();
        return this.cacheActionContext;
    }
    /**��ȡCasingBuild�����Ƿ񻺴�ActionFactory����*/
    public boolean isCacheContext() {
        return cacheContext;
    }
    /**����һ��ֵ����ֵ�����˵�����getActionFactory����������ActionContext�����Ƿ񻺴�����Ϊ��һ�ε���getActionFactory������ʹ�á�true��ʾ����,false��ʾ�����档*/
    public void setCacheContext(boolean cacheContext) {
        this.cacheContext = cacheContext;
    }
    protected abstract ActionContext createActionContext();
}