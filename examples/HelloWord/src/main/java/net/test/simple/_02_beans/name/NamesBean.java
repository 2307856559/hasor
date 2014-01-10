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
package net.test.simple._02_beans.name;
import net.hasor.plugins.bean.Bean;
import net.test.simple._02_beans.pojo.PojoBean;
/**
 * Ϊ�˼򵥣�NameBean �̳��� PojoBean���������� PojoBean ���������ʡ�
 * ��ͨ�� {@code @Bean} ע��Ϊ NameBean �������������ƽС�NameBean1���͡�NameBean2����
 * @version : 2014-1-3
 * @author ������(zyc@hasor.net)
 */
@Bean({ "NameBean1", "NameBean2" })
public class NamesBean extends PojoBean {}