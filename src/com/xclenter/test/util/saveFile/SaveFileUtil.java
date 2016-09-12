package com.xclenter.test.util.saveFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
	
	public static String utilFileSaveRootPath = "com.xclenter.monitor"
			+ File.separator + "utilFile";
	
	public static String logSaveRootPath = "com.xclenter.monitor"
			+ File.separator + "logs";
	
	private static void clearFileSaveSpace() {
		File projectSaveRootDir = new File(projectSaveRootPath);
		projectSaveRootDir.mkdirs();
		DeleteUtil.delAllFile(projectSaveRootPath);

		File middleFileSaveRootDir = new File(middleFileSaveRootPath);
		middleFileSaveRootDir.mkdirs();
		DeleteUtil.delAllFile(middleFileSaveRootPath);
		
		File utilFileSaveRootDir = new File(utilFileSaveRootPath);
		utilFileSaveRootDir.mkdirs();
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
	 * 在workbench startup的时候触发一次来初始化文件保存上下文 清空原有目录 将现有工程文件保存进去
	 */
	public static void initSaveContext() {
		
		Date date = new Date();
		projectSaveRootPath += File.separator + date.getTime();
		middleFileSaveRootPath += File.separator + date.getTime();
		
		clearFileSaveSpace();
		saveAllExistProjects();
	}
	
	public static void clearLegacy(){
		File projectSaveRootDir = new File(projectSaveRootPath);
		projectSaveRootDir.mkdirs();
		String[] savedProjects=projectSaveRootDir.list();
		if(savedProjects.length > 0){
			Arrays.sort(savedProjects);
			List<String> keepedProjects = new ArrayList<String>();
			keepedProjects.add(savedProjects[savedProjects.length -1]);
			DeleteUtil.delAllFileWithEX(projectSaveRootPath, keepedProjects);
		}
		

		File middleFileSaveRootDir = new File(middleFileSaveRootPath);
		middleFileSaveRootDir.mkdirs();
		String[] savedMiddleFiles = middleFileSaveRootDir.list();
		if(savedMiddleFiles.length > 0){
			Arrays.sort(savedMiddleFiles);
			List<String> keepedMiddleFiles = new ArrayList<String>();
			keepedMiddleFiles.add(savedMiddleFiles[savedMiddleFiles.length -1]);
			DeleteUtil.delAllFileWithEX(middleFileSaveRootPath, keepedMiddleFiles);
		}
		
		List<String> keepedlogFiles = new ArrayList<String>();
		keepedlogFiles.add("appRollingFile.log");
		DeleteUtil.delAllFileWithEX(logSaveRootPath, keepedlogFiles);
	}
	
}
