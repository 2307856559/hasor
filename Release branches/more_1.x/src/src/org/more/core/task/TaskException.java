package org.more.core.task;
/**
 * Task�쳣
 * @version 2009-5-15
 * @author ������ (zyc@byshell.org)
 */
public class TaskException extends RuntimeException {
    private static final long serialVersionUID = -5825306826820187090L;
    /** Task�쳣 */
    public TaskException() {
        super("Task�쳣");
    }
    /**
     * Task�쳣��������Ϣ�ɲ�������
     * @param msg �쳣��������Ϣ
     */
    public TaskException(String msg) {
        super(msg);
    }
    /**
     * Task�쳣��������Ϣ�ǳн���һ���쳣����
     * @param e �нӵ��쳣
     */
    public TaskException(Exception e) {
        super(e);
    }
}