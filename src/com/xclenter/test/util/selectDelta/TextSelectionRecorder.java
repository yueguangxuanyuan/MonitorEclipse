package com.xclenter.test.util.selectDelta;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TextSelectionRecorder {
	private static Logger logger = LogManager.getLogger("MessageLog");

	private static TextSelectionRecorder textSelectionRecorder;

	private TextSelectionRecorder() {
		lock = new Semaphore(1);
	}

	public static TextSelectionRecorder getTextSelectionRecorder() {
		if (textSelectionRecorder == null) {
			textSelectionRecorder = new TextSelectionRecorder();
		}

		return textSelectionRecorder;
	}

	// 当前记录的坐标信息
	private String pFilePath = null;

	private int pOffset = -1;

	private int pLength = 0;

	private String pText = null;

	private final int INIT = 1;

	private final int RECORDING = 2;

	private int STATE = INIT;

	private Semaphore lock;

	ScheduledExecutorService service;

	public void recordTextSelection(String filePath, int offset, int length, String text) {
		try {
			lock.acquire();
			switch (STATE) {
			case INIT:
				if (length > 0) {
					updateRecordingContext(filePath, offset, length, text);
				}
				break;
			case RECORDING:
				if (pFilePath != null && !pFilePath.equals(filePath)) {
					log();
					updateRecordingContext(filePath, -1, 0, null);
				}

				if (length > 0) {
					boolean isContinousSelect = (offset == pOffset) || (pOffset + pLength == offset + length);
					if (!isContinousSelect) {
						log();
					}
					updateRecordingContext(filePath, offset, length, text);
				} else {
					log();
					initSTATE();
				}
				break;
			default:
				break;
			}
			lock.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void log() {
		if (pLength != 0) {
			logger.info(":: action_type ::edit:: operation_type ::select:: type ::text:: filePath ::" + pFilePath
					+ ":: offset ::" + pOffset + ":: length ::" + pLength + ":: text ::" + pText);
		}
	}

	private void initSTATE() {
		pFilePath = null;
		pOffset = -1;
		pLength = 0;
		pText = null;
		STATE = INIT;
	}

	private void updateRecordingContext(String filePath, int offset, int length, String text) {
		if (service != null) {
			service.shutdownNow();
			try {
				service.awaitTermination(1, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		pFilePath = filePath;
		pOffset = offset;
		pLength = length;
		pText = text;
		STATE = RECORDING;

		service = Executors.newSingleThreadScheduledExecutor();
		/*
		 * 设定一个定时任务 如果用户长时间停留与一个选择内容上的时候调用
		 */
		service.schedule(new Runnable() {
			public void run() {
				boolean doSuccess = lock.tryAcquire();
				if (doSuccess) {
					log();
					initSTATE();
					lock.release();
				}
			}
		}, 2, TimeUnit.SECONDS);
	}
}