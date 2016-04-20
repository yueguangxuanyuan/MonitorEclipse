package com.xclenter.test.util.context;

import com.xclenter.test.util.documentDelta.DocumentDeltaRecorder;
import com.xclenter.test.util.selectDelta.TextSelectionRecorder;

//上下文管理模块 用来统一管理上下文
public class ContextUtil {
	private String projectName;
	private String fileName;
	private String filePath;
	private String fileFullPath;
	private static ContextUtil contextUtil;

	/*
	 * 通知上下文的改变
	 */
	private DocumentDeltaRecorder documentDeltaRecorder;

	private ContextUtil() {
		projectName = "undefined";
		fileName = "undefined";
		filePath = "undefined";
		fileFullPath = "undefined";

		documentDeltaRecorder = DocumentDeltaRecorder
				.getDocumentDeltaRecorder();
	}

	public static ContextUtil getContextUtil() {
		if (contextUtil == null) {
			contextUtil = new ContextUtil();
		}
		return contextUtil;
	}

	public String getProjectName() {
		return projectName;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void notifyEditorFileContextMayChange(String fileFullPath) {
		/*
		 * 记录文件的改变
		 */
		if (fileFullPath != null && fileFullPath.startsWith("/") && !this.fileFullPath.equals(fileFullPath)) {
			int firstDelimeter = fileFullPath.indexOf("/",1);
			int lastDelimeter = fileFullPath.lastIndexOf("/");
			/*
			 * 排除掉不符合 projectname/[filepath/]filename这样的情况 这样的组合
			 */
			if (firstDelimeter <= 0 || lastDelimeter < 0
					|| lastDelimeter >= (fileFullPath.length() - 1)) {
				return;
			}

			projectName = fileFullPath.substring(1, firstDelimeter);
			filePath = fileFullPath.substring(firstDelimeter + 1);
			fileName = fileFullPath.substring(lastDelimeter + 1);
			this.fileFullPath = fileFullPath;
			documentDeltaRecorder
					.notifyContextChange(projectName, fileFullPath);
		}
	}
}
