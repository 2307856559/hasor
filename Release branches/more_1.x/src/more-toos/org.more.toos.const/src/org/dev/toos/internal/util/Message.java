package org.dev.toos.internal.util;
import org.eclipse.core.runtime.IProgressMonitor;
/**
 * 
 * @version : 2013-2-5
 * @author ������ (zyc@byshell.org)
 */
public class Message {
    public static void errorInfo(String string, Throwable e) {
        e.printStackTrace();
        // TODO Auto-generated method stub
    }
    /**�����������*/
    public static void updateTask(IProgressMonitor monitor, String title, int countWorke, int worked) {
        monitor.beginTask(title, countWorke);
        monitor.worked(worked);
    }
}