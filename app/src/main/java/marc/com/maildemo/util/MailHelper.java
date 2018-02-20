package marc.com.maildemo.util;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;

import marc.com.maildemo.application.MailApplication;
import marc.com.maildemo.model.MailReceiver;

/**
 * Created by chengda
 * Date: 2018/2/18
 * Time: 20:25
 * Version: 1.0
 * Description:
 * Email:wangchengda1990@gmail.com
 **/
public class MailHelper {
	private static MailHelper mInstance;

	private HashMap<String,Integer> mServiceHashMap;

	private List<MailReceiver> mMailReceivers;

	private Context mContext;

	public static MailHelper getInstance(Context context){
		if(mInstance==null){
			synchronized (MailHelper.class){
				if(mInstance==null){
					mInstance = new MailHelper(context);
				}
			}
		}
		return mInstance;
	}

	private MailHelper(Context context) {
		mContext = context;
	}

	public String getUpdateUrlStr() throws Exception {
		String urlStr = null;
		if (mServiceHashMap == null) {
			mServiceHashMap = this.getServeHashMap();
		}
		if (mServiceHashMap.get("update") == 1) {
			urlStr = mMailReceivers.get(1).getSubject();
		}
		return urlStr;
	}

	public String getUserHelp() throws Exception {
		String userandmoney = null;
		if (mServiceHashMap == null) {
			mServiceHashMap = this.getServeHashMap();
		}
		if (mServiceHashMap.get("userhelp") == 1) {
			userandmoney = mMailReceivers.get(3).getSubject();
		}
		return userandmoney;
	}

	public int getAllUserHelp() throws Exception {
		String userandmoney = null;
		int money = 0;
		if (mServiceHashMap == null) {
			mServiceHashMap = this.getServeHashMap();
		}
		if (mServiceHashMap.get("userhelp") == 1) {
			userandmoney = mMailReceivers.get(3).getSubject();
		}
		if (userandmoney != null && userandmoney.contains("all-user-100")) {
			money = Integer.parseInt(userandmoney.substring(userandmoney.lastIndexOf("-" + 1),
					userandmoney.length()));
		}
		return money;
	}

	public boolean getAdControl() throws Exception {
		String ad = null;
		if (mServiceHashMap == null) {
			mServiceHashMap = this.getServeHashMap();
		}
		if (mServiceHashMap.get("adcontrol") == 1) {
			ad = mMailReceivers.get(2).getSubject();
		}
		if (ad.equals("ad=close")) {
			return false;
		}
		return true;
	}

	public HashMap<String, Integer> getServeHashMap() throws Exception {
		mServiceHashMap = new HashMap<String, Integer>();
		if (mMailReceivers == null) {
			mMailReceivers = getAllMail("INBOX");
		}
		String serviceStr = mMailReceivers.get(0).getSubject();
		if (serviceStr.contains("update 1.0=true")) {
			mServiceHashMap.put("update", 1);
		} else if (serviceStr.contains("update 1.0=false")) {
			mServiceHashMap.put("update", 0);
		}
		if (serviceStr.contains("adcontrol 1.0=true")) {
			mServiceHashMap.put("adcontrol", 1);
		} else if (serviceStr.contains("adcontrol 1.0=false")) {
			mServiceHashMap.put("adcontrol", 0);
		}
		if (serviceStr.contains("userhelp 1.0=true")) {
			mServiceHashMap.put("userhelp", 1);
		} else if (serviceStr.contains("userhelp 1.0=false")) {
			mServiceHashMap.put("userhelp", 0);
		}
		return mServiceHashMap;
	}

	/**
	 * 取得所有的邮件
	 *
	 * @param folderName 文件夹名，例：收件箱是"INBOX"
	 * @return　List<MailReceiver> 放有ReciveMail对象的List
	 * @throws MessagingException
	 */
	public List<MailReceiver> getAllMail(String folderName) throws MessagingException {
		List<MailReceiver> mailList = new ArrayList<MailReceiver>();

		// 连接服务器
		Store store = ((MailApplication)mContext.getApplicationContext()).getStore();
		// 打开文件夹
		Folder folder = store.getFolder(folderName);
		folder.open(Folder.READ_ONLY);
		// 总的邮件数
		int mailCount = folder.getMessageCount();
		if (mailCount == 0) {
			folder.close(true);
			store.close();
			return null;
		} else {
			// 取得所有的邮件
			Message[] messages = folder.getMessages();
			for (int i = 0; i < messages.length; i++) {
//            for (int i = 0; i < 10; i++) {
				// 自定义的邮件对象
				MailReceiver reciveMail = new MailReceiver((MimeMessage) messages[i]);
				mailList.add(reciveMail);// 添加到邮件列表中
			}
			return mailList;
		}
	}
}
