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
package org.more.beans.resource.annotation.core;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import org.more.beans.info.BeanInterface;
import org.more.beans.resource.annotation.Interface;
import org.more.beans.resource.annotation.util.AnnoContextStack;
import org.more.beans.resource.annotation.util.AnnoProcess;
/**
 * ����Interfaceע����ࡣ
 * @version 2010-1-16
 * @author ������ (zyc@byshell.org)
 */
public class Anno_Interface implements AnnoProcess {
    @Override
    public void beginAnnotation(Annotation anno, Object atObject, AnnoContextStack context) {
        ArrayList<BeanInterface> implInterfaces = new ArrayList<BeanInterface>();
        Interface facer = (Interface) anno;
        String delegateRefBean = facer.delegateRefBean();
        for (Class<?> face : facer.impls()) {
            BeanInterface bi = new BeanInterface();
            bi.setDelegateRefBean(delegateRefBean);
            bi.setPropType(face.getName());
            implInterfaces.add(bi);
        }
        context.setAttribute("implInterface", implInterfaces);
    }
    @Override
    public void endAnnotation(Annotation anno, Object atObject, AnnoContextStack context) {}
}