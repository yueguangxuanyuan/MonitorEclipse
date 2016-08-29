package com.xclenter.test.listener.developAction.build;

import org.eclipse.cdt.internal.ui.buildconsole.BuildConsole;
import org.eclipse.cdt.ui.IBuildConsoleManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.internal.ui.views.console.ProcessConsole;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleListener;

import com.xclenter.test.log.debug.ProcessConsoleRecorder;

public class ConsoleListener implements IConsoleListener {
	String workspaceRootPath;
	String fileSperator;
	
	private ProcessConsoleRecorder processConsoleRecorder ;
	
	public ConsoleListener() {
		workspaceRootPath = Platform.getLocation().toOSString();
		fileSperator = System.getProperty("file.separator");
		processConsoleRecorder = ProcessConsoleRecorder.getProcessConsoleRecorder();
	}

	@Override
	public void consolesAdded(IConsole[] arg0) {
		// TODO Auto-generated method stub
//		System.out.println("console added[");
		for (IConsole console : arg0) {
//			System.out
//					.println(console.getName() +"***"+ console.getClass().getName());
			/*
			 * normal运行时的输出窗口
			 */
			if (console instanceof ProcessConsole) {
				ProcessConsole processConsole = (ProcessConsole) console;
				String progressLabel = processConsole.getProcess().getLabel();
				String project = "unknown";
				String runType = "unknow";
				if (progressLabel != null
						&& progressLabel.startsWith(workspaceRootPath)) {
					runType = "normal";
					progressLabel = progressLabel.substring(workspaceRootPath
							.length());

					int splitIndex = progressLabel.indexOf(fileSperator,
							fileSperator.length());
					if (splitIndex > 0 && splitIndex < progressLabel.length()) {
						project = progressLabel.substring(
								fileSperator.length(), splitIndex);
					}
				}else if(progressLabel != null
						&& progressLabel.endsWith(".exe")){
					runType = "debug";
					int splitIndex = progressLabel.indexOf(".exe");
					if (splitIndex > 0
							&& splitIndex < progressLabel.length()) {
						project = progressLabel.substring(0, splitIndex);
					}
				}
				if(!project.equals("unknown")){
					String progressId = processConsole.getProcess().toString();
					String projectId = project + progressId.substring(progressId.lastIndexOf("@"));
					processConsoleRecorder.RecordDocument(projectId, processConsole.getDocument(),runType);
				}
			}
			// cpp 编译窗口
			if (console instanceof BuildConsole) {
				BuildConsole buildConsole = (BuildConsole) console;
				IBuildConsoleManager buildConsoleManager = buildConsole
						.getConsoleManager();
				buildConsoleManager
						.addConsoleListener(new BuildConsoleListener(
								buildConsoleManager));
			}
		}
		// System.out.println("]");
	}

	@Override
	public void consolesRemoved(IConsole[] arg0) {
		// TODO Auto-generated method stub
		// System.out.println("console removed[");
		// for(IConsole console : arg0){
		// System.out.println(console.getName());
		// }
		// System.out.println("]");
	}

}
