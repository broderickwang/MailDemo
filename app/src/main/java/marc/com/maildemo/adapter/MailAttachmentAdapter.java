package marc.com.maildemo.adapter;

import android.content.Context;

import java.util.List;

import marc.com.maildemo.R;
import marc.com.multrecycleadapter.CommonRecycleAdapter;
import marc.com.multrecycleadapter.ViewHolder;

/**
 * Created by chengda
 * Date: 2018/2/19
 * Time: 20:37
 * Version: 1.0
 * Description:
 * Email:wangchengda1990@gmail.com
 **/
public class MailAttachmentAdapter extends CommonRecycleAdapter<String> {
	public MailAttachmentAdapter(Context context, List<String> datas, int layoutId) {
		super(context, datas, layoutId);
	}

	@Override
	public void convert(ViewHolder holder, String item) {
		holder.setText(R.id.attach_text,item);
	}

	@Override
	public int getLayoutId(Object item, int position) {
		return 0;
	}
}
