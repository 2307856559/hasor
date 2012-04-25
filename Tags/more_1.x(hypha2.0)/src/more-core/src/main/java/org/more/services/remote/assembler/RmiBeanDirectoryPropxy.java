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
package org.more.services.remote.assembler;
import java.util.Map;
import org.more.services.remote.RemoteService;
import org.more.services.remote.RmiBeanCreater;
import org.more.services.remote.RmiBeanDirectory;
/**
 * {@link RmiBeanDirectory}�ӿڵĴ����ࡣ
 * @version : 2011-8-15
 * @author ������ (zyc@byshell.org)
 */
public class RmiBeanDirectoryPropxy implements RmiBeanDirectory {
    private String           refBean   = null;
    private RemoteService    service   = null;
    private RmiBeanDirectory directory = null;
    //
    public RmiBeanDirectoryPropxy(String refBean, RemoteService service) {
        this.refBean = refBean;
        this.service = service;
    }
    public Map<String, RmiBeanCreater> getCreaterMap() throws Throwable {
        if (this.directory == null)
            this.directory = this.service.getApplicationContext().getBean(this.refBean);
        return this.directory.getCreaterMap();
    }
}