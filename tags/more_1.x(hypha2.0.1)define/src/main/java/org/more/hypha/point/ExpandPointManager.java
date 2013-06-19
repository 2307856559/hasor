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
package org.more.hypha.point;
/**
 * ���ฺ��������ò���ִ����չ��Ļ��ࡣ
 * @version : 2011-6-29
 * @author ������ (zyc@byshell.org)
 */
public interface ExpandPointManager {
    /**
     * ˳����չ�����
     * @param pointType ִ�е���չ������
     * @param callBack ִ�е����ջص�����
     * @param vars ��������
     * @return ����ִ����չ��Ľ����
     */
    public <O> O exePoint(Class<? extends PointFilter> pointType, PointCallBack callBack, Object... vars) throws Throwable;
    /** ע��һ����չ��,��һ��������ע�����չ����������ͨ��enablePoint��disablePoint�������ý���ע�����չ�㡣ע������Ʋ����������� */
    public void regeditExpandPoint(String pointName, PointFilter point);
    /**������չ�㡣*/
    public void enablePoint(String pointName);
    /**������չ�㡣*/
    public void disablePoint(String pointName);
};