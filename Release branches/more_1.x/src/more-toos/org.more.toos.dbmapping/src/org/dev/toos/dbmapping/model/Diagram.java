package org.dev.toos.dbmapping.model;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * �����������ͼ
 * @version : 2013-3-8
 * @author ������ (zyc@byshell.org)
 */
public class Diagram extends AbstractModel {
    private static final long serialVersionUID = 6445402230441667429L;
    public static String      Prop_Element     = "Element";
    private List<Element>     elements         = new ArrayList<Element>(); //����Ԫ��
    //----------------------------------------------------------------------
    public Diagram() {}
    //----------------------------------------------------------------------
    public List<Element> getElements() {
        return Collections.unmodifiableList(elements);
    }
    /**�ӵ�ǰԪ�ش���һ����Ŀ��Ԫ�ص����ӡ�*/
    public Element createElement() {
        return new Element(this);
    }
    /*����Ԫ�ء�����create����������ʱ�����ø÷�����*/
    void addEmenemt(Element element) {
        this.elements.add(element);
    }
    /*����Ԫ�ء�����delete����������ʱ�����ø÷�����*/
    void removeElement(Element element) {
        if (elements.contains(element) == false)
            return;
        //
        this.elements.remove(element);
        element.getOutputList();
    }
    //
    //
    //
    //
    /**����ǰģ�Ͷ������л����档*/
    public InputStream getAsStream() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(os);
        out.writeObject(this);
        out.close();
        InputStream istream = new ByteArrayInputStream(os.toByteArray());
        os.close();
        return istream;
    }
    /**����������װ��ģ�Ͷ���*/
    public static Diagram makeFromStream(InputStream istream) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(istream);
        Diagram diagram = (Diagram) ois.readObject();
        ois.close();
        return diagram;
    }
}