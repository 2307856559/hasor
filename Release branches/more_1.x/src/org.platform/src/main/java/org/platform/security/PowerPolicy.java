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
package org.platform.security;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
* ��ʾ����Ϊ�����û��Ƿ�߱�ĳ��Ȩ�޵Ĳ����࣬������Ҫʵ��{@link SecurityPolicy}�ӿڡ�
* ͨ��Ȩ�޲��Կ���Ӧ�ò�ͬ��Ȩ��ģ�͡�
* @version : 2013-3-25
* @author ������ (zyc@byshell.org)
*/
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface PowerPolicy {
    /**Ȩ�޼����Գ�ʼ��������
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public InitParam[] initParam() default {};
    /**PowerPolicy�ڲ������ϵ�˳��Ĭ�ϣ�0������Խ������Խ����
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public int sort() default 0;
    /**Ȩ�޼�����������û�ж��������ʱʹ��������Ϊ��������*/
    public String policyName();
    /**��Ҫ���ò��Եķ�������������ʽ��Ĭ�����á�.*����*/
    public String[] applyRegExp() default ".*";
}