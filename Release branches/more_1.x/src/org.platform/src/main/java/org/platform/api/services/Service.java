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
package org.platform.api.services;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.platform.api.safety.Power;
/**
 * ��������Ϊһ�������࣬�������ʵ��{@link IService}�ӿ��Եõ���������֧�֡�
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Service {
    /**�������ƻ�ID����ͬһ���������п������ö����ͬ�����ơ�*/
    public String[] value();
    /**�����������������������Ŀ���ǷָͬĿ�Ļ�ϵͳ�ķ����ڲ�ͬ���������·����������ظ���*/
    public String scope() default "";
    /**����ĳ�ʼ���Ƿ�����װ�أ�Ĭ��true�����ڣ���
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public boolean lazyInit() default true;
    /**���ڱ�ʾ��������Ƿ�Ϊ����ģʽ��*/
    public boolean singlet() default true;
    /**ǿ�Ʒ������������Ҫ����������������֮�������������������˸�������lazyInit���������ÿ��ܻ��ܵ�Ӱ�졣ͬʱ�����������ֹͣ������÷���Ҳ���յ�ֹͣ���е��źš�*/
    public Class<?>[] startWith() default {};
    /**�Է����������Ϣ��
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public String description() default "";
    /**�ڹ������̨��ʾ����ʱʹ��displayName���ԡ�
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public String displayName() default "";
    /**�����ʹ����Ȩ��Χ��Ĭ��ֵ��{@link Access#Private}��
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public Access access() default Access.Private;
    /** ���������������
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public InitParam[] initParams() default {};
    /**
     * ������Χö�٣����ϵͳ�����˶��������ʲ��������ͨ������ͨ·ֱ�ӷ��ʵ��÷���
     * @version : 2013-3-12
     * @author ������ (zyc@byshell.org)
     */
    public static enum Access {
        /**��ȫ������*/
        Public,
        /**
         * ��Ҫͨ����֤֮��ſ���ʹ�á�������Ƶ��ض���Ȩ�޵�������ʹ��{@link Power}ע��������á�
         * @see org.platform.faces.safety.Power
         */
        Protected,
        /**ֻ��Ӧ�ó����ڲ�ʹ�ã����������⹫����*/
        Private,
    }
}