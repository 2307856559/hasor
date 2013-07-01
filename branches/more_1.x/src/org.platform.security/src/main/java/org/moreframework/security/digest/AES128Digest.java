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
package org.moreframework.security.digest;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.more.util.CommonCodeUtils.Base64;
import org.moreframework.security.Digest;
/**
 * AES���ܣ��������������������AES�����㷨ʹ�õ���128λ��
 * @version : 2013-4-24
 * @author ������ (zyc@byshell.org)
 */
public final class AES128Digest implements Digest {
    @Override
    public String encrypt(String strValue, String generateKey) throws Throwable {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128, new SecureRandom(generateKey.getBytes()));
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES");// ����������   
        cipher.init(Cipher.ENCRYPT_MODE, key);// ��ʼ��   
        byte[] bytesData = cipher.doFinal(strValue.getBytes("utf-8"));// ��ʽִ�м��ܲ���
        return Base64.base64EncodeFoArray(bytesData);
    };
    @Override
    public String decrypt(String strValue, String generateKey) throws Throwable {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128, new SecureRandom(generateKey.getBytes()));
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES");// ����������   
        cipher.init(Cipher.DECRYPT_MODE, key);// ��ʼ��   
        byte[] bytesData = Base64.base64DecodeToArray(strValue);
        bytesData = cipher.doFinal(bytesData);// ��ʽִ�м��ܲ���
        return new String(bytesData, "utf-8");
    }
}