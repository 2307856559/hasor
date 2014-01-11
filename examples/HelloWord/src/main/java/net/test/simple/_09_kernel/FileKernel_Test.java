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
package net.test.simple._09_kernel;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;
import net.hasor.core.AppContext;
import net.hasor.core.Hasor;
import net.hasor.core.Module;
import net.hasor.core.context.FileAppContext;
import net.hasor.core.plugin.PluginHelper;
import net.hasor.plugins.aop.AopPlugin;
import net.hasor.plugins.bean.BeanPlugin;
import net.hasor.plugins.event.ListenerPlugin;
import net.test.simple._09_kernel.mods.Mod_1;
import net.test.simple._09_kernel.mods.Mod_2;
import net.test.simple._09_kernel.mods.Mod_3;
import org.junit.Test;
/**
 * Hasor �ں���������
 * @version : 2014-1-10
 * @author ������(zyc@hasor.net)
 */
public class FileKernel_Test {
    private static String config = "src/main/resources/net/test/simple/_09_kernel/hasor-config.xml";
    @Test
    public void test_kernel() throws IOException, URISyntaxException {
        System.out.println("--->>test_kernel<<--");
        //Hasor ������һ�������ļ�������
        //---���������߱����� @AnnoModule ע�⹦�ܣ�Ҳ������ء�hasor-config.xml������static-config.xml�������ļ���
        //---��ͬ�� SimpleAppContext ���� FileAppContext ͨ�� File ����ʽ������Ҫ���ص������ļ���
        File configFile = new File(config);
        AppContext kernel = new FileAppContext(configFile);
        kernel.start();
    }
    /*2.���ģ�飨����΢�ں˲�֧�� @AnnoModule ע�����ģ��������Ҫͨ�����룩*/
    @Test
    public void test_module() {
        System.out.println("--->>test_module<<--");
        File configFile = new File(config);
        FileAppContext kernel = new FileAppContext(configFile);
        kernel.addModule(new Mod_1());//ģ��1
        kernel.addModule(new Mod_2());//ģ��2
        kernel.addModule(new Mod_3());//ģ��3
        //
        kernel.start();
    }
    /*3.�ֶ���Ӳ��������΢�ں˲�֧�� @Plugin ע����������Ҫͨ�����룩*/
    @Test
    public void test_plugins() throws InstantiationException, IllegalAccessException {
        System.out.println("--->>test_plugins<<--");
        File configFile = new File(config);
        FileAppContext kernel = new FileAppContext(configFile);
        kernel.addModule(PluginHelper.toModule(BeanPlugin.class));//@Bean ���
        kernel.addModule(PluginHelper.toModule(ListenerPlugin.class));//@Listener ���
        kernel.addModule(PluginHelper.toModule(AopPlugin.class));//@Aop @GlobalAop ���
        //
        kernel.start();
        // 
        Set<Class<?>> modSet = kernel.findClass(Module.class);
        Hasor.logInfo("mod set %s.", modSet);
    }
}