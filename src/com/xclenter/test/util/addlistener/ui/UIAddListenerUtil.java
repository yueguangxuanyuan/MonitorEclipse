package com.xclenter.test.util.addlistener.ui;

import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import com.xclenter.test.listener.ui.PartListener;
import com.xclenter.test.listener.ui.PerspectitveListener;


public class UIAddListenerUtil {

/*
 * ���Ĭ�ϵ�windowlistener 
 * ��Ҫ��workbench���� �Լ�  �´�window��ʱ�����
 */
	public static void addWindowListener(IWorkbenchWindow workbenchWindow){
		//��ȡĬ�ϵ�perspective   ������ ���һ������
    	IWorkbenchPage workbenchPage = workbenchWindow.getActivePage();
    	System.out.println("default perspective : "+workbenchPage.getPerspective().getLabel());
    	
    	//���page ��perspective������
    	workbenchWindow.addPerspectiveListener(new PerspectitveListener());
    	//workbenchWindow.addPageListener(new PageListener());
    	
    	//����part���������� 
    	IPartService partService = workbenchWindow.getPartService();
    	partService.addPartListener(new PartListener());
	}
}
