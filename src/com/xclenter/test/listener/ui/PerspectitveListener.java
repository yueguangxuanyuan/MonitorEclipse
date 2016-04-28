package com.xclenter.test.listener.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IWorkbenchPage;

//还有3左右的其他接口
public class PerspectitveListener implements IPerspectiveListener{
	private static Logger logger = LogManager.getLogger("MessageLog");
	@Override
	public void perspectiveActivated(IWorkbenchPage page,
			IPerspectiveDescriptor perspective) {
		log(perspective.getId(),"actived");
	}

	@Override
	public void perspectiveChanged(IWorkbenchPage page,
			IPerspectiveDescriptor perspective, String changeId) {
		log(perspective.getId(),"deactived");
	}
	
	private void log(String perspectiveLabel,String state){
		logger.info(":: action_type ::operation:: operation_type ::perspertive:: state ::"+state+":: perspectiveLabel ::" + perspectiveLabel);
	}
}
