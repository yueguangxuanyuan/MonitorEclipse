package com.xclenter.test.dao;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import com.xclenter.test.model.QuestionModel;
import com.xclenter.test.util.action.ExamAuth;
import com.xclenter.test.util.file.DeleteUtil;
import com.xclenter.test.util.file.FileUtil;
import com.xclenter.test.util.file.ReadFileUtil;
import com.xclenter.test.util.file.ZipUtil;

public class TestDao {

	private static TestDao testDao;

	private TestDao() {

	}

	public static TestDao getTestDao() {
		if (testDao == null) {
			testDao = new TestDao();
		}
		return testDao;
	}

	public CallResult testQuestion(String eid, QuestionModel question) {
		boolean state = false;
		String message = "";
		Object data = null;
		/*
		 * 读取 Release/projectName.exe 文件
		 */
		IWorkspaceRoot workspaceroot = ResourcesPlugin.getWorkspace().getRoot();
		String projectName = ExamAuth.getExamAuth()
				.getQuestionidToProjectNameMap().get(question.getQid());
		IProject project = workspaceroot.getProject(projectName);

		if (project != null && project.exists()) {
			String programPath = project.getLocation().toOSString()
					.replaceAll("\\*", "/")+File.separator
					+ "Release/" + projectName + ".exe";
			File program = new File(programPath);
			if (program.exists()) {
				/*
				 * 读取测试用例 并测试
				 */
				String tmpFilePath = FileUtil.tmpFileSaveRootPath
						+ File.separator + "unzipFile";
				File tmpFileFolder = new File(tmpFilePath);
				tmpFileFolder.mkdirs();
				DeleteUtil.delAllFile(tmpFilePath);

				String zipFilePath = FileUtil.downloadFileSaveRootPath
						+ File.separator + eid + File.separator
						+ question.getQid() + ".zip";
				ZipUtil.unZipFiles(new File(zipFilePath), tmpFilePath);
				File unzipedFile = tmpFileFolder.listFiles()[0];
				String questionTestCaseRootPath = unzipedFile.getAbsolutePath()
						+ File.separator + "test_cases";
				File questionTestCaseRootFolder = new File(
						questionTestCaseRootPath);
				String[] test_caseNames = questionTestCaseRootFolder.list();
				int test_caseCount = 0;
				int pass_caseCount = 0;
				for (String test_caseName : test_caseNames) {
					if (test_caseName.endsWith(".in")) {
						test_caseCount ++ ;
						String test_caseOutName = test_caseName.substring(0,
								test_caseName.length() - 3);
						test_caseOutName += ".out";
						String inFilePath = questionTestCaseRootPath+File.separator + test_caseName;
						String outFilePath = questionTestCaseRootPath+File.separator + test_caseOutName;
						if(testProgram(programPath,inFilePath,outFilePath)){
							pass_caseCount ++;
						}
					}
				}
				state = true;
				data = new TestCasePassResult(test_caseCount,pass_caseCount);
			}else{
				message = "Release/" + projectName + ".exe not exists";
			}
		} else {
			message = "you haven't created the project for the question-"
					+ question.getQid() + " (projectName : " + projectName
					+ ")";
		}
		return new CallResult(state, message,data);
	}
	
	public boolean testProgram(String programPath,String inFilePath,String outFilePath){
		String command = programPath;
		try {
			Process p = Runtime.getRuntime().exec(command);
			// Process p = pb.start();
			OutputStream os = p.getOutputStream();
			os.write(ReadFileUtil.readFileInBytes(inFilePath));
			os.close();
			InputStream is = p.getInputStream();
			byte[] programout = ReadFileUtil.readInputStreamIntoBytes(is);
			p.destroy();
			byte[] exceptout = ReadFileUtil.readFileInBytes(outFilePath);
			if (programout != null && exceptout != null
					&& programout.length == exceptout.length) {
				return Arrays.equals(programout, exceptout);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
