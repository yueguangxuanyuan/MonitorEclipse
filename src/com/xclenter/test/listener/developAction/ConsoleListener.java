package com.xclenter.test.listener.developAction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleListener;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleInputStream;

public class ConsoleListener implements IConsoleListener{

	@Override
	public void consolesAdded(IConsole[] arg0) {
		// TODO Auto-generated method stub
		System.out.println("console added[");
		for(IConsole console : arg0){
			System.out.println(console.getName());
//			if(console instanceof IOConsole){
//				IOConsole ioconsole=(IOConsole) console;
//				IOConsoleInputStream inputstream = ioconsole.getInputStream();
//				BufferedReader in= new BufferedReader(new InputStreamReader((InputStream)inputstream));
//				String instr = null;
//				try {
//					while (((instr = in.readLine()) != null)) {
//					    System.out.println(instr);
//					}
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			}
		}
		System.out.println("]");
	}

	@Override
	public void consolesRemoved(IConsole[] arg0) {
		// TODO Auto-generated method stub
		System.out.println("console removed[");
		for(IConsole console : arg0){
			System.out.println(console.getName());
		}
		System.out.println("]");
	}

}
