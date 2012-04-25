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
package org.more.core.log;
/**
 * �������
 * @version : 2011-7-27
 * @author ������ (zyc@byshell.org)
 */
public abstract class Level {
    public static final Level Below   = new Below();
    public static final Level High    = new High();
    public static final Level Default = new Default();
    //
    /**����0*/
    public final static class Default extends Level {};
    /**����1*/
    public final static class High extends Level {};
    /**����2*/
    public final static class Below extends Level {};
    //
    /**��ȡ�������ơ�*/
    public String getName() {
        return this.getClass().getSimpleName();
    };
}