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
/**
 * ���ฺ��������ò���ִ����չ��Ļ��ࡣ
 * @version 2011-1-14
 * @author ������ (zyc@byshell.org)
 */
public interface ExpandPointManager {
    /**
     * ˳��ִ��������ע�����չ�����ֱ��ִ���������ƥ�����͵���չ��Ϊֹ��
     * @param type ��չ�����͡�
     * @param params ִ�еĲ�����
     * @return ����ִ�н����
     */
    public Object exePointOnSequence(Class<? extends ExpandPoint> type, Object... params);
    /**
     * ˳��ִ��������ע�����չ����󣬵�����һ������ֵʱ����ִ����չ�㣬����ֱ��ִ�����������չ�㷵�ء�
     * @param type ��չ�����͡�
     * @param params ִ�еĲ�����
     * @return ����ִ�н����
     */
    public Object exePointOnReturn(Class<? extends ExpandPoint> type, Object... params);
    /** ע��һ����ִ�е���չ�㣬�����ظ�ע��ͬһ����չ�㡣 */
    public void regeditExpandPoint(ExpandPoint point);
};