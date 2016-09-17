package com.xclenter.test.dao;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.entity.FileEntity;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.xclenter.test.model.QuestionModel;
import com.xclenter.test.model.TaskModel;
import com.xclenter.test.util.HttpCommon;
import com.xclenter.test.util.ServerInfo;
import com.xclenter.test.util.action.ExamAuth;
import com.xclenter.test.util.file.FileUtil;
import com.xclenter.test.util.file.ReadFileUtil;
import com.xclenter.test.util.file.SaveFileUtil;
import com.xclenter.test.util.file.ZipUtil;

public class UploadDao {

	private static UploadDao uploadDao;

	private UploadDao() {

	}

	public static UploadDao getUploadDao() {
		if (uploadDao == null) {
			uploadDao = new UploadDao();
		}
		return uploadDao;
	}

	public CallResult uploadExamFile(String exam_id) {
		boolean state = false;
		String message = null;

		String url = "http://" + ServerInfo.serverIP
				+ "/exam/upload_exam_log_project/";
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("eid", exam_id);
		HashMap<String,String> filesToUpload = new HashMap<>(); 
		/*
		 * 压缩日志文件夹
		 */
		String logzipPath = FileUtil.tmpFileSaveRootPath+File.separator+exam_id + "_log.zip";
		ZipUtil.ZipFilesIntoFile(new File(logzipPath), new File[]{new File(FileUtil.logSaveRootPath)});
		filesToUpload.put("log", logzipPath);
		/*
		 * 压缩工程文件夹
		 */
		String projectzipPath = FileUtil.tmpFileSaveRootPath+File.separator+exam_id + "_project.zip";
		ArrayList<File> projectFiles = new ArrayList<>();
		
		HashMap<String,String> questionidToProjectNameMap = ExamAuth.getExamAuth().getQuestionidToProjectNameMap();
		IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();
		if(questionidToProjectNameMap != null){
			for(String qid : questionidToProjectNameMap.keySet()){
				IProject project= workspace.getProject(questionidToProjectNameMap.get(qid));
				if(project != null && project.exists()){
					projectFiles.add(new File(project.getLocationURI().getPath()));
				}
			}
		}
		if(projectFiles.size() > 0){
			ZipUtil.ZipFilesIntoFile(new File(projectzipPath), projectFiles.toArray(new File[0]));
			filesToUpload.put("project", projectzipPath);
		}
		
		JSONObject feedBack = HttpCommon.getHttpCommon()
				.getJsonResponseWithParams(url, params,filesToUpload);
		//清理tmp
		{
			File logzipfile = new File(logzipPath);
			if(logzipfile.exists()){
				logzipfile.delete();
			}
			File projectzipfile = new File(projectzipPath);
			if(projectzipfile.exists()){
				projectzipfile.delete();
			}
		}
		if (feedBack != null) {
			String result;
			try {
				result = feedBack.getString("result");
				if (result != null && result.equals("ok")) {
					state = true;
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

		return new CallResult(state, message);
	}
}
