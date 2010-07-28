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
/**
 * session��������submitͨ�����������������session����ʵ���������չ���ṩ�������ڵĹ���
 * @version : 2010-7-27
 * @author ������(zyc@byshell.org)
 */
public class SimpleSessionManager implements SessionManager {
    private ThreadLocal<Session> sessionPool = new ThreadLocal<Session>();
    /**����һ��Session*/
    public Session createSession() {
        Session session = sessionPool.get();
        if (session == null) {
            session = new SessionImpl();
            this.sessionPool.set(session);
        }
        return session;
    }
    /**��ͼ�ָ�һ��ָ��id��session��*/
    public Session getSession(String sessionID) {
        Session session = sessionPool.get();
        if (session == null)
            session = this.createSession();
        return session;
    }
    /**����ĳ��session�Ƿ��������sessio��������*/
    public boolean isBelong(Session session) {
        return true;
    }
}