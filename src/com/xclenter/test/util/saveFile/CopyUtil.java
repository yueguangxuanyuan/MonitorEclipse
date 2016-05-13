package com.xclenter.test.util.saveFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class CopyUtil {

	    /**
	     * ���Ƶ����ļ�(ԭ������)
	     * @param oldPathFile ׼�����Ƶ��ļ�Դ
	     * @param targetPath Ŀ��·��
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
	            if (oldfile.exists()) { //�ļ�����ʱ
	                InputStream inStream = new FileInputStream(oldPathFile); //����ԭ�ļ�
	                FileOutputStream fs = new FileOutputStream(targetPath);
	                byte[] buffer = new byte[1444];
	                while ((byteread = inStream.read(buffer)) != -1) {
	                    bytesum += byteread; //�ֽ��� �ļ���С
	                    fs.write(buffer, 0, byteread);
	                }
	                inStream.close();
	            }
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	    }

	    /**
	     * ���������ļ��е�����(������)
	     * @param oldPath ׼��������Ŀ¼
	     * @param newPath ָ������·������Ŀ¼
	     * @return
	     */
	    public static void copyFolderWithSelf(String oldPath, String newPath) {
	        try {
	            new File(newPath).mkdirs(); //����ļ��в����� �������ļ���
	            File dir = new File(oldPath);
				// Ŀ��
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
	                if (temp.isDirectory()) { //��������ļ���
	                	copyFolderWithSelf(oldPath + "/" + file[i], newPath);
	                }
	            }
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	    }

}
