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
 * submit����buildģʽ����{@link SubmitContext SubmitContext�ӿ�}�����׶Σ�
 * CasingDirector����Ҫ�����{@link CasingBuild CasingBuild}�л�ȡ����ֵȻ�󴴽�SubmitContext�ӿڶ���
 * ͨ����չCasingDirector����Ըı䴴��SubmitContext�ķ�ʽ�Ӷ���չmore��֧����ǡ�
 * @version 2009-12-1
 * @author ������ (zyc@byshell.org)
 */
public class CasingDirector {
    //========================================================================================Field
    private SubmitContext context = null; //���֮�����ɵ�SubmitContext����
    private Config        config  = null;
    //==================================================================================Constructor
    /**
     * ����һ��submit��������չ��������������
     * @param config ����������buildʱ��Ҫ�����ö���
     */
    public CasingDirector(Config config) {
        if (config == null)
            this.config = new MapSubmitConfig(new Hashtable<String, Object>(), null);
        else
            this.config = config;
    }
    //==========================================================================================Job
    /**
     * ��������������SubmitContext�������ɵ�SubmitContext�Ķ��������Ҫͨ��getResult������ȡ��
     * @param build ����SubmitContext����ʱ��Ҫ�õ�����������
     */
    public void build(CasingBuild build) throws Exception {
        build.init(this.config);//��ʼ��������
        this.context = this.buildContext(build, config);
    }
    /**�������ͨ����չ�÷�������������SubmitContext�Ĵ������̡��˽׶�build.init�����Ѿ������á�*/
    protected SubmitContext buildContext(CasingBuild build, Config config) {
        ActionContext context = build.getActionContext();
        if (context instanceof AbstractActionContext == true)
            //���ActionContext������AbstractActionContext��������ִ��AbstractActionContext���͵ĳ�ʼ������������AbstractActionContext���͵ĳ�ʼ�����̡�
            ((AbstractActionContext) context).init();
        return new ImplSubmitContext(context);
    }
    /**
     * ��ȡ���֮�����ɵ�SubmitContext����
     * @return �������֮�����ɵ�SubmitContext����
     */
    public SubmitContext getResult() {
        return context;
    }
}