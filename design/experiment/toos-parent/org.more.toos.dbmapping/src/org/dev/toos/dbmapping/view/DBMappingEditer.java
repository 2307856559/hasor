package org.dev.toos.dbmapping.view;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.dev.toos.dbmapping.model.Diagram;
import org.dev.toos.dbmapping.part.PartFactory;
import org.dev.toos.dbmapping.tools.PaletteFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.parts.GraphicalEditorWithPalette;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
/**
 * 
 * @version : 2013-3-5
 * @author ������ (zyc@byshell.org)
 */
public class DBMappingEditer extends GraphicalEditorWithPalette {
    private PaletteRoot paletteRoot = null;
    private Diagram     diagram     = new Diagram();
    //
    //
    public DBMappingEditer() {
        /*DefaultEditDomain�������ڹ�������ջ*/
        setEditDomain(new DefaultEditDomain(this));
    }
    private Diagram getModel() {
        return diagram;
    }
    @Override
    protected PaletteRoot getPaletteRoot() {
        if (paletteRoot == null)
            /*��ͼ�幤����*/
            this.paletteRoot = PaletteFactory.createToolBars();
        return this.paletteRoot;
    }
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        /*��������Part�Ĺ����ࡣ*/
        viewer.setEditPartFactory(new PartFactory());
        /*����RootEditPart��ͨ����չRootEditPart����ʵ����Ӹ��ֲ㡣*/
        viewer.setRootEditPart(new ScalableFreeformRootEditPart());
        /**/
        //viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
        //������ͼ�ϵĲ˵�
        //        ContextMenuProvider cmProvider = new EditorContextMenuProvider(viewer, getActionRegistry());
        //        viewer.setContextMenu(cmProvider);
        //        getSite().registerContextMenu(cmProvider, viewer);
    }
    @Override
    protected void initializeGraphicalViewer() {
        /**����*/
        getGraphicalViewer().setContents(this.getModel());
        /**�����϶�������������ʵ���϶�Ч��*/
        //        getGraphicalViewer().addDropTargetListener(new DiagramTemplateTransferDropTargetListener(getGraphicalViewer()));
    }
    @Override
    public void doSave(IProgressMonitor monitor) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            //
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(getModel());
            oos.close();
            //
            IFile file = ((IFileEditorInput) getEditorInput()).getFile();
            file.setContents(new ByteArrayInputStream(out.toByteArray()), true, // keep saving, even if IFile is out of sync with the Workspace
                    false, // dont keep history
                    monitor); // progress monitor
            getCommandStack().markSaveLocation();
        } catch (CoreException ce) {
            ce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    protected void setInput(IEditorInput input) {
        super.setInput(input);
        try {
            IFile file = ((IFileEditorInput) input).getFile();
            ObjectInputStream in = new ObjectInputStream(file.getContents());
            diagram = (Diagram) in.readObject();
            in.close();
            setPartName(file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}