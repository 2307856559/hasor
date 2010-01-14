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
package org.more.beans.resource.xml;
import java.util.Map;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
/**
 * �ýӿ������ڱ�ʾһ��XML�������񣬲���beans��ִ��xpath�ȵ�������DoEventIteration�ӿڵ�ʵ���ࡣ
 * @version 2009-11-24
 * @author ������ (zyc@byshell.org)
 */
public interface TaskProcess extends XMLStreamConstants {
    /**
     * xml��������ʹ�õ���stax������ʽ����ϵͳ�Ķ���һ���¼�ʱ������һ��onEvent�������ã�eventType��ֵ��������ʲô����ʲô�¼���
     * eventTypeֵ����XMLStreamConstants�ӿڶ���ĸýӿ���jdk1.6�Դ���һ���ӿ�(���ӿ��Ѿ��̳�������ӿ�)��
     * ������ʹ�ö�ջ��ʽ����ʾÿһ��Ԫ�ؽڵ㣬ֻ�н����ǩ���뿪��ǩʱ�Ż�������ջ�䶯�����¼�����Ӱ���ջ�䶯��
     * ������ڽ�����ǩʱ������ջ���������ݣ�ע���ջ�㼶˳���ǵ���ġ�
     * @param elementStack ��ǰ�¼�������ջ��
     * @param onXPath ��ǰ�¼��������ڵ�xpath·����
     * @param eventType �¼����͡�
     * @param reader �Ķ�����
     * @param tagProcessMap �Ѿ�ע��ı�ǩ���������ϡ�
     * @throws Exception ����ڽ����ڼ䷢���쳣
     */
    public void onEvent(XmlContextStack elementStack, String onXPath, int eventType, XMLStreamReader reader, Map<String, TagProcess> tagProcessMap) throws XMLStreamException, Exception;
    /**��ȡ����ֵ��*/
    public Object getResult();
    /**���ò������ö���*/
    public void setConfig(Object[] params);
}