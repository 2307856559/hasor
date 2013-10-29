/*
 * Copyright 2008-2009 the original 赵永春(zyc@hasor.net).
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
package net.hasor.core;
/**
 * Hasor模块，该方法中定义了模块生命周期。
 * @version : 2013-3-20
 * @author 赵永春 (zyc@hasor.net)
 */
public interface Module {
    /**初始化过程。*/
    public void init(ApiBinder apiBinder);
    /**启动信号*/
    public void start(AppContext appContext);
    /**停止信号
     * <p><i>提示</i>：在 stop 过程中调用 {@link AppContext#unRegisterService(Class)} 方法，
     * 将只会影响到由本模块 start 过程中注册的服务。其它模块注册的同类型模块不受影响。*/
    public void stop(AppContext appContext);
}