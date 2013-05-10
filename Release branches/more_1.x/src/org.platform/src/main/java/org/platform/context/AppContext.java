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
import java.io.File;
import java.io.IOException;
import java.util.Set;
import org.platform.binder.BeanInfo;
import com.google.inject.Injector;
/**
 * 
 * @version : 2013-3-26
 * @author ������ (zyc@byshell.org)
 */
public interface AppContext {
    /**����*/
    public void start();
    /**���ٷ�����*/
    public void destroyed();
    /**��ȡ������*/
    public Object getContext();
    /**��ȡϵͳ����ʱ��*/
    public long getAppStartTime();
    /**��ȡӦ�ó������á�*/
    public Settings getSettings();
    /**�ڿ��ɨ����ķ�Χ�ڲ��Ҿ��������༯�ϡ������������Ǽ̳е��ࡢ��ǵ�ע�⣩*/
    public Set<Class<?>> getClassSet(Class<?> featureType);
    /**ͨ�����ʹ�������ʵ����ʹ��guice*/
    public <T> T getInstance(Class<T> beanType);
    //    /**ͨ�����ƴ���beanʵ����ʹ��guice��*/
    //    public  <T extends IService> T getService(String servicesName);
    //    /**ͨ�����ʹ�������ʵ����ʹ��guice*/
    //    public  <T extends IService> T getService(Class<T> servicesType);
    /**���Guice������*/
    public Injector getGuice();
    /**ͨ������ȡBean�����͡�*/
    public <T> Class<T> getBeanType(String name);
    /**��ȡ�Ѿ�ע���Bean���ơ�*/
    public String[] getBeanNames();
    /**��ȡbean��Ϣ��*/
    public BeanInfo getBeanInfo(String name);
    /**ͨ�����ƴ���beanʵ����ʹ��guice��*/
    public <T> T getBean(String name);
    /**��ȡ������Ŀ¼������·������*/
    public String getWorkDir();
    /**��ȡ�����ļ�Ŀ¼������·������*/
    public String getDataDir();
    /**��ȡ��ʱ�����ļ�Ŀ¼��*/
    public String getTempDir();
    /**��ȡ����Ŀ¼��*/
    public String getCacheDir();
    /**��ȡ�����ļ�Ŀ¼���Զ���name������ӵ�����ֵ�С�*/
    public String getDataDir(String name);
    /**��ȡ��ʱ�����ļ�Ŀ¼���Զ���name������ӵ�����ֵ�С�*/
    public String getTempDir(String name);
    /**��ȡ����Ŀ¼���Զ���name������ӵ�����ֵ�С�*/
    public String getCacheDir(String name);
    /**����ʱĿ¼�´���һ������������ʱ�ļ����أ�����ʱ�ļ���������������˳�֮����ͬ������Ŀ¼һͬɾ����*/
    public File createTempFile() throws IOException;
    /**
    * ����·���㷨��
    * @param target Ŀ��
    * @param dirSize ÿ��Ŀ¼�¿���ӵ�е���Ŀ¼���ļ���Ŀ��
    */
    public String genPath(long number, int size);
}