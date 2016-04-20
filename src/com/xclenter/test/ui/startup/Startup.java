package com.xclenter.test.ui.startup;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointListener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.xclenter.test.listener.debugAction.BreakPointListener;
import com.xclenter.test.listener.resource.MyResourceChangeListener;
import com.xclenter.test.listener.ui.WindowListener;
import com.xclenter.test.util.addlistener.developAction.AddDocumentListenerUtil;
import com.xclenter.test.util.addlistener.ui.UIAddListenerUtil;

public class Startup implements IStartup{

	@Override
	public void earlyStartup() {
		// TODO Auto-generated method stub
		//��ȡworkbench
		IWorkbench workbench = PlatformUI.getWorkbench();
		//���workbench�ļ�����
		//workbench.addWorkbenchListener(new WorkbenchListener());
		workbench.addWindowListener(new WindowListener());//�����´򿪵�window �����ڶ�Ӧ��λ����ע�������
		//��Ĭ�ϵ�Workbench��ӵļ�����
        IWorkbenchWindow[] workbenchWindowArray = workbench.getWorkbenchWindows();
        
        for(IWorkbenchWindow workbenchWindow : workbenchWindowArray){
        	UIAddListenerUtil.addWindowListener(workbenchWindow);
        	IEditorPart editorpart = workbenchWindow.getActivePage().getActiveEditor();
        	if(editorpart != null){        		
        		AddDocumentListenerUtil.addDocumentListener(editorpart);
        	}
        }
        
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        workspace.addResourceChangeListener(new MyResourceChangeListener());
        
        DebugPlugin.getDefault().getBreakpointManager().addBreakpointListener(new BreakPointListener());
	}

}
