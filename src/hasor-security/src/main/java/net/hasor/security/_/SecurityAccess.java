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
import java.util.List;
import net.hasor.core.AppContext;
import net.hasor.security.Permission;
import net.hasor.security.Token;
/**
 * ������Ȩ������ͨ���ýӿڿ��Խ�Ȩ�޿��ƿ�����ӵ���ͬ��Ȩ��ģ�͡�
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
public interface SecurityAccess {
    /**ͨ��userCode��½ϵͳ*/
    public Token getUserInfo(String userCode);
    /**ͨ���ʺ������½ϵͳ*/
    public Token getUserInfo(String account, String password);
    /**װ���û���Ȩ�ޡ�*/
    public List<Permission> loadPermission(Token userInfo);
}