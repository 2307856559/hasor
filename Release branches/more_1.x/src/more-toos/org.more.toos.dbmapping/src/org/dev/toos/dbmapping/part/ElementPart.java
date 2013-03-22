package org.dev.toos.dbmapping.part;
import java.beans.PropertyChangeEvent;
import org.dev.toos.dbmapping.figures.ElementFigure;
import org.dev.toos.dbmapping.model.Element;
import org.dev.toos.dbmapping.policies.ElementDirectEditPolicy;
import org.dev.toos.dbmapping.policies.ElementEditPolicy;
import org.dev.toos.dbmapping.policies.ElementGraphicalNodeEditPolicy;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
/**
 * 
 * @version : 2013-3-5
 * @author ������ (zyc@byshell.org)
 */
public class ElementPart extends AbstractDBMappingGraphicalEditPart {
    @Override
    protected IFigure createFigure() {
        IFigure f = new ElementFigure((Element) this.getModel());
        //  f.setOpaque(true); // non-transparent figure
        f.setBackgroundColor(ColorConstants.green);
        return f;
    }
    //    /**��ִ�и�����ʱ��ʹ��DirectEditManager*/
    //    protected DirectEditManager editManager;
    //    public void performRequest(Request req) {
    //        if (req.getType().equals(RequestConstants.REQ_DIRECT_EDIT)) {
    //            if (this.editManager == null) {
    //                ElementFigure figure = (ElementFigure) getFigure();
    //                this.editManager = new ElementDirectEditManager(this, ComboBoxCellEditor.class, new ElementComboBoxCellEditorLocator(figure));
    //            }
    //            this.editManager.show();
    //        }
    //    }
    /**���Ա������ģ��*/
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        /*ˢ������*/
        if (Element.Prop_Location.equals(prop))
            refreshVisuals();
        /*ˢ������*/
        else if (Element.Prop_Name.equals(prop))
            refreshVisuals();
        /*ˢ������������*/
        else if (Element.Prop_OutputConnection.equals(prop))
            refreshSourceConnections();
        /*ˢ�����������*/
        else if (Element.Prop_InputConnection.equals(prop))
            refreshTargetConnections();
    }
    @Override
    protected void createEditPolicies() {
        /*�������༭ģʽ����*/
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new ElementDirectEditPolicy());
        /*ģ�͵�ɾ������*/
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new ElementEditPolicy());
        /*ͼ�λ��ڵ����*/
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ElementGraphicalNodeEditPolicy());
    }
}