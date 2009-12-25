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
import org.more.InvokeException;
/**
 * ��չ�����������submit2.0���κ������չ����Ҫ�̳�������������������µ������ʹ�á�
 * ��������������ɵ�ActionFactory���������ڹ���action����������FilterFactory��
 * ���ڹ���action�Ĺ�����������
 * Date : 2009-6-30
 * @author ������
 */
public abstract class CasingBuild {
    protected Config config = null; //������Ϣ
    /**
     * ��ʼ�����������Ҵ��ݳ�ʼ��������
     * @param config ��ʼ����������
     * @throws InvokeException ����ڳ�ʼ�������г����쳣���ڳ������쳣��
     */
    public void init(Config config) throws InvokeException {
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
     * ����submit2.0�����չ��һ�������齨Action��������sbumitͨ��ActionFactory���һ�ȡaction����
     * @return ���ش���submit2.0�����չ��һ�������齨Action��������
     */
    public abstract ActionFactory getActionFactory();
    /**
     * ����submit2.0�����չ��һ�������齨Action��������������sbumitͨ��FilterFactory���һ�ȡaction�����װ���������
     * @return ���ش���submit2.0�����չ��һ�������齨Action��������������
     */
    public abstract FilterFactory getFilterFactory();
}