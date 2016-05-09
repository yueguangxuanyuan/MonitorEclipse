package com.xclenter.test.listener.developAction;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.cdt.internal.core.model.CContainer;
import org.eclipse.cdt.internal.core.model.TranslationUnit;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.IMarkSelection;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

import com.xclenter.test.util.selectDelta.FileSelectionRecorder;
import com.xclenter.test.util.selectDelta.TextSelectionRecorder;

public class SelectionListener implements ISelectionListener {

	private static Logger logger = LogManager.getLogger("MessageLog");

	private TextSelectionRecorder textSelectionRecorder;
	
	private FileSelectionRecorder fileSelectionRecorder;

	public SelectionListener() {
		textSelectionRecorder = TextSelectionRecorder.getTextSelectionRecorder();
		fileSelectionRecorder = FileSelectionRecorder.getFileSelectionRecorder();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub
		if (!selection.isEmpty()) {
			if (selection instanceof ITextSelection) {
				ITextSelection testSelction = (ITextSelection) selection;

				String filePath = "";
				if (part instanceof IEditorPart) {
					IEditorPart editorPart = (IEditorPart) part;
					filePath = editorPart.getEditorInput().getToolTipText();
				} else {
					filePath = part.getTitle();
				}

				textSelectionRecorder.recordTextSelection(filePath, testSelction.getOffset(), testSelction.getLength(),
						testSelction.getText());
			} else if (selection instanceof IStructuredSelection) {
				IStructuredSelection structuredSelction = (IStructuredSelection) selection;
				Map<String,String> selectedFiles = new TreeMap();
				StringBuilder message = new StringBuilder();
				message.append("[");
				Iterator it = structuredSelction.iterator();
				while(it.hasNext()){
					Object temp = it.next();
					if(temp instanceof IProject){
						IProject project = (IProject) temp;
						message.append(project.getName() + " project," );
						selectedFiles.put(project.getName(), "project");
					}else if (temp instanceof TranslationUnit){
						TranslationUnit file = (TranslationUnit)temp;
						message.append(file.getPath() + " file," );
						selectedFiles.put(file.getPath().toString(), "file");
					}else if (temp instanceof CContainer){
						CContainer folder = (CContainer) temp;
						message.append(folder.getPath() + " folder," );
						selectedFiles.put(folder.getPath().toString(), "folder");
					}
				}
				message.append("]");
				log("structured", part.getTitle(), message.toString());
				fileSelectionRecorder.recordFileSelect(selectedFiles);
			} else if (selection instanceof IMarkSelection) {
				IMarkSelection markSelction = (IMarkSelection) selection;
				log("mark", part.getTitle(), markSelction.toString());
			}
		}
	}

	private void log(String type, String part, String selectionContent) {
		logger.info(":: action_type ::edit:: operation_type ::select:: type ::" + type + ":: part ::" + part
				+ ":: selctionContent ::" + selectionContent);
	}

}
