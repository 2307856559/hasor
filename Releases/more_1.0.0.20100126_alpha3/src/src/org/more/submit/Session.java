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
package org.more.submit;
import java.io.Serializable;
import org.more.util.attribute.IAttribute;
/**
 * ʹ��Session�ӿڱ������ݿ��Ա�֤����һ��Action���󵽴�ʱ��Ȼ���Է��ʵ���ŵ�Session�е����ݡ�
 * @version 2009-11-27
 * @author ������ (zyc@byshell.org)
 */
public interface Session extends IAttribute, Serializable {
    /**
     * ��ȡSession��SessionManager�е�Ψһ��ʶ�š�
     * @return ����Session��SessionManager�е�Ψһ��ʶ�š�
     */
    public String getSessionID();
    /**
     * ��ô������Sessionʱ�Ĵ���ʱ�䡣
     * @return ���ش������Sessionʱ��ʱ�䡣
     */
    public long getCreateTime();
}