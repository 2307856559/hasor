package org.more.dbpool.exception;
// �����Ѿ����л���δ��ȫ����
public class PoolNotStopException extends Exception {
    public PoolNotStopException() {
        super("�����Ѿ����л���δ��ȫ����");
    }
    public PoolNotStopException(String message) {
        super(message);
    }
}
