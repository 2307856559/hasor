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
import org.more.RepeateException;
/**
 *   ��չ�����Բ����ӿڣ��ýӿ���ǿ��Attribute�ӿڡ��ýӿڽ��������������ʱ������ͻ��
 * ���⡣�����õ���������ԭ�����Է���������ͻʱ����ʹ�á����Ե��滻ԭ�򡱽����滻�����������¼����滻ԭ��:
 *    ReplaceMode_Replace  (�������滻)��ԭ����Ĭ���滻ԭ��
 *    ReplaceMode_Original(����������ֵ����ԭʼ����)
 *    ReplaceMode_Throw   (��������������滻�������׳��쳣)��
 * Date : 2009-4-28
 * @author ������
 */
public interface IExtAttribute extends IAttribute {
    /** ���滻ģʽ(replacMode����)����ReplaceMode_Replaceʱ��������������������滻ԭ�����ԡ����滻ԭ����Ĭ���滻ԭ�� */
    public static final int ReplaceMode_Replace  = 0;
    /** 
     * ���滻ģʽ(replacMode����)����ReplaceMode_Originalʱ���������������������������Ա���ԭʼ���ԣ�
     * ������ģʽ�¿���ѡ�����Ƴ��������������ԡ� 
     */
    public static final int ReplaceMode_Original = 1;
    /** ���滻ģʽ(replacMode����)����ReplaceMode_Throwʱ��������������������׳�RepeateException�쳣�� */
    public static final int ReplaceMode_Throw    = 2;
    /**
     * �������ԣ����滻ģʽ(replaceMode����)����ReplaceMode_Replaceʱ��������������������滻ԭ�����ԡ�<br/>
     * ���滻ģʽ(replaceMode����)����ReplaceMode_Originalʱ���������������������������Ա���ԭʼ���ԣ�
     * ������ģʽ���û�����ѡ�����Ƴ��������������ԡ�<br/> ���滻ģʽ(replaceMode����)����ReplaceMode_Throwʱ��
     * ������������������׳�RepeateException�쳣��
     * @param name Ҫ�����������
     * @param value Ҫ���������ֵ
     * @throws RepeateException ���滻ģʽ(replaceMode����)����ReplaceMode_Throwʱ������������������ԡ�
     */
    public void setAttribute(String name, Object value) throws RepeateException;
    /**
     * ��ȡ���Ե��滻ģʽ��ֵ����ExtAttribute�ӿڵ�ReplaceMode�ֶζ��塣
     * @return �������Ե��滻ģʽ��ֵ����ExtAttribute�ӿڵ�ReplaceMode�ֶζ��塣
     */
    public int getReplaceMode();
}
