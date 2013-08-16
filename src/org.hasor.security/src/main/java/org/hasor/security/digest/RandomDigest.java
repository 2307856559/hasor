/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
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
package org.hasor.security.digest;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import org.hasor.security.Digest;
import org.more.util.CommonCodeUtils.Base64;
/**
 * ������ܡ�
 * @version : 2013-4-24
 * @author ������ (zyc@byshell.org)
 */
public final class RandomDigest implements Digest {
    @Override
    public String encrypt(String strValue, String generateKey) throws Throwable {
        PBEKeySpec pbks = new PBEKeySpec(generateKey.toCharArray());
        // �ɿ���������Կ
        SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey k = kf.generateSecret(pbks);
        // �����������
        byte[] salt = new byte[8];
        Random r = new Random(System.currentTimeMillis());
        r.nextBytes(salt);
        // ��������ʼ��������
        Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
        PBEParameterSpec ps = new PBEParameterSpec(salt, 1000);
        cipher.init(Cipher.ENCRYPT_MODE, k, ps);
        // ��ȡ���ģ�ִ�м���
        byte[] bytesData = cipher.doFinal(strValue.getBytes("utf-8"));// ��ʽִ�м��ܲ���
        byte[] finalBytesData = new byte[bytesData.length + salt.length];
        System.arraycopy(salt, 0, finalBytesData, 0, salt.length);
        System.arraycopy(bytesData, 0, finalBytesData, salt.length, bytesData.length);
        return Base64.base64EncodeFoArray(finalBytesData);
    }
    @Override
    public String decrypt(String strValue, String generateKey) throws Throwable {
        // ȡ�����ĺ���
        byte[] finalBytesData = Base64.base64DecodeToArray(strValue);
        byte[] salt = new byte[8];
        byte[] bytesData = new byte[finalBytesData.length - salt.length];// ��ʽִ�м��ܲ���
        System.arraycopy(finalBytesData, 0, salt, 0, salt.length);
        System.arraycopy(finalBytesData, salt.length, bytesData, 0, bytesData.length);
        //
        PBEKeySpec pbks = new PBEKeySpec(generateKey.toCharArray());
        // �ɿ���������Կ
        SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey k = kf.generateSecret(pbks);
        // ��������ʼ��������
        Cipher cp = Cipher.getInstance("PBEWithMD5AndDES");
        PBEParameterSpec ps = new PBEParameterSpec(salt, 1000);
        cp.init(Cipher.DECRYPT_MODE, k, ps);
        byte[] ptext = cp.doFinal(bytesData);
        return new String(ptext, "utf-8");
    };
}