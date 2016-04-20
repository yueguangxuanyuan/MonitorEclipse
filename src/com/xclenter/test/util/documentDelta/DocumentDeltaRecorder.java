package com.xclenter.test.util.documentDelta;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DocumentDeltaRecorder {
	private static Logger logger = LogManager.getLogger("MessageLog");  
	
	private final int INIT = 1;

	private final int DELETE = 2;

	private final int INSERT = 4;

	/*
	 * ��¼��ǰ recorder��״̬ ֻ��ȡ���ϵ�״ֵ̬
	 */
	private int STATE;

	/*
	 * ��¼����ֵ��״̬
	 */
	private final int RECORD = 1;

	private final int PASS = 2;

	/*
	 * ���浱ǰ������
	 */
	private String projectName;

	private String fileFullPath;

	private int line;

	private int lineOffset;

	private int offset;// Ԥ����һ�α仯������λ��

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
	 * ���ĵ������ı��ʱ����ã���֪�仯
	 */
	public void notifyDocumentChange(int fline, int flineOffset, int fOffset,
			int flength, String ftext) {
		// ����ɾ���Ͳ���

		if (flength > 0) {
			switch (STATE) {
			case INIT:
				initState(DELETE);
				deleteCharNum = flength;
				break;
			case DELETE:
				// �ж��Ƿ�������ɾ��
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
					// �ж��Ƿ�������ɾ��
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
	 * �������ĵ���ʱ����� ��¼�ı�
	 */
	public void notifyFlushLog(String effecedFilePath) {
		if(effecedFilePath.equals(fileFullPath)){
			log();
			initState(INIT);		
		}
	
	}

	/*
	 * �ڲ������¼��ģ��
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
	 * ��ʼ����ǰdelta��������
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
	 * �ж��Ƿ���Ҫ��¼ �����Ƿ���Ҫֱ�������־
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
