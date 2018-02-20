package marc.com.maildemo.ui;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import marc.com.maildemo.R;
import marc.com.maildemo.adapter.MailAttachmentAdapter;
import marc.com.maildemo.application.MailApplication;
import marc.com.maildemo.model.Email;
import marc.com.maildemo.util.IOUtil;
import marc.com.multrecycleadapter.OnItemClickListner;

public class MailInfoActivity extends AppCompatActivity {

	@BindView(R.id.tv_addr)
	TextView tvAddr;
	@BindView(R.id.tv_mailsubject)
	TextView tvMailsubject;
	@BindView(R.id.lv_mailattachment)
	RecyclerView lvMailattachment;
	@BindView(R.id.wv_mailcontent)
	WebView wvMailcontent;
	@BindView(R.id.tv_mailcontent)
	TextView tvMailcontent;
	@BindView(R.id.btn_cancel)
	Button btnCancel;
	@BindView(R.id.btn_relay)
	Button btnRelay;
	@BindView(R.id.btn_holder)
	LinearLayout btnHolder;

	private Email mEmail;

	private List<InputStream> mAttachmentsInputStreams;

	private MailAttachmentAdapter mAttachmentAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mail_info);
		ButterKnife.bind(this);

		init();
	}

	private void init() {
		mEmail = (Email) getIntent().getSerializableExtra("EMAIL");
		mAttachmentsInputStreams = ((MailApplication)getApplication()).getAttachmentsInputStream();

		if(mEmail!=null && mEmail.getAttachments().size()>0){
			mAttachmentAdapter = new MailAttachmentAdapter(
					MailInfoActivity.this,mEmail.getAttachments()
					,R.layout.attatchment_list_item);

			mAttachmentAdapter.setOnClickListner(new OnItemClickListner() {
				@Override
				public void onClice(RecyclerView parent,final int position) {
					InputStream is = mAttachmentsInputStreams.get(position);
					Observable.just(is)
							.map(new Function<InputStream, String>() {
								@Override
								public String apply(InputStream inputStream) throws Exception {
									return new IOUtil().stream2file(inputStream
											, Environment.getExternalStorageDirectory().getAbsolutePath()
											+ "/temp/" + mEmail.getAttachments().get(position));
								}
							})
							.subscribeOn(Schedulers.io())
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Consumer<String>() {
								@Override
								public void accept(String s) throws Exception {
									Toast.makeText(MailInfoActivity.this,
											"文件保存在："+s, Toast.LENGTH_SHORT).show();
								}
							});
				}
			});

			lvMailattachment.setVisibility(View.VISIBLE);
			lvMailattachment.setLayoutManager(new LinearLayoutManager(MailInfoActivity.this));
			lvMailattachment.setAdapter(mAttachmentAdapter);
		}

		tvAddr.setText(mEmail.getFrom());
		tvMailsubject.setText(mEmail.getSubject());
		if(mEmail.isHtml()){
			wvMailcontent.setVisibility(View.VISIBLE);
			wvMailcontent.getSettings().setDefaultTextEncodingName("UTF-8");
//			wvMailcontent.loadData(mEmail.getContent(),"text/html;charset=UTF-8",null);
			wvMailcontent.loadDataWithBaseURL(null,mEmail.getContent(),"text/html","utf-8",null);
			/*DisplayMetrics metrics = getResources().getDisplayMetrics();
			int scale = metrics.densityDpi;
			if(scale == 240){
				wvMailcontent.getSettings().getDefaultZoom()
			}*/
			wvMailcontent.setWebChromeClient(new WebChromeClient());
			tvMailcontent.setVisibility(View.GONE);
		}else{
			Log.d("TAG", "邮件文字内容: "+mEmail.getContent());
			tvMailcontent.setText(mEmail.getContent());
		}
	}

	@OnClick({R.id.btn_cancel, R.id.btn_relay, R.id.btn_holder})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.btn_cancel:
				break;
			case R.id.btn_relay:
				break;
			case R.id.btn_holder:
				break;
		}
	}
}
