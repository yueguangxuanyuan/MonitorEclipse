package com.xclenter.test.listener.ui;

import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IWorkbenchPage;

//还有3左右的其他接口
public class PerspectitveListener implements IPerspectiveListener{

	@Override
	public void perspectiveActivated(IWorkbenchPage page,
			IPerspectiveDescriptor perspective) {
		System.out.println(page.getLabel() +" ----"+ perspective.getId() +"     active");
	}

	@Override
	public void perspectiveChanged(IWorkbenchPage page,
			IPerspectiveDescriptor perspective, String changeId) {
		System.out.println(page.getLabel() +" ----"+ perspective.getId() +"     deactive");
	}

}
