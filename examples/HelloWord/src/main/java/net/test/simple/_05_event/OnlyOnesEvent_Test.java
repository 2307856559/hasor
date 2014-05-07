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
import net.hasor.core.EventListener;
import net.hasor.core.EventManager;
import net.hasor.quick.anno.AnnoStandardAppContext;
import org.junit.Test;
/**
 * ���ᱻִ��һ�ε��¼�
 * @version : 2013-8-11
 * @author ������ (zyc@hasor.net)
 */
public class OnlyOnesEvent_Test {
    private static String config = "net/test/simple/_05_event/event-config.xml";
    //
    /*���ԣ��첽�¼�*/
    @Test
    public void test_OnlyOnesEvent() throws IOException, URISyntaxException, InterruptedException {
        System.out.println("--->>test_OnlyOnesEvent<<--");
        AnnoStandardAppContext appContext = new AnnoStandardAppContext(config);
        appContext.start();
        //��ȡ�¼�������
        EventManager em = appContext.getEventManager();
        //push һ���¼����������ü������� push ֮��ֻ����Чһ�Ρ�
        em.pushListener("MyEvent", new MyEventListener());
        //����4���¼�������ֻ��һ���¼��ᱻ����
        em.doSync("MyEvent");
        em.doSync("MyEvent");
        em.doSync("MyEvent");
        em.doSync("MyEvent");
        //
        //�ٴ�ע�� MyEvent �¼�
        em.pushListener("MyEvent", new MyEventListener());
        //�����¼�
        em.doSync("MyEvent");
    }
    private static class MyEventListener implements EventListener {
        public void onEvent(String event, Object[] params) throws Throwable {
            System.out.println("onEvent");
        }
    }
}