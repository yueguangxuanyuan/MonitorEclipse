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
		//获取workbench
		IWorkbench workbench = PlatformUI.getWorkbench();
		//添加workbench的监听器
		//workbench.addWorkbenchListener(new WorkbenchListener());
		workbench.addWindowListener(new WindowListener());//监听新打开的window 并且在对应的位置上注册监听器
		//给默认的Workbench添加的监听器
        IWorkbenchWindow[] workbenchWindowArray = workbench.getWorkbenchWindows();
        
        for(IWorkbenchWindow workbenchWindow : workbenchWindowArray){
        	UIAddListenerUtil.addWindowListener(workbenchWindow);
        	IEditorPart editorpart = workbenchWindow.getActivePage().getActiveEditor();
        	if(editorpart != null){        		
        		AddDocumentListenerUtil.addDocumentListener(editorpart);
        	}        	
        }
        
        /*
         * 添加资源监听器
         */
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        workspace.addResourceChangeListener(new ResourceChangeListener());
        
        SaveFileUtil.initSaveContext();
        
        /*
        * 监听可捕获的命令
        */
       
        ((ICommandService) workbench.getService(ICommandService.class)).addExecutionListener(new ExecutionListener());
        /*
         * 监听 run和debug
         */
        DebugPlugin.getDefault().addDebugEventListener(new DebugEventSetListener());
        
        ConsolePlugin.getDefault().getConsoleManager().addConsoleListener(new ConsoleListener());
        
	}

}
