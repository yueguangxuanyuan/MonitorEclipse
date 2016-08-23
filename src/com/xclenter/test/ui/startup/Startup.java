package com.xclenter.test.ui.startup;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.console.ConsolePlugin;

import com.xclenter.test.common.addlistener.AddDocumentListenerUtil;
import com.xclenter.test.common.addlistener.UIAddListenerUtil;
import com.xclenter.test.listener.debugAction.BreakPointListener;
import com.xclenter.test.listener.debugAction.DebugEventSetListener;
import com.xclenter.test.listener.developAction.ExecutionListener;
import com.xclenter.test.listener.developAction.ResourceChangeListener;
import com.xclenter.test.listener.developAction.build.ConsoleListener;
import com.xclenter.test.listener.ui.WindowListener;
import com.xclenter.test.util.saveFile.SaveFileUtil;

public class Startup implements IStartup{

	@Override
	public void earlyStartup() {
		System.setProperty("openlog4j", "false");
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
        
        /*
         * �����Դ������
         */
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        workspace.addResourceChangeListener(new ResourceChangeListener());
        
        SaveFileUtil.initSaveContext();
        
        /*
        * �����ɲ��������
        */
       
        ((ICommandService) workbench.getService(ICommandService.class)).addExecutionListener(new ExecutionListener());
        /*
         * ���� run��debug
         */
        DebugPlugin.getDefault().addDebugEventListener(new DebugEventSetListener());
        
        ConsolePlugin.getDefault().getConsoleManager().addConsoleListener(new ConsoleListener());
        
	}

}
