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
package net.hasor.servlet.anno;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * ע��һ��web�����ڼ��׳��쳣�Ĵ���ӿڡ�
 * @version : 2013-3-20
 * @author ������ (zyc@hasor.net)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface WebError {
    //    /**�Է����������Ϣ��
    //     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    //    public String description() default "";
    //    /**�ڹ������̨��ʾ����ʱʹ��displayName���ԡ�
    //     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    //    public String displayName() default "";
    /**�ڵ������ϵ�˳��Ĭ�ϣ�0��������������
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public int sort() default 0;
    /** ���������������
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public WebInitParam[] initParams() default {};
    /**Ҫ������쳣���͡�*/
    public Class<? extends Throwable> value();
}