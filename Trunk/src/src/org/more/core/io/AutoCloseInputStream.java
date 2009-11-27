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
package org.more.core.io;
import java.io.IOException;
import java.io.InputStream;
/**
 * �����Զ��رյ�������
 * Date : 2009-5-13
 * @author ������
 */
public class AutoCloseInputStream extends InputStream {
    /** Ŀ���ȡ���� */
    private InputStream in    = null;
    /** Ŀ���ȡ���� */
    private boolean     close = false;
    //========================================================================================
    /**
     * �����Զ��رյ�������
     * @param in ��ȡ��Ŀ����
     */
    public AutoCloseInputStream(InputStream in) {
        this.in = in;
    }
    //========================================================================================
    /**
     * �����ȡ��Ŀ�����Ѿ���ȡ��ĩβ���Զ��رո��������Ҷ����Ѿ��رյ������ø÷�����ʼ�շ���-1��
     * @return �����ȡ��Ŀ�����Ѿ���ȡ��ĩβ���Զ��رո��������Ҷ����Ѿ��رյ������ø÷�����ʼ�շ���-1��
     */
    @Override
    public int read() throws IOException {
        if (this.close == true)
            return -1;
        int read = this.in.read();
        if (read == -1)
            this.close();
        return read;
    }
    @Override
    public int available() throws IOException {
        return this.in.available();
    }
    @Override
    public void close() throws IOException {
        this.in.close();
        this.close = true;
    }
    @Override
    public synchronized void mark(int readlimit) {
        this.in.mark(readlimit);
    }
    @Override
    public boolean markSupported() {
        return this.in.markSupported();
    }
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return this.in.read(b, off, len);
    }
    @Override
    public int read(byte[] b) throws IOException {
        return this.in.read(b);
    }
    @Override
    public synchronized void reset() throws IOException {
        this.in.reset();
    }
    @Override
    public long skip(long n) throws IOException {
        return this.in.skip(n);
    }
}
