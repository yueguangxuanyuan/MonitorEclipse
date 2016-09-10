package com.xclenter.test.util.addlistener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;

import com.xclenter.test.listener.developAction.SelectionListener;
import com.xclenter.test.listener.ui.PartListener;
import com.xclenter.test.listener.ui.PerspectitveListener;


public class UIAddListenerUtil {
	private static Logger logger = LogManager.getLogger("MessageLog");
/*
 * 添加默认的windowlistener 
 * 主要在workbench开启 以及  新打开window的时候调用
 */
	public static void addWindowListener(IWorkbenchWindow workbenchWindow){
		//获取默认的perspective   ！待定 最后不一定有用
    	IWorkbenchPage workbenchPage = workbenchWindow.getActivePage();
    	logger.info(":: action_type ::operation:: operation_type ::perspertive:: state ::actived:: perspectiveLabel ::"+workbenchPage.getPerspective().getLabel());
    	
    	//添加page 和perspective监听器
    	workbenchWindow.addPerspectiveListener(new PerspectitveListener());
    	//workbenchWindow.addPageListener(new PageListener());
    	
    	//监听part的生命周期 
    	IPartService partService = workbenchWindow.getPartService();
    	partService.addPartListener(new PartListener());
    	
    	//添加selection监听
    	workbenchPage.addSelectionListener(new SelectionListener());
	}
}
