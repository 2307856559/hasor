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
import org.more.util.StringUtil;
/**
 * ��URI����Ȩ���жϽӿڡ�
 * @version : 2013-4-9
 * @author ������ (zyc@byshell.org)
 */
enum UriPatternType {
    /**Ҫ���½*/
    Login,
    /**Ҫ��ǵ�½*/
    Logout,
    /**Ҫ������������*/
    Guest,
    /**Ҫ�����Ȩ�޵�*/
    Permission,
    /**���κ�Ҫ��*/
    None;
    //
    //
    public static UriPatternMatcher get(UriPatternType type, String requestURI, String patternCode) {
        if (StringUtil.isBlank(requestURI) == false)
            requestURI = requestURI.toLowerCase();
        switch (type) {
        case Login:
            return new Login_UriPatternMatcher(requestURI);
        case Logout:
            return new Logout_UriPatternMatcher(requestURI);
        case Guest:
            return new Guest_UriPatternMatcher(requestURI);
        case Permission:
            return new Permission_UriPatternMatcher(requestURI, patternCode);
        case None:
            return new None_UriPatternMatcher(requestURI);
        default:
            return null;
        }
    }
    //
    private static class None_UriPatternMatcher extends UriPatternMatcher {
        protected None_UriPatternMatcher(String requestURI) {
            super(requestURI);
        }
        @Override
        public boolean testPermission(AuthSession authSession) {
            return true;
        }
    }
    private static class Login_UriPatternMatcher extends UriPatternMatcher {
        protected Login_UriPatternMatcher(String requestURI) {
            super(requestURI);
        }
        @Override
        public boolean testPermission(AuthSession authSession) {
            if (authSession == null)
                return false;
            return authSession.isLogin();
        }
    }
    private static class Logout_UriPatternMatcher extends UriPatternMatcher {
        protected Logout_UriPatternMatcher(String requestURI) {
            super(requestURI);
        }
        @Override
        public boolean testPermission(AuthSession authSession) {
            if (authSession == null)
                return false;
            return !authSession.isLogin();
        }
    }
    private static class Guest_UriPatternMatcher extends UriPatternMatcher {
        protected Guest_UriPatternMatcher(String requestURI) {
            super(requestURI);
        }
        @Override
        public boolean testPermission(AuthSession authSession) {
            if (authSession == null)
                return false;
            return authSession.isGuest();
        }
    }
    private static class Permission_UriPatternMatcher extends UriPatternMatcher {
        private String patternCode = null;
        protected Permission_UriPatternMatcher(String requestURI, String patternCode) {
            super(requestURI);
            this.patternCode = patternCode;
        }
        @Override
        public boolean testPermission(AuthSession authSession) {
            if (authSession == null)
                return false;
            return authSession.hasPermission(this.patternCode);
        }
        public String toString() {
            return super.toString() + " on Permission=" + this.patternCode;
        }
    }
}