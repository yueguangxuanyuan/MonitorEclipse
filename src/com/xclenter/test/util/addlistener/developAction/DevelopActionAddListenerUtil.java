package com.xclenter.test.util.addlistener.developAction;

import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;

import com.xclenter.test.listener.developAction.DocumentListener;
import com.xclenter.test.util.context.ContextUtil;


public class DevelopActionAddListenerUtil {
	private static ContextUtil contextUtil;
	
	static{
		contextUtil = ContextUtil.getContextUtil();
	}
	

/*
 *  
 */
	public static void addDocumentListener(IEditorPart editorPart){
		contextUtil.notifyEditorFileContextMayChange("/"+editorPart.getEditorInput().getToolTipText());
		System.out.println("addDocumentListener :site:" + editorPart.getEditorInput().getToolTipText() );
		ITextEditor ite = (ITextEditor)editorPart;
		IDocument doc = ite.getDocumentProvider().getDocument(ite.getEditorInput());
		doc.addDocumentListener(new DocumentListener());
	}
}
