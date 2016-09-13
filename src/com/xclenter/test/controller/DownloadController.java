package com.xclenter.test.controller;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.xclenter.test.util.HttpCommon;
import com.xclenter.test.util.ServerInfo;
import com.xclenter.test.util.action.ActionAuth;
import com.xclenter.test.util.saveFile.SaveFileUtil;

public class DownloadController {

	private static DownloadController downloadDao;
	
	private DownloadController(){
		
	}
	
	public static DownloadController getDownloadDao(){
		if(downloadDao == null){
			downloadDao = new DownloadController();
		}
		return downloadDao;
	}
	
	public static List<TaskModel> getAvailable_tasks(){
		List<TaskModel> taskList = new ArrayList<TaskModel>();
		
		String url = "http://"+ServerInfo.serverIP+"/exams/get_active_list/";
		JSONObject feedBack = HttpCommon.getHttpCommon().getJsonResponseWithParams(url,null);
		if(feedBack != null){
			String result = feedBack.getString("result");
			feedBack=feedBack.getJSONObject("exams");
			if(result != null && result.equals("ok")){
				JSONArray idList =  feedBack.getJSONArray("id");
				if(idList != null){
					JSONArray nameList = feedBack.getJSONArray("name");
					JSONArray begin_timeList = feedBack.getJSONArray("begin_time");
					JSONArray end_timeList = feedBack.getJSONArray("end_time");
					
					for(int i = 0 ; i < idList.size() ; i++){
						String id = idList.getString(i);
						String name = nameList.getString(i);
						String begin_time = begin_timeList.getString(i);
						String end_time = end_timeList.getString(i);
						taskList.add(new TaskModel(id,name,begin_time,end_time));
					}
				}
			}
		}
		
		return taskList;
	}
}
