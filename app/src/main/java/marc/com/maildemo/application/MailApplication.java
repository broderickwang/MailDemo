package marc.com.maildemo.application;

import android.app.Application;

import java.io.InputStream;
import java.util.ArrayList;

import javax.mail.Store;

/**
 * Created by chengda
 * Date: 2018/2/15
 * Time: 16:12
 * Version: 1.0
 * Description:
 * Email:wangchengda1990@gmail.com
 **/
public class MailApplication extends Application {

	private Store mStore;

	private ArrayList<InputStream> mAttachmentsInputStream;

	public Store getStore() {
		return mStore;
	}

	public void setStore(Store store) {
		mStore = store;
	}

	public ArrayList<InputStream> getAttachmentsInputStream() {
		return mAttachmentsInputStream;
	}

	public void setAttachmentsInputStream(ArrayList<InputStream> attachmentsInputStream) {
		mAttachmentsInputStream = attachmentsInputStream;
	}
}
