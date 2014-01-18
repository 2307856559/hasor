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
package net.test.simple._06_guice;
import java.io.IOException;
import java.net.URISyntaxException;
import net.hasor.core.context.AnnoStandardAppContext;
import org.junit.Test;
/**
 * �� Guice ģ��ע�ᵽ Hasor ����ʾ
 * @version : 2013-8-11
 * @author ������ (zyc@hasor.net)
 */
public class Mod_Test {
    /*���ԣ�ʹ�� @GuiceModule ע��ע�� Guice ģ��*/
    @Test
    public void test_annoGuice() throws IOException, URISyntaxException, InterruptedException {
        System.out.println("--->>test_annoGuice<<--");
        AnnoStandardAppContext appContext = new AnnoStandardAppContext("net/test/simple/_06_guice/gucie-config.xml");
        //
        appContext.start();
        //��ȡģ���а󶨵� int ֵ
        System.out.println(appContext.getInstance(Integer.class));
    }
    /*���ԣ�ʹ�ô����ֶ�ע�� Guice ģ��*/
    @Test
    public void test_codeGuice() throws IOException, URISyntaxException, InterruptedException {
        System.out.println("--->>test_codeGuice<<--");
        AnnoStandardAppContext appContext = new AnnoStandardAppContext("net/test/simple/_06_guice/gucie-config.xml");
        //�� start ֮ǰע�� Guice ģ��
        appContext.addGuiceModule(new GuiceMod2());
        //
        appContext.start();
        //��ȡģ���а󶨵�ֵ
        System.out.println(appContext.getInstance(Integer.class));
        System.out.println(appContext.getInstance(String.class));
    }
}