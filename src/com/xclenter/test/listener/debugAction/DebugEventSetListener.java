package com.xclenter.test.listener.debugAction;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.IDebugEventSetListener;

public class DebugEventSetListener implements IDebugEventSetListener{

	/*
	*������ϵ������ģ����Լ�����������кͽ���
	*��Ҫ��Ҫ����Create �� terminal�¼�
	*
	*��Ҫ����������� ���ͬʱ���ж��progress  
	* - Ҳ������Ҫ��¼ÿ��progress��������
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
