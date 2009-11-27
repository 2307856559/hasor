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
package org.more.util.attribute;
/**
 *    ���ɵ����Է��������ýӿڽ�һ����չ��ExtAttribute�ӿڣ���֧�������滻ģʽ�ĸ���������
 * ʹ����ʹ�����ԵĹ����п������ɵ��л������滻���ԡ�
 * Date : 2009-4-28
 * @author ������
 */
public interface IFreeAttribute extends IExtAttribute {
    /** �������滻�����л���ReplaceMode_Replaceģʽ */
    public void changeReplace();
    /** �������滻�����л���ReplaceMode_Originalģʽ */
    public void changeOriginal();
    /** �������滻�����л���ReplaceMode_Throwģʽ */
    public void changeThrow();
}
