package com.xclenter.test.listener.developAction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;

import com.xclenter.test.util.ResourceUtil;

public class ResourceChangeListener implements IResourceChangeListener {
	private static Logger logger = LogManager.getLogger("MessageLog");
	
	@Override
	public void resourceChanged(IResourceChangeEvent resourceChangeEvent) {
		IResource resource = resourceChangeEvent.getResource();
		switch (resourceChangeEvent.getType()) {
		case IResourceChangeEvent.POST_CHANGE:
			log("post change", resource);
			// 分析delta并读取出来
			IResourceDelta resourceDelta = resourceChangeEvent.getDelta();
			try {
				resourceDelta.accept(new DeltaPrinter());
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case IResourceChangeEvent.POST_BUILD:
			log("post build", resource);
			break;
		case IResourceChangeEvent.PRE_BUILD:
			log("pre build", resource);
			break;
		case IResourceChangeEvent.PRE_CLOSE:
			log("pre close", resource);
			break;
		case IResourceChangeEvent.PRE_DELETE:
			log("pre delete", resource);
			break;
		case IResourceChangeEvent.PRE_REFRESH:
			log("pre refresh", resource);
			break;
		default:
			log("unknow type", resource);

		}

	}

	private void log(String type, IResource resource) {
		if (resource != null) {
			logger.info(":: action_type ::edit:: operation_type ::resource:: type ::" + type + ":: resource_path ::"
					+ resource.getFullPath() + ":: resourceType ::" + ResourceUtil.getResourceType(resource));
		}
	}
}
