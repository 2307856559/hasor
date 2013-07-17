package org.dev.toos.dbmapping.commands;
import org.dev.toos.dbmapping.model.Element;
import org.eclipse.gef.commands.Command;
/**
 * ɾ������
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
public class DeleteElement extends Command {
    private Element targetElement = null;
    //
    //
    public DeleteElement(Element targetElement) {
        this.targetElement = targetElement;
    }
    //
    //
    public String getLabel() {
        return "Delete Element";
    }
    public void execute() {
        if (this.targetElement != null)
            this.targetElement.delete();
    }
}
