/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.more.util.io;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
/**
 * ʹ��OutputStream���Writer�Ĺ�����
 * @version 2009-5-13
 * @author �����ռ�
 */
public class WriterOutputStream extends OutputStream {
    private Writer writer   = null;
    private String encoding = null;
    private byte[] buf      = new byte[1];
    //========================================================================================
    /**
     * ��Writer���ַ������ʽ�����Ĺ��캯��
     * @param writer   - OutputStreamʹ�õ�Reader
     * @param encoding - OutputStreamʹ�õ��ַ������ʽ��
     */
    public WriterOutputStream(Writer writer, String encoding) {
        this.writer = writer;
        this.encoding = encoding;
    }
    /**
     * ��Writer�������캯��
     * @param writer - OutputStreamʹ�õ�Writer
     */
    public WriterOutputStream(Writer writer) {
        this.writer = writer;
    }
    //========================================================================================
    /** @see java.io.OutputStream#close() */
    public void close() throws IOException {
        this.writer.close();
        this.writer = null;
        this.encoding = null;
    }
    /** @see java.io.OutputStream#flush() */
    public void flush() throws IOException {
        this.writer.flush();
    }
    /** @see java.io.OutputStream#write(byte[]) */
    public void write(byte[] b) throws IOException {
        if (this.encoding == null)
            this.writer.write(new String(b));
        else
            this.writer.write(new String(b, this.encoding));
    }
    /** @see java.io.OutputStream#write(byte[],int,int) */
    public void write(byte[] b, int off, int len) throws IOException {
        if (this.encoding == null)
            this.writer.write(new String(b, off, len));
        else
            this.writer.write(new String(b, off, len, this.encoding));
    }
    /** @see java.io.OutputStream#write(int) */
    public synchronized void write(int b) throws IOException {
        this.buf[0] = (byte) b;
        write(this.buf);
    }
}
