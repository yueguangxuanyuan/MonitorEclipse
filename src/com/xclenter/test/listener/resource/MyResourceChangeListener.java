package com.xclenter.test.listener.resource;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

import com.xclenter.test.util.documentDelta.DocumentDeltaRecorder;

public class MyResourceChangeListener implements IResourceChangeListener {
	@Override
	public void resourceChanged(IResourceChangeEvent resourceChangeEvent) {
		 IResource resource = resourceChangeEvent.getResource();
		switch (resourceChangeEvent.getType()) {
		case IResourceChangeEvent.POST_CHANGE:
			//System.out.println("post change " + this.getClass().getName());
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
			System.out.println("post build " + this.getClass().getName());
			break;
		case IResourceChangeEvent.PRE_BUILD:
			System.out.println("pre build " + this.getClass().getName());
			break;
		case IResourceChangeEvent.PRE_CLOSE:
			 System.out.print("Project ");
             System.out.print(resource.getFullPath());
             System.out.println(" is about to close.");
			break;
		case IResourceChangeEvent.PRE_DELETE:
			System.out.print("Project ");
            System.out.print(resource.getFullPath());
            System.out.println(" is about to be deleted.");
			break;
		case IResourceChangeEvent.PRE_REFRESH:
			System.out.println("pre refresh " + this.getClass().getName());
			break;
		default:
			System.out.println("unknow type " + this.getClass().getName());

		}

	}
}

class DeltaPrinter implements IResourceDeltaVisitor {
	DocumentDeltaRecorder documentDeltaRecorder;
	
	public DeltaPrinter(){
		documentDeltaRecorder = DocumentDeltaRecorder.getDocumentDeltaRecorder();
	}
    public boolean visit(IResourceDelta delta) {
       IResource res = delta.getResource();
       boolean doVisitChildren = true;
       switch (delta.getKind()) {
          case IResourceDelta.ADDED:
             System.out.println("Resource " +res.getFullPath()  + "added");
             break;
          case IResourceDelta.REMOVED:
             System.out.println("Resource "+ res.getFullPath() +" removed");
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
        		 System.out.println("Resource " +res.getFullPath() +" changed ");
                 break;
             default:
        	 }
             break;
       }
       return doVisitChildren; // visit the children
    }
 }
