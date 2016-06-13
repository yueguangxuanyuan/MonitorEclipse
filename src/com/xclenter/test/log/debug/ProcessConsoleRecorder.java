package com.xclenter.test.log.debug;

import java.util.HashMap;
import java.util.concurrent.Semaphore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jface.text.IDocument;


public class ProcessConsoleRecorder {
	private static Logger logger = LogManager.getLogger("MessageLog");
	private static ProcessConsoleRecorder processConsoleRecorder;
	
	
	HashMap<String,IDocument> consoleDocumentRecorder; 
	/*
	 * 保证每个StringBuilder的顺序访问
	 */
	HashMap<String,Semaphore> lockRecorder; 
	
	/*
	 * 保证 HashMap的顺序访问 规避并发读写风险
	 */
    Semaphore documentMapLocker ;
    Semaphore lockMapLocker;
	private ProcessConsoleRecorder() {
		consoleDocumentRecorder = new HashMap<>();
		lockRecorder = new HashMap<>();
		documentMapLocker = new Semaphore(1);
		lockMapLocker = new Semaphore(1);
	}

	public static ProcessConsoleRecorder getProcessConsoleRecorder() {
		if (processConsoleRecorder == null) {
			processConsoleRecorder = new ProcessConsoleRecorder();
		}

		return processConsoleRecorder;
	}
	
	public void RecordDocument(String projectId,IDocument document){
		/*
		 * 拿到针对该project的文档锁
		 */
		Semaphore documentLocker = null;
		try {
			lockMapLocker.acquire();
		   
			if(!lockRecorder.containsKey(projectId)){
				lockRecorder.put(projectId, new Semaphore(1));
			}
			documentLocker = lockRecorder.get(projectId);
			lockMapLocker.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		 * 断言 保证程序正常运行
		 */
		
		if(documentLocker == null){
			return;
		}
		try {
			documentLocker.acquire();
			
			
			documentMapLocker.acquire();
		
			consoleDocumentRecorder.put(projectId, document);
			
			documentMapLocker.release();
		
			documentLocker.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void RecordRunMessage(String projectId){
		/*
		 * 拿到针对该project的文档锁
		 */
		Semaphore documentLocker = null;
		try {
			lockMapLocker.acquire();
		   
			if(!lockRecorder.containsKey(projectId)){
				lockRecorder.put(projectId, new Semaphore(1));
			}
			documentLocker = lockRecorder.get(projectId);
			lockMapLocker.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		 * 断言 保证程序正常运行
		 */
		
		if(documentLocker == null){
			return;
		}
		try {
			documentLocker.acquire();
			
			
			documentMapLocker.acquire();
			
			IDocument document = null;
			if(consoleDocumentRecorder.containsKey(projectId)){
				document = consoleDocumentRecorder.get(projectId);
			}
			documentMapLocker.release();
			
			if(document != null){
				Thread.sleep(500);
				log(projectId,document.get());
			}
			documentLocker.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void log(String projectId,String message) {
		int indexOfSplit = projectId.indexOf("@");
		if(indexOfSplit > 0 && indexOfSplit < projectId.length()){
			String projectName = projectId.substring(0,indexOfSplit);
			String processId = projectId.substring(indexOfSplit+1);
			logger.info(":: action_type ::output:: runType ::normal:: project ::"+projectName+":: processId ::"+processId+":: length ::"+message.length()+":: message ::"+message);
		}
	}
}
