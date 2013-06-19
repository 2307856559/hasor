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
package org.more.web.submit.scope;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.more.util.attribute.IAttribute;
import org.more.util.attribute.TransformToMap;
import org.more.web.submit.WebHelper;
/**
 * �����ṩCookie��{@link IAttribute IAttribute�ӿ�}�Ĵ���
 * @version 2009-12-28
 * @author ������ (zyc@byshell.org)
 */
public class CookieScope implements IAttribute<String> {
    public static final String Name   = "Cookie";
    private int                maxAge = 31536000; //365��
    //
    protected HttpServletRequest getHttpRequest() {
        return WebHelper.getHttpRequest();
    };
    protected HttpServletResponse getHttpResponse() {
        return WebHelper.getHttpResponse();
    };
    public void clearAttribute() {
        Cookie[] cs = this.getHttpRequest().getCookies();
        for (int i = 0; i < cs.length; i++) {
            Cookie c = cs[i];
            c.setMaxAge(0);
            this.getHttpResponse().addCookie(c);
        }
    };
    public boolean contains(String name) {
        Cookie[] cs = this.getHttpRequest().getCookies();
        for (int i = 0; i < cs.length; i++)
            if (cs[i].equals(name) == true)
                return true;
        return false;
    };
    public String getAttribute(String name) {
        Cookie[] cs = this.getHttpRequest().getCookies();
        if (cs != null)
            for (int i = 0; i < cs.length; i++)
                if (cs[i].equals(name) == true)
                    return cs[i].getValue();
        return null;
    };
    public String[] getAttributeNames() {
        Vector<String> v = new Vector<String>(0);
        Cookie[] cs = this.getHttpRequest().getCookies();
        for (int i = 0; i < cs.length; i++)
            v.add(cs[i].getName());
        String[] ns = new String[v.size()];
        v.toArray(ns);
        return ns;
    };
    public void removeAttribute(String name) {
        Cookie[] cs = this.getHttpRequest().getCookies();
        for (int i = 0; i < cs.length; i++)
            if (cs[i].equals(name) == true) {
                Cookie c = cs[i];
                c.setMaxAge(0);
                this.getHttpResponse().addCookie(c);
            }
    };
    public void setAttribute(String name, String value) {
        //Cookie��Χ
        Cookie c = new Cookie(name, value);
        c.setMaxAge(maxAge);//365��ʧЧ
        this.getHttpResponse().addCookie(c);
    };
    public void setCookieAttribute(String name, String value, int age) {
        Cookie c = new Cookie(name, value);
        c.setMaxAge(age);
        this.getHttpResponse().addCookie(c);
    };
    public void setCookieAttribute(Cookie cookie) {
        this.getHttpResponse().addCookie(cookie);
    };
    public Cookie getCookieAttribute(String name) {
        Cookie[] cs = this.getHttpRequest().getCookies();
        for (int i = 0; i < cs.length; i++)
            if (cs[i].equals(name) == true)
                return cs[i];
        return null;
    }
    public Map<String, String> toMap() {
        return new TransformToMap<String>(this);
    }
};