package org.more.dbpool.exception;
// ���ӳ��Ѿ����ͣ������ṩ����
public class PoolFullException extends Exception {
    public PoolFullException() {
        super("���ӳ��Ѿ����ͣ������ṩ����");
    }
    public PoolFullException(String message) {
        super(message);
    }
}
