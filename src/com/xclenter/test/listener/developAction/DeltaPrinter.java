package com.xclenter.test.listener.developAction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;

import com.xclenter.test.log.documentDelta.DocumentDeltaRecorder;
import com.xclenter.test.util.resource.ResourceUtil;
import com.xclenter.test.util.saveFile.SaveFileUtil;

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
        		  String fileRelatePath = SaveFileUtil.saveMiddleFile(res.getFullPath().toString(),res.getLocation().toOSString());;
        		  log("added",res.getFullPath().toString(), resouceType,fileRelatePath);
        	  }else{
        		  log("added",res.getFullPath().toString(), resouceType);
        	  }
        	  
             break;
          case IResourceDelta.REMOVED:
             log("removed",res.getFullPath().toString(),ResourceUtil.getResourceType(res));
             break;
          case IResourceDelta.CHANGED:
        	 switch(res.getType()){
        	 case IResource.FILE:
        		 if(delta.getFlags() == IResourceDelta.CONTENT){
        			 /*
            		  * 当出现文件内容修改的时候 将该文件的变化刷入硬盘中
            		  */
            		 documentDeltaRecorder.notifyFlushLog(res.getFullPath().toString());;
//            		 IFile file = (IFile) res;
//            		 IFileState[] states =  null;
//            		 try{
//            			 states = file.getHistory(null);
//            		 }catch (Throwable _e) {
//            		 }
//            		 if(states != null && states.length > 0){
//            			 System.out.println(states[0].getModificationTime() + "-" + System.currentTimeMillis());
//            		 }
            		 
            		 log("changed",res.getFullPath().toString(),ResourceUtil.getResourceType(res));
        		 }
        		 break;
             default:
        	 }
             break;
       }
       return doVisitChildren; // visit the children
    }
 }