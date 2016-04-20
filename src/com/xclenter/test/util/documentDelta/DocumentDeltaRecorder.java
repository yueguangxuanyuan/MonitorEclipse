package com.xclenter.test.util.documentDelta;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DocumentDeltaRecorder {
	private static Logger logger = LogManager.getLogger("MessageLog");  
	
	private final int INIT = 1;

	private final int DELETE = 2;

	private final int INSERT = 4;

	/*
	 * 记录当前 recorder的状态 只能取以上的状态值
	 */
	private int STATE;

	/*
	 * 记录插入值的状态
	 */
	private final int RECORD = 1;

	private final int PASS = 2;

	/*
	 * 保存当前上下文
	 */
	private String projectName;

	private String fileFullPath;

	private int line;

	private int lineOffset;

	private int offset;// 预期下一次变化发生的位置

	private int deleteCharNum;

	private static DocumentDeltaRecorder documentDeltaRecorder;

	StringBuilder deltaStringBulider;

	private DocumentDeltaRecorder() {
		deltaStringBulider = new StringBuilder();
		STATE = INIT;

		projectName = "undefined";
		fileFullPath = "undefined";
		line = -1;
		lineOffset = -1;
		deleteCharNum = 0;
	}

	public static DocumentDeltaRecorder getDocumentDeltaRecorder() {
		if (documentDeltaRecorder == null) {
			documentDeltaRecorder = new DocumentDeltaRecorder();
		}
		return documentDeltaRecorder;
	}

	/*
	 * 在文档发生改变的时候调用，感知变化
	 */
	public void notifyDocumentChange(int fline, int flineOffset, int fOffset,
			int flength, String ftext) {
		// 区分删除和插入

		if (flength > 0) {
			switch (STATE) {
			case INIT:
				initState(DELETE);
				deleteCharNum = flength;
				break;
			case DELETE:
				// 判断是否是连续删除
				if (fOffset == (offset - flength)) {
					deleteCharNum += flength;
				} else {
					log();
					initState(DELETE);
					deleteCharNum = flength;
				}
				break;
			case INSERT:
				log();
				initState(DELETE);
				deleteCharNum = flength;
				break;
			default:
				break;
			}
			offset = fOffset;
			line = fline;
			lineOffset = flineOffset;
		} else {
			int result = judgeInsertContent(ftext);

			boolean needRecord = (result & RECORD) != 0;
			boolean needPass = (result & PASS) != 0;

			switch (STATE) {
			case INIT:
				if (needRecord) {
					initState(INSERT);
					offset = fOffset + ftext.length();
					line = fline;
					lineOffset = flineOffset;
					deltaStringBulider.append(ftext);

					if (needPass) {
						log();
						initState(INIT);
					}
				}
				break;
			case DELETE:
				if (needRecord) {
					log();
					initState(INSERT);
					offset = fOffset + ftext.length();
					line = fline;
					lineOffset = flineOffset;
					deltaStringBulider.append(ftext);

					if (needPass) {
						log();
						initState(INIT);
					}
				}
				break;
			case INSERT:
				if (needRecord) {
					// 判断是否是连续删除
					if (fOffset != offset) {
						log();
						initState(INSERT);
						line = fline;
						lineOffset = flineOffset;
					}
					offset = fOffset + ftext.length();
					deltaStringBulider.append(ftext);	
				}
				
				if (needPass) {
					log();
					initState(INIT);
				}
				break;
			default:
				break;
			}

		}
	}

	public void notifyContextChange(String projectName, String fileFullPath) {
		notifyFlushLog(this.fileFullPath);
		this.projectName = projectName;
		this.fileFullPath = fileFullPath;
	}

	/*
	 * 当保存文档的时候调用 记录改变
	 */
	public void notifyFlushLog(String effecedFilePath) {
		if(effecedFilePath.equals(fileFullPath)){
			log();
			initState(INIT);		
		}
	
	}

	/*
	 * 内部负责记录的模块
	 */
	private void log() {
		switch (STATE) {
		case INIT:
			break;
		case DELETE:
			if (deleteCharNum > 0) {
				String message = "Delete :: project :: " + projectName
						+ " :: filePath :: " + fileFullPath + " :: location ::"
						+ line + "," + lineOffset + " ::length:: "
						+ deleteCharNum;
				logger.info(message);
			}
		case INSERT:
			if(deltaStringBulider.length() > 0){
				String message = "INSERT :: project :: " + projectName
						+ " :: filePath :: " + fileFullPath + " :: location ::"
						+ line + "," + lineOffset + " ::text:: "
						+ deltaStringBulider.toString();
				logger.info(message);
			}
		default:
			break;
		}
	}

	/*
	 * 初始化当前delta的上下文
	 */
	private void initState(int state) {
		switch (state) {
		case INIT:
		case DELETE:
		case INSERT:
			STATE = state;
			line = -1;
			lineOffset = -1;
			deleteCharNum = 0;
			deltaStringBulider = new StringBuilder();
			break;
		default:
			break;
		}
	}

	/*
	 * 判断是否需要记录 或者是否需要直接输出日志
	 */
	private int judgeInsertContent(String ftext) {
		int result = 0;

		if (!ftext.matches("\\s*")) {
			result |= RECORD;
		}
		if (ftext.substring(ftext.length() - 1).matches("\\s|\n")) {
			result |= PASS;
		}
		return result;
	}
}
