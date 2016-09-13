package com.xclenter.test.controller;

public class TaskModel {
	
	private String id;
	private String name;
	private String begin_time;
	private String end_time;
	
	public TaskModel(String id, String name, String begin_time,
			String end_time) {
		super();
		this.id = id;
		this.name = name;
		this.begin_time = begin_time;
		this.end_time = end_time;
	}
	
	public String getId() {
		return id;
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

}
