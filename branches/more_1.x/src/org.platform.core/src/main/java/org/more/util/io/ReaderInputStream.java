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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
/**
 * ʹ��InputStream��ȡReader�Ĺ�����
 * @version 2009-5-13
 * @author �����ռ�
 */
public class ReaderInputStream extends InputStream {
    private Reader                reader       = null;
    private ByteArrayOutputStream byteArrayOut = null;
    private Writer                writer       = null;
    private char[]                chars        = null;
    private byte[]                buffer       = null;
    private int                   index, length = 0;
    //========================================================================================
    /**
     * ��Reader�������캯��
     * @param readerString - Ҫ�Ķ����ַ�����
     */
    public ReaderInputStream(String readerString) {
        this.reader = new StringReader(readerString);
        byteArrayOut = new ByteArrayOutputStream();
        writer = new OutputStreamWriter(byteArrayOut);
        chars = new char[1024];
    }
    /**
     * ��Reader�������캯��
     * @param reader - InputStreamʹ�õ�Reader
     */
    public ReaderInputStream(Reader reader) {
        this.reader = reader;
        byteArrayOut = new ByteArrayOutputStream();
        writer = new OutputStreamWriter(byteArrayOut);
        chars = new char[1024];
    }
    /**
     * ��Reader���ַ������ʽ�����Ĺ��캯��
     * @param reader   - InputStreamʹ�õ�Reader
     * @param encoding - InputStreamʹ�õ��ַ������ʽ.
     * @throws ����ַ������ʽ��֧��,����UnsupportedEncodingException�쳣
     */
    public ReaderInputStream(Reader reader, String encoding) throws UnsupportedEncodingException {
        this.reader = reader;
        byteArrayOut = new ByteArrayOutputStream();
        writer = new OutputStreamWriter(byteArrayOut, encoding);
        chars = new char[1024];
    }
    //========================================================================================
    /** @see java.io.InputStream#read() */
    public int read() throws IOException {
        if (index >= length)
            fillBuffer();
        if (index >= length)
            return -1;
        return 0xff & buffer[index++];
    }
    private void fillBuffer() throws IOException {
        if (length < 0)
            return;
        int numChars = reader.read(chars);
        if (numChars < 0) {
            length = -1;
        } else {
            byteArrayOut.reset();
            writer.write(chars, 0, numChars);
            writer.flush();
            buffer = byteArrayOut.toByteArray();
            length = buffer.length;
            index = 0;
        }
    }
    /** @see java.io.InputStream#read(byte[], int, int) */
    public int read(byte[] data, int off, int len) throws IOException {
        if (index >= length)
            fillBuffer();
        if (index >= length)
            return -1;
        int amount = Math.min(len, length - index);
        System.arraycopy(buffer, index, data, off, amount);
        index += amount;
        return amount;
    }
    /** @see java.io.InputStream#available() */
    public int available() throws IOException {
        return (index < length) ? length - index : ((length >= 0) && reader.ready()) ? 1 : 0;
    }
    /** @see java.io.InputStream#close() */
    public void close() throws IOException {
        reader.close();
    }
}
