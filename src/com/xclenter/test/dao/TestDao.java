package com.xclenter.test.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.microsmadio.invoker.executor.TestCase;
import com.microsmadio.invoker.executor.TestCaseExecutor;
import com.microsmadio.invoker.listener.ExecutionEventArgs;
import com.microsmadio.invoker.listener.IExecutionSuccessListener;
import com.microsmadio.invoker.listener.IReadProcessStreamListener;
import com.microsmadio.invoker.listener.ITimeOutListener;
import com.microsmadio.invoker.listener.IWriteProcessStreamListener;
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

	final int SUCCESS = 1;
	final int TIME_OUT = 2;
	final int NORMAL_FAIL = 3;

	public CallResult testQuestion(String eid, QuestionModel question) {
		boolean state = false;
		String message = "";
		Object data = null;
		/*
		 * ��ȡ Release/projectName.exe �ļ�
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
					byte[] encryptedQuestion = ReadFileUtil
							.readFileInBytes(encryptedFilePath);
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
					int timeout_caseCount = 0;
					JSONObject test_result = new JSONObject();
					JSONArray passedcaseids = new JSONArray();
					JSONArray timeoutcaseids = new JSONArray();
					JSONArray wrongcaseids = new JSONArray();
					for (String test_caseName : test_caseNames) {
						if (test_caseName.endsWith(".in")) {
							test_caseCount++;
							String test_caseid = test_caseName.substring(0,
									test_caseName.length() - 3);
							String test_caseOutName = test_caseid + ".out";
							String inFilePath = questionTestCaseRootPath
									+ File.separator + test_caseName;
							String outFilePath = questionTestCaseRootPath
									+ File.separator + test_caseOutName;
							switch (testProgram(programPath, inFilePath,
									outFilePath)) {
							case SUCCESS:
								passedcaseids.put(test_caseid);
								pass_caseCount++;
								break;
							case TIME_OUT:
								timeoutcaseids.put(test_caseid);
								timeout_caseCount++;
								break;
							default:
								wrongcaseids.put(test_caseid);
								break;
							}
						}
					}
					test_result.put("success", passedcaseids);
					test_result.put("wrong", wrongcaseids);
					test_result.put("timeout", timeoutcaseids);
					state = true;
					data = new TestCasePassResult(test_caseCount,
							pass_caseCount, timeout_caseCount,
							test_result.toString());
				} catch (Exception e) {
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

	private class ProgramtestResult {
		private boolean isSuccess;
		private boolean isTimeout;

		public ProgramtestResult() {
			super();
			this.isSuccess = false;
			this.isTimeout = false;
		}

		public boolean isSuccess() {
			return isSuccess;
		}

		public void setSuccess(boolean isSuccess) {
			this.isSuccess = isSuccess;
		}

		public boolean isTimeout() {
			return isTimeout;
		}

		public void setTimeout(boolean isTimeout) {
			this.isTimeout = isTimeout;
		}
	}

	TestCaseExecutor pp = new TestCaseExecutor();

	public int testProgram(String programPath, final String inFilePath,
			final String outFilePath) {
		String command = programPath;
		final ProgramtestResult programtestResult = new ProgramtestResult();
		IWriteProcessStreamListener writeStreamHandler = new IWriteProcessStreamListener() {
			@Override
			public void onWriteProcessStream(OutputStream stream) {
				try {
					BufferedReader input = new BufferedReader(new FileReader(
							(inFilePath)));
					BufferedWriter bw = new BufferedWriter(
							new OutputStreamWriter(stream));
					String line = null;
					while ((line = input.readLine()) != null) {
						bw.write(line);
						bw.newLine();
					}
					bw.close();
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		IReadProcessStreamListener readStreamHandler = new IReadProcessStreamListener() {
			@Override
			public void onReadProcessStream(InputStream stream) {
				byte[] programout = ReadFileUtil
						.readInputStreamIntoBytes(stream);
				byte[] exceptout = ReadFileUtil.readFileInBytes(outFilePath);
				if (programout != null && exceptout != null) {
					programtestResult.setSuccess(checkProgramout(programout,
							exceptout));
				}
			}
		};

		TestCase t1 = new TestCase(command, 1000);
		t1.setTaskInputCallback(writeStreamHandler);
		t1.setTaskOutputCallback(readStreamHandler);
		boolean r = false;
		ITimeOutListener timeOutListener = new ITimeOutListener() {
			public void onExecutionTimeOut(ExecutionEventArgs eventArgs) {
				programtestResult.setTimeout(true);
			}
		};
		pp.addTimeOutListener(timeOutListener);
		r = pp.submit(t1);
		pp.removeTimeOutListener(timeOutListener);

		int returnVal = NORMAL_FAIL;
		if (r && programtestResult.isSuccess()) {
			returnVal = SUCCESS;
		} else if (programtestResult.isTimeout()) {
			returnVal = TIME_OUT;
		}
		return returnVal;
	}

	/*
	 * �Աȳ��������Ԥ�����
	 */
	private boolean checkProgramout(byte[] programout, byte[] exceptout) {
		String[] programout_stringArray = spliteByteArray(programout);
		String[] exceptout_stringArray = spliteByteArray(exceptout);
		return Arrays.equals(programout_stringArray, exceptout_stringArray);
	}

	private String[] spliteByteArray(byte[] input) {
		String input_str = new String(input);
		input_str = input_str.replaceAll("\\s+", " ");
		input_str = input_str.trim();

		return input_str.split(" ");
	}

	/*
	 * �ز�һ����Ŀ
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
					TestCasePassResult tcpr = (TestCasePassResult) result
							.getData();
					scoreJSON.put("score", calculateScore(tcpr));
					scoreJSONArray.put(scoreJSON);
				} else {
					JSONObject errorMessageJSON = new JSONObject();
					errorMessageJSON.put("qid", question.getQid());
					errorMessageJSON.put("error_msg", result.getMessage());
					errorMessageJSONArray.put(errorMessageJSON);
				}
			}
			if (errorMessageJSONArray.length() == 0) {
				state = true;
			}
			message = errorMessageJSONArray.toString();
			data = scoreJSONArray;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			message = "json error";
			e.printStackTrace();
		}
		return new CallResult(state, message, data);
	}

	public String calculateScore(TestCasePassResult tcpr) {
		double score = 0;
		if (tcpr.getCaseCount() != 0) {
			score = tcpr.getPassedCaseCount() / ((double) tcpr.getCaseCount());
		}
		return String.format("%.3f", score);
	}
}
