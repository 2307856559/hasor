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
package org.more.hypha.beans;
import org.more.hypha.ApplicationContext;
/**
 * ����ע��������ӿڣ��ýӿڸ����ĳ��bean���и���ע������Ĵ���
 * @version 2010-9-18
 * @author ������ (zyc@byshell.org)
 */
public interface ExportIoc {
    /**
     * ִ��ע��ķ������ڸýӿ����������ע��Ĺ��̡�
     * @param object Ҫע���Ŀ�����ԡ�
     * @param initParam ����bean������������
     * @param define bean�Ķ�����Ϣ��
     * @param context Ӧ�û���
     */
    public Object iocProcess(Object object, Object[] initParam, AbstractBeanDefine define, ApplicationContext context);
}