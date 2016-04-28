package com.xclenter.test.listener.developAction;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;

/*
 *  如果修改没有保存的话  这样记录到的是无效的修改
 *  这个方法协助记录到的结果需要通过与MyResourceChange监听到的结果交互之后 得到最终的有效操作序列
 */
import com.xclenter.test.util.documentDelta.DocumentDeltaRecorder;
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
