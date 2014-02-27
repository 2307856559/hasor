package org.dev.toos.dbmapping.part;
import org.dev.toos.dbmapping.model.Connection;
import org.dev.toos.dbmapping.model.Diagram;
import org.dev.toos.dbmapping.model.Element;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
/**
 * 
 * @version : 2013-3-14
 * @author ������ (zyc@byshell.org)
 */
public class PartFactory implements EditPartFactory {
    @Override
    public EditPart createEditPart(EditPart context, Object modelElement) {
        /*����EditPart*/
        EditPart part = getPartForElement(modelElement);
        /*����ģ�Ͷ���*/
        part.setModel(modelElement);
        return part;
    }
    /**����Part*/
    private EditPart getPartForElement(Object modelElement) {
        if (modelElement instanceof Diagram)
            return new DiagramPart();
        if (modelElement instanceof Element)
            return new ElementPart();
        if (modelElement instanceof Connection)
            return new ConnectionPart();
        throw new RuntimeException("Can't create part for model element: " + ((modelElement != null) ? modelElement.getClass().getName() : "null"));
    }
}