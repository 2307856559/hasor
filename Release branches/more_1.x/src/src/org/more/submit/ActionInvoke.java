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
 * ���ڱ�ʾһ�����Ե��õ�submit3.0���󣬿��Ե��õĶ���һ���Ϊ����һ����action����������һ����action�������Ŀ�귽����
 * <br/>Date : 2009-12-1
 * @author ������
 */
public interface ActionInvoke {
    /**
     * ���������Դ���ҷ��ط���ֵ������ڵ����ڼ䷢���쳣���׳�Throwable�쳣��
     * @param stack ����ʱ���ݵ�ջ����
     * @return ���ص�����Դ֮������ķ���ֵ��
     * @throws Throwable ��������쳣��
     */
    public Object invoke(ActionStack stack) throws Throwable;
}