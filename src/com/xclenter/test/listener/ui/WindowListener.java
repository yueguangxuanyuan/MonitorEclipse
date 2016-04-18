package com.xclenter.test.listener.ui;

import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchWindow;

import com.xclenter.test.util.addlistener.ui.UIAddListenerUtil;

public class WindowListener implements IWindowListener{

	@Override
	public void windowActivated(IWorkbenchWindow window) {
		return;
	}

	@Override
	public void windowDeactivated(IWorkbenchWindow window) {
		return;
	}

	@Override
	public void windowClosed(IWorkbenchWindow window) {
		return;
	}

	@Override
	public void windowOpened(IWorkbenchWindow window) {
		System.out.println(window.getActivePage().getLabel() +"     opened: windowListener");
		UIAddListenerUtil.addWindowListener(window);
	}

}
