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
package org.platform.services;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.google.inject.ScopeAnnotation;
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
    /**����ĳ�ʼ���Ƿ�����װ�أ�Ĭ��true�����ڣ���*/
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
    /** ���������������
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public InitParam[] initParams() default {};
    /**������������Ĭ��ֵ��{@link Level#Standard}��*/
    public Level level() default Level.Standard;
    /**
     * ������������
     * @version : 2013-3-12
     * @author ������ (zyc@byshell.org)
     */
    public static enum Level {
        /**������������������������ΪFramework��������Listener�����׶η���ͻᱻ������*/
        Framework,
        /**��׼���𣬸������ͨ�����״γ�ʼ��Filterʱ��������*/
        Standard,
        /**����Ʒ���𣬸��������ڳ�ʼ����Standard����֮����г�ʼ�������������һ���ص����������������������ʧ��ֻ����һ��������Ϣ֪ͨ������̨��*/
        Gift,
    }
}