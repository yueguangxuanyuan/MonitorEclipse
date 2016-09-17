package com.xclenter.test.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

import com.xclenter.test.util.file.CopyUtil;
import com.xclenter.test.util.file.DeleteUtil;
import com.xclenter.test.util.file.FileUtil;
import com.xclenter.test.util.file.HashUtil;

public class FileDao {

	private static String middleFileSavePath;

	private static String projectSavePath;

	private static void clearFileSaveSpace() {
		File projectSaveRootDir = new File(projectSavePath);
		projectSaveRootDir.mkdirs();
		DeleteUtil.delAllFile(projectSavePath);

		File middleFileSaveRootDir = new File(middleFileSavePath);
		middleFileSaveRootDir.mkdirs();
		DeleteUtil.delAllFile(middleFileSavePath);

		File utilFileSaveRootDir = new File(FileUtil.utilFileSaveRootPath);
		utilFileSaveRootDir.mkdirs();

		File downloadFileSaveRootDir = new File(
				FileUtil.downloadFileSaveRootPath);
		downloadFileSaveRootDir.mkdirs();

		File tmpFileSaveRootDir = new File(FileUtil.tmpFileSaveRootPath);
		tmpFileSaveRootDir.mkdirs();
		DeleteUtil.delAllFile(FileUtil.tmpFileSaveRootPath);
	}

	public static String saveMiddleFile(String workspacePath, String OSPath) {
		String fileRelatePath = "unknown";
		File file = new File(OSPath);
		/*
		 * 确保文件存在
		 */
		if (file.exists() && file.isFile()) {
			fileRelatePath = workspacePath.substring(1);
			String fileHash = HashUtil.getHash(OSPath, "MD5");
			fileHash += "-" + HashUtil.getHash(OSPath, "SHA1");

			fileRelatePath += "-" + fileHash;

			fileRelatePath += "-" + System.currentTimeMillis();

			String targetPath = middleFileSavePath + File.separator
					+ fileRelatePath;

			CopyUtil.CopySingleFileTo(OSPath, targetPath);
		}
		return fileRelatePath;
	}

	private static void saveAllExistProjects() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProject[] projects = workspace.getRoot().getProjects();

		for (IProject project : projects) {
			CopyUtil.copyFolderWithSelf(project.getLocation().toOSString(),
					projectSavePath);
		}
	}

	/*
	 * 在workbench startup的时候触发一次来初始化文件保存上下文 清空原有目录 将现有工程文件保存进去
	 */
	public static void initSaveContext() {

		Date date = new Date();
		projectSavePath = FileUtil.projectSaveRootPath + File.separator
				+ date.getTime();
		middleFileSavePath = FileUtil.middleFileSaveRootPath + File.separator
				+ date.getTime();

		clearFileSaveSpace();
		saveAllExistProjects();
	}

	public static void clearLegacy() {
		File projectSaveRootDir = new File(FileUtil.projectSaveRootPath);
		projectSaveRootDir.mkdirs();
		String[] savedProjects = projectSaveRootDir.list();
		if (savedProjects.length > 0) {
			Arrays.sort(savedProjects);
			List<String> keepedProjects = new ArrayList<String>();
			keepedProjects.add(savedProjects[savedProjects.length - 1]);
			DeleteUtil.delAllFileWithEX(FileUtil.projectSaveRootPath,
					keepedProjects);
		}

		File middleFileSaveRootDir = new File(FileUtil.middleFileSaveRootPath);
		middleFileSaveRootDir.mkdirs();
		String[] savedMiddleFiles = middleFileSaveRootDir.list();
		if (savedMiddleFiles.length > 0) {
			Arrays.sort(savedMiddleFiles);
			List<String> keepedMiddleFiles = new ArrayList<String>();
			keepedMiddleFiles
					.add(savedMiddleFiles[savedMiddleFiles.length - 1]);
			DeleteUtil.delAllFileWithEX(FileUtil.middleFileSaveRootPath,
					keepedMiddleFiles);
		}

		List<String> keepedlogFiles = new ArrayList<String>();
		keepedlogFiles.add("appRollingFile.log");
		DeleteUtil.delAllFileWithEX(FileUtil.logSaveRootPath, keepedlogFiles);
	}
}
