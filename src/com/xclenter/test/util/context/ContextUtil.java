package com.xclenter.test.util.context;

import com.xclenter.test.util.documentDelta.DocumentDeltaRecorder;
import com.xclenter.test.util.selectDelta.TextSelectionRecorder;

//�����Ĺ���ģ�� ����ͳһ����������
public class ContextUtil {
	private String projectName;
	private String fileName;
	private String filePath;
	private String fileFullPath;
	private static ContextUtil contextUtil;

	/*
	 * ֪ͨ�����ĵĸı�
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
		 * ��¼�ļ��ĸı�
		 */
		if (fileFullPath != null && fileFullPath.startsWith("/") && !this.fileFullPath.equals(fileFullPath)) {
			int firstDelimeter = fileFullPath.indexOf("/",1);
			int lastDelimeter = fileFullPath.lastIndexOf("/");
			/*
			 * �ų��������� projectname/[filepath/]filename��������� ���������
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
