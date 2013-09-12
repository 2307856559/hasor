/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
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
package net.hasor.core.context;
import java.net.URI;
import net.hasor.core.AppContext;
import net.hasor.core.Environment;
import net.hasor.core.environment.StandardEnvironment;
/**
 * {@link AppContext}�ӿ�Ĭ��ʵ�֡�
 * @version : 2013-4-9
 * @author ������ (zyc@hasor.net)
 */
public class DefaultAppContext extends AbstractAppContext {
    public DefaultAppContext() {
        this(null, null);
    }
    public DefaultAppContext(URI mainSettings) {
        this(mainSettings, null);
    }
    public DefaultAppContext(URI mainSettings, Object context) {
        if (context != null)
            this.setContext(context);
        if (mainSettings != null)
            this.mainSettings = mainSettings;
    }
    //
    private URI mainSettings = null;
    protected Environment createEnvironment() {
        if (mainSettings != null)
            return new StandardEnvironment(this.mainSettings);
        else
            return new StandardEnvironment();
    }
}