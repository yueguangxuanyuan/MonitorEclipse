package com.xclenter.test.util.saveFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class CopyUtil {

	    /**
	     * 复制单个文件(原名复制)
	     * @param oldPathFile 准备复制的文件源
	     * @param targetPath 目标路径
	     * @return
	     */
	    public static void CopySingleFileTo(String oldPathFile, String targetPath) {
	        try {
	            int bytesum = 0;
	            int byteread = 0;
	            File oldfile = new File(oldPathFile);
	            File targetfile = new File(targetPath);
	            File targetDir = targetfile.getParentFile();
	            if(targetDir != null){
	            	targetDir.mkdirs();
	            }
	            if (oldfile.exists()) { //文件存在时
	                InputStream inStream = new FileInputStream(oldPathFile); //读入原文件
	                FileOutputStream fs = new FileOutputStream(targetPath);
	                byte[] buffer = new byte[1444];
	                while ((byteread = inStream.read(buffer)) != -1) {
	                    bytesum += byteread; //字节数 文件大小
	                    fs.write(buffer, 0, byteread);
	                }
	                inStream.close();
	            }
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	    }

	    /**
	     * 复制整个文件夹的内容(含自身)
	     * @param oldPath 准备拷贝的目录
	     * @param newPath 指定绝对路径的新目录
	     * @return
	     */
	    public static void copyFolderWithSelf(String oldPath, String newPath) {
	        try {
	            new File(newPath).mkdirs(); //如果文件夹不存在 则建立新文件夹
	            File dir = new File(oldPath);
				// 目标
	            newPath +=  File.separator + dir.getName();
				File moveDir = new File(newPath);
				if(dir.isDirectory()){
					if (!moveDir.exists()) {
						moveDir.mkdirs();
					}
				}
	            String[] file = dir.list();
	            File temp = null;
	            for (int i = 0; i < file.length; i++) {
	                if (oldPath.endsWith(File.separator)) {
	                    temp = new File(oldPath + file[i]);
	                } else {
	                    temp = new File(oldPath + File.separator + file[i]);
	                }
	                if (temp.isFile()) {
	                    FileInputStream input = new FileInputStream(temp);
	                    FileOutputStream output = new FileOutputStream(newPath +
	                            "/" +
	                            (temp.getName()).toString());
	                    byte[] b = new byte[1024 * 5];
	                    int len;
	                    while ((len = input.read(b)) != -1) {
	                        output.write(b, 0, len);
	                    }
	                    output.flush();
	                    output.close();
	                    input.close();
	                }
	                if (temp.isDirectory()) { //如果是子文件夹
	                	copyFolderWithSelf(oldPath + "/" + file[i], newPath);
	                }
	            }
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	    }

}
