package org.more.webserver;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;
/**
 * @author Administrator
 */
public class TomcatService {
    public String type = "AAAAAA";
    // ////////////////////////////////////////////////////////////////////
    public static void main(String[] args) throws Exception {
        String catalina_home = "d:/";
        Tomcat tomcat = new Tomcat();
        tomcat.setHostname("localhost");
        tomcat.setPort(8080); //���ù���Ŀ¼,��ʵûʲô��,tomcat��Ҫʹ�����Ŀ¼����дһЩ���� 
        tomcat.setBaseDir(catalina_home);
        StandardHost host = new StandardHost();
        tomcat.start();
        //
    }
}