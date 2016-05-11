package com.xclenter.test.listener.developAction.build;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.cdt.ui.IBuildConsoleEvent;
import org.eclipse.cdt.ui.IBuildConsoleListener;
import org.eclipse.cdt.ui.IBuildConsoleManager;
import org.eclipse.jface.text.IDocument;


public class BuildConsoleListener implements IBuildConsoleListener{
/*
 * 记录已经添加了 Listener 的 工程  避免 DcoumentListener 重复添加
 */
	private IBuildConsoleManager buildConsoleManager;
	
	private Set<String> recordedProjects;
	public BuildConsoleListener(IBuildConsoleManager buildConsoleManager){
		this.buildConsoleManager = buildConsoleManager;
		recordedProjects = new HashSet<String>();
	}
	
	public void consoleChange(IBuildConsoleEvent arg0) {
		
		String projectName = arg0.getProject().getName();
		if(arg0.getType()== IBuildConsoleEvent.CONSOLE_START && recordedProjects.add(projectName)){
			IDocument document =buildConsoleManager.getConsoleDocument(arg0.getProject());
			document.addDocumentListener(new BuildConsoleDocumentListener(projectName));
		}
	}
}
