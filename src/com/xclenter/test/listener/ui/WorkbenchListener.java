package com.xclenter.test.listener.ui;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;

public class WorkbenchListener implements IWorkbenchListener{

	@Override
	public boolean preShutdown(IWorkbench workbench, boolean forced) {
		System.out.println(this.getClass().getName() +" preshutdown");
		return true;
	}

	@Override
	public void postShutdown(IWorkbench workbench) {
		System.out.println(this.getClass().getName() +" postshutdown");

	}

}
