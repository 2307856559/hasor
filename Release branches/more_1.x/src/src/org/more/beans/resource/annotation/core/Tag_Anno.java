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
package org.more.beans.resource.annotation.core;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLStreamReader;
import org.more.beans.resource.annotation.util.AnnoEngine;
import org.more.beans.resource.annotation.util.PackageUtil;
import org.more.beans.resource.annotation.util.PackageUtilExclude;
import org.more.beans.resource.xml.TagProcess;
import org.more.beans.resource.xml.XmlContextStack;
import org.more.util.StringConvert;
/**
* ����ɨ������bean�������ơ�
* @version 2010-1-10
* @author ������ (zyc@byshell.org)
*/
public class Tag_Anno extends TagProcess {
    private boolean                 xml_enable  = true; //�����ļ������Ƿ�������״̬��
    private String                  xml_package = ".*"; //
    private boolean                 isLock      = false; //ֻ��isLock�����Ϊ�����������Ѿ�ִ�й���ʼ�����̡�����������Ч��
    private HashMap<String, String> names;              //���ڱ���ɨ�赽��bean����������
    private LinkedList<String>      initNames;          //���ڱ���ɨ�赽��bean����������
    //=========================================================================================Job
    public void lockScan() {
        this.isLock = true;
    }
    public void unLockScan() {
        this.isLock = false;
    }
    @Override
    public void doStartEvent(String xPath, XMLStreamReader xmlReader, XmlContextStack context) throws IOException {
        /*ֻ��isLock�����Ϊ�����������Ѿ�ִ�й���ʼ�����̡�����������Ч-------*/
        if (isLock == true && this.names != null)
            return;
        /*�ж��Ƿ��ǵ�ǰ������Դ���ı�ǩ-----------------------------------*/
        String tagPrefix = context.getTagPrefix();
        String tagName = context.getTagName();
        if ("anno".equals(tagPrefix) == false || "anno".equals(tagName) == false)
            return;
        /*��ȡ����anno��ǩ������--------------------------------------------*/
        int attCount = xmlReader.getAttributeCount();
        for (int i = 0; i < attCount; i++) {
            String key = xmlReader.getAttributeLocalName(i);
            if (key.equals("enable") == true)
                xml_enable = StringConvert.parseBoolean(xmlReader.getAttributeValue(i), true);
            else if (key.equals("package") == true) {
                xml_package = xmlReader.getAttributeValue(i);
                xml_package = (xml_package == null) ? "*" : xml_package.replace(",", ")|(");
                //�滻�ַ���ʵ��֧�� ��*������?��ͨ�����
                xml_package = xml_package.replace(".", "\\.");
                xml_package = xml_package.replace("*", ".*");
                xml_package = xml_package.replace("?", ".");
                xml_package = "(" + xml_package + ")";
            }
        }
        if (xml_enable == false)
            return;
        /*ɨ������----------------------------------------------------------*/
        this.names = new HashMap<String, String>();//��ʼ��names����
        this.initNames = new LinkedList<String>();
        //ȡ�����д��ڰ���·���µ����ļ��Լ���Դ�ļ����б�
        PackageUtil util = new PackageUtil();
        final String _package = xml_package;
        LinkedList<String> classNames = util.scanClassPath(new PackageUtilExclude() {
            @Override
            public boolean exclude(String name) {
                return !name.matches(_package);
            }
        });
        //ɨ��ÿһ����ѡ�е���Դ
        AnnoEngine ae = new AnnoEngine();
        Scan_ClassName scanName = new Scan_ClassName();
        for (String className : classNames) {
            try {
                scanName.reset();
                ae.runTask(Class.forName(className), scanName, null);
                if (scanName.isBean() == true) {
                    this.names.put(scanName.getBeanName(), className);
                    if (scanName.isInit() == false)
                        this.initNames.add(scanName.getBeanName());
                }
            } catch (Exception e) {}
        }
    }
    public Map<String, String> getScanBeansResult() {
        return this.names;
    }
    public void destroy() {
        this.unLockScan();
        if (this.names != null)
            this.names.clear();
        this.names = null;
        if (this.initNames != null)
            this.initNames.clear();
        this.initNames = null;
    }
    public List<String> getScanInitBeansResult() {
        return initNames;
    }
}