package marc.com.maildemo.util;

import android.content.Context;
import android.content.Intent;

/**
 * Created by chengda
 * Date: 2018/2/15
 * Time: 16:32
 * Version: 1.0
 * Description:
 * Email:wangchengda1990@gmail.com
 **/
public class Util {

	public static void startActivity(Context context,Class claz){
		Intent intent = new Intent(context,claz);
		context.startActivity(intent);
	}
}
