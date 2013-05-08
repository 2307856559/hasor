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
package org.platform.security;
import java.io.IOException;
import javax.servlet.ServletException;
import org.platform.context.ViewContext;
/***
 * ִ��������ת����ת����
 * @version : 2013-5-8
 * @author ������ (zyc@byshell.org)
 */
public interface SecurityForward {
    /**url����������δ�������*/
    public static enum ForwardType {
        /**�����ת��*/
        Forward,
        /**�ͻ����ض���*/
        Redirect,
        /**�׳��쳣*/
        Exception,
        /**����response�ͻ���״̬*/
        State
    }
    /**��ȡ��ת����*/
    public ForwardType getForwardType();
    /**ִ����ת*/
    public void forward(ViewContext viewContext) throws IOException, ServletException;
}