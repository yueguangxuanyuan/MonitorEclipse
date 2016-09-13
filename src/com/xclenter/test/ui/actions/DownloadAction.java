package com.xclenter.test.ui.actions;

import java.util.HashSet;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.xclenter.test.controller.DownloadController;
import com.xclenter.test.controller.TaskModel;
import com.xclenter.test.ui.dialog.TaskSelectDialog;
import com.xclenter.test.util.action.ActionAuth;

/**
 * Our sample action implements workbench action delegate. The action proxy will
 * be created by the workbench and shown in the UI. When the user tries to use
 * the action, this delegate will be created and execution will be delegated to
 * it.
 * 
 * @see IWorkbenchWindowActionDelegate
 */
public class DownloadAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
	
	/**
	 * The constructor.
	 */
	public DownloadAction() {
	
	}

	/**
	 * The action has been activated. The argument of the method represents the
	 * 'real' action sitting in the workbench UI.
	 * 
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		/*
		 * we should check  if they have taken one of task
		 */
		if(!ActionAuth.isLogin()){
			MessageBox messageBox = new MessageBox(window.getShell(),SWT.ICON_INFORMATION);
			messageBox.setMessage("Please Login first");
			messageBox.open();
		}else{
			List<TaskModel> taskModel = DownloadController.getAvailable_tasks();
			
			TaskSelectDialog taskSelectDialog = new TaskSelectDialog(window.getShell(), new HashSet<String>());
			
			taskSelectDialog.open();
		}
        
	}

	/**
	 * Selection in the workbench has been changed. We can change the state of
	 * the 'real' action here if we want, but this can only happen after the
	 * delegate has been created.
	 * 
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		
	}

	/**
	 * We can use this method to dispose of any system resources we previously
	 * allocated.
	 * 
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to be able to provide parent shell
	 * for the message dialog.
	 * 
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}