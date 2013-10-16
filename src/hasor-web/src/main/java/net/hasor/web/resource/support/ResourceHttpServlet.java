/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
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
package net.hasor.web.resource.support;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.hasor.Hasor;
import net.hasor.core.AppContext;
import net.hasor.web.resource.ResourceLoader;
import org.more.util.ContextClassLoaderLocal;
import org.more.util.FileUtils;
import org.more.util.IOUtils;
import org.more.util.StringUtils;
import com.google.inject.Provider;
/**
 * ����װ��jar����zip���е���Դ
 * @version : 2013-6-5
 * @author ������ (zyc@hasor.net)
 */
public class ResourceHttpServlet extends HttpServlet {
    private static final long                                serialVersionUID = 2470188139577613256L;
    private static ContextClassLoaderLocal<ResourceLoader[]> LoaderList       = new ContextClassLoaderLocal<ResourceLoader[]>();
    private static ContextClassLoaderLocal<File>             CacheDir         = new ContextClassLoaderLocal<File>();
    private Map<String, ReadWriteLock>                       cachingRes       = new HashMap<String, ReadWriteLock>();
    @Inject
    private AppContext                                       appContext;
    private boolean                                          isDebug;
    //
    public synchronized void init(ServletConfig config) throws ServletException {
        this.isDebug = this.appContext.getEnvironment().isDebug();
        //
        ResourceLoader[] resLoaderArray = LoaderList.get();
        if (resLoaderArray != null)
            return;
        Provider<ResourceLoaderProvider>[] provider = appContext.getProviderByBindingType(ResourceLoaderProvider.class);
        resLoaderArray = new ResourceLoader[provider.length];
        for (int i = 0; i < provider.length; i++) {
            ResourceLoaderProvider plProvider = provider[i].get();
            plProvider.setAppContext(this.appContext);
            resLoaderArray[i] = plProvider.get();
        }
        LoaderList.set(resLoaderArray);
    }
    public synchronized static void initCacheDir(File cacheDir) {
        FileUtils.deleteDir(cacheDir);
        cacheDir.mkdirs();
        CacheDir.set(cacheDir);
        Hasor.info("use cacheDir %s", cacheDir);
    }
    //
    //
    //
    //
    /**��Ӧ��Դ*/
    private void forwardTo(File file, ServletRequest request, ServletResponse response) throws IOException, ServletException {
        if (response.isCommitted() == true)
            return;
        HttpServletRequest req = (HttpServletRequest) request;
        String requestURI = req.getRequestURI();
        String fileExt = requestURI.substring(requestURI.lastIndexOf("."));
        String typeMimeType = req.getSession(true).getServletContext().getMimeType(fileExt);
        if (StringUtils.isBlank(typeMimeType))
            Hasor.error("%s not mapping MimeType!", requestURI); //typeMimeType = this.getMimeType().get(fileExt.substring(1).toLowerCase());
        //
        if (typeMimeType != null)
            response.setContentType(typeMimeType);
        FileInputStream cacheFile = new FileInputStream(file);
        IOUtils.copy(cacheFile, response.getOutputStream());
        cacheFile.close();
    }
    //
    //
    /*��ȡ ReadWriteLock ��*/
    private synchronized ReadWriteLock getReadWriteLock(String requestURI) {
        ReadWriteLock cacheRWLock = null;
        if (this.cachingRes.containsKey(requestURI) == true) {
            cacheRWLock = this.cachingRes.get(requestURI);
        } else {
            cacheRWLock = new ReentrantReadWriteLock();
            this.cachingRes.put(requestURI, cacheRWLock);
        }
        return cacheRWLock;
    }
    /*�ͷ� ReadWriteLock ��*/
    private synchronized void releaseReadWriteLock(String requestURI) {
        if (this.cachingRes.containsKey(requestURI) == true)
            this.cachingRes.remove(requestURI);
    }
    /**��Դ������ڷ���*/
    public void service(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        //1.ȷ��ʱ������
        HttpServletRequest req = (HttpServletRequest) request;
        String requestURI = req.getRequestURI();
        try {
            requestURI = URLDecoder.decode(requestURI, "utf-8");
        } catch (Exception e) {}
        //2.��黺��·�����Ƿ����
        File cacheFile = new File(CacheDir.get(), requestURI);
        if (this.isDebug) {
            boolean mark = this.cacheRes(cacheFile, requestURI, request, response);
            if (mark)
                this.forwardTo(cacheFile, request, response);
            else
                ((HttpServletResponse) response).sendError(404, "not exist :" + requestURI);
            return;
        }
        //3.������-A
        ReadWriteLock cacheRWLock = this.getReadWriteLock(requestURI);
        //4.�����»����ļ������������Ϊ�˷�ֹ���洩͸
        boolean forwardType = true;
        cacheRWLock.readLock().lock();//��ȡ����
        if (!cacheFile.exists()) {
            /*������*/
            cacheRWLock.readLock().unlock();
            cacheRWLock.writeLock().lock();
            /*��debugģʽ�£�������δ����ʱ*/
            if (this.isDebug || !cacheFile.exists()) {
                forwardType = this.cacheRes(cacheFile, requestURI, request, response);//������ʧ��ʱ����false
            }
            cacheRWLock.readLock().lock();
            cacheRWLock.writeLock().unlock();
        }
        cacheRWLock.readLock().unlock();//��ȡ����
        //5.�������
        if (forwardType)
            this.forwardTo(cacheFile, request, response);
        else
            ((HttpServletResponse) response).sendError(404, "not exist this resource ��" + requestURI + "��");
        //6.�ͷ���-A
        this.releaseReadWriteLock(requestURI);
    }
    /*��Դ����*/
    private boolean cacheRes(File cacheFile, String requestURI, ServletRequest request, ServletResponse response) throws IOException, ServletException {
        //3.����������Դ 
        ResourceLoader inLoader = null;
        InputStream inStream = null;
        ResourceLoader[] loaderList = LoaderList.get();
        for (ResourceLoader loader : loaderList) {
            if (loader.exist(requestURI) == false)
                continue;
            if (this.isDebug) {
                if (!loader.canModify(requestURI) && cacheFile.exists())
                    return true;
            }
            //
            inLoader = loader;
            inStream = loader.getResourceAsStream(requestURI);
            if (inStream != null)
                break;
        }
        if (inStream == null)
            return false;
        //4.д����ʱ�ļ���
        cacheFile.getParentFile().mkdirs();
        FileOutputStream out = new FileOutputStream(cacheFile);
        IOUtils.copy(inStream, out);
        inLoader.close(inStream);
        out.flush();
        out.close();
        return true;
    }
}