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
import java.io.UnsupportedEncodingException;
/**
 * �򵥱����࣬�����ṩ��Base64�ı���ͽ����Լ�MD5���ܴ���ȡ�ķ�����ע����಻���˽������ݽ��б��������
 * @version 2009-4-28
 * @author ������ (zyc@byshell.org)
 */
public final class SimpleCode {
    // ===============================================================
    private static char[] base64EncodeChars = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
    private static byte[] base64DecodeChars = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1 };
    // ===============================================================
    /**
     * �����ݽ���Base64���롣
     * @param data Ҫ�����ԭʼ���ݡ�
     * @return ���ر���֮��Ľ����
     */
    public static String encodeBase64(byte[] data) {
        StringBuffer sb = new StringBuffer();
        int len = data.length;
        int i = 0;
        int b1, b2, b3;
        while (i < len) {
            b1 = data[i++] & 0xff;
            if (i == len) {
                sb.append(SimpleCode.base64EncodeChars[b1 >>> 2]);
                sb.append(SimpleCode.base64EncodeChars[(b1 & 0x3) << 4]);
                sb.append("==");
                break;
            }
            b2 = data[i++] & 0xff;
            if (i == len) {
                sb.append(SimpleCode.base64EncodeChars[b1 >>> 2]);
                sb.append(SimpleCode.base64EncodeChars[(b1 & 0x03) << 4 | (b2 & 0xf0) >>> 4]);
                sb.append(SimpleCode.base64EncodeChars[(b2 & 0x0f) << 2]);
                sb.append("=");
                break;
            }
            b3 = data[i++] & 0xff;
            sb.append(SimpleCode.base64EncodeChars[b1 >>> 2]);
            sb.append(SimpleCode.base64EncodeChars[(b1 & 0x03) << 4 | (b2 & 0xf0) >>> 4]);
            sb.append(SimpleCode.base64EncodeChars[(b2 & 0x0f) << 2 | (b3 & 0xc0) >>> 6]);
            sb.append(SimpleCode.base64EncodeChars[b3 & 0x3f]);
        }
        return sb.toString();
    }
    /**
     * ����Base64���ݡ�
     * @param str Ҫ�����Base64�ַ�����
     * @return ���ؽ���֮���ԭʼ���ݡ�
     * @throws UnsupportedEncodingException �����������з����쳣��
     */
    public static byte[] decodeBase64(String str) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        byte[] data = str.getBytes("US-ASCII");
        int len = data.length;
        int i = 0;
        int b1, b2, b3, b4;
        while (i < len) {
            /* b1 */
            do
                b1 = SimpleCode.base64DecodeChars[data[i++]];
            while (i < len && b1 == -1);
            if (b1 == -1)
                break;
            /* b2 */
            do
                b2 = SimpleCode.base64DecodeChars[data[i++]];
            while (i < len && b2 == -1);
            if (b2 == -1)
                break;
            sb.append((char) (b1 << 2 | (b2 & 0x30) >>> 4));
            /* b3 */
            do {
                b3 = data[i++];
                if (b3 == 61)
                    return sb.toString().getBytes("ISO-8859-1");
                b3 = SimpleCode.base64DecodeChars[b3];
            } while (i < len && b3 == -1);
            if (b3 == -1)
                break;
            sb.append((char) ((b2 & 0x0f) << 4 | (b3 & 0x3c) >>> 2));
            /* b4 */
            do {
                b4 = data[i++];
                if (b4 == 61)
                    return sb.toString().getBytes("ISO-8859-1");
                b4 = SimpleCode.base64DecodeChars[b4];
            } while (i < len && b4 == -1);
            if (b4 == -1)
                break;
            sb.append((char) ((b3 & 0x03) << 6 | b4));
        }
        return sb.toString().getBytes("ISO-8859-1");
    }
    /**
     * ȡ���ֽ������MD5��
     * @param source �ֽ�����
     * @return ����MD5��
     */
    public static String parseMD5(byte[] source) {
        String s = null;
        // �������ֽ�ת���� 16 ���Ʊ�ʾ���ַ�
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest(); // MD5 �ļ�������һ�� 128
            // λ�ĳ������� ���ֽڱ�ʾ���� 16 ���ֽ�
            char str[] = new char[16 * 2]; // ÿ���ֽ��� 16
            // ���Ʊ�ʾ�Ļ���ʹ�������ַ��� ���Ա�ʾ�� 16 ������Ҫ 32 ���ַ�
            int k = 0; // ��ʾת������ж�Ӧ���ַ�λ��
            for (int i = 0; i < 16; i++) { // �ӵ�һ���ֽڿ�ʼ����
                // MD5 ��ÿһ���ֽ�ת���� 16 �����ַ���ת��
                byte byte0 = tmp[i]; // ȡ�� i ���ֽ�
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // ȡ�ֽ��и�
                // 4 λ������ת��, >>> Ϊ�߼����ƣ�������λһ������
                str[k++] = hexDigits[byte0 & 0xf]; // ȡ�ֽ��е�
                // 4 λ������ת��
            }
            s = new String(str); // ����Ľ��ת��Ϊ�ַ���
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
}