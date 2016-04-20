package com.xclenter.test.listener.ui;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jface.text.IMarkSelection;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

import com.xclenter.test.util.selectDelta.TextSelectionRecorder;

public class SelectionListener implements ISelectionListener {

	private static Logger logger = LogManager.getLogger("MessageLog");

	private TextSelectionRecorder textSelectionRecorder;

	public SelectionListener() {
		textSelectionRecorder = TextSelectionRecorder
				.getTextSelectionRecorder();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub
		if (!selection.isEmpty()) {
			if (selection instanceof ITextSelection) {
				ITextSelection testSelction = (ITextSelection) selection;
//				log("text", part.getTitle(),
//						"offset-" + testSelction.getOffset() + "text-"
//								+ testSelction.getText() + "length-"
//								+ testSelction.getLength());
				
				String filePath="";
				if(part instanceof IEditorPart){
					IEditorPart editorPart = (IEditorPart) part;
					filePath = editorPart.getEditorInput().getToolTipText();
				}else{
					filePath = part.getTitle();
				}
				
				textSelectionRecorder.recordTextSelection(filePath,
						testSelction.getOffset(), testSelction.getLength(),
						testSelction.getText());
			} else if (selection instanceof IStructuredSelection) {
				IStructuredSelection structuredSelction = (IStructuredSelection) selection;
				log("structured", part.getTitle(),
						structuredSelction.toString());
			} else if (selection instanceof IMarkSelection) {
				IMarkSelection testSelction = (IMarkSelection) selection;
				log("mark", part.getTitle(), testSelction.toString());
			}
		}
	}

	private void log(String type, String part, String selectionContent) {
		logger.info("type::" + type + "::part::" + part + "::selctionContent::"
				+ selectionContent);
	}

}

