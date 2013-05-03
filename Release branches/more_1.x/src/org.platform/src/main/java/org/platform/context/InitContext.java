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
package org.platform.context;
import java.util.Enumeration;
import java.util.Set;
import javax.servlet.ServletContext;
import org.platform.context.setting.Config;
/**
 * 
 * @version : 2013-3-26
 * @author ������ (zyc@byshell.org)
 */
public interface InitContext {
    /**�ڿ��ɨ����ķ�Χ�ڲ��Ҿ��������༯�ϡ������������Ǽ̳е��ࡢ��ǵ�ע�⣩*/
    public Set<Class<?>> getClassSet(Class<?> featureType);
    /**��ȡ������ʼ��������*/
    public String getInitParameter(String name);
    /**��ȡ������ʼ���������Ƽ��ϡ�*/
    public Enumeration<String> getInitParameterNames();
    /**��ȡӦ�ó������á�*/
    public Config getConfig();
    /**��ȡ{@link ServletContext}��������*/
    public ServletContext getServletContext();
    /**��ȡϵͳ����ʱ�䡣*/
    public long getStartTime();
}