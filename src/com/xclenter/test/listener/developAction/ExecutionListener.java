package com.xclenter.test.listener.developAction;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;

import com.xclenter.test.util.context.ContextUtil;
import com.xclenter.test.util.context.IGetContext;
import com.xclenter.test.util.selectDelta.FileSelectionRecorder;
import com.xclenter.test.util.selectDelta.TextSelectionRecorder;

public class ExecutionListener implements IExecutionListener {
	private static Logger logger = LogManager.getLogger("MessageLog");

	private IGetContext iGetContext;
	private IGetContext iGetPasteContext;
	private Map selectContext;
	private String pasteMessage;

	public ExecutionListener() {
		iGetContext = TextSelectionRecorder.getTextSelectionRecorder();
		iGetPasteContext = FileSelectionRecorder.getFileSelectionRecorder();
		selectContext = null;
		pasteMessage = "unknown";
	}

	@Override
	public void notHandled(String arg0, NotHandledException arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postExecuteFailure(String arg0, ExecutionException arg1) {
		// TODO Auto-generated method stub
		log("fail-" + arg0);
	}

	@Override
	public void postExecuteSuccess(String arg0, Object arg1) {
		// TODO Auto-generated method stub

		/*
		 * ��¼��Eclipse�ڲ��ĸ�����Ϊ Ŀǰ��¼���� 1 �ļ����ַ������ݵĸ��� ���ѡ�������� ��¼���Ƶ�������
		 * 
		 * 2�����ļ��еĸ��ƣ�֮�������� �����¼�����ļ��ľ���·�� ͬʱ �ո�֮��������ļ������� folder file ������ unknow
		 * 
		 * Eclipse�ڲ��ļ������� ����ͬ�������� ���𣺼����ڼ����ļ������ļ��е�ʱ���������
		 */
		if (arg0.equals("org.eclipse.ui.edit.copy")
				|| arg0.equals("org.eclipse.ui.edit.cut")) {
			String message = getCopyPasteMessage(arg0, true);
			log(message);
		} else if (arg0.equals("org.eclipse.ui.edit.paste")) {
			log(pasteMessage);
		} else {
			log("success-" + arg0);
		}
	}

	@Override
	public void preExecute(String arg0, ExecutionEvent arg1) {
		// TODO Auto-generated method stub
		log("preexecute-" + arg0);
		if (arg0.equals("org.eclipse.ui.edit.copy")
				|| arg0.equals("org.eclipse.ui.edit.cut")) {
			selectContext = iGetContext.getContext();
		}

		if (arg0.equals("org.eclipse.ui.edit.paste")) {
			pasteMessage = getCopyPasteMessage(arg0, false);
		}
	}

	private String getCopyPasteMessage(String commandId, boolean isCopyCut) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable clipT = clipboard.getContents(null);
		StringBuilder message = new StringBuilder();
		message.append("type:" + commandId);
		if (clipT != null) {
			if (clipT.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				try {
					@SuppressWarnings("unchecked")
					List<File> copyContent = (List<File>) clipT
							.getTransferData(DataFlavor.javaFileListFlavor);

					message.append(":type:file:content:[");
					for (File tempFile : copyContent) {
						message.append(tempFile.getAbsolutePath());
						if (tempFile.isDirectory()) {
							message.append(" folder");
						} else if (tempFile.isFile()) {
							message.append(" file");
						} else {
							message.append(" unknown");
						}
						message.append(",");
					}
					message.append("]");
					if(!isCopyCut){
						message.append(":targetPath:" + iGetPasteContext.getContext().get("targetPath"));
					}
					return message.toString();
				} catch (UnsupportedFlavorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (clipT.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				try {
					String copyContent = (String) clipT
							.getTransferData(DataFlavor.stringFlavor);
					/*
					 * ͨ��ѡ���������ж��Ƿ��ǵ�ǰ�ļ�
					 */

					String context = "unknown";
					if (isCopyCut) {
						String selectText = null;
						if (selectContext.containsKey("text")) {
							// �����ϵͳճ������ ���з��ı仯
							selectText = (String) selectContext.get("text");
							if (selectText != null) {
								selectText = selectText
										.replaceAll("\r\n", "\n");
							}
						}
						if (copyContent != null
								&& copyContent.equals(selectText)) {
							context = "@filePath:"
									+ selectContext.get("filePath")
									+ "@offset:" + selectContext.get("offset");
						}
					}
					/*
					 * ���ѡ�������޷��ƶϳ������� ��ô�͸�����ǰ��Ծ�ļ� ��ΪĬ��ֵ
					 */
					if (context.equals("unknown")) {
						Map globalContext = ContextUtil.getContextUtil()
								.getContext();
						context = "@filePath:"
								+ globalContext.get("fileFullPath")
								+ "@offset:-1";
					}

					message.append(":type:text:context:" + context
							+ ":content:" + copyContent);
					return message.toString();
				} catch (UnsupportedFlavorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		return "unknowMessage";
	}

	private void log(String message) {
		logger.info(":: action_type ::operation:: operation_type ::excution:: message ::"
				+ message);
	}
}
