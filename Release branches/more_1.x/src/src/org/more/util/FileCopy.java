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
package org.more.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Date;
/**
 * �ļ�������
 * @version 2009-4-29(08�꿪����09������)
 * @author ������ (zyc@byshell.org)
 */
public final class FileCopy {
    /**
     * Javaԭʼ��ʽ�����ļ���f1 ������ f2�������ļ��Ļ�������СΪ2097152�ֽڡ�
     * @param f1 ԭʼ�ļ�
     * @param f2 Ŀ���ļ�
     * @param append ���Ŀ���ļ�������׷�ӻ��Ǹ���
     * @return ����ִ��ʱ�䡣
     * @throws IOException �������IO�쳣
     */
    public static long forJava(File f1, File f2, boolean append) throws IOException {
        long time = new Date().getTime();
        int length = 2097152;
        FileInputStream in = new FileInputStream(f1);
        FileOutputStream out = new FileOutputStream(f2, append);
        byte[] buffer = new byte[length];
        while (true) {
            int ins = in.read(buffer);
            if (ins == -1) {
                in.close();
                out.flush();
                out.close();
                return new Date().getTime() - time;
            } else
                out.write(buffer, 0, ins);
        }
    }
    /**
     * ʹ��JavaNIO�ܵ��Թܵ���ʽ�����ļ��������ļ��Ļ�������СΪ2097152�ֽڡ�
     * @param f1 ԭʼ�ļ�
     * @param f2 Ŀ���ļ�
     * @param append ���Ŀ���ļ�������׷�ӻ��Ǹ���
     * @return ����ִ��ʱ�䡣
     * @throws IOException �������IO�쳣
     */
    public static long forChannel(File f1, File f2, boolean append) throws IOException {
        long time = new Date().getTime();
        int length = 2097152;
        FileInputStream in = new FileInputStream(f1);
        FileOutputStream out = new FileOutputStream(f2, append);
        FileChannel inC = in.getChannel();
        FileChannel outC = out.getChannel();
        ByteBuffer b = null;
        while (true) {
            if (inC.position() == inC.size()) {
                inC.close();
                outC.close();
                return new Date().getTime() - time;
            }
            if ((inC.size() - inC.position()) < length) {
                length = (int) (inC.size() - inC.position());
            } else
                length = 2097152;
            b = ByteBuffer.allocateDirect(length);
            inC.read(b);
            b.flip();
            outC.write(b);
            outC.force(false);
        }
    }
    /**
     * ʹ��JavaNIO �ļ��ڴ�ӳ�䷽ʽ�����ļ��������ļ��Ļ�������СΪ2097152�ֽڡ�
     * @param f1 ԭʼ�ļ�
     * @param f2 Ŀ���ļ�
     * @return ����ִ��ʱ�䡣
     * @throws IOException �������IO�쳣
     */
    public static long forImage(File f1, File f2) throws IOException {
        long time = new Date().getTime();
        int length = 2097152;
        FileInputStream in = new FileInputStream(f1);
        RandomAccessFile out = new RandomAccessFile(f2, "rw");
        FileChannel inC = in.getChannel();
        MappedByteBuffer outC = null;
        MappedByteBuffer inbuffer = null;
        byte[] b = new byte[length];
        while (true) {
            if (inC.position() == inC.size()) {
                inC.close();
                outC.force();
                out.close();
                return new Date().getTime() - time;
            }
            if ((inC.size() - inC.position()) < length) {
                length = (int) (inC.size() - inC.position());
            } else {
                length = 20971520;
            }
            b = new byte[length];
            inbuffer = inC.map(MapMode.READ_ONLY, inC.position(), length);
            inbuffer.load();
            inbuffer.get(b);
            outC = out.getChannel().map(MapMode.READ_WRITE, inC.position(), length);
            inC.position(b.length + inC.position());
            outC.put(b);
            outC.force();
        }
    }
    /**
     * ʹ��JavaNIO�ܵ��Թܵ����䷽ʽ�����ļ��������ļ��Ļ�������СΪ2097152�ֽڡ�
     * @param f1 ԭʼ�ļ�
     * @param f2 Ŀ���ļ�
     * @param append ���Ŀ���ļ�������׷�ӻ��Ǹ���
     * @return ����ִ��ʱ�䡣
     * @throws IOException �������IO�쳣
     */
    public static long forTransfer(File f1, File f2, boolean append) throws IOException {
        long time = new Date().getTime();
        int length = 2097152;
        FileInputStream in = new FileInputStream(f1);
        FileOutputStream out = new FileOutputStream(f2, append);
        FileChannel inC = in.getChannel();
        FileChannel outC = out.getChannel();
        int i = 0;
        while (true) {
            if (inC.position() == inC.size()) {
                inC.close();
                outC.close();
                return new Date().getTime() - time;
            }
            if ((inC.size() - inC.position()) < 20971520)
                length = (int) (inC.size() - inC.position());
            else
                length = 20971520;
            inC.transferTo(inC.position(), length, outC);
            inC.position(inC.position() + length);
            i++;
        }
    }
}