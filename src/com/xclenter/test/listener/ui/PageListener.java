package com.xclenter.test.listener.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IWorkbenchPage;

public class PageListener implements IPageListener{
	
	private static Logger logger = LogManager.getLogger("MessageLog");
	
	@Override
	public void pageActivated(IWorkbenchPage page) {
		log(page.getLabel(),"active");
	}

	@Override
	public void pageClosed(IWorkbenchPage page) {
		log(page.getLabel(),"close");
	}

	@Override
	public void pageOpened(IWorkbenchPage page) {
		log(page.getLabel(),"opened");
	}
	
	private void log(String pageLabel,String state){
		logger.info(":: action_type ::operation:: operation_type ::page:: state ::"+state+":: pageLabel ::" + pageLabel);

	}

}
