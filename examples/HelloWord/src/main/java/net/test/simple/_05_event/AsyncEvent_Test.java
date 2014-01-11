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
package net.test.simple._05_event;
import java.io.IOException;
import java.net.URISyntaxException;
import net.hasor.core.context.AnnoStandardAppContext;
import org.junit.Test;
/**
 * �첽���¼���ʾ
 * @version : 2013-8-11
 * @author ������ (zyc@hasor.net)
 */
public class AsyncEvent_Test {
    private static String config = "net/test/simple/_05_event/event-config.xml";
    public static String  Type_A = "Event_A";
    public static String  Type_B = "Event_B";
    //
    /*���ԣ��첽�¼�*/
    @Test
    public void test_AsyncEvent() throws IOException, URISyntaxException, InterruptedException {
        System.out.println("--->>test_AsyncEvent<<--");
        AnnoStandardAppContext appContext = new AnnoStandardAppContext(config);
        appContext.start();
        //
        for (int i = 0; i < 10; i++)
            /*�����첽�¼�*/
            appContext.getEventManager().doAsync(Type_B, i);
        System.out.println("after Event do sth...");
        /*����event-config.xml�������� 3 ���¼��̳߳أ���˳��� 3 �����ϵ��¼������ŶӴ���*/
        System.in.read();
    }
}