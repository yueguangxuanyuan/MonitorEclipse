package com.xclenter.test.listener.debugAction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.core.resources.IResource;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.IDebugEventSetListener;

import com.xclenter.test.util.resource.ResourceUtil;

public class DebugEventSetListener implements IDebugEventSetListener {
	private static Logger logger = LogManager.getLogger("MessageLog");

	/*
	 * 这里联系上下文模块可以监听程序的运行和结束需要主要监听Create 和 terminal事件
	 * 
	 * 需要解决的问题是 如果同时运行多个progress - 也就是需要记录每个progress的上下文
	 */

	@Override
	public void handleDebugEvents(DebugEvent[] arg0) {
		// TODO Auto-generated method stub
		StringBuilder message = new StringBuilder();
		message.append("getDebugEvent[");
		for (DebugEvent debugEvent : arg0) {
			message.append( debugEvent.toString() + ",");
		}
		message.append("]");
		log(message.toString());
	}

	private void log(String message) {
		logger.info(":: action_type ::run:: message ::"+message);
	}
}
