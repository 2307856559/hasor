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
import java.io.File;
import org.more.util.Config;
/**
 * ActionContextBuild���ṩ�����֧�Ż����������Ľӿڣ�
 * �κ�more�����֧�Ż���ͨ��SubmitBuild����ʱ����Ҫʵ�ָýӿڡ�
 * @version 2010-7-26
 * @author ������ (zyc@byshell.org)
 */
public interface ActionContextBuild {
    /**��ʼ�����������������configFile��String���Ͷ����ʾ�����ļ�λ�á�
     * @throws Throwable */
    public void init(Config config) throws Throwable;
    /**��������������ActionContext����ActionContext�ᾭ��SubmitBuild�ٴ�����ΪSubmitContext*/
    public ActionContext getActionContext();
    /**���û���Ŀ¼�����ʹ�����·������������·���ġ�*/
    public void setBaseDir(File baseDir);
};