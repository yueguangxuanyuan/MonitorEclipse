package com.xclenter.test.ui.actions;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.jface.dialogs.MessageDialog;

import com.xclenter.test.dao.CallResult;
import com.xclenter.test.dao.TestCasePassResult;
import com.xclenter.test.dao.TestDao;
import com.xclenter.test.model.QuestionModel;
import com.xclenter.test.ui.dialog.LoginDialog;
import com.xclenter.test.ui.dialog.TestQuestionSelectDialog;
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
public class TestAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
	private static Logger logger = LogManager.getLogger("MessageLog");

	/**
	 * The constructor.
	 */
	public TestAction() {

	}

	/**
	 * The action has been activated. The argument of the method represents the
	 * 'real' action sitting in the workbench UI.
	 * 
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		if (!LoginAuth.isLogin()) {
			MessageBox messageBox = new MessageBox(window.getShell(),
					SWT.ICON_INFORMATION);
			messageBox.setMessage("Please Login first");
			messageBox.open();
		} else if (ExamAuth.getExamAuth().isInExam()) {
			List<QuestionModel> currentQuestions = ExamAuth.getExamAuth()
					.getQuestionOfCurrentExam();
			final String eid = ExamAuth.getExamAuth().getCurrentExam_id();
			TestQuestionSelectDialog testDialog = new TestQuestionSelectDialog(
					window.getShell(), eid, currentQuestions) {
				protected void doAfterSelect(QuestionModel questionmodel) {
					// TODO Auto-generated method stub
					CallResult callresult = TestDao.getTestDao().testQuestion(
							examid, questionmodel);
					if (callresult.getState()) {
						String message = "";
						TestCasePassResult passresult = (TestCasePassResult) callresult
								.getData();
						boolean isPass = false;
						if (passresult.getCaseCount() > passresult
								.getPassedCaseCount()) {
							message = "fail to pass test case.";
						} else {
							message = "succss to pass test case.";
							isPass = true;
						}
						String passInfo = "total " + passresult.getCaseCount()
								+ " - passed "
								+ passresult.getPassedCaseCount();
						log(LoginAuth.getUsername(),eid,questionmodel.getQid(),isPass,passInfo);
						message += "(total " + passresult.getCaseCount()
								+ " - passed "
								+ passresult.getPassedCaseCount() + ")";
						MessageBox messageBox = new MessageBox(
								window.getShell(), SWT.ICON_INFORMATION);
						messageBox.setMessage(message);
						messageBox.open();
					} else {
						MessageBox messageBox = new MessageBox(getShell(),
								SWT.ICON_INFORMATION);
						messageBox.setMessage(callresult.getMessage());
						messageBox.open();
					}
				}
			};
			testDialog.open();
		} else {
			MessageBox messageBox = new MessageBox(window.getShell(),
					SWT.ICON_INFORMATION);
			messageBox.setMessage("you are not in an exam");
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

	private void log(String username, String eid, String qid, boolean isPass,
			String msg) {
		logger.info(":: action_type ::test:: username ::" + username
				+ ":: examid ::" + eid + ":: questionid ::" + qid
				+ ":: isPass ::" + isPass + ":: message ::" + msg);
	}
}