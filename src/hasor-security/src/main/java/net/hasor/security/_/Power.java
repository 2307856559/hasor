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
package net.hasor.security._;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Ȩ�����ã��������õ��༶��ͷ��������ϡ�
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface Power {
    /**Ȩ�޵���룬���û����дȨ�޵�code��Ȩ�޼��Ҫ���������Ƿ��½��*/
    public String[] value() default "";
    /**Ȩ����֤�ȼ���*/
    public Level level() default Level.NeedLogin;
    /**������ʧ��ʱ���ص��쳣���ݡ�*/
    public String errorMsg() default "";
    /**
     * ��֤����ö��
     * @version : 2013-3-12
     * @author ������ (zyc@byshell.org)
     */
    public static enum Level {
        /**���ɷ��ʡ�Level 0*/
        Free,
        /**��Ҫ������½��Level 1*/
        NeedLogin,
        /**��Ҫ���Ȩ�޵㡣Level 2 */
        NeedAccess
    }
}