package com.xclenter.test.dao;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.xclenter.test.model.QuestionModel;
import com.xclenter.test.model.TaskModel;
import com.xclenter.test.util.HttpCommon;
import com.xclenter.test.util.ServerInfo;
import com.xclenter.test.util.file.DeleteUtil;
import com.xclenter.test.util.file.FileUtil;
import com.xclenter.test.util.file.SaveFileUtil;

public class DownloadDao {

	private static DownloadDao downloadDao;

	private DownloadDao() {

	}

	public static DownloadDao getDownloadDao() {
		if (downloadDao == null) {
			downloadDao = new DownloadDao();
		}
		return downloadDao;
	}

	public CallResult getAvailable_tasks() {
		boolean state = false;
		String message = null;
		List<TaskModel> taskList = new ArrayList<TaskModel>();

		String url = "http://" + ServerInfo.serverIP
				+ "/exams/get_active_list/";
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("is_homework", "false");

		JSONObject feedBack = HttpCommon.getHttpCommon()
				.getJsonResponseWithParams(url, params);
		if (feedBack != null) {
			String result;
			try {
				result = feedBack.getString("result");

				if (result != null && result.equals("ok")) {
					feedBack = feedBack.getJSONObject("exams");
					JSONArray idList = feedBack.getJSONArray("id");
					if (idList != null) {
						JSONArray nameList = feedBack.getJSONArray("name");
						JSONArray begin_timeList = feedBack
								.getJSONArray("begin_time");
						JSONArray end_timeList = feedBack
								.getJSONArray("end_time");
						JSONArray is_homeworkList = feedBack
								.getJSONArray("is_homework");
						for (int i = 0; i < idList.length(); i++) {
							String id = idList.getString(i);
							String name = nameList.getString(i);
							String begin_time = begin_timeList.getString(i);
							String end_time = end_timeList.getString(i);
							boolean is_homework = is_homeworkList.getBoolean(i);
							taskList.add(new TaskModel(id, name, begin_time,
									end_time, is_homework));
						}
						state = true;
					} else {
						message = "server error";
					}
				} else {
					message = feedBack.getString("msg");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				message = "server re-msg error";
				e.printStackTrace();
			}
		} else {
			message = "server not available";
		}

		return new CallResult(state, message, taskList);
	}

	public CallResult getQuestionsOfExam(String eid) {
		boolean state = false;
		String message = null;
		Object data = null;
		String url = "http://" + ServerInfo.serverIP + "/exam/download_total/";
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("eid", eid);
		JSONObject feedBack = HttpCommon.getHttpCommon()
				.getJsonResponseWithParams(url, params);
		try {
			if (feedBack != null && feedBack.getString("result") != null) {
				if (feedBack.getString("result").equals("ok")) {
					JSONArray nameList = feedBack.getJSONArray("name");
					JSONArray questionList = feedBack.getJSONArray("question");
					JSONArray question_idList = feedBack
							.getJSONArray("question_id");
					ArrayList<QuestionModel> questions = new ArrayList<>();
					if (nameList != null) {
						clearExamDownloadSpace(eid);
						for (int i = 0; i < nameList.length(); i++) {
							String question_id = question_idList.getString(i);
							String name = nameList.getString(i);
							String encryptedQuestion = questionList
									.getString(i);
							// System.out.println(Arrays.toString(
							// encryptedQuestion.getBytes()));
							saveQuestionOfExam(eid, question_id, name,
									encryptedQuestion);
							questions.add(new QuestionModel(question_id,name));
						}
						data = questions;
						state = true;
					} else {
						message = "server error";
					}
				} else {
					message = feedBack.getString("msg");
				}
			} else {
				message = "server not available";
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			message = "server re-msg error";
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			message = "error encoding";
			e.printStackTrace();
		}
		return new CallResult(state, message,data);
	}

	private void clearExamDownloadSpace(String eid) {
		String path = FileUtil.downloadFileSaveRootPath + File.separator
				+ eid;
		File examSaveRootPath = new File(path);
		if (!examSaveRootPath.exists()) {
			examSaveRootPath.mkdirs();
		}
		DeleteUtil.delAllFile(path);
	}

	private void saveQuestionOfExam(String eid, String question_id,
			String name, String question) throws UnsupportedEncodingException {
		String path = FileUtil.downloadFileSaveRootPath + File.separator
				+ eid;
		path += File.separator + question_id + "_" + name;
//		String decryptedQuestion = EncryptUtil.getEncryptUtil().decryptString(
//				question);
		SaveFileUtil.saveFileWithByte(path + File.separator + question_id
				+ ".zip", question.getBytes("ISO-8859-1"));

	}
}
