package com.xclenter.test.listener.developAction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;

import com.xclenter.test.log.documentDelta.DocumentDeltaRecorder;
import com.xclenter.test.util.resource.ResourceUtil;
import com.xclenter.test.util.saveFile.FileUtil;

public class DeltaPrinter implements IResourceDeltaVisitor {
	
	private static Logger logger = LogManager.getLogger("MessageLog");
	private DocumentDeltaRecorder documentDeltaRecorder;

	public DeltaPrinter(){
		documentDeltaRecorder = DocumentDeltaRecorder.getDocumentDeltaRecorder();
	}
	private void log(String type,String fileFullPath,String resourceType,String fileRelatePath){
		logger.info(":: action_type ::edit:: operation_type ::resource:: type ::"+type+":: resource_path ::" + fileFullPath+":: resourceType ::"+resourceType+":: fileRelatePath ::" + fileRelatePath);
	}
	private void log(String type,String fileFullPath,String resourceType){
		logger.info(":: action_type ::edit:: operation_type ::resource:: type ::"+type+":: resource_path ::" + fileFullPath+":: resourceType ::"+resourceType);
	}
    public boolean visit(IResourceDelta delta) {
       IResource res = delta.getResource();
       boolean doVisitChildren = true;
       switch (delta.getKind()) {
          case IResourceDelta.ADDED:
        	  String resouceType = ResourceUtil.getResourceType(res);
        	  /*
        	   * 如果是文件 就需要保存文件的内容 
        	   */
        	  if(resouceType.equals("File")){
        		  String fileRelatePath = FileUtil.saveMiddleFile(res.getFullPath().toString(),res.getLocation().toOSString());;
        		  log("added",res.getFullPath().toString(), resouceType,fileRelatePath);
        	  }else{
        		  log("added",res.getFullPath().toString(), resouceType);
        	  }
        	  
             break;
          case IResourceDelta.REMOVED:
             log("removed",res.getFullPath().toString(),ResourceUtil.getResourceType(res));
             break;
          case IResourceDelta.CHANGED:
        	 switch(delta.getFlags()){
        	 case IResourceDelta.CONTENT:
        		 
        		 break;
             default:
        	 }
        	 switch(res.getType()){
        	 case IResource.FILE:
        		 documentDeltaRecorder.notifyFlushLog(res.getFullPath().toString());;
        		  log("changed",res.getFullPath().toString(),ResourceUtil.getResourceType(res));
                 break;
             default:
        	 }
             break;
       }
       return doVisitChildren; // visit the children
    }
 }