package com.xclenter.test.log.build;

import java.util.HashMap;
import java.util.concurrent.Semaphore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BuildRecorder {
	private static Logger logger = LogManager.getLogger("MessageLog");
	private static BuildRecorder buildRecorder;
	
	
	HashMap<String,StringBuilder> messageRecorder; 
	/*
	 * 保证每个StringBuilder的顺序访问
	 */
	HashMap<String,Semaphore> lockRecorder; 
	
	/*
	 * 保证 HashMap的顺序访问 规避并发读写风险
	 */
    Semaphore messageMapLocker ;
    Semaphore lockMapLocker;
	private BuildRecorder() {
		messageRecorder = new HashMap<>();
		lockRecorder = new HashMap<>();
		messageMapLocker = new Semaphore(1);
		lockMapLocker = new Semaphore(1);
	}

	public static BuildRecorder getBuildRecorder() {
		if (buildRecorder == null) {
			buildRecorder = new BuildRecorder();
		}

		return buildRecorder;
	}
	
	private final int MESSAGE_RECORD=1;
	private final int MESSAGE_END = 2;
	
	public void RecordMessage(String projectName,String message){
		Semaphore stringBuilderLocker = null;
		try {
			lockMapLocker.acquire();
		   
			if(!lockRecorder.containsKey(projectName)){
				lockRecorder.put(projectName, new Semaphore(1));
			}
			stringBuilderLocker = lockRecorder.get(projectName);
			lockMapLocker.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		 * 断言 保证程序正常运行
		 */
		
		if(stringBuilderLocker == null){
			return;
		}
		try {
			stringBuilderLocker.acquire();
			
			StringBuilder messageBuilder = null;
			messageMapLocker.acquire();
			if(!messageRecorder.containsKey(projectName)){
				messageRecorder.put(projectName, new StringBuilder());
			}
			messageBuilder = messageRecorder.get(projectName);
			messageMapLocker.release();
			
			int messageRule = judgeMessage(message);
			
			if((messageRule & MESSAGE_RECORD) > 0){
				messageBuilder.append(message);
				if((messageRule & MESSAGE_END) > 0){
					log(projectName, messageBuilder.toString());
					/*
					 * 重置消息
					 */
					messageMapLocker.acquire();
					messageRecorder.put(projectName, new StringBuilder());
					messageMapLocker.release();
				}
			}
		
			stringBuilderLocker.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private int judgeMessage(String message){
		int result = 0;
		if (!message.matches("\\s*")) {
			result |= MESSAGE_RECORD;
			
			if(message.contains("Info: Nothing to build for")||message.contains("Build Finished")){
				result |= MESSAGE_END;
			}
		}
		return result;
	}
	
	private void log(String projectName,String message) {
		logger.info(":: action_type ::build:: project ::"+projectName+":: message ::\n"+message);
	}

}
