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
package org.more.hypha;
import org.more.util.attribute.IAttribute;
/**
 * EL���ʽִ������ͨ���ýӿڿ���ִ��el��ʾ�����ҵõ�el���ʽ��ִ�н����
 * Date : 2011-4-13
 * @author ������ (zyc@byshell.org)
 */
public interface EvalExpression extends IAttribute {
    /**��ȡ���el���ʽ���ַ�����ʽ��*/
    public String getExpressionString();
    /**ִ��el���ʽ��������ʾ����ִ��el���ʽʱ��ʹ�õ�this��˭�� */
    public Object eval(Object thisObject) throws Throwable;
};