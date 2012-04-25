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
 * @version : 2011-11-7
 * @author ������ (zyc@byshell.org)
 */
public abstract class MD5 {
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