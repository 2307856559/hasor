/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
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
package net.hasor.web.servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
/**
 * Servlet�쳣�������
 * @version : 2013-4-12
 * @author ������ (zyc@hasor.net)
 */
public interface WebErrorHook {
    /**
     * �����ص��쳣���д�����������޷������쳣���ߴ����ڼ��������Խ��쳣�׳����׳����쳣�����½����쳣���������д���<br/>
     * �쳣������ȳ������ѯ�����Ѿ�ע��ErrorHook�����ErrorHook�׳��쳣����ѯ��ʹ���µ��쳣���¿�ʼ��Ĭ����ѯ10�Ρ�
     */
    public void doError(ServletRequest request, ServletResponse response, Throwable error) throws Throwable;
}