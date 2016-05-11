package com.xclenter.test.listener.developAction;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;

import com.xclenter.test.log.documentDelta.DocumentDeltaRecorder;
public class DocumentListener implements IDocumentListener{
	private DocumentDeltaRecorder documentDeltaRecorder;
	
	public  DocumentListener() {
		documentDeltaRecorder = DocumentDeltaRecorder.getDocumentDeltaRecorder();
	}
	
	@Override
	public void documentAboutToBeChanged(DocumentEvent event) {

	}

	@Override
	public void documentChanged(DocumentEvent event) {
		try {
			IDocument document = event.getDocument();
			int offset = event.fOffset;
			int line = document.getLineOfOffset(offset);
			int lineOffset = document.getLineOffset(line);
			line ++;
			lineOffset = event.fOffset-lineOffset ;
			documentDeltaRecorder.notifyDocumentChange(line,event.fOffset, lineOffset, offset, event.fLength, event.fText);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
