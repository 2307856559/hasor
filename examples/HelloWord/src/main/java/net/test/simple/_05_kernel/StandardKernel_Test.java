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
import java.io.IOException;
import java.net.URISyntaxException;
import net.hasor.core.AppContext;
import net.hasor.core.context.StandardAppContext;
import org.junit.Test;
/**
 * Hasor �ں���������
 * @version : 2014-1-10
 * @author ������(zyc@hasor.net)
 */
public class StandardKernel_Test {
    @Test
    public void testStandardKernel() throws IOException, URISyntaxException {
        //Hasor �ı�׼����
        //--���������߱����� @AnnoModule ע�⹦�ܣ����ǻ����Ĭ��λ���µġ�hasor-config.xml������static-config.xml�������ļ���
        AppContext kernel = new StandardAppContext();
        kernel.start();
        //
    }
}