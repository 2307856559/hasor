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
package org.more.classcode;
import java.lang.reflect.Method;
/**
 * ��ʹ��{@link ClassEngine}���addDelegate���������ί�У���ӵ�ί�д�����������Ǹýӿڶ���
 * ��ί�еķ����������ע��ί��ʱ���ݵ�MethodDelegate�ӿڶ�����Ϊ�ص�����
 * @version 2010-9-3
 * @author ������ (zyc@byshell.org)
 */
public interface MethodDelegate {
    /**
     * �����ɵ��฽�ӽӿڷ���������ʱ�򼤷��ýӿڵķ�����ͨ��callMethod���Ի�ñ����õķ�������
     * @param callMethod �����õĺ�����
     * @param target ����ί�з����������
     * @param params �����÷���ʱ�����Ĳ��������û�в�����������һ�������顣
     * @return ���ط���ִ�н����ע��������ؽӿڷ����ķ���ֵ���з���{@link ClassEngine}�����Զ�ת�����ʽ��
     * �����������������java������������ذ��ո��ӵĽӿڷ���������ط���ֵ���򽫲�������ת���쳣��
     * @throws InvokeException �����ù����з������쳣��
     */
    public Object invoke(Method callMethod, Object target, Object[] params) throws InvokeException;
}