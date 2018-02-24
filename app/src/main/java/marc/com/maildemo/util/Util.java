package marc.com.maildemo.util;

import android.content.Context;
import android.content.Intent;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;

/**
 * Created by chengda
 * Date: 2018/2/15
 * Time: 16:32
 * Version: 1.0
 * Description:
 * Email:wangchengda1990@gmail.com
 **/
public class Util {

	public static void startActivity(Context context,Class claz){
		Intent intent = new Intent(context,claz);
		context.startActivity(intent);
	}

	/**
	 * 判断是否为乱码
	 *
	 * @param str
	 * @return
	 */
	public static boolean isMessyCode(String str) {
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			// 当从Unicode编码向某个字符集转换时，如果在该字符集中没有对应的编码，则得到0x3f（即问号字符?）
			//从其他字符集向Unicode编码转换时，如果这个二进制数在该字符集中没有标识任何的字符，则得到的结果是0xfffd
			if ((int) c == 0xfffd) {
				// 存在乱码
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取所有的文件夹
	 * @param store
	 * @return
	 */
	public Folder[] getAllFolders(Store store){
		try {
			Folder folder = store.getDefaultFolder();
			return folder.list();
		} catch (MessagingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
