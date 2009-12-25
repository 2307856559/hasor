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
 * submit2.0��������չ���������������ò��������CasingBuild���������ɵĸ�������Ĺ��ߡ�
 * ͨ��CasingBuild���֮����Ի��ActionManager����
 * Date : 2009-6-30
 * @author ������
 */
public class CasingDirector {
    private ActionManager manager = null; //���֮�����ɵ�ActionManager����
    private Config        config  = null;
    /**
     * ����һ��submit2.0��������չ��������������
     * @param config ����������buildManagerʱ��Ҫ�����ö���
     */
    public CasingDirector(Config config) {
        if (config == null)
            this.config = new MapSubmitConfig(new Hashtable<String, Object>(), null);
        this.config = config;
    }
    /**
     * ��������������ActionManager�������ɵ�ActionManager�Ķ��������Ҫͨ��getResult������ȡ��
     * @param build ����ActionManager����ʱ��Ҫ�õ�����������
     */
    public void buildManager(CasingBuild build) {
        build.init(this.config);//��ʼ��������
        ActionFactory saf = build.getActionFactory();
        FilterFactory sff = build.getFilterFactory();
        //
        ActionContext ac = ActionContext.newInstance(saf);
        ac.setFilterFactory(sff);
        ActionManager am = new ActionManager();
        am.setContext(ac);
        this.manager = am;
    }
    /**
     * ��ȡ���֮�����ɵ�ActionManager����
     * @return �������֮�����ɵ�ActionManager����
     */
    public ActionManager getResult() {
        return manager;
    }
}
