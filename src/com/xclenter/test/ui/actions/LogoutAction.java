package com.xclenter.test.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.xclenter.test.dao.CallResult;
import com.xclenter.test.dao.LoginDao;
import com.xclenter.test.util.action.ExamAuth;
import com.xclenter.test.util.action.LoginAuth;

/**
 * Our sample action implements workbench action delegate. The action proxy will
 * be created by the workbench and shown in the UI. When the user tries to use
 * the action, this delegate will be created and execution will be delegated to
 * it.
 * 
 * @see IWorkbenchWindowActionDelegate
 */
public class LogoutAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	private LoginDao loginDao;

	/**
	 * The constructor.
	 */
	public LogoutAction() {
		loginDao = LoginDao.getLoginDao();
	}

	/**
	 * The action has been activated. The argument of the method represents the
	 * 'real' action sitting in the workbench UI.
	 * 
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		if (!LoginAuth.isLogin()) {
			MessageBox messagebox = new MessageBox(window.getShell(),
					SWT.ICON_INFORMATION);
			messagebox
					.setMessage(" you have logged out.  no need to do it again");
			messagebox.open();
		} else {
			String username = LoginAuth.getUsername();
			CallResult logoutResult = loginDao.logout();

			MessageBox messagebox = new MessageBox(window.getShell(),
					SWT.ICON_INFORMATION);
			if (logoutResult.getState()) {
				/*
				 * ÔÚµÇ³öµÄÊ±ºò ×¢Ïú¿¼ÊÔ×´Ì¬ºÍµÇÂ½×´Ì¬
				 */
				LoginAuth.saveUser_key("");
				LoginAuth.changeLoginState(false, "");
				ExamAuth.getExamAuth().setCurrentExam(null);
				messagebox.setMessage(username
						+ " : you have logged out successfully. ");
			} else {
				messagebox.setMessage(username + " : you fail to log out . ("
						+ logoutResult.getMessage() + ")");
			}
			messagebox.open();
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