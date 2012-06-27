package org.more.webui.web;
import org.more.webui.context.FacesConfig;
import org.more.webui.context.FacesContextFactory;
import org.more.webui.lifestyle.Lifecycle;
import org.more.webui.lifestyle.phase.ApplyRequestValue_Phase;
import org.more.webui.lifestyle.phase.InitView_Phase;
import org.more.webui.lifestyle.phase.InvokeApplication_Phase;
import org.more.webui.lifestyle.phase.Render_Phase;
import org.more.webui.lifestyle.phase.RestoreView_Phase;
import org.more.webui.lifestyle.phase.UpdateModules_Phase;
import org.more.webui.lifestyle.phase.Validation_Phase;
/**
 * 
 * @version : 2012-6-27
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractWebUIFactory {
    /**�����������ڶ���*/
    public Lifecycle createLifestyle(FacesConfig config) {
        /*�����������ڶ���*/
        Lifecycle lifestyle = new Lifecycle() {};
        {
            //��1�׶Σ����ڳ�ʼ����ͼ�е����ģ������
            lifestyle.addPhase(new InitView_Phase());
            //��2�׶Σ�����UI���״̬��
            lifestyle.addPhase(new RestoreView_Phase());
            //��3�׶Σ������������Ҫ����������ֵ���뵽�����ϡ�
            lifestyle.addPhase(new ApplyRequestValue_Phase());
            //��4�׶Σ������ģ���е����ݽ�����֤��
            lifestyle.addPhase(new Validation_Phase());
            //��5�׶Σ������ģ���е�ֵ���õ�ӳ���bean�С�
            lifestyle.addPhase(new UpdateModules_Phase());
            //��6�׶Σ�����������Ϣ������Command��Click�¼����ȶ�����
            lifestyle.addPhase(new InvokeApplication_Phase());
            //��7�׶Σ���ִ�����UI��Ϣ��Ⱦ���ͻ����С�
            lifestyle.addPhase(new Render_Phase());
        }
        return lifestyle;
    }
    public FacesContextFactory createFacesContextFactory() {
        return new DefaultFacesContext();
    }
}
///*����������ڼ�����*/
//Set<Class<?>> classSet = config.getClassSet(UIPhase.class);
//for (Class<?> type : classSet)
//    if (PhaseListener.class.isAssignableFrom(type) == false)
//        throw new ClassCastException(type + " to PhaseListener");
//    else
//        lifestyle.addPhaseListener((PhaseListener) AppUtil.getObj(type));