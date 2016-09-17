package com.xclenter.test.util.action;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.xclenter.test.model.ExamModel;
import com.xclenter.test.model.QuestionModel;
import com.xclenter.test.util.file.FileUtil;
import com.xclenter.test.util.file.ReadFileUtil;
import com.xclenter.test.util.file.SaveFileUtil;

public class ExamAuth {
	private ExamModel currentExam = null;
	private static String filePath = FileUtil.utilFileSaveRootPath
			+ File.separator + "exam";

	private static ExamAuth examAuth;

	private ExamAuth() {
		String examInstance = ReadFileUtil.readFileInString(filePath);
		if (examInstance != null && !examInstance.equals("")) {
			currentExam = ExamModel.fromJsonString(examInstance);
		}
	}

	public static ExamAuth getExamAuth() {
		if (examAuth == null) {
			examAuth = new ExamAuth();
		}
		return examAuth;
	}

	public boolean isInExam() {
		boolean result = false;
		if (currentExam != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			try {
				Date start_time = sdf.parse(currentExam.getBegin_time());
				Date end_time = sdf.parse(currentExam.getEnd_time());
				Date current_time = new Date();
				if (current_time.after(start_time)
						&& current_time.before(end_time)) {
					return true;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public String getCurrentExam_id() {
		String eid = null;
		if(currentExam != null){
			eid = currentExam.getEid();
		}
		return eid;
	}

	public List<QuestionModel> getQuestionOfCurrentExam() {
		List<QuestionModel> questions = null;
		if(currentExam != null){
			questions = currentExam.getQuestions();
		}
		return questions;
	}
	
	public HashMap<String,String> getQuestionidToProjectNameMap(){
		HashMap<String,String> questionidToProjectMap = new HashMap<>();
		if(currentExam != null){
			for(QuestionModel question : currentExam.getQuestions()){
				questionidToProjectMap.put(question.getQid(), "Q"+question.getQid());
				
			}
		}
		return questionidToProjectMap;
	}
	
	public void setCurrentExam(ExamModel currentExam) {
		this.currentExam = currentExam;
		String content = "";
		if (currentExam != null) {
			content = currentExam.toString();
		}
		SaveFileUtil.saveFileWithString(filePath, content);
	}
}
