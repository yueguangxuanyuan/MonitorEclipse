package com.xclenter.test.listener.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchWindow;

import com.xclenter.test.util.addlistener.ui.UIAddListenerUtil;

public class WindowListener implements IWindowListener{
	private static Logger logger = LogManager.getLogger("MessageLog");
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
		logger.info("window::"+window.getActivePage().getLabel() +"::opened");
		UIAddListenerUtil.addWindowListener(window);
	}

}
