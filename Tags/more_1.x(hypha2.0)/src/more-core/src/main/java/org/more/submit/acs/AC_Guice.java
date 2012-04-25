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
package org.more.submit.acs;
import java.util.ArrayList;
import java.util.Collection;
import org.more.core.error.LoadException;
import org.more.core.log.Log;
import org.more.core.log.LogFactory;
import org.more.submit.ActionInvoke;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
/**
 * ������չ��{@link AC_Simple}�������ж��Ƿ���һ��guice Bean��
 * �������ʹ��guice����������ʹ��{@link AC_Simple}��ʽ������
 * @version : 2011-7-14
 * @author ������ (zyc@byshell.org)
 */
public class AC_Guice extends AC_Simple {
    private static Log log            = LogFactory.getLog(AC_Guice.class);
    private String     defaultPackage = null;
    public Injector    guiceInjector  = null;
    public Object      modules        = null;
    //
    public AC_Guice() {
        super();
    }
    public AC_Guice(String defaultPackage) {
        super(defaultPackage);
    }
    public AC_Guice(String defaultPackage, ClassLoader actionLoader) {
        super(defaultPackage, actionLoader);
    }
    public ActionInvoke getActionInvoke(String classKey, String methodKey) throws Throwable {
        classKey = this.defaultPackage + classKey;
        Class<?> type = this.getActionLoader().loadClass(classKey);
        //
        if (this.guiceInjector == null) {
            ArrayList<Module> mods = new ArrayList<Module>();
            //����modules��������Guice  Injector
            if (this.modules.getClass().isArray() == true)
                //1.������ʽ
                for (Object obj : (Object[]) this.modules)
                    this.add(mods, obj);
            else if (Collection.class.isAssignableFrom(this.modules.getClass()) == true)
                //2.������ʽ
                for (Object obj : (Collection<?>) this.modules)
                    this.add(mods, obj);
            else
                //3.����
                this.add(mods, this.modules);
            this.guiceInjector = Guice.createInjector(mods);
            log.info("AC for Guice Injector created.");
        }
        //����Guice��������
        Object obj = this.guiceInjector.getInstance(type);
        if (obj == null)
            throw new LoadException("װ��action�����쳣��");
        return new DefaultActionInvoke(obj, methodKey);
    };
    private void add(ArrayList<Module> modules, Object module) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        if (module != null)
            modules.add(this.passModule(module));
    };
    private Module passModule(Object obj) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        if (obj == null)
            return null;
        if (obj instanceof Module)
            return (Module) obj;
        else
            return (Module) Thread.currentThread().getContextClassLoader().loadClass(obj.toString()).newInstance();
    }
};