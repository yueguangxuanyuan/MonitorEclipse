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
	 * 这里联系上下文模块可以监听程序的运行和结束需要主要监听Create 和 terminal事件
	 * 
	 * 需要解决的问题是 如果同时运行多个progress - 也就是需要记录每个progress的上下文
	 */

	@Override
	public void handleDebugEvents(DebugEvent[] arg0) {
		// TODO Auto-generated method stub
		for (DebugEvent debugEvent : arg0) {
			/*
			 * 这里只关注Create 和 Terminal 两种事件 分别记录了程序 运行的开始和结束 debug 的开始和结束
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
				 * 在Mars 版本中 debug和normal  不能通过 process实力区分
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
					 * 保证create 和 terminal 顺序输出
					 */
					message = debugEvent.toString();
					if (judgeIfNeedLog(eventType, project, runType)) {
						/*
						 * 如果运行结束了 通知 console 将程序输出 记录下来
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
