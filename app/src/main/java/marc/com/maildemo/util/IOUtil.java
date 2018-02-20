package marc.com.maildemo.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chengda
 * Date: 2018/2/20
 * Time: 17:30
 * Version: 1.0
 * Description:
 * Email:wangchengda1990@gmail.com
 **/
public class IOUtil {
	/**
	 * 输入流保存到文件
	 *
	 * @param source 输入流来源
	 * @param targetPath 目标文件路径
	 * @return 文件路径
	 */
	public String stream2file(InputStream source, String targetPath) {
		File target = new File(targetPath);
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			if (!target.exists()) {
				String dir = targetPath.substring(0, targetPath.lastIndexOf("/"));
//				new File(dir).mkdirs();
				File file = new File(dir);
				if(!file.exists()){
					file.mkdirs();
				}
				/*try {
					target.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}*/
				String fileName = targetPath.substring( targetPath.lastIndexOf("/"),targetPath.length());
				target = new File(dir,fileName);
			}
			inBuff = new BufferedInputStream(source);
			outBuff = new BufferedOutputStream(new FileOutputStream(target));
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			outBuff.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inBuff != null) {
					inBuff.close();
				}
				if (outBuff != null) {
					outBuff.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (target.length() > 0) {
			return target.getAbsolutePath();
		} else {
			target.delete();
			return null;
		}
	}

	/**
	 * 字节数组转输入流
	 *
	 * @param data 字节数组
	 * @return 输入流
	 */
	public InputStream Byte2InputStream(byte[] data) {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		return bais;
	}

	/**
	 * 输入流转字节数组
	 *
	 * @param is 输入流
	 * @return 字节数组
	 */
	public byte[] InputStream2Bytes(InputStream is) {
		String str = "";
		byte[] readByte = new byte[1024];
		try {
			while (is.read(readByte, 0, 1024) != -1) {
				str += new String(readByte).trim();
			}
			return str.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] getFileBytes(File file) throws IOException {
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			int bytes = (int) file.length();
			byte[] buffer = new byte[bytes];
			int readBytes = bis.read(buffer);
			if (readBytes != buffer.length) {
				throw new IOException("Entire file not read");
			}
			return buffer;
		} finally {
			if (bis != null) {
				bis.close();
			}
		}
	}
}
