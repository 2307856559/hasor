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
package org.more.submit.casing.spring;
import org.more.InvokeException;
import org.more.submit.ActionFactory;
import org.more.submit.CasingBuild;
import org.more.submit.Config;
import org.more.submit.FilterFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
/**
 * ���ֶ�������Spring֮����Ҫ��spring��submit������Ҫʹ��SpringCasingBuilder���м��ɡ�
 * ע��SpringCasingBuilder�ڳ�ʼ��ʱ����Ҫspring��AbstractApplicationContext���Ͷ���
 * һ������£�spring��context���󶼼̳���AbstractApplicationContext����
 * Date : 2009-6-30
 * @author ������
 */
public class SpringCasingBuilder extends CasingBuild {
    private SpringActionFactory        saf           = null;
    private SpringFilterFactory        sff           = null;
    private AbstractApplicationContext springContext = null;
    /**
     * ͨ��һ��SpringContext���󴴽�SpringCasingBuilder��
     * @param springContext SpringContext����
     */
    public SpringCasingBuilder(AbstractApplicationContext springContext) {
        if (springContext == null)
            throw new NullPointerException("springContext��������Ϊ�ա�");
        this.springContext = springContext;
    }
    /**
     * ͨ��һ��SpringContext���󴴽�SpringCasingBuilder��
     * @param configLocation �����ļ�λ�ø������ļ�λ���Ǹ���ClassPath
     */
    public SpringCasingBuilder(String configLocation) {
        this.springContext = new FileSystemXmlApplicationContext(configLocation);
        if (this.springContext == null)
            throw new InvokeException("�޷�����FileSystemXmlApplicationContext�������������ļ�λ��");
    }
    @Override
    public void init(Config config) throws InvokeException {
        super.init(config);
        saf = new SpringActionFactory((AbstractApplicationContext) springContext);//Spring��action����
        sff = new SpringFilterFactory((AbstractApplicationContext) springContext);//Spring��filter����
    }
    @Override
    public ActionFactory getActionFactory() {
        return this.saf;
    }
    @Override
    public FilterFactory getFilterFactory() {
        return this.sff;
    }
}