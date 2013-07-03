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
package org.hasor.security.support;
import org.hasor.security.RoleIdentity;
import org.hasor.security.RoleIdentityUtil;
import org.hasor.security.UserInfo;
/**
 * �����{@link UserInfo}�ӿڡ�
 * @version : 2013-5-3
 * @author ������ (zyc@byshell.org)
 */
public abstract class BaseUserInfo implements UserInfo {
    @Override
    public boolean isGuest() {
        return false;
    }
    @Override
    public RoleIdentity getIdentity() {
        return RoleIdentityUtil.getTypeIdentity(this.getClass());
    }
}