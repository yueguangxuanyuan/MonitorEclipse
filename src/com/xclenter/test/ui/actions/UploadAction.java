package com.xclenter.test.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.jface.dialogs.MessageDialog;
import org.json.JSONArray;

import com.xclenter.test.dao.CallResult;
import com.xclenter.test.dao.TestDao;
import com.xclenter.test.dao.UploadDao;
import com.xclenter.test.ui.dialog.LoginDialog;
import com.xclenter.test.util.action.ActionUtil;
import com.xclenter.test.util.action.ExamAuth;
import com.xclenter.test.util.action.LoginAuth;
import com.xclenter.test.util.file.SaveFileUtil;

/**
 * Our sample action implements workbench action delegate. The action proxy will
 * be created by the workbench and shown in the UI. When the user tries to use
 * the action, this delegate will be created and execution will be delegated to
 * it.
 * 
 * @see IWorkbenchWindowActionDelegate
 */
public class UploadAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	/**
	 * The constructor.
	 */
	public UploadAction() {

	}

	/**
	 * The action has been activated. The argument of the method represents the
	 * 'real' action sitting in the workbench UI.
	 * 
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		if (LoginAuth.isLogin()) {
			if (ExamAuth.getExamAuth().isInExam()) {
				CallResult calculateScoreResult = TestDao.getTestDao()
						.getQuestionScore(
								ExamAuth.getExamAuth().getCurrentExam_id(),
								ExamAuth.getExamAuth()
										.getQuestionOfCurrentExam());
				if (calculateScoreResult.getState()) {
					JSONArray scoreJSONArray = (JSONArray) calculateScoreResult.getData();
					CallResult result = UploadDao.getUploadDao()
							.uploadExamFile(
									ExamAuth.getExamAuth().getCurrentExam_id(),scoreJSONArray);
					if (result.getState()) {
						MessageBox messageBox = new MessageBox(
								window.getShell(), SWT.ICON_INFORMATION);
						messageBox.setMessage("success to upload exam-"
								+ ExamAuth.getExamAuth().getCurrentExam_id());
						messageBox.open();
					} else {
						MessageBox messageBox = new MessageBox(
								window.getShell(), SWT.ICON_INFORMATION);
						messageBox.setMessage("fail to upload exam-"
								+ ExamAuth.getExamAuth().getCurrentExam_id()
								+ " (msg:" + result.getMessage() + ")");
						messageBox.open();
					}
				} else {
					MessageBox messageBox = new MessageBox(window.getShell(),
							SWT.ICON_INFORMATION);
					messageBox
							.setMessage("error happened when calculate scores.("
									+ calculateScoreResult.getMessage() + ")");
					messageBox.open();
				}
			} else {
				MessageBox messageBox = new MessageBox(window.getShell(),
						SWT.ICON_INFORMATION);
				messageBox.setMessage("you are not in exam");
				messageBox.open();
			}
		} else {
			MessageBox messageBox = new MessageBox(window.getShell(),
					SWT.ICON_INFORMATION);
			messageBox.setMessage("Please Login first");
			messageBox.open();
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