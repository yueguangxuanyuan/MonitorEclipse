package com.xclenter.test.ui.actions;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.jface.dialogs.MessageDialog;

import com.xclenter.test.controller.CallResult;
import com.xclenter.test.controller.LoginController;
import com.xclenter.test.ui.dialog.LoginDialog;
import com.xclenter.test.util.HttpCommon;
import com.xclenter.test.util.ServerInfo;
import com.xclenter.test.util.action.ActionAuth;
import com.xclenter.test.util.action.ActionUtil;
import com.xclenter.test.util.saveFile.SaveFileUtil;

/**
 * Our sample action implements workbench action delegate. The action proxy will
 * be created by the workbench and shown in the UI. When the user tries to use
 * the action, this delegate will be created and execution will be delegated to
 * it.
 * 
 * @see IWorkbenchWindowActionDelegate
 */
public class LoginAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	private LoginController loginDao;

	/**
	 * The constructor.
	 */
	public LoginAction() {
		loginDao = LoginController.getLoginDao();
	}

	/**
	 * The action has been activated. The argument of the method represents the
	 * 'real' action sitting in the workbench UI.
	 * 
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		if (ActionAuth.isLogin()) {
			MessageBox messagebox = new MessageBox(window.getShell(),
					SWT.ICON_INFORMATION);
			messagebox
					.setMessage(ActionAuth.getUsername()
							+ " . you have logged in. if you want to change user, you need to log out firstly");
			messagebox.open();
		} else {
			LoginDialog loginDialog = new LoginDialog(window.getShell()) {
				@Override
				protected void okPressed() {
					this.saveInput();
					String account = this.getUsername();
					String password = this.getPassword();

					CallResult login_result = loginDao.login(account, password);

					String message;
					if (login_result.getState()) {
						message = "welcome , Mr/Miss "
								+ ActionAuth.getUsername();
						if(login_result.getMessage() != null){
							message += "(" + login_result.getMessage() + ")";
						}
					} else {
						message = "something wrong please check inputvalue/network";
						message += "(" + login_result.getMessage() + ")";
					}
					MessageBox messagebox = new MessageBox(window.getShell(),
							SWT.ICON_INFORMATION);
					messagebox.setMessage(message);
					messagebox.open();
					if (login_result.getState()) {
						super.okPressed();
					}
				}
			};

			loginDialog.create();
			if (loginDialog.open() == Window.OK) {
				return;
			}
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