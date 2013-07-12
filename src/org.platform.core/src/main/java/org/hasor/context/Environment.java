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
package org.hasor.context;
import java.util.Map;
/**
 * ������������
 * @version : 2013-6-19
 * @author ������ (zyc@byshell.org)
 */
public interface Environment {
    /**��ȡ�����ռ�ӿ�*/
    public WorkSpace getWorkSpace();
    /**�����ַ��������ַ����ж���Ļ��������滻Ϊ��������ֵ�����������������ִ�Сд��<br/>
     * <font color="ff0000"><b>ע��</b></font>��ֻ�б��ٷֺŰ��������Ĳ��ֲű�������Ϊ������������
     * ����޷�����ĳ����������ƽ̨���׳�һ�����棬���ҽ�������������ͬ�ٷֺ���Ϊ��������ֵһ�𷵻ء�<br/>
     * <font color="00aa00"><b>����</b></font>�������ж����˱���Hasor_Home=C:/hasor��Java_Home=c:/app/java������Ľ������Ϊ
     * <div>%hasor_home%/tempDir/uploadfile.tmp&nbsp;&nbsp;--&gt;&nbsp;&nbsp;C:/hasor/tempDir/uploadfile.tmp</div>
     * <div>%JAVA_HOME%/bin/javac.exe&nbsp;&nbsp;--&gt;&nbsp;&nbsp;c:/app/java/bin/javac.exe</div>
     * <div>%work_home%/data/range.png&nbsp;&nbsp;--&gt;&nbsp;&nbsp;%work_home%/data/range.png��������һ������</div>
     * */
    public String evalString(String eval);
    /**���ݻ����������ƻ�ȡ����������ֵ����������ڸû��������Ķ����򷵻�null.*/
    public String getEnvVar(String varName);
    /**��ӻ�����������ӵĻ�������������Ӱ�쵽ϵͳ��������������ʹ���ڲ�Map���滷�������Ӷ�����Ӱ��JVM�������С�*/
    public void addEnvVar(String varName, String value);
    /**ɾ�������������÷������ڲ�Mapɾ��������Ļ�����������������Ŀ����Ϊ�˱���Ӱ��JVM�������С�*/
    public void remoteEnvVar(String varName);
    /**��ȡ����Ļ����������ϣ�Map��ʽ���ء�*/
    public Map<String, String> getEnv();
}