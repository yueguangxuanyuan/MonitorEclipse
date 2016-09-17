package com.xclenter.test.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExamModel {
	private String eid;
	private String name;
	private String begin_time;
	private String end_time;
	private List<QuestionModel> questions;

	public ExamModel(String eid, String name, String begin_time,
			String end_time, List<QuestionModel> questions) {
		super();
		this.eid = eid;
		this.name = name;
		this.begin_time = begin_time;
		this.end_time = end_time;
		this.questions = questions;
	}

	public String getEid() {
		return eid;
	}

	public String getName() {
		return name;
	}

	public String getBegin_time() {
		return begin_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public List<QuestionModel> getQuestions() {
		return questions;
	}

	@Override
	public String toString() {
		JSONObject exam = new JSONObject();
		try {
			exam.put("eid", eid);
			exam.put("name", name);
			exam.put("begin_time", begin_time);
			exam.put("end_time", end_time);
			
			JSONArray questionsJson = new JSONArray();
			for(QuestionModel question : questions){
				JSONObject questionJson = new JSONObject();
				questionJson.put("qid", question.getQid());
				questionJson.put("name", question.getName());
				questionsJson.put(questionJson);
			}
			exam.put("questions", questionsJson);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return exam.toString();
	}
	
	public static ExamModel fromJsonString(String jsonString){
		ExamModel exam = null;
		try{
			JSONObject examJSON = new JSONObject(jsonString);
			String eid = examJSON.getString("eid");
			String name = examJSON.getString("name");
			String begin_time = examJSON.getString("begin_time");
			String end_time = examJSON.getString("end_time");
			
			ArrayList<QuestionModel> questions = new ArrayList<>();
			JSONArray questionsJson = examJSON.getJSONArray("questions");
			for(int i = 0 ; i < questionsJson.length() ;i++){
				JSONObject questionJson = questionsJson.getJSONObject(i);
				String qid = questionJson.getString("qid");
				String questionname = questionJson.getString("name");
				questions.add(new QuestionModel(qid,questionname));
			}
			exam = new ExamModel(eid,name,begin_time,end_time,questions);
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return exam;
	}
}
