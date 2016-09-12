package com.xclenter.test.listener.debugAction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.cdt.dsf.gdb.launching.InferiorRuntimeProcess;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.RuntimeProcess;

import com.xclenter.test.log.ProcessConsoleRecorder;

public class DebugEventSetListener implements IDebugEventSetListener {
	private static Logger logger = LogManager.getLogger("MessageLog");

	String workspaceRootPath;
	String fileSperator;
	private boolean ifNeedRecordAllEvent;
	private ProcessConsoleRecorder processConsoleRecorder;

	public DebugEventSetListener() {
		workspaceRootPath = Platform.getLocation().toOSString();
		fileSperator = System.getProperty("file.separator");

		String condition = System.getProperty("RecordAllEvent");
		ifNeedRecordAllEvent = (condition != null) && condition.equals("true");

		processConsoleRecorder = ProcessConsoleRecorder
				.getProcessConsoleRecorder();
	}

	/*
	 * ������ϵ������ģ����Լ�����������кͽ�����Ҫ��Ҫ����Create �� terminal�¼�
	 * 
	 * ��Ҫ����������� ���ͬʱ���ж��progress - Ҳ������Ҫ��¼ÿ��progress��������
	 */

	@Override
	public void handleDebugEvents(DebugEvent[] arg0) {
		// TODO Auto-generated method stub
		for (DebugEvent debugEvent : arg0) {
			/*
			 * ����ֻ��עCreate �� Terminal �����¼� �ֱ��¼�˳��� ���еĿ�ʼ�ͽ��� debug �Ŀ�ʼ�ͽ���
			 */
			String eventType = "notCare";
			String runType = "unknown";
			String project = "unknown";
			String processId = "unknown";
			String message = null;
			switch (debugEvent.getKind()) {
			case DebugEvent.CREATE:
			case DebugEvent.TERMINATE:
				eventType = debugEvent.getKind() == DebugEvent.CREATE ? "start"
						: "end";
				Object source = debugEvent.getSource();
//				System.out.println(source.getClass());
				if (source instanceof IProcess) {
					IProcess process = (IProcess) source;
					processId = process.toString();
					processId = processId
							.substring(processId.lastIndexOf("@") + 1);
				}
				/*
				 * ��Mars �汾�� debug��normal  ����ͨ�� processʵ������
				 */
				
//				if (source instanceof InferiorRuntimeProcess) {
//					runType = "debug";
//					InferiorRuntimeProcess gdbProcess = (InferiorRuntimeProcess) source;
//					String progressLabel = gdbProcess.getLabel();
//					System.out.println(progressLabel);
//					if (progressLabel != null && progressLabel.endsWith(".exe")) {
//						int splitIndex = progressLabel.indexOf(".exe");
//						if (splitIndex > 0
//								&& splitIndex < progressLabel.length()) {
//							project = progressLabel.substring(0, splitIndex);
//						}
//					}
//					message = debugEvent.toString();
//					break;
//				}

				
				if (source instanceof RuntimeProcess) {
					
					RuntimeProcess runtimeProgress = (RuntimeProcess) source;

					String progressLabel = runtimeProgress.getLabel();
					if(progressLabel != null){
						if (progressLabel.startsWith(workspaceRootPath)) {
							runType = "normal";
							progressLabel = progressLabel
									.substring(workspaceRootPath.length());
	
							int splitIndex = progressLabel.indexOf(fileSperator,
									fileSperator.length());
							if (splitIndex > 0
									&& splitIndex < progressLabel.length()) {
								project = progressLabel.substring(
										fileSperator.length(), splitIndex);
							}
						}else if(progressLabel.endsWith(".exe")){
							runType = "debug";
							int splitIndex = progressLabel.indexOf(".exe");
							if (splitIndex > 0
									&& splitIndex < progressLabel.length()) {
								project = progressLabel.substring(0, splitIndex);
							}
						}
					}
					
					/*
					 * ��֤create �� terminal ˳�����
					 */
					message = debugEvent.toString();
					if (judgeIfNeedLog(eventType, project, runType)) {
						/*
						 * ������н����� ֪ͨ console ��������� ��¼����
						 */
						if (eventType.equals("end")) {
							processConsoleRecorder.RecordRunMessage(project + "@"
									+ processId,runType);
						}
						log(eventType, project, runType, message, processId);
					}
					return;
				}
				
				message = debugEvent.toString();
				break;
			default:
				message = debugEvent.toString();
				break;
			}
			if (judgeIfNeedLog(eventType, project, runType)) {
				log(eventType, project, runType, message, processId);
			}
		}
	}

	private boolean judgeIfNeedLog(String eventType, String project,
			String runType) {
		if (!ifNeedRecordAllEvent) {
			if (eventType.equals("notCare") || runType.equals("unknown")
					|| project.equals("unknown")) {
				return false;
			}
		}
		return true;
	}

	private void log(String eventType, String project, String runType,
			String message, String processId) {
		logger.info(":: action_type ::run:: runType::" + runType + ":: type ::"
				+ eventType + ":: project ::" + project + ":: processId ::"
				+ processId + ":: message ::" + message);
	}
}
