package com.xclenter.test.model;

import java.util.ArrayList;
import java.util.HashMap;
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
	
	private HashMap<String,String> score;

	public ExamModel(String eid, String name, String begin_time,
			String end_time, List<QuestionModel> questions) {
		super();
		this.eid = eid;
		this.name = name;
		this.begin_time = begin_time;
		this.end_time = end_time;
		this.questions = questions;
		score = new HashMap<String,String>();
		for(QuestionModel question : questions){
			score.put(question.getQid(), "0");
		}
	}
	
	public ExamModel(String eid, String name, String begin_time,
			String end_time, List<QuestionModel> questions,
			HashMap<String, String> score) {
		super();
		this.eid = eid;
		this.name = name;
		this.begin_time = begin_time;
		this.end_time = end_time;
		this.questions = questions;
		this.score = score;
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
	
	

	public HashMap<String, String> getScore() {
		return score;
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
			
			JSONArray scoresJson = new JSONArray();
			for(String qid: score.keySet()){
				JSONObject scoreJson = new JSONObject();
				scoreJson.put("qid", qid);
				scoreJson.put("score", score.get(qid));
				scoresJson.put(scoreJson);
			}
			exam.put("score", scoresJson);
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
			HashMap<String,String> scoreMap = new HashMap<>();
			JSONArray scoresJson = examJSON.getJSONArray("score");
			for(int i = 0 ; i < scoresJson.length() ;i++){
				JSONObject scoreJson = scoresJson.getJSONObject(i);
				String qid = scoreJson.getString("qid");
				String score = scoreJson.getString("score");
				scoreMap.put(qid,score);
			}
			exam = new ExamModel(eid,name,begin_time,end_time,questions,scoreMap);
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return exam;
	}
}
