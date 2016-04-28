package com.xclenter.test.listener.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

import com.xclenter.test.util.addlistener.developAction.AddDocumentListenerUtil;
import com.xclenter.test.util.context.ContextUtil;

public class PartListener implements IPartListener{
	
	private static Logger logger = LogManager.getLogger("MessageLog");
	private ContextUtil contextUtil;
	
	public PartListener() {
		contextUtil = ContextUtil.getContextUtil();
	}
	
	@Override
	public void partActivated(IWorkbenchPart part) {
		log(part.getTitle(),"actived");
		if (part instanceof IEditorPart) {
			IEditorPart editorPart = (IEditorPart) part;
			contextUtil.notifyEditorFileContextMayChange("/"+editorPart.getEditorInput().getToolTipText());
		}	
		return;
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
		return;
	}

	@Override
	public void partClosed(IWorkbenchPart part) {
		log(part.getTitle(),"closed");
		return;
	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
		return;
	}

	@Override
	public void partOpened(IWorkbenchPart part) {
		log(part.getTitle(),"opened");
		if (part instanceof IEditorPart) {
			IEditorPart editorPart = (IEditorPart) part;
			AddDocumentListenerUtil.addDocumentListener(editorPart);
		}
		return;
	}
	
	private void log(String partLabel,String state){
		logger.info(":: action_type ::operation:: operation_type ::part:: state ::"+state+":: partLabel ::" + partLabel);

	}

}
