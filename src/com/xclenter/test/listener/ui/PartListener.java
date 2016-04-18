package com.xclenter.test.listener.ui;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

import com.xclenter.test.util.addlistener.developAction.DevelopActionAddListenerUtil;
import com.xclenter.test.util.context.ContextUtil;

public class PartListener implements IPartListener{
	private ContextUtil contextUtil;
	
	public PartListener() {
		contextUtil = ContextUtil.getContextUtil();
	}
	
	@Override
	public void partActivated(IWorkbenchPart part) {
		if (part instanceof IEditorPart) {
			IEditorPart editorPart = (IEditorPart) part;
			System.out.println("partListener editor active :site: " + editorPart.getEditorInput().getToolTipText() );
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
		System.out.println(part.getTitle() + "   closed : partListener");
		if (part instanceof IEditorPart) {
			IEditorPart editorPart = (IEditorPart) part;
			System.out.println("partListener editor closed :site: " + editorPart.getEditorInput().getToolTipText() );
		}
		return;
	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
		return;
	}

	@Override
	public void partOpened(IWorkbenchPart part) {
		System.out.println(part.getTitle() + "   opened : partListener");
		if (part instanceof IEditorPart) {
			IEditorPart editorPart = (IEditorPart) part;
			DevelopActionAddListenerUtil.addDocumentListener(editorPart);
		}
		return;
	}

}
