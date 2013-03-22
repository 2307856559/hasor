package org.dev.toos.ui.wizards;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
/**
 * ������ĿJavaProjectWizard
 * 
 * @version : 2013-3-14
 * @author ������ (zyc@byshell.org)
 */
public class MoreProjectWizard extends Wizard implements INewWizard {
    // protected MavenProjectWizardLocationPage locationPage; /*ѡ��λ��*/
    // protected MavenProjectWizardArchetypePage archetypePage; /*ģ��ѡ��*/
    // protected MavenProjectWizardArtifactPage artifactPage; /*��Ŀ����*/
    // protected MavenProjectWizardArchetypeParametersPage parametersPage;
    // /*��������*/
    protected DependentWizardPage      configProjectPage           = null; /* noe��Ŀ���� */
    protected SettingProjectWizardPage settingNewProjectWizardPage = null; /* noe��Ŀ���� */
    //
    //
    public MoreProjectWizard() {
        this.setWindowTitle("New Noe Project");
    }
    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        // TODO Auto-generated method stub
    }
    @Override
    public void addPages() {
        // MavenPlugin.getProjectConfigurationManager().createSimpleProject(arg0,
        // arg1, arg2, arg3, arg4, arg5);
        /* �����ѡ�������� */
        if (this.configProjectPage == null)
            this.configProjectPage = new DependentWizardPage();
        if (this.settingNewProjectWizardPage == null)
            this.settingNewProjectWizardPage = new SettingProjectWizardPage();
        addPage(this.configProjectPage);
        addPage(this.settingNewProjectWizardPage);
        /* ��������Maven��Ŀ����������Ϣ�� */
        // this.locationPage.setTitle("Noe Project Maven Settings.");
        // this.archetypePage.setTitle("Noe Project Maven Settings.");
        // this.parametersPage.setTitle("Noe Project Maven Settings.");
        // this.artifactPage.setTitle("Noe Project Maven Settings.");
        this.configProjectPage.setTitle("select project for dependent.");
        this.settingNewProjectWizardPage.setTitle("select project for dependent.");
    }
    @Override
    public boolean performFinish() {
        // TODO Auto-generated method stub
        return true;
    }
}