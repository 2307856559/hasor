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
package net.test.simple._05_kernel;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import net.hasor.core.AppContext;
import net.hasor.core.context.FileAppContext;
import org.junit.Test;
/**
 * Hasor �ں���������
 * @version : 2014-1-10
 * @author ������(zyc@hasor.net)
 */
public class FileKernel_Test {
    @Test
    public void testFileKernel() throws IOException, URISyntaxException {
        //Hasor ������һ�������ļ�������
        //---���������߱����� @AnnoModule ע�⹦�ܣ�Ҳ������ء�hasor-config.xml������static-config.xml�������ļ���
        //---��ͬ�� SimpleAppContext ���� FileAppContext ͨ�� File ����ʽ������Ҫ���ص������ļ���
        File configFile = new File("src/main/resources/net/test/simple/_05_kernel/hasor-config.xml");
        AppContext kernel = new FileAppContext(configFile);
        kernel.start();
    }
}