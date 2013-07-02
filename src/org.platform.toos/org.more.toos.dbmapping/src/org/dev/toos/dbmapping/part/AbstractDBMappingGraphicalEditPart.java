package org.dev.toos.dbmapping.part;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.dev.toos.dbmapping.model.AbstractModel;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
/**
 * 
 * @version : 2013-3-14
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractDBMappingGraphicalEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener {
    public void activate() {
        if (!isActive()) {
            //ע�ᵽģ�͵����Լ������ϡ�
            AbstractModel modelObject = (AbstractModel) getModel();
            modelObject.addPropertyChangeListener(this);
        }
    }
    public void deactivate() {
        if (isActive()) {
            super.deactivate();
            //���ע�ᵽģ�͵ļ������ϡ�
            AbstractModel modelObject = (AbstractModel) getModel();
            modelObject.removePropertyChangeListener(this);
        }
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {}
}