package org.more.dbpool.exception;
/// <summary>
/// ConnectionType���ʹ���
/// </summary>
public class CreateConnectionException extends Exception
{
        public CreateConnectionException() { super("��������ʱ������"); }
        public CreateConnectionException(String message) { super(message); }
}
