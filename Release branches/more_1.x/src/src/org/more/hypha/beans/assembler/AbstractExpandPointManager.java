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
package org.more.hypha.beans.assembler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.more.hypha.ExpandPoint;
import org.more.hypha.ExpandPointManager;
/**
 * {@link ExpandPointManager}�ӿڵ�һ������ʵ�֣����ฺ����ò���ִ����չ�㡣
 * @version 2011-1-14
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractExpandPointManager implements ExpandPointManager {
    private Map<Class<?>, List<ExpandPoint>> expandMap  = new HashMap<Class<?>, List<ExpandPoint>>();
    private List<ExpandPoint>                expandList = new ArrayList<ExpandPoint>();
    /** */
    public Object exePoint(Class<? extends ExpandPoint> type, Object[] params) {
        // TODO Auto-generated method stub
        return null;
    };
};