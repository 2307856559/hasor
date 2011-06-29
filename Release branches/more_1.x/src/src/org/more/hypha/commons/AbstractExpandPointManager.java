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
package org.more.hypha.commons;
import java.util.ArrayList;
import java.util.HashMap;
import org.more.hypha.ApplicationContext;
import org.more.hypha.ExpandPointManager;
import org.more.hypha.PointCallBack;
import org.more.hypha.PointChain;
import org.more.hypha.PointFilter;
import org.more.hypha.context.AbstractApplicationContext;
import org.more.log.ILog;
import org.more.log.LogFactory;
/**
 * ���ฺ��������ò���ִ����չ��Ļ��ࡣ
 * @version : 2011-6-29
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractExpandPointManager implements ExpandPointManager {
    private static ILog                  log                = LogFactory.getLog(AbstractExpandPointManager.class);
    private AbstractApplicationContext   applicationContext = null;
    private HashMap<String, PointFilter> enableFilterMap    = new HashMap<String, PointFilter>();
    private HashMap<String, PointFilter> disableFilterMap   = new HashMap<String, PointFilter>();
    //
    private ArrayList<String>            filterNameList     = new ArrayList<String>();
    private ArrayList<PointFilter>       filterList         = new ArrayList<PointFilter>();
    /*------------------------------------------------------------------------------*/
    public void init(AbstractApplicationContext applicationContext) {
        if (applicationContext != null)
            log.info("init ExpandPointManager, AbstractDefineResource = {%0}", applicationContext);
        else
            log.warning("init ExpandPointManager, AbstractDefineResource is null.");
        this.applicationContext = applicationContext;
    };
    /**����{@link AbstractApplicationContext}����*/
    protected AbstractApplicationContext getDefineResource() {
        return this.applicationContext;
    };
    /*------------------------------------------------------------------------------*/
    public synchronized void regeditExpandPoint(String pointName, PointFilter point) {
        if (point == null) {
            log.warning("regedit ExpandPoint an error ExpandPoint is null.");
            return;
        }
        if (this.filterNameList.contains(pointName) == true) {
            log.warning("regedit ExpandPoint name is repeat.");
            return;
        }
        log.info("regedit ExpandPoint {%0} OK!", pointName);
        this.filterNameList.add(pointName);
        this.filterList.add(point);
        this.enableFilterMap.put(pointName, point);
        this.chainCache.clear();//��ջ��档
    }
    public synchronized void enablePoint(String pointName) {
        if (this.filterNameList.contains(pointName) == false)
            return;
        if (this.enableFilterMap.containsKey(pointName) == true)
            return;
        this.enableFilterMap.put(pointName, this.disableFilterMap.remove(pointName));
        this.chainCache.clear();//��ջ��档
    };
    public synchronized void disablePoint(String pointName) {
        if (this.filterNameList.contains(pointName) == false)
            return;
        if (this.disableFilterMap.containsKey(pointName) == true)
            return;
        this.disableFilterMap.put(pointName, this.enableFilterMap.remove(pointName));
        this.chainCache.clear();//��ջ��档
    };
    //Ϊ������;�������Ŀ����Ϊ�˱��ⷴ��������������
    private HashMap<Class<? extends PointFilter>, PropxyChain> chainCache = new HashMap<Class<? extends PointFilter>, PropxyChain>();
    public <O> O exePoint(Class<? extends PointFilter> pointType, PointCallBack callBack, Object... vars) throws Throwable {
        if (pointType == null || callBack == null) {
            log.warning("exePoint error param 'pointType' or 'callBack' is null.");
            return null;
        }
        //�������յ���
        PropxyChain propxy = null;
        if (this.chainCache.containsKey(pointType) == false) {
            PointChain rootChain = new FinalPointChain(callBack);
            propxy = new PropxyChain((FinalPointChain) rootChain);
            //������������
            for (PointFilter filter : filterList)
                if (pointType.isInstance(filter) == true)
                    rootChain = new NodePointChain(filter, rootChain);
            propxy.setRoot(rootChain);
            this.chainCache.put(pointType, propxy);
        } else
            propxy = this.chainCache.get(pointType);
        //ִ�е���
        propxy.setCallBack(callBack);
        return (O) propxy.doChain(this.applicationContext, vars);
    }
};
/**����Chain,�����ڵ�Ŀ����Ϊ��ʹ�����FinalPointChain�����Զ�̬�ĸı����ջص���*/
class PropxyChain implements PointChain {
    private FinalPointChain finalChain = null;
    private PointChain      rootChain  = null;
    //
    public PropxyChain(FinalPointChain finalChain) {
        this.finalChain = finalChain;
    }
    public void setRoot(PointChain rootChain) {
        this.rootChain = rootChain;
    }
    public void setCallBack(PointCallBack callBack) {
        this.finalChain.setCallBack(callBack);
    }
    public Object doChain(ApplicationContext applicationContext, Object[] params) throws Throwable {
        return this.rootChain.doChain(applicationContext, params);
    }
}
/**���������е�һ��*/
class NodePointChain implements PointChain {
    private PointFilter currentFilter = null;
    private PointChain  nextChain     = null;
    //
    public NodePointChain(PointFilter currentFilter, PointChain nextChain) {
        this.currentFilter = currentFilter;
        this.nextChain = nextChain;
    }
    public Object doChain(ApplicationContext applicationContext, Object[] params) throws Throwable {
        return this.currentFilter.doFilter(applicationContext, params, this.nextChain);
    };
};
/**���յĹ�������*/
class FinalPointChain implements PointChain {
    private PointCallBack callBack = null;
    //
    public FinalPointChain(PointCallBack callBack) {
        this.callBack = callBack;
    }
    public void setCallBack(PointCallBack callBack) {
        this.callBack = callBack;
    }
    public Object doChain(ApplicationContext applicationContext, Object[] params) throws Throwable {
        return this.callBack.call(applicationContext, params);
    };
};