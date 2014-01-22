package org.noe.platform.modules.freemarker.loader.resource;
import java.io.InputStream;
import java.net.URL;
import org.noe.platform.modules.freemarker.loader.IResourceLoader;
/**
 * {@link ClassPathResourceLoader}��ʹ��һ��classpath·����Ϊ���·����
 * ������������һ����Ϊ��org.more.res������Դ����
 * {@link #getResourceAsStream(String)}��������Ϊ��/abc/aa/htm����
 * ��ô�����Դ��ʵ�ʵ�ַ��λ��classpath�µġ�org/more/res/abc/aa/htm����
 * @version : 2011-9-14
 * @author ������ (zyc@byshell.org) 
 */
public class ClassPathResourceLoader implements IResourceLoader {
    private String      packageName = null;
    private ClassLoader classLoader = null;
    /***/
    public ClassPathResourceLoader(String packageName, ClassLoader classLoader) {
        this.packageName = packageName;
        this.classLoader = classLoader;
    }
    /**��ȡ��Դ��ȡ�İ�·����*/
    public String getPackageName() {
        return this.packageName;
    }
    /**��ȡװ����Դʹ�õ���װ������*/
    public ClassLoader getClassLoader() {
        return this.classLoader;
    }
    public URL getResource(String name) {
        String $name = this.packageName + "/" + name;
        if ($name.charAt(0) == '/')
            $name = $name.substring(1);
        $name = $name.replaceAll("/{2}", "/");
        return this.classLoader.getResource($name);
    }
    public InputStream getResourceAsStream(String name) {
        String $name = this.packageName + "/" + name;
        if ($name.charAt(0) == '/')
            $name = $name.substring(1);
        $name = $name.replaceAll("/{2}", "/");
        return this.classLoader.getResourceAsStream($name);
    }
}