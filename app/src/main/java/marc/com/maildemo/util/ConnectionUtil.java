package marc.com.maildemo.util;

import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

/**
 * Created by chengda
 * Date: 2018/2/18
 * Time: 19:54
 * Version: 1.0
 * Description:
 * Email:wangchengda1990@gmail.com
 **/
public class ConnectionUtil {
	private static Store mStore = null;

	public static Store login(String host,String user,String password){
		//连接服务器
		Session session = Session.getDefaultInstance(System.getProperties(),null);

		try {
			mStore = session.getStore("pop3");
			mStore.connect(host,user,password);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return mStore;
	}

	public static String getPOP3Host(String user) {
		if (user.contains("163")) {
			return "pop.163.com";
		} else {
			return null;
		}
	}

	public static String getSMTPHost(String user) {
		if (user.contains("163")) {
			return "smtp.163.com";
		} else {
			return null;
		}
	}
}
