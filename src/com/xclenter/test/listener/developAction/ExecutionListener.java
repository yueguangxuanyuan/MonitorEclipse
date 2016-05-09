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
		 * 记录在Eclipse内部的复制行为 目前记录两种 1 文件内字符串内容的复制 结合选择上下文 记录复制的上下文
		 * 
		 * 2工程文件夹的复制，之后辅助利用 这里记录的是文件的绝对路径 同时 空格之后包括了文件的类型 folder file 或者是 unknow
		 * 
		 * Eclipse内部的剪切行文 处理同复制行文 区别：剪切在剪切文件或者文件夹的时候存在问题
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
					 * 通过选择内容来判断是否是当前文件
					 */

					String context = "unknown";
					if (isCopyCut) {
						String selectText = null;
						if (selectContext.containsKey("text")) {
							// 处理掉系统粘贴板中 换行符的变化
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
					 * 如果选择内容无法推断出上下文 那么就给出当前活跃文件 作为默认值
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
