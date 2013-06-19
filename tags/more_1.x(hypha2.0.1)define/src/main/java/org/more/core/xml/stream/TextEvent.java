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
package org.more.core.xml.stream;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
/**
 *
 * @version 2010-9-8
 * @author ������ (zyc@byshell.org)
 */
public class TextEvent extends XmlStreamEvent {
    private Type type = null;
    public TextEvent(String xpath, XmlReader xmlReader, XMLStreamReader reader, Type type) {
        super(xpath, xmlReader, reader);
        this.type = type;
    }
    /**
    *
    * @version 2010-9-11
    * @author ������ (zyc@byshell.org)
    */
    public enum Type {
        /***/
        CDATA,
        /***/
        Chars,
        /***/
        Comment,
        /***/
        Space,
    }
    /**�����ǰ�¼���һ��CDATA�¼��򷵻�true��*/
    public boolean isCDATAEvent() {
        return this.type == Type.CDATA;
    };
    /**�����ǰ�¼���һ��Chars�¼��򷵻�true��*/
    public boolean isCharsEvent() {
        return this.type == Type.Chars;
    };
    /**�����ǰ�¼���һ��Space�¼��򷵻�true��*/
    public boolean isSpaceEvent() {
        return this.type == Type.Space;
    };
    /**�����ǰ�¼���һ��Comment�¼��򷵻�true��*/
    public boolean isCommentEvent() {
        return this.type == Type.Comment;
    };
    /** ������ָ�������пո���ɵ��ַ������¼����򷵻� true��*/
    public boolean isWhiteSpace() {
        return this.getReader().isWhiteSpace();
    }
    /**���ַ�������ʽ����ȥ��ǰ��ո�ͻس���getText()ֵ�� */
    public String getTrimText() {
        String value = getText();
        if (value != null)
            return value.trim();
        else
            return null;
    }
    /**���ַ�������ʽ���ؽ����¼��ĵ�ǰֵ���˷������� CHARACTERS �¼����ַ���ֵ������ COMMENT ��ֵ��CDATA �ڵ��ַ���ֵ��SPACE �¼����ַ���ֵ�� */
    public String getText() {
        return this.getReader().getText();
    };
    /**����һ���������¼����ַ������顣 */
    public char[] getTextCharacters() {
        return this.getReader().getTextCharacters();
    };
    /**��ȡ�� CHARACTERS��SPACE �� CDATA �¼��������ı��� */
    public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) throws XMLStreamException {
        return this.getReader().getTextCharacters(sourceStart, target, targetStart, length);
    };
    /**�����ı��ַ������д��ı��¼����ַ����г��ȡ�*/
    public int getTextLength() {
        return this.getReader().getTextLength();
    };
    /** ���ش洢�����ı��¼��ģ���һ���ַ�λ�ô����ı��ַ������ƫ������ */
    public int getTextStart() {
        return this.getReader().getTextStart();
    }
    /**���¼����ĵ������Լ���*/
    public boolean isPartner(XmlStreamEvent e) {
        if (e instanceof TextEvent)
            return true;
        else
            return false;
    };
    /**�ı��¼�������ı�������Comment�����ǹ����¼�������Ϊ˽���¼���*/
    public boolean isPublicEvent() {
        if (this.isCommentEvent() == true)
            return true;
        else
            return false;
    }
}