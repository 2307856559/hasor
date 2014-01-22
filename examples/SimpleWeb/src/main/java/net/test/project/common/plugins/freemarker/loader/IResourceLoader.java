package org.noe.platform.modules.freemarker.loader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
/**
 * ������Դ·��װ�ظ���Դ����
 * @version : 2011-9-14
 * @author ������ (zyc@byshell.org) 
 */
public interface IResourceLoader {
    public URL getResource(String resourcePath) throws IOException;
    /**װ��ָ����Դ��*/
    public InputStream getResourceAsStream(String resourcePath) throws IOException;
}