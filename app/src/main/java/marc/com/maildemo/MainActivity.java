package marc.com.maildemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import marc.com.maildemo.ui.InboxActivity;
import marc.com.maildemo.util.Util;

public class MainActivity extends AppCompatActivity {

	@BindView(R.id.outbox)
	Button mOutbox;
	@BindView(R.id.inbox)
	Button mInbox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
	}

	@OnClick({R.id.outbox, R.id.inbox})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.outbox:
				break;
			case R.id.inbox:
				startActivity(new Intent(this, InboxActivity.class).putExtra("TYPE","INBOX"));
				break;
		}
	}
}
