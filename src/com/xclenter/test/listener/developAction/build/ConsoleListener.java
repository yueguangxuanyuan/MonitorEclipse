package com.xclenter.test.listener.developAction.build;


import org.eclipse.cdt.internal.ui.buildconsole.BuildConsole;
import org.eclipse.cdt.ui.IBuildConsoleManager;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleListener;
import org.eclipse.ui.console.IOConsole;


public class ConsoleListener implements IConsoleListener{

	@Override
	public void consolesAdded(IConsole[] arg0) {
		// TODO Auto-generated method stub
//		System.out.println("console added[");
		for(IConsole console : arg0){
//			System.out.println(console.getName()+ console.getClass().getName());
			if(console instanceof IOConsole){
				IOConsole ioconsole=(IOConsole) console;
			}
			//cpp ±àÒë´°¿Ú
			if(console instanceof BuildConsole){
				BuildConsole buildConsole = (BuildConsole) console;
				IBuildConsoleManager buildConsoleManager = buildConsole.getConsoleManager();
				buildConsoleManager.addConsoleListener(new BuildConsoleListener(buildConsoleManager));
			}
		}
//		System.out.println("]");
	}

	@Override
	public void consolesRemoved(IConsole[] arg0) {
		// TODO Auto-generated method stub
//		System.out.println("console removed[");
//		for(IConsole console : arg0){
//			System.out.println(console.getName());
//		}
//		System.out.println("]");
	}

}
