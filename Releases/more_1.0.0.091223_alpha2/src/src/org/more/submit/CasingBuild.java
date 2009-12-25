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
 * ��չ�����������submit3.0���κ������չ����Ҫ�̳�������������������µ������ʹ�á�
 * ��������������ɵ�ActionContext����
 * <br/>Date : 2009-12-2
 * @author ������
 */
public abstract class CasingBuild {
    //========================================================================================Field
    protected Config config = null; //������Ϣ
    //==========================================================================================Job
    /**
     * ��ʼ�����������Ҵ��ݳ�ʼ��������
     * @param config ��ʼ����������
     */
    public void init(Config config) {
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
     * ����submit3.0�����չ��һ�������齨Action��������sbumitͨ��ActionContext���һ�ȡaction����
     * @return ���ش���submit3.0�����չ��һ�������齨Action��������
     */
    public abstract ActionContext getActionFactory();
}