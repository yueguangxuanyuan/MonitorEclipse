package com.xclenter.test.listener.developAction;

import java.util.HashSet;

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
	private HashSet<String> fileExtensionFilter;

	public DeltaPrinter() {
		documentDeltaRecorder = DocumentDeltaRecorder
				.getDocumentDeltaRecorder();

		fileExtensionFilter = new HashSet<String>();
		fileExtensionFilter.add("cpp");
		fileExtensionFilter.add("c");
		fileExtensionFilter.add("h");

	}

	private void log(String type, String fileFullPath, String resourceType,
			String fileRelatePath) {
		logger.info(":: action_type ::edit:: operation_type ::resource:: type ::"
				+ type
				+ ":: resource_path ::"
				+ fileFullPath
				+ ":: resourceType ::"
				+ resourceType
				+ ":: fileRelatePath ::"
				+ fileRelatePath);
	}

	private void log(String type, String fileFullPath, String resourceType) {
		logger.info(":: action_type ::edit:: operation_type ::resource:: type ::"
				+ type
				+ ":: resource_path ::"
				+ fileFullPath
				+ ":: resourceType ::" + resourceType);
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
			if (resouceType.equals("File")) {
				String fileExtension = res.getFileExtension();
				if (fileExtension != null
						&& fileExtensionFilter.contains(fileExtension)) {
					String fileRelatePath = SaveFileUtil.saveMiddleFile(res
							.getFullPath().toString(), res.getLocation()
							.toOSString());
					log("added", res.getFullPath().toString(), resouceType,
							fileRelatePath);
				}
			} else {
				log("added", res.getFullPath().toString(), resouceType);
			}

			break;
		case IResourceDelta.REMOVED:
			log("removed", res.getFullPath().toString(),
					ResourceUtil.getResourceType(res));
			break;
		case IResourceDelta.CHANGED:
			switch (res.getType()) {
			case IResource.FILE:
				if ((delta.getFlags() & IResourceDelta.CONTENT) > 0) {
					/*
					 * 当出现文件内容修改的时候 将该文件的变化刷入硬盘中
					 */
					documentDeltaRecorder.notifyFlushLog(res.getFullPath()
							.toString());
					;
					String fileExtension = res.getFileExtension();
					if (fileExtension != null
							&& fileExtensionFilter.contains(fileExtension)) {
						String fileRelatePath = SaveFileUtil.saveMiddleFile(res
								.getFullPath().toString(), res.getLocation()
								.toOSString());
						log("changed", res.getFullPath().toString(),
								ResourceUtil.getResourceType(res),
								fileRelatePath);
					}
				}
				break;
			default:
			}
			break;
		}
		return doVisitChildren; // visit the children
	}
}