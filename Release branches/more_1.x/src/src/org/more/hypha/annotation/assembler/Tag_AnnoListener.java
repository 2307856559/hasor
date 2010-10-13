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
package org.more.hypha.annotation.assembler;
import org.more.hypha.Event;
import org.more.hypha.EventListener;
/**
 * ����{@link Tag_AnnoListener}����
 * @version 2010-10-13
 * @author ������ (zyc@byshell.org)
 */
public class Tag_AnnoListener implements EventListener {
    private String packageText = null;
    /**����{@link Tag_AnnoListener}����*/
    public Tag_AnnoListener(String packageText) {
        this.packageText = packageText;
    }
    /**����ע�������*/
    public void onEvent(Event event) {
        System.out.println("start ANNO \t" + this.packageText);
        // TODO Auto-generated method stub
    }
}