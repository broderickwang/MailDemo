package marc.com.maildemo.adapter;

import android.content.Context;
import android.view.View;

import java.util.List;

import marc.com.maildemo.R;
import marc.com.maildemo.model.Email;
import marc.com.maildemo.model.SimpleEmail;
import marc.com.multrecycleadapter.CommonRecycleAdapter;
import marc.com.multrecycleadapter.ViewHolder;

/**
 * Created by chengda
 * Date: 2018/2/18
 * Time: 20:52
 * Version: 1.0
 * Description:
 * Email:wangchengda1990@gmail.com
 **/
public class InboxListAdapter extends CommonRecycleAdapter<SimpleEmail> {
	public InboxListAdapter(Context context, List<SimpleEmail> datas, int layoutId) {
		super(context, datas, layoutId);
	}

	@Override
	public void convert(ViewHolder holder, SimpleEmail item) {
		holder.setText(R.id.tv_from,item.getFrom());
		holder.setText(R.id.tv_sentdate,item.getSendTime());
		holder.setText(R.id.tv_subject,item.getSubject());
		if(item.isNew()){
			holder.setVisibility(R.id.tv_new, View.VISIBLE);
		}
	}

	@Override
	public int getLayoutId(Object item, int position) {
		return 0;
	}
}
