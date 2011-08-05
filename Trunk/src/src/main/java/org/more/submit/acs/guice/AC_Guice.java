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
package org.more.submit.acs.guice;
import java.util.ArrayList;
import java.util.Collection;
import org.more.core.log.Log;
import org.more.core.log.LogFactory;
import org.more.submit.acs.simple.AC_Simple;
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
    private static Log log           = LogFactory.getLog(AC_Guice.class);
    public Injector    guiceInjector = null;
    public Object      modules       = null;
    //
    protected Object getBean(Class<?> type) throws Throwable {
        GBean annoBean = type.getAnnotation(GBean.class);
        if (annoBean == null)
            return super.getBean(type);
        if (guiceInjector == null) {
            ArrayList<Module> mods = new ArrayList<Module>();
            //����modules��������Guice  Injector
            if (this.modules != null)
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
        return guiceInjector.getInstance(type);
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
    };
};