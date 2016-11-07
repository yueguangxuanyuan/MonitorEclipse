package com.xclenter.test.ui.actions;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.CSourceEntry;
import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.core.settings.model.ICSourceEntry;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.xclenter.test.dao.CallResult;
import com.xclenter.test.dao.DownloadDao;
import com.xclenter.test.model.ExamModel;
import com.xclenter.test.model.QuestionModel;
import com.xclenter.test.model.TaskModel;
import com.xclenter.test.ui.dialog.TaskSelectDialog;
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
public class DownloadAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
	private DownloadDao downloadDao;
	private static Logger logger = LogManager.getLogger("MessageLog");

	/**
	 * The constructor.
	 */
	public DownloadAction() {
		downloadDao = DownloadDao.getDownloadDao();
	}

	/**
	 * The action has been activated. The argument of the method represents the
	 * 'real' action sitting in the workbench UI.
	 * 
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		/*
		 * we should check if they have taken one of task
		 */
		if (!LoginAuth.isLogin()) {
			MessageBox messageBox = new MessageBox(window.getShell(),
					SWT.ICON_INFORMATION);
			messageBox.setMessage("Please Login first");
			messageBox.open();
		} else if (ExamAuth.getExamAuth().isInExam()) {
			MessageBox messageBox = new MessageBox(window.getShell(),
					SWT.ICON_INFORMATION);
			messageBox
					.setMessage("You have In exam."
							+ ExamAuth.getExamAuth().getCurrentExam_id()
							+ " (you need log out to attend another exam.Dont forget to summit your work)");
			messageBox.open();
		} else {
			CallResult result = downloadDao.getAvailable_tasks();
			if (result.getState()) {
				List<TaskModel> taskModel = (List<TaskModel>) result.getData();
				TaskSelectDialog taskSelectDialog = new TaskSelectDialog(
						window.getShell(), taskModel) {
					@Override
					protected void doAfterSelect(TaskModel tablemodel) {
						// TODO Auto-generated method stub
						CallResult result = downloadDao
								.getQuestionsOfExam(tablemodel.getId());

						if (result.getState()) {
							ExamModel examModel = new ExamModel(
									tablemodel.getId(), tablemodel.getName(),
									tablemodel.getBegin_time(),
									tablemodel.getEnd_time(),
									(List<QuestionModel>) result.getData());
							ExamAuth.getExamAuth().setCurrentExam(examModel);
							CallResult setResult = setUpExamEnvironment();
							if (setResult.getState()) {
								MessageBox messageBox = new MessageBox(
										getShell(), SWT.ICON_INFORMATION);
								messageBox.setMessage("exam-"
										+ ExamAuth.getExamAuth()
												.getCurrentExam_id()
										+ " has already been set up");
								messageBox.open();
							} else {
								MessageBox messageBox = new MessageBox(
										getShell(), SWT.ICON_INFORMATION);
								messageBox.setMessage(setResult.getMessage());
								messageBox.open();
							}
						} else {
							MessageBox messageBox = new MessageBox(getShell(),
									SWT.ICON_INFORMATION);
							messageBox.setMessage(result.getMessage());
							messageBox.open();
						}
					}
				};
				taskSelectDialog.open();
			} else {
				MessageBox messageBox = new MessageBox(window.getShell(),
						SWT.ICON_INFORMATION);
				messageBox.setMessage(result.getMessage());
				messageBox.open();
			}
		}

	}

	private CallResult setUpExamEnvironment() {
		IWorkspaceRoot workspaceroot = ResourcesPlugin.getWorkspace().getRoot();
		HashMap<String, String> questionidToProjectNameMap = ExamAuth
				.getExamAuth().getQuestionidToProjectNameMap();
		try {
			IProject examInfoProject = workspaceroot.getProject("ExamInfo");
			if (examInfoProject != null && examInfoProject.exists()) {
				examInfoProject.close(null);
				examInfoProject.delete(true,true,null);
			}
			examInfoProject.create(null);
			String examInfoProjectPath = examInfoProject.getLocation()
					.toOSString();
			String examInfo = ExamAuth.getExamAuth().getExamInfo();
			SaveFileUtil.saveFileWithString(examInfoProjectPath
					+ File.separator + "examInfo", examInfo);
			CallResult result = downloadDao.unzipQuestionDescription(
					examInfoProjectPath, ExamAuth.getExamAuth()
							.getCurrentExam_id(), questionidToProjectNameMap);
			examInfoProject.open(null);
			return result;
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new CallResult(false, "error happened");
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