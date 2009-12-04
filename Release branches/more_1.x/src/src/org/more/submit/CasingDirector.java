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
import java.util.Hashtable;
import org.more.submit.support.MapSubmitConfig;
/**
 * submit3.0��������չ��������������ͨ��CasingBuild���֮����Ի��SubmitContext����
 * <br/>Date : 2009-12-1
 * @author ������
 */
public class CasingDirector {
    //========================================================================================Field
    private SubmitContext manager = null; //���֮�����ɵ�SubmitContext����
    private Config        config  = null;
    //==================================================================================Constructor
    /**
     * ����һ��submit2.0��������չ��������������
     * @param config ����������buildManagerʱ��Ҫ�����ö���
     */
    public CasingDirector(Config config) {
        if (config == null)
            this.config = new MapSubmitConfig(new Hashtable<String, Object>(), null);
        this.config = config;
    }
    //==========================================================================================Job
    /**
     * ��������������SubmitContext�������ɵ�SubmitContext�Ķ��������Ҫͨ��getResult������ȡ��
     * @param build ����SubmitContext����ʱ��Ҫ�õ�����������
     */
    public void buildManager(CasingBuild build) {
        build.init(this.config);//��ʼ��������
        this.manager = new SubmitContext(build.getActionFactory());
    }
    /**
     * ��ȡ���֮�����ɵ�SubmitContext����
     * @return �������֮�����ɵ�SubmitContext����
     */
    public SubmitContext getResult() {
        return manager;
    }
}