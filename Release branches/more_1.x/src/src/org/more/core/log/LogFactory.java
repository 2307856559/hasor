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
package org.more.core.log;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import org.more.core.log.objects.DefaultLog;
/**
 * ��־ϵͳ,Ĭ�������ļ���mylog.config
 * @version 2009-5-13
 * @author ������ (zyc@byshell.org)
 */
public class LogFactory {
    private static final String     Log_Config_File = "mylog.config";
    private static ComFactory       comFactory      = null;
    private static ComObjectFactory comCache        = null;
    private static boolean          init            = false;
    public static final int         Mode_Debug      = 1;
    public static final int         Mode_Run        = 2;
    public static int               nowMode         = LogFactory.Mode_Run;
    // ===============================================================
    private static void init(CommandReader cr) throws IOException {
        if (comFactory == null)
            comFactory = ComFactory.getComFactory();
        if (comCache == null)
            comCache = new ComObjectFactory(comFactory);
        comFactory.clear();
        comCache.clear();
        //
        CommandAttribut ca = null;
        while (true) {
            ca = cr.readCommandAttribut();
            if (ca == null)
                break;
            //
            if (ca.getName().equals("dim")) {
                // Log
                String[] names = ca.getValue().split(",");
                for (String name : names)
                    comFactory.createComBean(name);
            } else
                comFactory.addAttribut(ca);
        }
        init = true;
        cr.close();
    }
    /**
     * ���Ĭ����־�������Ĭ������������������ֻ�������̨�������ʾ��Ĭ����־����ʹ�õ���
     * DefaultLog��DefaultLogFormater��DefaultLogWrite�����齨����
     * @return ����Ĭ����־�������Ĭ������������������ֻ�������̨�����
     */
    public static ILog getLog() {
        ILog log = new DefaultLog();
        //        log.setFormater(new DefaultLogFormater());
        //        log.addWrite(new DefaultLogWrite(), ILog.LogLevel_ALL);
        return log;
    }
    /**
     * ������úõ���־��������ڻ����־����ʱ��������־���󴫵ݶ�̬������
     * @param name ���õ���־��������
     * @param objects �ڻ����־����ʱ��Ҫ����־���󴫵ݵĲ����б�
     * @return �������úõ���־�������
     */
    public static ILog getLog(String name, Object... objects) {
        try {
            if (init == false)
                lookUp();
        } catch (Exception e) {}
        //
        try {
            Object obj = comCache.getComObject(name, objects);// ��ͼ�ӻ���װ��
            if (obj instanceof ILog)
                return (ILog) obj;
            else
                return new NullLog(LogFactory.nowMode);
        } catch (Exception e) {
            return new NullLog(LogFactory.nowMode);
        }
    }
    /**
     * ʹ��ϵͳĬ����־�ļ�����ʼ����־ϵͳ��ϵͳĬ����־�ļ�mylog.config������ڳ�������·���¡�
     * @throws IOException �������I/O�쳣
     */
    public static void lookUp() throws IOException {
        InputStream is = LogFactory.class.getClassLoader().getResourceAsStream(LogFactory.Log_Config_File);
        init(new CommandReader(new InputStreamReader(is, "utf-8")));// ��ʼ��
    }
    /**
     * ʹ��ָ����־�ļ�����ʼ����־ϵͳ��
     * @param file �����ļ�
     * @throws IOException �������I/O�쳣
     */
    public static void lookUp(File file) throws IOException {
        if (!file.canRead())
            return;
        init(new CommandReader(new FileReader(file)));
    }
    /**
     * ʹ��ָ����־�����ַ�������ʼ����־ϵͳ��
     * @param config �����ַ���
     * @throws IOException �������I/O�쳣
     */
    public static void lookUp(String config) throws IOException {
        init(new CommandReader(new StringReader(config)));
    }
    public static ILog getLog(final Class<?> type) {
        final String TYPE = type.getSimpleName();
        return new ILog() {
            public void warning(String msg, Object... infoObjects) {
                System.err.println("[Warning]" + TYPE + ": " + msg);
            }
            public void info(String msg, Object... infoObjects) {
                System.out.println("[Info]" + TYPE + ": " + msg);
            }
            public void error(String msg, Object... infoObjects) {
                System.err.println("[Error]" + TYPE + ": " + msg);
            }
            public void debug(String msg, Object... infoObjects) {
                //       System.out.println("[Debug]" + TYPE + ": " + msg);
            }
        };
    }
}