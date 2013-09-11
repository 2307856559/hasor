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
package org.hasor.test.core.context;
import java.io.IOException;
import java.net.URISyntaxException;
import net.hasor.core.ApiBinder;
import net.hasor.core.AppContext;
import net.hasor.core.Module;
import net.hasor.core.context.DefaultAppContext;
import org.junit.Test;
/**
 * ��������������ʾ
 * @version : 2013-8-11
 * @author ������ (zyc@hasor.net)
 */
public class DefaultAppContext_Test {
    @Test
    public void testDefaultAppContext() throws IOException, URISyntaxException, InterruptedException {
        System.out.println("--->>testDefaultAppContext<<--");
        DefaultAppContext appContext = new DefaultAppContext();
        appContext.addModule(new Module() {
            @Override
            public void stop(AppContext appContext) {
                // TODO Auto-generated method stub
            }
            @Override
            public void start(AppContext appContext) {
                // TODO Auto-generated method stub
            }
            @Override
            public void init(ApiBinder apiBinder) {
                // TODO Auto-generated method stub
            }
        });
        //
        appContext.start();
    }
}