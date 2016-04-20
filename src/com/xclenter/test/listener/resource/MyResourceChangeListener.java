package com.xclenter.test.listener.resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

import com.xclenter.test.util.documentDelta.DocumentDeltaRecorder;

public class MyResourceChangeListener implements IResourceChangeListener {
	private static Logger logger = LogManager.getLogger("MessageLog");
	@Override
	public void resourceChanged(IResourceChangeEvent resourceChangeEvent) {
		 IResource resource = resourceChangeEvent.getResource();
		switch (resourceChangeEvent.getType()) {
		case IResourceChangeEvent.POST_CHANGE:
			log("post change",resource);
			//分析delta并读取出来
			IResourceDelta resourceDelta = resourceChangeEvent.getDelta();
			try {
				resourceDelta.accept(new DeltaPrinter());
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case IResourceChangeEvent.POST_BUILD:
			log("post build",resource);
			break;
		case IResourceChangeEvent.PRE_BUILD:
			log("pre build",resource);
			break;
		case IResourceChangeEvent.PRE_CLOSE:
			log("pre close",resource);
			break;
		case IResourceChangeEvent.PRE_DELETE:
			log("pre delete",resource);
			break;
		case IResourceChangeEvent.PRE_REFRESH:
			log("pre refresh",resource);
			break;
		default:
			log("unknow type",resource);

		}

	}
	
	private void log(String type, IResource resource){
		if(resource != null){
			logger.info("type:"+type+"::" + resource.getFullPath());
		}
	}
}

class DeltaPrinter implements IResourceDeltaVisitor {
	
	private static Logger logger = LogManager.getLogger("MessageLog");
	private DocumentDeltaRecorder documentDeltaRecorder;

	public DeltaPrinter(){
		documentDeltaRecorder = DocumentDeltaRecorder.getDocumentDeltaRecorder();
	}
    public boolean visit(IResourceDelta delta) {
       IResource res = delta.getResource();
       boolean doVisitChildren = true;
       switch (delta.getKind()) {
          case IResourceDelta.ADDED:
             logger.info("Resource::" +res.getFullPath()  + "::added");
             break;
          case IResourceDelta.REMOVED:
             logger.info("Resource::"+ res.getFullPath() +"::removed");
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
        		 logger.info("Resource::" +res.getFullPath() +"::changed ");
                 break;
             default:
        	 }
             break;
       }
       return doVisitChildren; // visit the children
    }
 }
