package org.dev.toos.dbmapping.part;
import org.dev.toos.dbmapping.policies.ConnectionEditPolicy;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
/**
 * 
 * @version : 2013-3-5
 * @author ������ (zyc@byshell.org)
 */
public class ConnectionPart extends AbstractDBMappingGraphicalEditPart {
    protected IFigure createFigure() {
        /*����һ��������*/
        PolylineConnection conn = new PolylineConnection();
        /*��ԭ�㿪ʼ��һ�����������Ρ�*/
        PolygonDecoration polygon = new PolygonDecoration();
        polygon.setFill(false);
        //polygon.setTemplate(PolygonDecoration.INVERTED_TRIANGLE_TIP);
        conn.setSourceDecoration(polygon);
        /*��Ŀ��㻭һ��ʵ�������Ρ�*/
        conn.setTargetDecoration(new PolygonDecoration());
        /*BendpointConnectionRouter������ȷ������������һ��������·����*/
        conn.setConnectionRouter(new BendpointConnectionRouter());
        return conn;
    }
    protected void createEditPolicies() {
        //        /**����ѡ��ı�������ʼ�˵���ԡ�*/
        //        installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new ConnectionBendpointEditPolicy((Connection) this.getModel()));
        /**����ѡ��ı�������ֹ�˵���ԡ�*/
        installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
        /**�������ӱ�ɾ������*/
        installEditPolicy(EditPolicy.CONNECTION_ROLE, new ConnectionEditPolicy());
    }
    @Override
    public void setSelected(int value) {
        super.setSelected(value);
        if (value != EditPart.SELECTED_NONE)
            ((PolylineConnection) getFigure()).setLineWidth(2);
        else
            ((PolylineConnection) getFigure()).setLineWidth(1);
    }
}