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
import org.platform.Assert;
/**
 * 
 * @version : 2013-4-20
 * @author ������ (zyc@byshell.org)
 */
class DefaultSecurityQuery implements SecurityQuery {
    protected SecurityNode testSecurityNode = new FixedValue(true);
    /*-------------------------------------------------------------------------------*/
    /*-�߼���-*/
    @Override
    public SecurityQuery and(String permissionCode) {
        Assert.isNotNull(permissionCode);
        return this.andCustomer(new CheckPermission(new Permission(permissionCode)));
    }
    @Override
    public SecurityQuery and(Permission permission) {
        Assert.isNotNull(permission);
        return this.andCustomer(new CheckPermission(permission));
    }
    @Override
    public SecurityQuery and(SecurityQuery securityQuery) {
        Assert.isNotNull(securityQuery);
        return this.andCustomer(securityQuery);
    }
    /*-�߼���-*/
    @Override
    public SecurityQuery or(String permissionCode) {
        Assert.isNotNull(permissionCode);
        return this.orCustomer(new CheckPermission(new Permission(permissionCode)));
    }
    @Override
    public SecurityQuery or(Permission permission) {
        Assert.isNotNull(permission);
        return this.orCustomer(new CheckPermission(permission));
    }
    @Override
    public SecurityQuery or(SecurityQuery securityQuery) {
        Assert.isNotNull(securityQuery);
        return this.orCustomer(securityQuery);
    }
    /*-�߼���-*/
    @Override
    public SecurityQuery not() {
        this.testSecurityNode = new CheckNot(this.testSecurityNode);
        return this;
    }
    @Override
    public SecurityQuery not(String permissionCode) {
        Assert.isNotNull(permissionCode);
        return this.notCustomer(new CheckPermission(new Permission(permissionCode)));
    }
    @Override
    public SecurityQuery not(Permission permission) {
        Assert.isNotNull(permission);
        return this.notCustomer(new CheckPermission(permission));
    }
    @Override
    public SecurityQuery not(SecurityQuery securityQuery) {
        Assert.isNotNull(securityQuery);
        return this.notCustomer(securityQuery);
    }
    /*-����-*/
    @Override
    public SecurityQuery andGuest() {
        return this.andCustomer(new CheckGuest());
    }
    @Override
    public SecurityQuery orGuest() {
        return this.orCustomer(new CheckGuest());
    }
    @Override
    public SecurityQuery notGuest() {
        return this.notCustomer(new CheckGuest());
    }
    @Override
    public SecurityQuery andLogin() {
        return this.andCustomer(new CheckLogin());
    }
    @Override
    public SecurityQuery orLogin() {
        return this.orCustomer(new CheckLogin());
    }
    @Override
    public SecurityQuery andLogout() {
        return this.andCustomer(new CheckNot(new CheckLogin()));
    }
    @Override
    public SecurityQuery orLogout() {
        return this.orCustomer(new CheckNot(new CheckLogin()));
    }
    @Override
    public SecurityQuery andCustomer(SecurityNode customerTest) {
        Assert.isNotNull(customerTest);
        this.testSecurityNode = new CheckAnd(this.testSecurityNode, customerTest);
        return this;
    }
    @Override
    public SecurityQuery orCustomer(SecurityNode customerTest) {
        Assert.isNotNull(customerTest);
        this.testSecurityNode = new CheckOr(this.testSecurityNode, customerTest);
        return this;
    }
    @Override
    public SecurityQuery notCustomer(SecurityNode customerTest) {
        Assert.isNotNull(customerTest);
        return this.andCustomer(new CheckNot(customerTest));
    }
    @Override
    public String toString() {
        return "SecurityQuery =" + this.testSecurityNode.toString();
    }
    /*-------------------------------------------------------------------------------*/
    @Override
    public boolean testPermission(AuthSession authSession) {
        return this.testSecurityNode.testPermission(authSession);
    }
    /*-------------------------------------------------------------------------------*/
    /** ���� */
    private static abstract class AbstractCheckResolver implements SecurityNode {
        private SecurityNode prev = null;
        private SecurityNode next = null;
        public AbstractCheckResolver(SecurityNode prev, SecurityNode next) {
            this.prev = prev;
            this.next = next;
        }
        /**��ȡ��һ���ڵ㡣*/
        public SecurityNode getPrev() {
            return this.prev;
        }
        /**����Ȩ��ֵ��*/
        public abstract boolean testPermission(AuthSession authSession);
        /**��ȡ��һ���ڵ㡣*/
        public SecurityNode getNext() {
            return this.next;
        }
        @Override
        public abstract String toString();
    }
    /** ����Ȩ���ж����߼����롱�Ĵ��� */
    private static class CheckAnd extends AbstractCheckResolver {
        public CheckAnd(SecurityNode prev, SecurityNode next) {
            super(prev, next);
        }
        @Override
        public boolean testPermission(AuthSession authSession) {
            boolean prevPerm = this.getPrev().testPermission(authSession);
            boolean nextPerm = this.getNext().testPermission(authSession);
            return prevPerm == true && nextPerm == true;
        }
        @Override
        public String toString() {
            return "(" + this.getPrev() + " and " + this.getNext() + ")";
        }
    }
    /** ����Ȩ���ж����߼����򡱵Ĵ��� */
    private static class CheckOr extends AbstractCheckResolver {
        public CheckOr(SecurityNode prev, SecurityNode next) {
            super(prev, next);
        }
        @Override
        public boolean testPermission(AuthSession authSession) {
            boolean prevPerm = this.getPrev().testPermission(authSession);
            boolean nextPerm = this.getNext().testPermission(authSession);
            return prevPerm == true || nextPerm == true;
        }
        @Override
        public String toString() {
            return "(" + this.getPrev() + " or " + this.getNext() + ")";
        }
    }
    /** ����Ȩ���ж����߼����ǡ��Ĵ��� */
    private static class CheckNot implements SecurityNode {
        private SecurityNode node = null;
        public CheckNot(SecurityNode node) {
            this.node = node;
        }
        @Override
        public boolean testPermission(AuthSession authSession) {
            return !this.node.testPermission(authSession);
        }
        @Override
        public String toString() {
            return "!" + this.node;
        }
    }
    /** ��Ȩ�޵���ֵ����ʾ�û��Ƿ�߱���Ȩ�޵㡣 */
    private static class CheckPermission implements SecurityNode {
        private Permission permission = null;
        public CheckPermission(Permission permission) {
            this.permission = permission;
        }
        @Override
        public boolean testPermission(AuthSession authSession) {
            return authSession.hasPermission(this.permission);
        }
        @Override
        public String toString() {
            return "[" + this.permission.getPermissionCode() + "]";
        }
    }
    /** �����Ƿ��½ */
    private static class CheckLogin implements SecurityNode {
        @Override
        public boolean testPermission(AuthSession authSession) {
            return authSession.isLogin();
        }
        @Override
        public String toString() {
            return "[AuthSession.isLogin]";
        }
    }
    /** �����Ƿ��½ */
    private static class CheckGuest implements SecurityNode {
        @Override
        public boolean testPermission(AuthSession authSession) {
            return authSession.isGuest();
        }
        @Override
        public String toString() {
            return "[AuthSession.isGuest]";
        }
    }
    /** �̶�ֵ */
    private static class FixedValue implements SecurityNode {
        private boolean defaultValue = true;
        public FixedValue(boolean defaultValue) {
            this.defaultValue = defaultValue;
        }
        @Override
        public boolean testPermission(AuthSession authSession) {
            return this.defaultValue;
        }
        @Override
        public String toString() {
            return String.valueOf(this.defaultValue);
        }
    }
}