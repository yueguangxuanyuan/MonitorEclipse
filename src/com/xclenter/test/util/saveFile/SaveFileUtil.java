package com.xclenter.test.util.saveFile;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

public class SaveFileUtil {

	private static String projectSaveRootPath = "com.xclenter.monitor"
			+ File.separator + "project";

	private static String middleFileSaveRootPath = "com.xclenter.monitor"
			+ File.separator + "middleFile";

	private static void clearFileSaveSpace() {
		File projectSaveRootDir = new File(projectSaveRootPath);
		projectSaveRootDir.mkdirs();
		DeleteUtil.delAllFile(projectSaveRootPath);

		File middleFileSaveRootDir = new File(middleFileSaveRootPath);
		middleFileSaveRootDir.mkdirs();
		DeleteUtil.delAllFile(middleFileSaveRootPath);

	}

	public static String saveMiddleFile(String workspacePath, String OSPath) {
		String fileRelatePath = "unknown";
		File file = new File(OSPath);
		/*
		 * ȷ���ļ�����
		 */
		if (file.exists() && file.isFile()) {
			fileRelatePath = workspacePath.substring(1);
			String fileHash = HashUtil.getHash(OSPath, "MD5");
			fileHash += "-"+HashUtil.getHash(OSPath, "SHA1");
					
			fileRelatePath += "-" + fileHash;

			fileRelatePath += "-" + System.currentTimeMillis();
			
			int splitor = fileRelatePath.lastIndexOf("/");
			
			String targetPath = middleFileSaveRootPath+File.separator+fileRelatePath;
			
			CopyUtil.CopySingleFileTo(OSPath,targetPath);
		}
		return fileRelatePath;
	}

	private static void saveAllExistProjects() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProject[] projects = workspace.getRoot().getProjects();

		StringBuilder message = new StringBuilder();

		for (IProject project : projects) {
			CopyUtil.copyFolderWithSelf(project.getLocation().toOSString(),
					projectSaveRootPath);
		}
	}

	/*
	 * ��workbench startup��ʱ�򴥷�һ������ʼ���ļ����������� ���ԭ��Ŀ¼ �����й����ļ������ȥ
	 */
	public static void initSaveContext() {
		clearFileSaveSpace();
		saveAllExistProjects();
	}
}
