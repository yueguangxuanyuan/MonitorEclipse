package com.xclenter.test.listener.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;

public class WorkbenchListener implements IWorkbenchListener{
	private static Logger logger = LogManager.getLogger("MessageLog");
	@Override
	public boolean preShutdown(IWorkbench workbench, boolean forced) {
		
		return true;
	}

	@Override
	public void postShutdown(IWorkbench workbench) {

	}

}
