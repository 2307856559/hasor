package org.hasor.test.mvc.app.beans;
import java.io.IOException;
import net.hasor.core.anno.Bean;
import net.hasor.core.anno.context.AnnoAppContext;
import org.hasor.test.mvc.plugin.log.OutLog;
import org.hasor.test.mvc.plugin.safety.Power;
import org.hasor.test.mvc.plugin.safety.SafetyContext;
/**
 * 
 * @version : 2013-7-25
 * @author ������ (zyc@hasor.net)
 */
@Bean("SafeBean")
public class SafeBean {
    @OutLog
    @Power("abc")
    public void print() {
        System.out.println("�ڴ�֮ǰ�����־!");
    }
    //
    //
    public static void main(String[] args) throws IOException {
        AnnoAppContext aac = new AnnoAppContext();
        aac.start();
        //
        SafeBean safeBean = (SafeBean) aac.getBean("SafeBean");
        SafetyContext sc = aac.getInstance(SafetyContext.class);
        System.out.println("--------��Ȩ�޵���------");
        try {
            safeBean.print();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("--------��Ȩ�޵���------");
        try {
            sc.addPower("abc");
            safeBean.print();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        aac.destroy();
    }
}