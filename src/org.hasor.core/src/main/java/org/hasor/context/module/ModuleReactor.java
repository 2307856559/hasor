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
package org.hasor.context.module;
import java.util.ArrayList;
import java.util.Map;
import org.hasor.Hasor;
import org.hasor.context.ModuleInfo;
/**
 * ģ�鷴Ӧ�ѣ������Ŀ���Ƕ�ģ���������
 * @version : 2013-7-26
 * @author ������ (zyc@byshell.org)
 */
public class ModuleReactor {
    public ModuleInfo[] getResult(ModuleInfo[] modules) {
        if (modules==null)
            return modules;
        /*1.����ѭ�����*/
        Hasor.info("begin cycle check...mods %s.", new Object[] { modules });
        for (ModuleInfo info:modules){
            ModuleInfoBean infoBean=(ModuleInfoBean) info;
            infoBean.getAfterDependency();
            infoBean.getAfterDependency();
            
            info.getDependency();
            
        }s
        
        
        
        /*2.����������*/
        return modules;
    }
    
    
    
    
    
    
}