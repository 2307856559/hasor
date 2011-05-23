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
package org.more.hypha.beans.assembler.builder;
import org.more.hypha.ApplicationContext;
import org.more.hypha.NoDefineBeanException;
import org.more.hypha.beans.define.RelationBeanDefine;
import org.more.hypha.commons.engine.AbstractBeanBuilder;
import org.more.log.ILog;
import org.more.log.LogFactory;
import org.more.util.attribute.IAttribute;
/**
 * ��������bean��
 * @version 2011-2-15
 * @author ������ (zyc@byshell.org)
 */
public class RelationBeanBuilder extends AbstractBeanBuilder<RelationBeanDefine> {
    private static ILog         log       = LogFactory.getLog(RelationBeanBuilder.class);
    private static final String CacheName = "RelationBeanBuilder_CacheType";
    /*------------------------------------------------------------------------------*/
    public Class<?> loadType(RelationBeanDefine define, Object[] params) throws Throwable {
        IAttribute fungiAtt = define.getFungi();
        //1.���Ի��档
        if (fungiAtt.contains(CacheName) == true) {
            Class<?> fungiClass = (Class<?>) fungiAtt.getAttribute(CacheName);
            log.debug("return from fungi cache ,cacheName is {%0}, type = {%1}", CacheName, fungiClass);
            return fungiClass;
        }
        //2.ȷ��bean��
        String ref = define.getRef();
        String refPackage = define.getRefPackage();
        ApplicationContext app = this.getApplicationContext();
        if (app.containsBean(ref) == false) {
            log.warning("ref bean {%0} bean is not exist", CacheName, ref);
            ref = refPackage + "." + ref;
            if (app.containsBean(ref) == false) {
                log.error("ref bean {%0} is not exist", CacheName, ref);
                throw new NoDefineBeanException("ref bean " + ref + " is not exist");
            }
        }
        //3.��ȡref
        Class<?> bType = app.getBeanType(ref, params);
        fungiAtt.setAttribute(CacheName, bType);
        log.debug("ref bean type is {%0}.", bType);
        return bType;
    }
    @SuppressWarnings("unchecked")
    public <O> O createBean(RelationBeanDefine define, Object[] params) throws Throwable {
        String ref = define.getRef();
        String refPackage = define.getRefPackage();
        ApplicationContext app = this.getApplicationContext();
        if (app.containsBean(ref) == false) {
            log.warning("ref bean {%0} bean is not exist", CacheName, ref);
            ref = refPackage + "." + ref;
            if (app.containsBean(ref) == false) {
                log.error("ref bean {%0} is not exist", CacheName, ref);
                throw new NoDefineBeanException("ref bean " + ref + " is not exist");
            }
        }
        Object obj = app.getBean(ref, params);
        log.debug("ref bean value = {%0}", obj);
        return (O) obj;
    }
};