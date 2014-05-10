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
package net.hasor.core.context;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import net.hasor.core.Settings;
import net.hasor.core.XmlNode;
import org.more.UndefinedException;
import org.more.util.StringUtils;
/**
 * 
 * @version : 2014-5-10
 * @author ������ (zyc@byshell.org)
 */
public class StandardAppContext extends AbstractConfigResourceAppContext {
    private static final RegisterManager NullRegister = null;
    /**�����������ļ�*/
    public StandardAppContext() throws IOException, URISyntaxException {
        this(NullRegister);
    }
    /**�����������ļ�*/
    public StandardAppContext(File mainSettings) {
        this(mainSettings, NullRegister);
    }
    /**�����������ļ�*/
    public StandardAppContext(URI mainSettings) {
        this(mainSettings, NullRegister);
    }
    /**�����������ļ�*/
    public StandardAppContext(String mainSettings) throws IOException, URISyntaxException {
        this(mainSettings, NullRegister);
    }
    /**�����������ļ�*/
    public StandardAppContext(RegisterManager registerManager) throws IOException, URISyntaxException {
        super();
        this.setRegisterContext(registerManager);
    }
    /**�����������ļ�*/
    public StandardAppContext(File mainSettings, RegisterManager registerManager) {
        super(mainSettings);
        this.setRegisterContext(registerManager);
    }
    /**�����������ļ�*/
    public StandardAppContext(URI mainSettings, RegisterManager registerManager) {
        super(mainSettings);
        this.setRegisterContext(registerManager);
    }
    /**�����������ļ�*/
    public StandardAppContext(String mainSettings, RegisterManager registerManager) throws IOException, URISyntaxException {
        super(mainSettings);
        this.setRegisterContext(registerManager);
    }
    //
    //
    //
    private RegisterManager registerManager = null;
    private void setRegisterContext(RegisterManager registerManager) {
        this.registerManager = registerManager;
    }
    protected RegisterManager getRegisterManager() {
        if (this.registerManager == null) {
            String createrToUse = null;
            //1.ȡ�ü���������ManagerCreater����
            Settings setting = this.getSettings();
            String defaultManager = setting.getString("hasor.registerManager.default");
            XmlNode[] managerArray = setting.getXmlNodeArray("hasor.registerManager");
            for (XmlNode manager : managerArray) {
                List<XmlNode> createrList = manager.getChildren("creater");
                for (XmlNode creater : createrList) {
                    String createrName = creater.getAttribute("name");
                    if (StringUtils.equalsIgnoreCase(createrName, defaultManager)) {
                        createrToUse = creater.getAttribute("class");
                        break;
                    }
                }
                if (createrToUse != null)
                    break;
            }
            //2.�Ŵ�
            if (createrToUse == null)
                throw new UndefinedException(String.format("%s is not define.", defaultManager));
            //3.����Creater
            try {
                Class<?> createrType = Thread.currentThread().getContextClassLoader().loadClass(createrToUse);
                RegisterManagerCreater creater = (RegisterManagerCreater) createrType.newInstance();
                this.registerManager = creater.create(this.getEnvironment());
            } catch (RuntimeException e) {
                throw e;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        if (this.registerManager == null)
            throw new NullPointerException("registerManager is null.");
        return this.registerManager;
    }
}