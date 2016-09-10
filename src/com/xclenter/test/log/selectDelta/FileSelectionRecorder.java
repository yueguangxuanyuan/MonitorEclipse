package com.xclenter.test.log.selectDelta;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Semaphore;

import com.xclenter.test.util.context.IGetContext;

public class FileSelectionRecorder implements IGetContext {
	private static FileSelectionRecorder fileSelectionRecorder;

	private FileSelectionRecorder() {
		selectedFiles = null;
		locker = new Semaphore(1);
	}

	public static FileSelectionRecorder getFileSelectionRecorder() {

		if (fileSelectionRecorder == null) {
			fileSelectionRecorder = new FileSelectionRecorder();
		}
		return fileSelectionRecorder;

	}

	private Map<String, String> selectedFiles;
	Semaphore locker;

	public void recordFileSelect(Map<String, String> selectedFiles) {
		/*
		 * 将选择结果缓存下来
		 */
		try {
			locker.acquire();
			this.selectedFiles = new TreeMap<String, String>(selectedFiles);
			locker.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map getContext() {
		// TODO Auto-generated method stub
		Map context = new HashMap();
		try {
			locker.acquire();
			for (String path : selectedFiles.keySet()) {
				String type = selectedFiles.get(path);
				if(type.equals("project") || type.equals("folder")){
					context.put("targetPath", path);
					break;
				}
			}
			locker.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return context;
	}
}
