package marc.com.maildemo.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import marc.com.maildemo.R;
import marc.com.maildemo.adapter.FileAdapter;
import marc.com.maildemo.model.Email;
import marc.com.maildemo.util.MailSenter;
import marc.com.maildemo.util.PreferencesUtil;

public class EditEmailActivity extends AppCompatActivity {

    private static final String SMTPHOST = "smtphost";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @BindView(R.id.et_addr)
    EditText mEtAddr;
    @BindView(R.id.et_mailsubject)
    EditText mEtMailsubject;
    @BindView(R.id.btn_addattachment)
    Button mBtnAddattachment;
    @BindView(R.id.lv_mailattachment)
    RecyclerView mLvMailattachment;
    @BindView(R.id.et_mailcontent)
    EditText mEtMailcontent;
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;
    @BindView(R.id.btn_sent)
    Button mBtnSent;
    @BindView(R.id.btn_holder)
    LinearLayout mBtnHolder;

    private Context mContext;
    private int mType;
    private Email mEmail;
    private Handler mHandler;
    private ArrayList<String> mAttachments;
    private FileAdapter mFileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_email);
        ButterKnife.bind(this);

        getExtra();
        init();
    }

    private void init() {
        mContext = this;
        mHandler = new MyHandler(this);
        mAttachments = new ArrayList<>();
        mFileAdapter = new FileAdapter(mContext,null,R.layout.attatchment_list_item);
        mFileAdapter.setmDatas(mAttachments);

        mLvMailattachment.setLayoutManager(new LinearLayoutManager(mContext));
        mLvMailattachment.setAdapter(mFileAdapter);

        if (mType == 1) {
            mEtAddr.setText(mEmail.getFrom());
            mEtMailsubject.setText("回复：" + mEmail.getSubject());
        } else if (mType == 2) {
            mEtMailsubject.setText("转发：" + mEmail.getSubject());
            mEtMailcontent.setText(mEmail.getContent());
        }
//        mAttachments = mEmail.getAttachments();
    }

    @OnClick({R.id.btn_addattachment, R.id.btn_cancel, R.id.btn_sent})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_addattachment:
                startActivityForResult(new Intent(EditEmailActivity.this, FileExplorerActivity.class), 1);
                break;
            case R.id.btn_cancel:
                EditEmailActivity.this.finish();
                break;
            case R.id.btn_sent:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Email email = new Email();
                            email.setTo(mEtAddr.getText().toString());
                            email.setSubject(mEtMailsubject.getText().toString());
                            email.setContent(mEtMailcontent.getText().toString());
                            email.setAttachments(mAttachments);

                            MailSenter mailSenter = new MailSenter(mHandler, PreferencesUtil.getSharedStringData(mContext, SMTPHOST)
                                    , PreferencesUtil.getSharedStringData(mContext, USERNAME),
                                    PreferencesUtil.getSharedStringData(mContext, PASSWORD));
                            mHandler.obtainMessage(0).sendToTarget();
                            mailSenter.send(email);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1 && data != null) {
            String filepath = data.getStringExtra("FILEPATH");
            if (filepath.length() > 0) {
                mLvMailattachment.setVisibility(View.VISIBLE);
                mAttachments.add(filepath);
                mFileAdapter.notifyDataSetChanged();
            }
        }
    }

    private void getExtra() {
        mType = getIntent().getIntExtra("TYPE", 0);
        mEmail = (Email) getIntent().getSerializableExtra("EMAIL");
    }

    private static class MyHandler extends Handler {

        private WeakReference<EditEmailActivity> wrActivity;
        private ProgressDialog pd;

        public MyHandler(EditEmailActivity activity) {
            this.wrActivity = new WeakReference<EditEmailActivity>(activity);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            final EditEmailActivity activity = wrActivity.get();
            switch (msg.what) {
                case 0:
                    pd = ProgressDialog.show(activity.mContext, "发送邮件","正在发送....", true, false);
                    break;
                case 1:
                    pd.dismiss();
                    Toast.makeText(activity.getApplicationContext(), "发送成功！", Toast.LENGTH_LONG).show();
                    activity.finish();
                    break;
                case 3:
//                    activity.adapter.notifyDataSetChanged();
                    break;
                default:
                    Toast.makeText(activity.mContext, "发送出现错误！", Toast.LENGTH_LONG).show();
                    break;
            }
        };
    };
}
