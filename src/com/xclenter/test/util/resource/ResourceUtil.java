package com.xclenter.test.util.resource;

import org.eclipse.core.resources.IResource;

public class ResourceUtil {
	public static String  getResourceType(IResource resource){
		String resourceType;
		if(resource == null){
			return "NullResource";
		}
		switch (resource.getType()) {
		case IResource.FILE:
			resourceType = "File";
			break;
		case IResource.FOLDER:
			resourceType = "Folder";
			break;
		case IResource.PROJECT:
			resourceType = "Project";
			break;
		case IResource.ROOT:
			resourceType = "Root";
			break;
		default:
			resourceType = "Unknow";
			break;
		}
		return resourceType;
	}
}
