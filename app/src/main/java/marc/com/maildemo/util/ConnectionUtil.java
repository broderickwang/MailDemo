package marc.com.maildemo.util;

import java.util.Properties;

import javax.mail.Authenticator;
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

	/**
	 * 以IMAP的方式登录邮件服务器，需要邮件服务器支持IMAP，163 IMAP默认不打开。
	 * @param host
	 * @param user
	 * @param password
	 * @return
	 */
	public static Store loginWithIMap(String host, final String user,final String password){
		//连接服务器
		Properties properties = System.getProperties();
		properties.setProperty("mail.store.protocol", "imap");
		properties.setProperty("mail.imap.host", "imap.smartncs.com");
		properties.setProperty("mail.imap.port", "143");
		final String[] us = user.split("@");
		Session session = Session.getInstance(properties,new javax.mail.Authenticator() {
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(us[0], password);
			}
		});

		try {
			mStore = session.getStore("imap");
			mStore.connect(user,password);
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

	public static String getIMAPHost(String user){
		if(user.contains("163")){
			return "imap.163.com";
		}else if(user.contains("smartncs")){
			return "imap.smartncs.com";
		}else{
			return null;
		}
	}

	public static String getSMTPHost(String user) {
		if (user.contains("163")) {
			return "smtp.163.com";
		} else if(user.contains("smartncs")){
			return "smtp.smartncs.com";
		} else {
			return null;
		}
	}
}
