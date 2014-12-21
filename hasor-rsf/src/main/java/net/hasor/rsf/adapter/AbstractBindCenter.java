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
package net.hasor.rsf.adapter;
import java.net.URL;
import net.hasor.rsf.BindCenter;
import net.hasor.rsf.RsfBindInfo;
/**
 * 注册中心
 * @version : 2014年11月30日
 * @author 赵永春(zyc@hasor.net)
 */
public abstract class AbstractBindCenter implements BindCenter {
    /**查找一个有效的连接。*/
    public abstract URL findServiceAddress(RsfBindInfo<?> bindInfo);
    /**被明确为无效的连接。*/
    public abstract void invalidAddress(URL address);
}