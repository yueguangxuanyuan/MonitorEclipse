package com.xclenter.test.dao;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.xclenter.test.model.QuestionModel;
import com.xclenter.test.util.action.ExamAuth;
import com.xclenter.test.util.file.DeleteUtil;
import com.xclenter.test.util.file.EncryptUtil;
import com.xclenter.test.util.file.FileUtil;
import com.xclenter.test.util.file.ReadFileUtil;
import com.xclenter.test.util.file.SaveFileUtil;
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
					.replaceAll("\\*", "/")
					+ File.separator + "Release/" + projectName + ".exe";
			File program = new File(programPath);
			if (program.exists()) {
				/*
				 * 读取测试用例 并测试
				 */
				String tmpFilePath = FileUtil.tmpFileSaveRootPath
						+ File.separator + "unzipFile";
				final String zipFileRootPath = FileUtil.tmpFileSaveRootPath
						+ File.separator + "zipFile";

				File tmpFileFolder = new File(tmpFilePath);
				tmpFileFolder.mkdirs();
				DeleteUtil.delAllFile(tmpFilePath);

				File zipFileRootFolder = new File(zipFileRootPath);
				zipFileRootFolder.mkdirs();
				DeleteUtil.delAllFile(zipFileRootPath);

				String encryptedFilePath = FileUtil.downloadFileSaveRootPath
						+ File.separator + eid + File.separator
						+ question.getQid();
				try {
					String zipFilePath = zipFileRootPath + File.separator
							+ question.getQid() + ".zip";
					String encryptedQuestion = "";

					encryptedQuestion = new String(
							ReadFileUtil.readFileInBytes(encryptedFilePath),
							"ISO-8859-1");
					byte[] decryptedQuestion = EncryptUtil.getEncryptUtil()
							.decrypt(encryptedQuestion);
					SaveFileUtil.saveFileWithByte(zipFilePath,
							decryptedQuestion);

					ZipUtil.unZipFiles(new File(zipFilePath), tmpFilePath);
					File unzipedFile = tmpFileFolder.listFiles()[0];
					String questionTestCaseRootPath = unzipedFile
							.getAbsolutePath() + File.separator + "test_cases";
					File questionTestCaseRootFolder = new File(
							questionTestCaseRootPath);
					String[] test_caseNames = questionTestCaseRootFolder.list();
					int test_caseCount = 0;
					int pass_caseCount = 0;
					for (String test_caseName : test_caseNames) {
						if (test_caseName.endsWith(".in")) {
							test_caseCount++;
							String test_caseOutName = test_caseName.substring(
									0, test_caseName.length() - 3);
							test_caseOutName += ".out";
							String inFilePath = questionTestCaseRootPath
									+ File.separator + test_caseName;
							String outFilePath = questionTestCaseRootPath
									+ File.separator + test_caseOutName;
							if (testProgram(programPath, inFilePath,
									outFilePath)) {
								pass_caseCount++;
							}
						}
					}
					state = true;
					data = new TestCasePassResult(test_caseCount,
							pass_caseCount);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					DeleteUtil.delAllFile(tmpFilePath);
					DeleteUtil.delAllFile(zipFileRootPath);
				}
			} else {
				message = "Release/" + projectName + ".exe not exists";
			}
		} else {
			message = "you haven't created the project for the question-"
					+ question.getQid() + " (projectName : " + projectName
					+ ")";
		}
		return new CallResult(state, message, data);
	}

	public boolean testProgram(String programPath, String inFilePath,
			String outFilePath) {
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
/*
 * 重测一遍题目
 */
	public CallResult getQuestionScore(String eid, List<QuestionModel> questions) {
		boolean state = false;
		String message = "";
		Object data = null;
		try {
			JSONArray scoreJSONArray = new JSONArray();
			JSONArray errorMessageJSONArray = new JSONArray();
			for (QuestionModel question : questions) {
				CallResult result = testQuestion(eid, question);
				if (result.getState()) {
					JSONObject scoreJSON = new JSONObject();
					scoreJSON.put("qid", question.getQid());
					TestCasePassResult tcpr = (TestCasePassResult) result.getData();
					scoreJSON.put("score", calculateScore(tcpr));
					scoreJSONArray.put(scoreJSON);
				} else {
					JSONObject errorMessageJSON = new JSONObject();
					errorMessageJSON.put("qid", question.getQid());
					errorMessageJSON.put("error_msg", result.getMessage());
					errorMessageJSONArray.put(errorMessageJSON);
				}
			}
			if(errorMessageJSONArray.length() == 0){
				state = true;
			}
			message  = errorMessageJSONArray.toString();
			data = scoreJSONArray;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			message = "json error";
			e.printStackTrace();
		}
		return new CallResult(state, message, data);
	}
	
	public String calculateScore(TestCasePassResult tcpr){
		double score = 0;
		if(tcpr.getCaseCount() != 0){
			score = tcpr.getPassedCaseCount()/((double)tcpr.getCaseCount());
		}
		return String.format("%.3f", score);
	}
}
