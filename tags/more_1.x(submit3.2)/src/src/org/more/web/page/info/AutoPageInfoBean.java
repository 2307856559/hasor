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
package org.more.web.page.info;
/**
 * 
 * @version 2009-6-19
 * @author ������ (zyc@byshell.org)
 */
public class AutoPageInfoBean {
    //����5��������Ҫ����AutoPageInfo���������ݱ���һ�µġ�
    private String url    = "";        //����URL
    private int    start  = 0;         //ҳ��ʼ���
    private int    step   = 1;         //ҳ��������_ҳ��С
    private int    count  = 10;        //�ܹ���¼����
    private int    number = 0;         //��ǰ��ҳ����
    private String prefix = "autoPage"; //����ǰ׺
    //============================================================
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public int getStart() {
        return start;
    }
    public void setStart(int start) {
        this.start = start;
    }
    public int getStep() {
        return step;
    }
    public void setStep(int step) {
        this.step = step;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public String getPrefix() {
        return prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
}
