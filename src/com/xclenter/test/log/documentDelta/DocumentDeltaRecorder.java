package com.xclenter.test.log.documentDelta;

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

	private int fileOffset;

	private int offset;// Ԥ����һ�α仯������λ��

	private int deleteCharNum;

	private static DocumentDeltaRecorder documentDeltaRecorder;

	StringBuilder deltaStringBulider;

	private boolean ifNeedRecordEmptyChar;

	private DocumentDeltaRecorder() {
		String condition = System.getProperty("PassEmptyChar");

		ifNeedRecordEmptyChar = !((condition != null) && condition.equals("true"));

		projectName = "undefined";
		fileFullPath = "undefined";
		initState(INIT);
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
	public void notifyDocumentChange(int fline, int ffileOffset, int flineOffset, int fOffset, int flength,
			String ftext) {
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
			fileOffset = ffileOffset;
			if (ftext != null && ftext.length() > 0) {
				notifyDocumentChange(fline, ffileOffset, flineOffset, fOffset, 0, ftext);
			}
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
					fileOffset = ffileOffset;
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
					fileOffset = ffileOffset;
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
						fileOffset = ffileOffset;
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
		if (effecedFilePath.equals(fileFullPath)) {
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
				String message = ":: action_type ::edit:: operation_type ::delete:: project ::" + projectName
						+ ":: filePath ::" + fileFullPath + ":: location ::" + line + "," + lineOffset
						+ ":: fileOffset ::" + fileOffset + ":: length ::" + deleteCharNum;
				logger.info(message);
			}
		case INSERT:
			if (deltaStringBulider.length() > 0) {
				String message = ":: action_type ::edit:: operation_type ::insert:: project ::" + projectName
						+ ":: filePath ::" + fileFullPath + ":: location ::" + line + "," + lineOffset
						+ ":: fileOffset ::" + fileOffset + ":: text_length ::" + deltaStringBulider.length()
						+ ":: text ::" + deltaStringBulider.toString();
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
			fileOffset = -1;
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

		if (ifNeedRecordEmptyChar) {
			result |= RECORD;
		} else {
			if (!ftext.matches("\\s*")) {
				result |= RECORD;
			}
			if (ftext.substring(ftext.length() - 1).matches("\\s|\n")) {
				result |= PASS;
			}
		}
		return result;
	}
}
