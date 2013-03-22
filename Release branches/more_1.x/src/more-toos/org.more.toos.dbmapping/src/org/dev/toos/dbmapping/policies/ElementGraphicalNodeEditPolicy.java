package org.dev.toos.dbmapping.policies;
import org.dev.toos.dbmapping.commands.ConnectionReconnect;
import org.dev.toos.dbmapping.commands.CreateConnection;
import org.dev.toos.dbmapping.model.Connection;
import org.dev.toos.dbmapping.model.Element;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
/**
 * 
 * @version : 2013-3-13
 * @author ������ (zyc@byshell.org)
 */
public class ElementGraphicalNodeEditPolicy extends GraphicalNodeEditPolicy {
    @Override
    protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
        /*�������Ӵ�Ԫ���ϴ���ʱ��*/
        Element source = (Element) getHost().getModel();
        int style = ((Integer) request.getNewObjectType()).intValue();
        CreateConnection cmd = new CreateConnection(source, null, style);
        request.setStartCommand(cmd);//����������getConnectionCompleteCommandʱ��ʹ�á�
        return cmd;
    }
    @Override
    protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
        /*�����Ӵ�������ʱ�����ӵ�Ŀ������ϣ���*/
        CreateConnection cmd = (CreateConnection) request.getStartCommand();
        cmd.setTarget((Element) getHost().getModel());
        return cmd;
    }
    @Override
    protected Command getReconnectTargetCommand(ReconnectRequest request) {
        /*������������Ԫ��ʱ�Ĵ���*/
        Connection conn = (Connection) request.getConnectionEditPart().getModel();
        Element newTarget = (Element) getHost().getModel();
        return new ConnectionReconnect(null, newTarget, conn);
    }
    @Override
    protected Command getReconnectSourceCommand(ReconnectRequest request) {
        /*�����������Ӹ�Ԫ������ʱ�Ĵ���*/
        Connection conn = (Connection) request.getConnectionEditPart().getModel();
        Element newSource = (Element) getHost().getModel();
        return new ConnectionReconnect(newSource, null, conn);
    }
}
