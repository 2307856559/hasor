package org.dev.toos.dbmapping.policies;
import org.dev.toos.dbmapping.commands.RenameElement;
import org.dev.toos.dbmapping.figures.ElementFigure;
import org.dev.toos.dbmapping.model.Element;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
/**
 * 
 * @version : 2013-3-13
 * @author ������ (zyc@byshell.org)
 */
public class ElementDirectEditPolicy extends DirectEditPolicy {
    @Override
    protected Command getDirectEditCommand(DirectEditRequest request) {
        /**����*/
        Element element = (Element) getHost().getModel();
        String newName = (String) request.getCellEditor().getValue();
        return new RenameElement(element, newName);
    }
    @Override
    protected void showCurrentEditValue(DirectEditRequest request) {
        //��Figure������ʾֵ(��ʵ���Բ��ã�ԭ����Figure����ʱ���Ѿ���������ʾֵ)
        String value = (String) request.getCellEditor().getValue();
        ((ElementFigure) getHostFigure()).setName(value);
    }
}