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
package org.hasor.test.simple.events;
import java.io.IOException;
import java.net.URISyntaxException;
import net.hasor.core.Settings;
import net.hasor.core.SettingsListener;
import net.hasor.core.context.AnnoStandardAppContext;
import org.junit.Test;
/**
 * ��������������ʾ
 * @version : 2013-8-11
 * @author ������ (zyc@hasor.net)
 */
public class Event_Test {
    public static String Type_A = "EventType_A";
    public static String Type_B = "EventType_B";
    //
    @Test
    public void testAsyncEvent() throws IOException, URISyntaxException, InterruptedException {
        System.out.println("--->>testAsyncEvent<<--");
        AnnoStandardAppContext appContext = new AnnoStandardAppContext("org/hasor/test/simple/events/event-config.xml");
        appContext.start();
        //
        for (int i = 0; i < 10; i++)
            /*����ͬ���¼�*/
            appContext.getEventManager().doAsynEventIgnoreThrow(Type_B, i);
        System.out.println("after Event do sth...");
        /*����event-config.xml��������5���¼��̳߳أ�����첽�¼�����5��һ��ִ�С�*/
        System.in.read();
    }
    @Test
    public void testSyncEvent() throws IOException, URISyntaxException, InterruptedException {
        System.out.println("--->>testSyncEvent<<--");
        AnnoStandardAppContext appContext = new AnnoStandardAppContext("org/hasor/test/simple/events/event-config.xml");
        appContext.start();
        //
        for (int i = 0; i < 10; i++)
            /*����ͬ���¼�*/
            appContext.getEventManager().doSyncEventIgnoreThrow(Type_A, i);
        System.out.println("after Event do sth...");
    }
}
class TestSetting implements SettingsListener {
    public void onLoadConfig(Settings newConfig) {
        System.out.println(newConfig.getString("hasor.forceModule"));
    }
}