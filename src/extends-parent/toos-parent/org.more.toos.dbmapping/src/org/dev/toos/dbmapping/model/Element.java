package org.dev.toos.dbmapping.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
/**
 * ����ڵ�Ԫ��
 * @version : 2013-3-8
 * @author ������ (zyc@byshell.org)
 */
public class Element extends AbstractModel {
    private static final long serialVersionUID      = -6193319926276663305L;
    public static String      Prop_OutputConnection = "OutputConnection";
    public static String      Prop_InputConnection  = "InputConnection";
    public static String      Prop_Name             = "Name";
    public static String      Prop_Location         = "Location";
    private Diagram           diagram               = null;
    private Point             location              = new Point(0, 0);            //ģ��λ��
    private Dimension         size                  = new Dimension(50, 50);      //ģ�ʹ�С
    private String            name                  = "New Element";
    private List<Connection>  outConnection         = new ArrayList<Connection>(); //�����ڵ�
    private List<Connection>  inConnection          = new ArrayList<Connection>(); //����ڵ�
    //----------------------------------------------------------------------
    protected Element(Diagram diagram) {
        this.diagram = diagram;
    }
    public Diagram getDiagram() {
        return diagram;
    }
    /**��ģ���д�����Ԫ�ء�*/
    public void create() {
        this.diagram.addEmenemt(this);
        for (Connection out : this.outConnection)
            out.connect();
        for (Connection in : this.inConnection)
            in.connect();
        this.diagram.fireStructureChange(Diagram.Prop_Element, this);
    }
    /**��ģ����ɾ����Ԫ�ء�*/
    public void delete() {
        for (Connection out : this.outConnection)
            out.disconnect();
        for (Connection in : this.inConnection)
            in.disconnect();
        this.diagram.removeElement(this);
        this.diagram.fireStructureChange(Diagram.Prop_Element, this);
    }
    /**�ӵ�ǰԪ�ش���һ����Ŀ��Ԫ�ص����ӡ�*/
    public Connection createConnection(Element targetElement) {
        return new Connection(this, targetElement);
    }
    /**��ȡ���������*/
    public List<Connection> getOutputList() {
        return Collections.unmodifiableList(outConnection);
    }
    /**��ȡ���������ӡ�*/
    public List<Connection> getInputList() {
        return Collections.unmodifiableList(inConnection);
    }
    /*�������ӡ��������connect����ʱ��ʹ�ø÷�����*/
    void addOutput(Connection connection) {
        this.outConnection.add(connection);
    }
    /*�������ӡ��������disconnect����ʱ��ʹ�ø÷�����*/
    void removeOutput(Connection connection) {
        this.outConnection.remove(connection);
    }
    /*�������ӡ��������connect����ʱ��ʹ�ø÷�����*/
    void addInput(Connection connection) {
        this.inConnection.add(connection);
    }
    /*�������ӡ��������disconnect����ʱ��ʹ�ø÷�����*/
    void removeInput(Connection connection) {
        this.inConnection.remove(connection);
    }
    //
    //
    //
    //
    public Point getLocation() {
        return location;
    }
    public void setLocation(Point location) {
        this.firePropertyChange(Prop_Location, this.location, location);
        this.location = location;
    }
    public Dimension getSize() {
        return size;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.firePropertyChange(Prop_Name, this.name, name);
        this.name = name;
    }
    //    /** 
    //     * Return a pictogram (small icon) describing this model element.
    //     * Children should override this method and return an appropriate Image.
    //     * @return a 16x16 Image or null
    //     */
    //    public abstract Image getIcon();
}