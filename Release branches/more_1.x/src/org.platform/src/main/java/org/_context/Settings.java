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
package org.platform.runtime._context;
import org.more.core.global.Global;
/**
 * 
 * @version : 2013-1-10
 * @author ������ (zyc@byshell.org)
 */
public abstract class Settings {
    public abstract Global getGlobal();
    //-----------------------------------------------------------------
    /**��ȡ�������ơ�*/
    public String getAppName() {
        return this.getString("appSettings.appName");
    };
    /**��ȡ�������ơ�*/
    public String getDisplayName() {
        return this.getString("appSettings.displayName");
    };
    /**����ȫ�����ò��������ҷ�����Boolean��ʽ����*/
    public boolean getBoolean(String name) {
        return this.getGlobal().getBoolean(name, false);
    };
    /**����ȫ�����ò��������ҷ�����Integer��ʽ����*/
    public int getInteger(String name) {
        return this.getGlobal().getInteger(name, 0);
    };
    /**����ȫ�����ò��������ҷ�����String��ʽ����*/
    public String getString(String name) {
        return this.getGlobal().getString(name, "");
    };
    /**����ȫ�����ò��������ҷ�����{@link File}��ʽ�������ڱ�ʾĿ¼����*/
    public String getDirectoryPath(String name) {
        return this.getGlobal().getDirectoryPath(name);
    };
}