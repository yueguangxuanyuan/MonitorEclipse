package com.xclenter.test.listener.ui;

import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IWorkbenchPage;

public class PageListener implements IPageListener{

	@Override
	public void pageActivated(IWorkbenchPage page) {
		
		System.out.println(page.getLabel() + "   active : pageListener");
		
	}

	@Override
	public void pageClosed(IWorkbenchPage page) {
		
		System.out.println(page.getLabel() + "   close : pageListener");

	}

	@Override
	public void pageOpened(IWorkbenchPage page) {
		
		System.out.println(page.getLabel() + "   opened : pageListener");
		
	}

}
