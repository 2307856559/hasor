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
package org.platform.api.upfile;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * �ϴ��ļ��Ĵ����࣬��Ҫʵ��{@link IUpFile}�ӿڡ�
 * @see org.platform.upfile.IUpFile
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface UpLoadFile {
    /**�ϴ������ʼ��������
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public InitParam[] initParam() default {};
    /**�ϴ�����ע��ķ�������
     * �������Ϊ����ʹ����ļ��������Ϊ���ơ�*/
    public String[] upName();
    /**�ϴ��������������������ݳ��ȡ�Ĭ�ϣ�0(������)��
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public long maxSize() default 0;
    /**�ϴ������������С�������ݳ��ȡ�Ĭ�ϣ�0(������)��
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public long minSize() default 0;
    /**�Ƿ�����һ�������ж���ϴ�ʵ������
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public boolean allowMulti() default false;
    /**�ϴ�����������ļ����͡�
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public String[] allowFiles() default { "*.*" };
    /**�ϴ�������ԡ�Ĭ��:{@link AccessPolicy#Public}��
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public AccessPolicy policy() default AccessPolicy.Public;
    /**
     * ������Χö�٣����ϵͳ�����˶��������ʲ��������ͨ������ͨ·ֱ�ӷ��ʵ��÷���
     * @version : 2013-3-12
     * @author ������ (zyc@byshell.org)
     */
    public static enum AccessPolicy {
        /**��ȫ������*/
        Public,
        /**��Ҫ����{@link IUpFilePolicy}���Լ��顣*/
        Policy,
    }
}