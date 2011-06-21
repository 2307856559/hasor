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
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.more.core.copybean.CopyBeanUtil;
import org.more.util.StringConvertUtil;
import org.more.web.page.PageInfo;
/**
 * 
 * @version 2009-6-19
 * @author ������ (zyc@byshell.org)
 */
public class AutoPageInfo extends PageInfo {
    //����5����������Ϊ��ҳ��ÿ��������������
    private String              url      = "";                           //����URL
    private int                 start    = 0;                            //ҳ��ʼ���
    private int                 step     = 1;                            //ҳ��������_ҳ��С
    private int                 count    = 10;                           //�ܹ���¼����
    private String              prefix   = "autoPage";                   //
    //
    private Map<String, String> paramMap = new HashMap<String, String>();
    private HttpServletRequest  request  = null;                         //ûʲôʵ���ô������Զ��ڴ�request����ȡ���ԵĲ����Ѿ�������requestʱ���С�
    //============================================
    public AutoPageInfo() {}
    public AutoPageInfo(HttpServletRequest request) {
        this.fromParamRequest(request);
    }
    //============================================
    /** ���³�ʼ����ҳ���� */
    public void initData() {
        //
        if (this.count > 0 && this.step == 0)
            this.step = 1;
        else if (this.count < 0 && this.step == 0)
            this.step = -1;
        this.prefix = (this.prefix == null || this.prefix.equals("")) ? "autoPage" : this.prefix;
        this.url = (this.url == null || this.url.equals("") == true) ? request.getRequestURI() : this.url;//��ȡ��ǰҳ��URL����ָ����URL
        //����ת��URL��ĩβ����
        if (this.url.indexOf("?") != -1) {
            //�����ʺţ������ж����һ���ַ��Ƿ�Ϊ&����
            if (this.url.charAt(this.url.length()) != '&')
                this.url = this.url + "&";
        } else
            //�������ʺ�
            this.url = this.url + "?";
        //=====���������ʷ����׼����������=====
        this.removeAll();
        paramMap.put(this.prefix + "_start", String.valueOf(this.start));
        paramMap.put(this.prefix + "_step", String.valueOf(this.step));
        paramMap.put(this.prefix + "_count", String.valueOf(this.count));
        paramMap.put(this.prefix + "_prefix", String.valueOf(this.prefix));
        paramMap.remove(this.prefix + "_current");
        for (String k : paramMap.keySet())
            this.url += k + "=" + paramMap.get(k) + "&";
        //=====������Ŀ=====
        for (int i = this.start; i <= this.count; i = i + this.step) {
            String u = url + this.prefix + "_current=" + i;
            AutoPageInfoBean apBean = new AutoPageInfoBean();
            CopyBeanUtil.newInstance().copy(this, apBean);
            apBean.setUrl(u);//����URL
            apBean.setNumber(i);//����ҳ��
            this.addItem(apBean);
        }
    }
    /** �����µ�request��������ʱʹ�ø÷�����ȡrequest�е����� */
    private void fromParamRequest(HttpServletRequest request) {
        if (request == null)
            return;
        //=====��ȡ��ҳ��Ϣ����=====
        paramMap = new HashMap<String, String>();
        CopyBeanUtil.newInstance().copy(request, paramMap, "ref");
        //this.prefix + "_start"
        //this.prefix + "_step"
        //this.prefix + "_count"
        //this.prefix + "_prefix"
        //this.prefix + "_current"
        this.start = StringConvertUtil.parseInt(paramMap.get(this.prefix + "_start"), this.start);
        this.step = StringConvertUtil.parseInt(paramMap.get(this.prefix + "_step"), this.step);
        this.count = StringConvertUtil.parseInt(paramMap.get(this.prefix + "_count"), this.count);
        this.prefix = paramMap.get(this.prefix + "_prefix");
        this.url = paramMap.get(this.prefix + "_url");
    }
    //============================================
    public void setUrl(String url) {
        this.url = url;
    }
    public void setStep(int step) {
        this.step = step;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public void setStart(int start) {
        this.start = start;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public void setRequest(HttpServletRequest request) {
        this.request = request;
        this.fromParamRequest(request);
    }
    //============================================
    public HttpServletRequest getRequest() {
        return this.request;
    }
    public String getUrl() {
        return url;
    }
    public int getStep() {
        return step;
    }
    public int getCount() {
        return count;
    }
    public int getStart() {
        return start;
    }
    public String getPrefix() {
        return prefix;
    }
}