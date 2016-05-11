package com.xclenter.test.util.context;

import java.util.HashMap;
import java.util.Map;

import com.xclenter.test.log.documentDelta.DocumentDeltaRecorder;
import com.xclenter.test.log.selectDelta.TextSelectionRecorder;

//�����Ĺ���ģ�� ����ͳһ����������
public class ContextUtil implements IGetContext{
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

	@Override
	public Map getContext() {
		// TODO Auto-generated method stub
		Map context = new HashMap();
		context.put("projectName", projectName);
		context.put("filePath", filePath);
		context.put("fileName", fileName);
		context.put("fileFullPath", fileFullPath);
		return context;
	}
}
