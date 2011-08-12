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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import org.more.core.error.InitializationException;
import org.more.core.log.impl.LogConfigParser;
import org.more.core.log.impl.Log_Propxy;
import org.more.util.ResourcesUtil;
/**
 * ��־ϵͳ,Ĭ�������ļ���default.config
 * @version : 2011-7-29
 * @author ������ (zyc@byshell.org)
 */
public class LogFactory {
    /**Ĭ�������ļ�λ�á�*/
    public static String      DefaultConfig = "META-INF/resource/log/default.config";
    private static LogFactory factory       = null;
    //
    /**��ȡ{@link LogFactory}��־����������û����ʹ��Ĭ�������ļ���������*/
    public static LogFactory getFactory() {
        if (factory == null)
            try {
                factory = createFactory();
            } catch (IOException e) {
                throw new InitializationException("create LogFactory error!", e);
            }
        return factory;
    }
    /**ʹ�����������ļ�����һ��{@link LogFactory}��־�������*/
    public static LogFactory createFactory() throws IOException {
        List<URL> urls = ResourcesUtil.getResources(DefaultConfig);
        LogFactory factory = new LogFactory();
        for (URL url : urls) {
            InputStream is = url.openStream();
            factory.loadConfig(is);
            is.close();
        }
        return factory;
    };
    /**ʹ�����������ļ�����һ��{@link LogFactory}��־�������fileStringָ������ԴĿ¼�е������ļ�λ�á�*/
    public static LogFactory createFactory(String fileString) throws IOException {
        LogFactory factory = createFactory();
        List<URL> urls = ResourcesUtil.getResources(fileString);
        if (urls != null)
            for (URL url : urls) {
                InputStream is = url.openStream();
                factory.loadConfig(is);
                is.close();
            }
        return factory;
    };
    /**ʹ�����������ļ�����һ��{@link LogFactory}��־�������fileָ�������ļ���*/
    public static LogFactory createFactory(File file) throws IOException {
        LogFactory factory = createFactory();
        factory.loadConfig(new FileInputStream(file));
        return factory;
    };
    /**ʹ�����������ļ�����һ��{@link LogFactory}��־�������isָ�������ļ���������*/
    public static LogFactory createFactory(InputStream is) throws IOException {
        LogFactory factory = createFactory();
        if (is != null)
            factory.loadConfig(is);
        return factory;
    };
    /**��ȡ{@link Log}�ӿڶ���*/
    public static Log getLog(Class<?> type) {
        return getFactory().getLogAsLevel(Level.Default, type);
    };
    /*-----------------------------------------------------------------------------*/
    private Level[] levels = null;
    //
    //
    //
    /**��ȡ���м�������Log*/
    public Log getLogAsALL(Class<?> owner) {
        Log_Propxy allLog = new Log_Propxy();
        for (Level l : this.getLevels())
            allLog.add(this.getLogAsLevel(l, owner));
        return allLog;
    };
    public Level[] getLevels() {
        return this.levels.clone();
    };
    /**��ȡĳһ�������ϵ����Log*/
    public Log getLogAsLevel(Level level, Class<?> owner) {
        return new Log() {
            public void warning(String msg, Object... infoObjects) {
                System.out.println(msg);
            }
            public void out(String type, String msg, Object... infoObjects) {
                System.out.println("[" + type + "]" + msg);
            }
            public void info(String msg, Object... infoObjects) {
                System.out.println(msg);
            }
            public void error(String msg, Object... infoObjects) {
                System.out.println(msg);
            }
            public void debug(String msg, Object... infoObjects) {
                //  System.out.println(msg);
            }
        };
    };
    public void loadConfig(InputStream is) {
        LogConfigParser parser = new LogConfigParser();
        //
        //
        //
        //this.levels = parser.getLevels();
    };
};