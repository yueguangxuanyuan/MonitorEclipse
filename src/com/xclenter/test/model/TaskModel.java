package com.xclenter.test.model;

public class TaskModel {
	
	private String id;
	private String name;
	private String begin_time;
	private String end_time;
	private boolean is_homework;
	
	public TaskModel(String id, String name, String begin_time,
			String end_time,boolean is_homework) {
		super();
		this.id = id;
		this.name = name;
		this.begin_time = begin_time;
		this.end_time = end_time;
		this.is_homework = is_homework;
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
	public boolean getIs_homework(){
		return is_homework;
	}
}
