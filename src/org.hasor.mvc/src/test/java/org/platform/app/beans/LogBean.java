package org.platform.app.beans;
import java.io.IOException;
import org.hasor.context.anno.Bean;
import org.hasor.context.anno.context.AnnoAppContextSupportModule;
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
        AnnoAppContextSupportModule aac = new AnnoAppContextSupportModule();
        aac.start();
        //
        LogBean logBean = (LogBean) aac.getBean("LogBean");
        logBean.print();
        //
        aac.destroy();
    }
}