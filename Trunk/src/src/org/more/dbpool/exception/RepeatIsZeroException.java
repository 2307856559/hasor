package org.more.dbpool.exception;
// ���ü����Ѿ�Ϊ0��
public class RepeatIsZeroException extends Exception {
    public RepeatIsZeroException() {
        super("���ü����Ѿ�Ϊ0");
    }
    public RepeatIsZeroException(String message) {
        super(message);
    }
}
