package com.xclenter.test.listener.debugAction;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.IDebugEventSetListener;

public class DebugEventSetListener implements IDebugEventSetListener{

	/*
	*这里联系上下文模块可以监听程序的运行和结束
	*需要主要监听Create 和 terminal事件
	*
	*需要解决的问题是 如果同时运行多个progress  
	* - 也就是需要记录每个progress的上下文
	*/
	
	@Override
	public void handleDebugEvents(DebugEvent[] arg0) {
		// TODO Auto-generated method stub
		System.out.print("getDebugEvent");
		for(DebugEvent debugEvent : arg0){
			System.out.print("-|"+debugEvent.toString()+"|-");
		}
		System.out.println();
	}

}
