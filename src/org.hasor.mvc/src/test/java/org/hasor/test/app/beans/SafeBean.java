package org.hasor.test.app.beans;
import java.io.IOException;
import org.hasor.context.anno.Bean;
import org.hasor.context.anno.context.AnnoAppContextSupportModule;
import org.hasor.test.plugin.log.OutLog;
import org.hasor.test.plugin.safety.Power;
import org.hasor.test.plugin.safety.SafetyContext;
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
        AnnoAppContextSupportModule aac = new AnnoAppContextSupportModule();
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