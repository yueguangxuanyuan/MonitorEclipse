package com.xclenter.test.listener.developAction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;;

public class ExecutionListener implements IExecutionListener{
	private static Logger logger = LogManager.getLogger("MessageLog");
	@Override
	public void notHandled(String arg0, NotHandledException arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postExecuteFailure(String arg0, ExecutionException arg1) {
		// TODO Auto-generated method stub
		log("fail-"+arg0 );
	}

	@Override
	public void postExecuteSuccess(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		log("success-"+arg0 );
	}

	@Override
	public void preExecute(String arg0, ExecutionEvent arg1) {
		// TODO Auto-generated method stub
		log("preexecute-"+arg0 );
	}
	
	private void log (String message){
		logger.info(":: action_type ::operation:: operation_type ::excution:: message ::"+message);
	}
}
