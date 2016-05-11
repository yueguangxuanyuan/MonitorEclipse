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
	 * ������ϵ������ģ����Լ�����������кͽ�����Ҫ��Ҫ����Create �� terminal�¼�
	 * 
	 * ��Ҫ����������� ���ͬʱ���ж��progress - Ҳ������Ҫ��¼ÿ��progress��������
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
