package com.xclenter.test.listener.developAction.build;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;

import com.xclenter.test.log.BuildRecorder;

public class BuildConsoleDocumentListener implements IDocumentListener{
	
	private String projectName;
	private BuildRecorder buildRecorder;
	public  BuildConsoleDocumentListener(String  projectName) {
		this.projectName = projectName;
		buildRecorder = BuildRecorder.getBuildRecorder();
	}
	
	@Override
	public void documentAboutToBeChanged(DocumentEvent event) {

	}

	@Override
	public void documentChanged(DocumentEvent event) {
		buildRecorder.RecordMessage(projectName, event.fText);
	}

}
