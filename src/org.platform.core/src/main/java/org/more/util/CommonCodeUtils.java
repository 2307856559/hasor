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
import java.security.NoSuchAlgorithmException;
/**
 * 
 * @version : 2012-6-21
 * @author ������ (zyc@byshell.org)
 */
public abstract class CommonCodeUtils {
    /**
     * �򵥱����࣬�����ṩ��Base64�ı��룬�ñ����������׵�js����������ʵ�ֻ�����뻥��ת�����ܡ�
     * @version 2009-4-28
     * @author ������ (zyc@byshell.org)
     */
    public static abstract class Base64 {
        protected static final String Base64Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@*-"; // supplement
        /***/
        //    /**������Base64��Ӧ��JSbase64����/����ű�*/
        //    public static Reader getJSReader() throws UnsupportedEncodingException {
        //        return new InputStreamReader(Base64.class.getResourceAsStream("/META-INF/resource/util/base64.js"), "utf-8");
        //    };
        /**
         * ʹ��UTF-8�������Base64����
         * @param s Ҫ�����ԭʼ����
         * @return ���ر���֮����ַ�����
         */
        public static String base64Encode(final String s) {
            if (s == null || s.length() == 0)
                return s;
            byte[] b = null;
            try {
                b = s.getBytes("UTF-8");
            } catch (java.io.UnsupportedEncodingException e) {
                e.printStackTrace();
                return s;
            }
            return base64EncodeFoArray(b);
        }
        /** Encoding a byte array to a string follow the Base64 regular. */
        public static String base64EncodeFoArray(final byte[] s) {
            if (s == null)
                return null;
            if (s.length == 0)
                return "";
            StringBuffer buf = new StringBuffer();
            int b0, b1, b2, b3;
            int len = s.length;
            int i = 0;
            while (i < len) {
                byte tmp = s[i++];
                b0 = (tmp & 0xfc) >> 2;
                b1 = (tmp & 0x03) << 4;
                if (i < len) {
                    tmp = s[i++];
                    b1 |= (tmp & 0xf0) >> 4;
                    b2 = (tmp & 0x0f) << 2;
                    if (i < len) {
                        tmp = s[i++];
                        b2 |= (tmp & 0xc0) >> 6;
                        b3 = tmp & 0x3f;
                    } else
                        b3 = 64; // 1 byte "-" is supplement
                } else
                    b2 = b3 = 64;// 2 bytes "-" are supplement
                buf.append(Base64Chars.charAt(b0));
                buf.append(Base64Chars.charAt(b1));
                buf.append(Base64Chars.charAt(b2));
                buf.append(Base64Chars.charAt(b3));
            }
            return buf.toString();
        }
        /** Decoding a string to a string follow the Base64 regular. */
        public static String base64Decode(final String s) {
            byte[] b = base64DecodeToArray(s);
            if (b == null)
                return null;
            if (b.length == 0)
                return "";
            try {
                return new String(b, "UTF-8");
            } catch (java.io.UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }
        /** Decoding a string to a byte array follow the Base64 regular */
        public static byte[] base64DecodeToArray(final String s) {
            if (s == null)
                return null;
            int len = s.length();
            if (len == 0)
                return new byte[0];
            if (len % 4 != 0)
                throw new java.lang.IllegalArgumentException(s);
            byte[] b = new byte[(len / 4) * 3];
            int i = 0, j = 0, e = 0, c, tmp;
            while (i < len) {
                c = Base64Chars.indexOf((int) s.charAt(i++));
                tmp = c << 18;
                c = Base64Chars.indexOf((int) s.charAt(i++));
                tmp |= c << 12;
                c = Base64Chars.indexOf((int) s.charAt(i++));
                if (c < 64) {
                    tmp |= c << 6;
                    c = Base64Chars.indexOf((int) s.charAt(i++));
                    if (c < 64)
                        tmp |= c;
                    else
                        e = 1;
                } else {
                    e = 2;
                    i++;
                }
                b[j + 2] = (byte) (tmp & 0xff);
                tmp >>= 8;
                b[j + 1] = (byte) (tmp & 0xff);
                tmp >>= 8;
                b[j + 0] = (byte) (tmp & 0xff);
                j += 3;
            }
            if (e != 0) {
                len = b.length - e;
                byte[] copy = new byte[len];
                System.arraycopy(b, 0, copy, 0, len);
                return copy;
            }
            return b;
        }
    }
    /**
     * MD5�㷨�ṩ
     * @version : 2011-11-7
     * @author ������ (zyc@byshell.org)
     */
    public static abstract class MD5 {
        public static String encodeMD5(byte[] source) throws NoSuchAlgorithmException {
            String s = null;
            // �������ֽ�ת���� 16 ���Ʊ�ʾ���ַ�
            char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest(); // MD5 �ļ�������һ�� 128 λ�ĳ����������ֽڱ�ʾ���� 16 ���ֽ�
            char str[] = new char[16 * 2]; // ÿ���ֽ��� 16 ���Ʊ�ʾ�Ļ���ʹ�������ַ������Ա�ʾ�� 16 ������Ҫ 32 ���ַ�
            int k = 0; // ��ʾת������ж�Ӧ���ַ�λ��
            for (int i = 0; i < 16; i++) { // �ӵ�һ���ֽڿ�ʼ���� MD5 ��ÿһ���ֽ�ת���� 16 �����ַ���ת��
                byte byte0 = tmp[i]; // ȡ�� i ���ֽ�
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // ȡ�ֽ��и� 4 λ������ת��, >>> Ϊ�߼����ƣ�������λһ������
                str[k++] = hexDigits[byte0 & 0xf]; // ȡ�ֽ��е� 4 λ������ת��
            }
            s = new String(str); // ����Ľ��ת��Ϊ�ַ���
            return s;
        }
        public static String getMD5(String source) throws NoSuchAlgorithmException {
            return getMD5(source.getBytes());
        }
        public static String getMD5(byte[] source) throws NoSuchAlgorithmException {
            String s = null;
            // �������ֽ�ת���� 16 ���Ʊ�ʾ���ַ�
            char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest(); // MD5 �ļ�������һ�� 128 λ�ĳ����������ֽڱ�ʾ���� 16 ���ֽ�
            char str[] = new char[16 * 2]; // ÿ���ֽ��� 16 ���Ʊ�ʾ�Ļ���ʹ�������ַ������Ա�ʾ�� 16 ������Ҫ 32 ���ַ�
            int k = 0; // ��ʾת������ж�Ӧ���ַ�λ��
            for (int i = 0; i < 16; i++) { // �ӵ�һ���ֽڿ�ʼ���� MD5 ��ÿһ���ֽ�ת���� 16 �����ַ���ת��
                byte byte0 = tmp[i]; // ȡ�� i ���ֽ�
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // ȡ�ֽ��и� 4 λ������ת��, >>> Ϊ�߼����ƣ�������λһ������
                str[k++] = hexDigits[byte0 & 0xf]; // ȡ�ֽ��е� 4 λ������ת��
            }
            s = new String(str); // ����Ľ��ת��Ϊ�ַ���
            return s;
        }
    }
}