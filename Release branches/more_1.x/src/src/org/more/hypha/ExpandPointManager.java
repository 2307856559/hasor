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
 * ��չ���������������չ�㶼��Ҫע����{@link ExpandPointManager}�ӿ��С�
 * @version 2011-1-14
 * @author ������ (zyc@byshell.org)
 */
public interface ExpandPointManager {
    /**
     * ִ����չ�㷽�������Ҳ��Ұ�����չ��ִ���߼�����ִ�С���ִ�����֮�󷵻�ִ�н����
     * @param type Ҫִ�е���չ�����͡�
     * @param params ��ִ���ڼ䴫�ݵĲ�����
     * @return ����ִ����չ��֮���ִ�н����
     */
    public Object exePoint(Class<? extends ExpandPoint> type, Object[] params);
};