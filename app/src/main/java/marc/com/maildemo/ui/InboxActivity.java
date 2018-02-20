package marc.com.maildemo.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wang.avi.AVLoadingIndicatorView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

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
				});

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
}
