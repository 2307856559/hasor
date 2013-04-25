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
package org.platform.security.digest;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import org.more.util.CommonCodeUtil.Base64;
import org.platform.security.CodeDigest;
/**
 * DES���ܡ�
 * @version : 2013-4-24
 * @author ������ (zyc@byshell.org)
 */
public final class DesDigest implements CodeDigest {
    private static final String DES = "DES";
    /**���� */
    public String encode(String strValue, String generateKey) throws Exception {
        SecureRandom sr = new SecureRandom(); //DES�㷨Ҫ����һ�������ε������Դ
        DESKeySpec dks = new DESKeySpec(generateKey.getBytes()); // ��ԭʼ�ܳ����ݴ���DESKeySpec����
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES); // ����һ���ܳ׹�����Ȼ��������DESKeySpecת����,һ��SecretKey����
        SecretKey securekey = keyFactory.generateSecret(dks); // Cipher����ʵ����ɼ��ܲ���
        Cipher cipher = Cipher.getInstance(DES); // ���ܳ׳�ʼ��Cipher����
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);// ���ڣ���ȡ���ݲ�����
        byte[] bytesData = cipher.doFinal(strValue.getBytes("utf-8"));// ��ʽִ�м��ܲ���
        return Base64.base64EncodeFoArray(bytesData);
    };
    /**����*/
    public String decode(String strValue, String generateKey) throws Exception {
        byte[] bytesData = Base64.base64DecodeToArray(strValue);
        SecureRandom sr = new SecureRandom(); //DES�㷨Ҫ����һ�������ε������Դ
        DESKeySpec dks = new DESKeySpec(generateKey.getBytes()); // ��ԭʼ�ܳ����ݴ���DESKeySpec����
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES); // ����һ���ܳ׹�����Ȼ��������DESKeySpecת����,һ��SecretKey����
        SecretKey securekey = keyFactory.generateSecret(dks); // Cipher����ʵ����ɼ��ܲ���
        Cipher cipher = Cipher.getInstance(DES); // ���ܳ׳�ʼ��Cipher����
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);// ���ڣ���ȡ���ݲ�����
        bytesData = cipher.doFinal(bytesData);// ��ʽִ�н��ܲ���
        return new String(bytesData, "utf-8");
    };
}