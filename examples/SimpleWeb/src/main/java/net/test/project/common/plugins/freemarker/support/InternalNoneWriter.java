package org.noe.platform.modules.freemarker.support;
import java.io.IOException;
import java.io.Writer;
/**
 * һ���޵׶�Writer
 * @version : 2013-5-17
 * @author ������ (zyc@byshell.org)
 */
class InternalNoneWriter extends Writer {
    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {}
    @Override
    public void flush() throws IOException {}
    @Override
    public void close() throws IOException {}
}