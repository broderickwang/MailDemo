package marc.com.maildemo.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.POP3Folder;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.UIDFolder;
import javax.mail.URLName;
import javax.mail.internet.MimeMessage;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import marc.com.maildemo.R;
import marc.com.maildemo.adapter.InboxListAdapter;
import marc.com.maildemo.application.MailApplication;
import marc.com.maildemo.model.Email;
import marc.com.maildemo.model.MailReceiver;
import marc.com.maildemo.util.MailHelper;
import marc.com.multrecycleadapter.OnItemClickListner;

public class InboxActivity extends AppCompatActivity {


	@BindView(R.id.in_mail_list)
	RecyclerView mInMailList;
	@BindView(R.id.avi)
	AVLoadingIndicatorView mLoadingView;

	private String mType;
	
	private Context mContext;

	private InboxListAdapter mInboxListAdapter;

	private ArrayList<ArrayList<InputStream>> mAttachmentsInputStreamsList;

	private List<Email> mEmails ;

	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inbox);
		ButterKnife.bind(this);

		init();
	}

	private void init() {
		mHandler = new MyHandler(this);
		mType = getIntent().getStringExtra("TYPE");
		mContext = InboxActivity.this;
		mAttachmentsInputStreamsList = new ArrayList<>();
		mEmails = new ArrayList<>();

		mInboxListAdapter = new InboxListAdapter(mContext,null,R.layout.inbox_list_item);
		mInboxListAdapter.setOnClickListner(new OnItemClickListner() {
			@Override
			public void onClice(RecyclerView parent, int position) {
				((MailApplication)getApplication()).setAttachmentsInputStream(mAttachmentsInputStreamsList.get(position));
				Intent intent = new Intent(mContext,MailInfoActivity.class).putExtra("EMAIL",mEmails.get(position));
				startActivity(intent);
			}
		});
		mInboxListAdapter.setmDatas(mEmails);

		mLoadingView.smoothToShow();

		Observable.just(mContext)
				.map(new Function<Context, List<Email>>() {
					@Override
					public List<Email> apply(Context context) throws Exception {
						return /*getAllMailsByUID(mType)*/
								MailHelper
										.getInstance(mContext)
										.getAllMailsAndRefresh(mType,mAttachmentsInputStreamsList,mEmails,mHandler);
					}
				})
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<List<Email>>() {
					@Override
					public void accept(List<Email> emails) throws Exception {
						mLoadingView.smoothToHide();
					}
				});

		/*Observable.just(mContext)
				.map(new Function<Context, List<MailReceiver>>() {
					@Override
					public List<MailReceiver> apply(Context context) throws Exception {
						return MailHelper.getInstance(context).getAllMail(mType);
					}
				})
				.map(new Function<List<MailReceiver>, List<Email>>() {
					@Override
					public List<Email> apply(List<MailReceiver> mailReceivers) throws Exception {

						for (MailReceiver mailReceiver : mailReceivers) {
							Email email = new Email();
							try {
								email.setMessageID(mailReceiver.getMessageID());
								email.setFrom(mailReceiver.getFrom());
								email.setTo(mailReceiver.getMailAddress("TO"));
								email.setCc(mailReceiver.getMailAddress("CC"));
								email.setBcc(mailReceiver.getMailAddress("BCC"));
								email.setSubject(mailReceiver.getSubject());
								email.setSentdata(mailReceiver.getSentData());
								email.setContent(mailReceiver.getMailContent());
								email.setReplysign(mailReceiver.getReplySign());
								email.setHtml(mailReceiver.isHtml());
								email.setNews(mailReceiver.isNew());
								email.setAttachments(mailReceiver.getAttachments());
								email.setCharset(mailReceiver.getCharset());
								mAttachmentsInputStreamsList.add(0,mailReceiver.getAttachmentsInputStreams());
								mEmails.add(0,email);
//								mInboxListAdapter.notifyDataSetChanged();
								mHandler.obtainMessage(0).sendToTarget();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						return mEmails;
					}
				})
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<List<Email>>() {
					@Override
					public void accept(List<Email> emails) throws Exception {
						mLoadingView.smoothToHide();
					}
				});*/

		mInMailList.setLayoutManager(new LinearLayoutManager(mContext));
		mInMailList.setAdapter(mInboxListAdapter);
	}

	private static class MyHandler extends Handler {

		private WeakReference<InboxActivity> wrActivity;

		public MyHandler(InboxActivity activity) {
			this.wrActivity = new WeakReference<InboxActivity>(activity);
		}

		@Override
		public void handleMessage(android.os.Message msg) {
			final InboxActivity activity = wrActivity.get();
			switch (msg.what) {
				case 0:
					activity.mInboxListAdapter.notifyDataSetChanged();
					break;
				case 1:
					break;
				default:
					break;
			}
		};
	};

	public List<Email> getAllMailsByUID(String folderName) throws MessagingException{
		List<Email> mailList = new ArrayList<Email>();
		// 连接服务器
		Store store = ((MailApplication)mContext.getApplicationContext()).getStore();
		if(store == null) {
			Log.e("TAG", "getAllMail: store 为空 ",null );
			return mailList;
		}
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
			FetchProfile profile = new FetchProfile();
			profile.add(UIDFolder.FetchProfileItem.UID);
			profile.add(FetchProfile.Item.ENVELOPE);

			if(folder instanceof IMAPFolder){
				IMAPFolder inbox = (IMAPFolder) folder;
				Message[] messages = inbox.getMessages();
				for (int i = 0; i < messages.length; i++) {
					try{
						MimeMessage mimeMessage = (MimeMessage) messages[i];
						/*String uid = inbox.getUID(mimeMessage);
						System.out.println("uid=" + uid);
						int UnreadMessageCount = inbox.getUnreadMessageCount();
						System.out.println("UnreadMessageCount="+UnreadMessageCount);
						int NewMessageCount = inbox.getNewMessageCount();
						System.out.println("NewMessageCount="+NewMessageCount);
						URLName urlName = inbox.getURLName();
						System.out.println("urlName="+urlName);*/
						Email email = new Email();
						MailReceiver mailReceiver = new MailReceiver(mimeMessage);

						email.setMessageID(mailReceiver.getMessageID());
						email.setFrom(mailReceiver.getFrom());
						email.setTo(mailReceiver.getMailAddress("TO"));
						email.setCc(mailReceiver.getMailAddress("CC"));
						email.setBcc(mailReceiver.getMailAddress("BCC"));
						email.setSubject(mailReceiver.getSubject());
						email.setSentdata(mailReceiver.getSentData());
						email.setContent(mailReceiver.getMailContent());
						email.setReplysign(mailReceiver.getReplySign());
						email.setHtml(mailReceiver.isHtml());
						email.setNews(mailReceiver.isNew());
						email.setAttachments(mailReceiver.getAttachments());
						email.setCharset(mailReceiver.getCharset());
						mAttachmentsInputStreamsList.add(0,mailReceiver.getAttachmentsInputStreams());
						mEmails.add(0,email);
						mHandler.obtainMessage(0).sendToTarget();
					} catch (Exception e) {
						e.printStackTrace();
						return mailList;
					}
				}
				return mailList;
			}

		}
		return mailList;
	}

	public List<Email> getAllMail(String folderName) throws MessagingException {
		List<Email> mailList = new ArrayList<Email>();

		// 连接服务器
		Store store = ((MailApplication)mContext.getApplicationContext()).getStore();
		if(store == null) {
			Log.e("TAG", "getAllMail: store 为空 ",null );
			return mailList;
		}
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
				MailReceiver mailReceiver = new MailReceiver((MimeMessage) messages[i]);
				Email email = new Email();
				try {
					email.setMessageID(mailReceiver.getMessageID());
					email.setFrom(mailReceiver.getFrom());
					email.setTo(mailReceiver.getMailAddress("TO"));
					email.setCc(mailReceiver.getMailAddress("CC"));
					email.setBcc(mailReceiver.getMailAddress("BCC"));
					email.setSubject(mailReceiver.getSubject());
					email.setSentdata(mailReceiver.getSentData());
					email.setContent(mailReceiver.getMailContent());
					email.setReplysign(mailReceiver.getReplySign());
					email.setHtml(mailReceiver.isHtml());
					email.setNews(mailReceiver.isNew());
					email.setAttachments(mailReceiver.getAttachments());
					email.setCharset(mailReceiver.getCharset());
					mAttachmentsInputStreamsList.add(0,mailReceiver.getAttachmentsInputStreams());
					mEmails.add(0,email);
//								mInboxListAdapter.notifyDataSetChanged();
					mHandler.obtainMessage(0).sendToTarget();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return mailList;
		}
	}
}
