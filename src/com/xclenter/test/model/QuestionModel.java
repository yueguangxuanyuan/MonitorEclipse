package com.xclenter.test.model;

public class QuestionModel {
	private String qid;
	private String name;

	public QuestionModel(String qid, String name) {
		super();
		this.qid = qid;
		this.name = name;
	}

	public String getQid() {
		return qid;
	}

	public String getName() {
		return name;
	}

}
