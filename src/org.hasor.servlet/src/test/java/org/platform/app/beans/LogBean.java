package org.platform.app.beans;
import java.io.IOException;
import org.hasor.annotation.Bean;
import org.hasor.annotation.context.AnnoAppContext;
import org.platform.plugin.log.OutLog;
/**
 * 
 * @version : 2013-7-25
 * @author ������ (zyc@byshell.org)
 */
@Bean("LogBean")
public class LogBean {
    @OutLog
    public void print() {
        System.out.println("�ڴ�֮ǰ�����־!");
    }
    //
    //
    public static void main(String[] args) throws IOException {
        AnnoAppContext aac = new AnnoAppContext();
        aac.start();
        //
        LogBean logBean = (LogBean) aac.getBean("LogBean");
        logBean.print();
        //
        aac.destroy();
    }
}