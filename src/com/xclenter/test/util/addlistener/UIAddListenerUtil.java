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
 * ���Ĭ�ϵ�windowlistener 
 * ��Ҫ��workbench���� �Լ�  �´�window��ʱ�����
 */
	public static void addWindowListener(IWorkbenchWindow workbenchWindow){
		//��ȡĬ�ϵ�perspective   ������ ���һ������
    	IWorkbenchPage workbenchPage = workbenchWindow.getActivePage();
    	logger.info(":: action_type ::operation:: operation_type ::perspertive:: state ::actived:: perspectiveLabel ::"+workbenchPage.getPerspective().getLabel());
    	
    	//���page ��perspective������
    	workbenchWindow.addPerspectiveListener(new PerspectitveListener());
    	//workbenchWindow.addPageListener(new PageListener());
    	
    	//����part���������� 
    	IPartService partService = workbenchWindow.getPartService();
    	partService.addPartListener(new PartListener());
    	
    	//���selection����
    	workbenchPage.addSelectionListener(new SelectionListener());
	}
}
