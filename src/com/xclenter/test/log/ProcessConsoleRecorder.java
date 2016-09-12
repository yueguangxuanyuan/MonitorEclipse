package com.xclenter.test.log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Semaphore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jface.text.IDocument;


public class ProcessConsoleRecorder {
	private static Logger logger = LogManager.getLogger("MessageLog");
	private static ProcessConsoleRecorder processConsoleRecorder;
	
	
	HashMap<String,IDocument> consoleDocumentRecorder; 
	/*
	 * ��֤ÿ��StringBuilder��˳�����
	 */
	HashMap<String,Semaphore> lockRecorder; 
	HashSet<String> programsWaitingForExpose;
	/*
	 * ��֤ HashMap��˳����� ��ܲ�����д����
	 */
    Semaphore documentMapLocker ;
    Semaphore lockMapLocker;
    Semaphore pweLocker;
	private ProcessConsoleRecorder() {
		consoleDocumentRecorder = new HashMap<>();
		lockRecorder = new HashMap<>();
		programsWaitingForExpose = new HashSet<>();
		documentMapLocker = new Semaphore(1);
		lockMapLocker = new Semaphore(1);
		pweLocker = new Semaphore(1);
	}

	public static ProcessConsoleRecorder getProcessConsoleRecorder() {
		if (processConsoleRecorder == null) {
			processConsoleRecorder = new ProcessConsoleRecorder();
		}

		return processConsoleRecorder;
	}
	
	public void RecordDocument(String projectId,IDocument document,String runType){
		/*
		 * �õ���Ը�project���ĵ���
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
		 * ���� ��֤������������
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
		
		boolean needRecord = false;//�ж��Ƿ�������Ҫ�ӳ�����Ķ��� 
		try{
		  pweLocker.acquire();	
		  if(programsWaitingForExpose.remove(projectId)){
			  needRecord = true;
		  }
		  pweLocker.release();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		if(needRecord == true){
			 RecordRunMessage(projectId,runType);
		}
	}
	
	public void RecordRunMessage(String projectId,String  runType){
		/*
		 * �õ���Ը�project���ĵ���
		 */
		Semaphore documentLocker = null;
		try {
			lockMapLocker.acquire();
		   
			if(!lockRecorder.containsKey(projectId)){
//				lockRecorder.put(projectId, new Semaphore(1));
				//��������� ˵��  console����û���ɣ������������ȴ�������
				pweLocker.acquire();
				programsWaitingForExpose.add(projectId);
				pweLocker.release();
			}
			documentLocker = lockRecorder.get(projectId);
			lockMapLocker.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		 * ���� ��֤������������
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
				log(projectId,runType,document.get());
			}
			documentLocker.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void log(String projectId,String runType,String message) {
		int indexOfSplit = projectId.indexOf("@");
		if(indexOfSplit > 0 && indexOfSplit < projectId.length()){
			String projectName = projectId.substring(0,indexOfSplit);
			String processId = projectId.substring(indexOfSplit+1);
			logger.info(":: action_type ::output:: runType ::"+runType+":: project ::"+projectName+":: processId ::"+processId+":: length ::"+message.length()+":: message ::"+message);
		}
	}
}
